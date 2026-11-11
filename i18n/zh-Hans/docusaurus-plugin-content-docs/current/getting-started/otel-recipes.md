---
title: OTel 后端方案
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - 云资源清单
  - 漂移检测
description: 基于 StackQL otel 输出在 ClickHouse 和 Datadog 中实现云资源清单、漂移和权限分配历史，并附定时 CI 示例
image: "/img/stackql-featured-image.png"
---
另请参阅：  
[[` 输出模式 `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` 将查询转换为带时间戳的快照：每行一条 OpenTelemetry 日志记录，每条语句一条完成记录，并且每条记录都带有关联键 `stackql.snapshot.id`、`stackql.query.hash` 和 `stackql.row.fingerprint`（参阅 [`otel` 输出](/getting-started/output-modes#otel-output)）。本页在该格式所面向的两种后端中端到端地使用这些记录：ClickHouse（包括 ClickStack）中的类型化资源清单表，以及 Datadog 中的日志查询和监控器。最后一节是一个定时 CI 作业，它为每次运行标记 trace id 和资源属性。

这些方案使用两个周期性查询：一个是 Compute Engine 实例的资源清单，另一个是某个项目的 IAM 绑定，展开为每个主体和角色一行。两个查询都投影出一个 `identity` 列，即后端用来跟踪资源的键（`id`、`arn`、`selfLink`，或用于复合键的拼接值）；指纹则是值。

```sql
-- inventory: one row per instance
select id as identity, name, status, machineType, zone
from google.compute.instances
where project = 'stackql-demo' and zone = 'australia-southeast1-a';

-- entitlements: one row per principal and role
select iam.role || '/' || split_part(json_each.value, ':', 2) as identity,
iam.role, split_part(json_each.value, ':', 2) as principal
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members)
where projectsId = 'stackql-demo';
```

每个查询都有一个稳定的 `stackql.query.hash`，这些方案按它过滤，因此一个序列就是一个周期性查询，不含其他内容。在 ClickHouse 中将 `{query_hash:String}` 用作查询参数，或粘贴完成记录中的哈希值。

## ClickHouse {#clickhouse}

OpenTelemetry Collector 的 ClickHouse 导出器（ClickStack 自带）将日志记录写入 `otel_logs`，其中属性为字符串映射（`ResourceAttributes`、`LogAttributes`），此外还有 `Timestamp`、`Body`、`TraceId` 和 `ScopeName`。将导出器指向 ClickStack 的 OTLP 接入端点或某个 Collector：

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### 类型化资源清单表 {#typed-inventory-table}

直接查询 `otel_logs` 是可行的，但这样每次提问都要付出映射查找的代价。物化视图将行记录提取到一个以 `(query_hash, identity, snapshot_time)` 为键的类型化表中：

```sql
CREATE TABLE stackql_inventory
(
    snapshot_time DateTime64(9),
    snapshot_id   String,
    query_hash    String,
    provider      LowCardinality(String),
    service       LowCardinality(String),
    resource      LowCardinality(String),
    identity      String,
    fingerprint   String,
    attributes    Map(LowCardinality(String), String),
    document      String,
    environment   LowCardinality(String),
    trace_id      String
)
ENGINE = MergeTree
ORDER BY (query_hash, identity, snapshot_time);

CREATE MATERIALIZED VIEW stackql_inventory_mv TO stackql_inventory AS
SELECT
    Timestamp                                    AS snapshot_time,
    LogAttributes['stackql.snapshot.id']         AS snapshot_id,
    LogAttributes['stackql.query.hash']          AS query_hash,
    LogAttributes['stackql.provider']            AS provider,
    LogAttributes['stackql.service']             AS service,
    LogAttributes['stackql.resource']            AS resource,
    LogAttributes['identity']                    AS identity,
    LogAttributes['stackql.row.fingerprint']     AS fingerprint,
    LogAttributes                                AS attributes,
    Body                                         AS document,
    ResourceAttributes['deployment.environment'] AS environment,
    TraceId                                      AS trace_id
FROM otel_logs
WHERE ScopeName = 'github.com/stackql/stackql/internal/stackql/output'
  AND mapContains(LogAttributes, 'stackql.row.index');
```

完成记录进入一张单独的小表。它定义了下文每个方案中“最新快照”的含义：快照只有在其完成记录到达后才算数，因此中途失败的运行绝不会被误认为是资产变少了。

```sql
CREATE TABLE stackql_snapshots
(
    snapshot_time DateTime64(9),
    snapshot_id   String,
    query_hash    String,
    query         String,
    rows_returned UInt32,
    duration_ms   UInt32,
    environment   LowCardinality(String)
)
ENGINE = MergeTree
ORDER BY (query_hash, snapshot_time);

CREATE MATERIALIZED VIEW stackql_snapshots_mv TO stackql_snapshots AS
SELECT
    Timestamp                                              AS snapshot_time,
    LogAttributes['stackql.snapshot.id']                   AS snapshot_id,
    LogAttributes['stackql.query.hash']                    AS query_hash,
    LogAttributes['stackql.query']                         AS query,
    toUInt32OrZero(LogAttributes['stackql.rows_returned']) AS rows_returned,
    toUInt32OrZero(LogAttributes['stackql.duration_ms'])   AS duration_ms,
    ResourceAttributes['deployment.environment']           AS environment
FROM otel_logs
WHERE ScopeName = 'github.com/stackql/stackql/internal/stackql/output'
  AND LogAttributes['stackql.snapshot.complete'] = 'true';
```

物化视图只能看到在其创建之后插入的行。对于更早写入的记录，使用与视图相同的 select 通过 `INSERT INTO ... SELECT` 进行回填。

### 每个资源的最新状态 {#latest-state-per-resource}

当前的资产状况就是最新完整快照的行集：

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

要获取曾经出现过的每个资源的最后已知状态（包括此后已消失的资源），请使用 `argMax` 聚合：

```sql
SELECT
    identity,
    argMax(document, snapshot_time)    AS document,
    argMax(fingerprint, snapshot_time) AS fingerprint,
    max(snapshot_time)                 AS last_seen
FROM stackql_inventory
WHERE query_hash = {query_hash:String}
GROUP BY identity
ORDER BY last_seen DESC;
```

### 两个快照之间的漂移 {#drift-between-two-snapshots}

按标识连接两个快照。指纹不相等即为变化；缺少一侧则是出现或消失的资源：

```sql
WITH
    (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
     ORDER BY snapshot_time DESC LIMIT 1) AS latest,
    (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
     ORDER BY snapshot_time DESC LIMIT 1 OFFSET 1) AS previous
SELECT
    if(n.identity != '', n.identity, p.identity) AS identity,
    multiIf(p.identity = '', 'appeared', n.identity = '', 'disappeared', 'changed') AS change,
    p.document AS before,
    n.document AS after
FROM (SELECT identity, fingerprint, document FROM stackql_inventory WHERE snapshot_id = latest) AS n
FULL OUTER JOIN
     (SELECT identity, fingerprint, document FROM stackql_inventory WHERE snapshot_id = previous) AS p
ON n.identity = p.identity
WHERE n.fingerprint != p.fingerprint
ORDER BY change, identity;
```

替换为任意两个快照 id，即可对任意时间点进行差异比较。只需要其中一侧时，同样的问题就是一个反连接：

```sql
-- appeared since the previous snapshot
SELECT n.identity, n.document
FROM (SELECT identity, document FROM stackql_inventory WHERE snapshot_id = latest) AS n
LEFT ANTI JOIN (SELECT identity FROM stackql_inventory WHERE snapshot_id = previous) AS p
ON n.identity = p.identity;

-- disappeared since the previous snapshot
SELECT p.identity, p.document
FROM (SELECT identity, document FROM stackql_inventory WHERE snapshot_id = previous) AS p
LEFT ANTI JOIN (SELECT identity FROM stackql_inventory WHERE snapshot_id = latest) AS n
ON p.identity = n.identity;
```

`change` 列体现的是投影范围内的漂移：未出现在 `SELECT` 中的列不会引起变化，因此只投影 `name` 和 `status` 的查询只报告状态变化，不报告其他内容。

### 权限分配历史 {#entitlement-history}

对于权限分配查询，标识是主体和角色的组合，因此某个组合在资源清单表中的时间线就是该权限分配的存续期：

```sql
WITH (SELECT max(snapshot_time) FROM stackql_snapshots WHERE query_hash = {query_hash:String}) AS latest_time
SELECT
    attributes['principal']           AS principal,
    attributes['role']                AS role,
    min(snapshot_time)                AS first_seen,
    max(snapshot_time)                AS last_seen,
    max(snapshot_time) < latest_time  AS revoked
FROM stackql_inventory
WHERE query_hash = {query_hash:String}
GROUP BY principal, role
ORDER BY last_seen DESC, principal, role;
```

`first_seen` 和 `last_seen` 会把被撤销后又重新授予的同一项授予合并为一个时间段；当需要关注其间的空档时，在相同分组上使用 `groupArray(snapshot_time)` 可列出该组合出现过的每个快照。本页的其他内容都无需修改即可适用：对权限分配查询应用漂移方案，可列出两次运行之间的授予和撤销。

## Datadog {#datadog}

将记录发送到 Datadog Agent 的 OTLP 接收器（在 Agent 的 `otlp_config` 中启用 HTTP 接收器，端口 `4318`），或发送到运行 Datadog 导出器的 OpenTelemetry Collector 的 OTLP 接收器。Datadog 自己的 OTLP 接入端点可直接接收这些记录，API 密钥作为请求头传入；区域 URL 请从 Datadog 的 OTLP 摄取文档中获取。

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://datadog-agent.example:4318/v1/logs" } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

```shell
stackql exec --output otel ... \
--otel.config '{ "exporter": { "endpoint": "<regional OTLP logs intake URL>", "headers": { "dd-api-key": "'"$DD_API_KEY"'" } } }'
```

### 属性映射 {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|`service.name` 资源属性|`service` 标签（`stackql`，或 `OTEL_SERVICE_NAME`）|
|`deployment.environment` 资源属性|`env` 标签|
|`cloud.provider`、`cloud.account.id`、`cloud.region`、`cloud.availability_zone` 及其他资源属性|日志属性，例如 `@cloud.account.id`|
|`stackql.*` 日志属性|日志属性，例如 `@stackql.snapshot.id`；为用于过滤的属性添加分面|
|行的列（`identity`、`name`、`status` 等）|以列名命名的日志属性，例如 `@status`|
|记录正文|`message`，即 JSON 形式的行|
|`traceId`、`spanId`|`trace_id`、`span_id`，关联一次运行中的每条语句|

<br />

### 日志查询 {#log-queries}

Datadog 回答存在性和历史方面的问题；快照之间的连接在 ClickHouse 中完成，或者通过 Collector 将同一数据流扇出到两者。

```
# completion records of one recurring query, newest first: the latest snapshot id
service:stackql @stackql.snapshot.complete:true @stackql.query.hash:<hash>

# the rows of one snapshot
service:stackql @stackql.query.hash:<hash> @stackql.snapshot.id:<snapshot id>

# the timeline of one resource; a new fingerprint value is a change
service:stackql @identity:<instance id>

# every principal that has held a role, grouped by @principal in Log Analytics;
# the first and last log in a group are first seen and last seen
service:stackql @stackql.query.hash:<entitlements hash> @role:roles/owner
```

针对完成记录的监控器监视的是管道本身：基于 `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` 的日志监控器可发现返回为空的资源清单，而基于 `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` 并带有“no data”条件的监控器可发现未执行的定时运行。

## 定时 CI {#scheduled-ci}

定时作业形成序列。该示例通过 GitHub Actions 每小时运行这两个查询：导出器在 `OTEL_CONFIG` 中配置一次并传给 `--otel.config`，`TRACEPARENT` 让一次运行中的每条语句具有相同的 trace id，`OTEL_RESOURCE_ATTRIBUTES` 在每条记录上标记环境、作业名称和运行 id，`.otlp.jsonl` 文件作为制品保留，因此即使推送失败也仍留有一份副本。

```yaml
name: cloud-inventory
on:
  schedule:
    - cron: '0 * * * *'
  workflow_dispatch:
jobs:
  snapshot:
    runs-on: ubuntu-latest
    env:
      GOOGLE_CREDENTIALS: ${{ secrets.GOOGLE_CREDENTIALS }}
      OTEL_CONFIG: '{ "exporter": { "endpoint": "${{ vars.OTLP_LOGS_ENDPOINT }}", "headers": { "authorization": "${{ secrets.OTLP_INGESTION_KEY }}" } } }'
      OTEL_RESOURCE_ATTRIBUTES: deployment.environment=prod,stackql.job=cloud-inventory,stackql.run=${{ github.run_id }}
    steps:
      - uses: stackql/setup-stackql@v2
      - name: snapshot
        run: |
          export TRACEPARENT="00-$(openssl rand -hex 16)-$(openssl rand -hex 8)-01"
          stackql exec --output otel -f instances.otlp.jsonl --otel.config "$OTEL_CONFIG" \
            "select id as identity, name, status, machineType, zone from google.compute.instances \
             where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
          stackql exec --output otel -f entitlements.otlp.jsonl --otel.config "$OTEL_CONFIG" \
            "select iam.role || '/' || split_part(json_each.value, ':', 2) as identity, \
             iam.role, split_part(json_each.value, ':', 2) as principal \
             from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) \
             where projectsId = 'stackql-demo'"
      - uses: actions/upload-artifact@v4
        with:
          name: inventory-${{ github.run_id }}
          path: "*.otlp.jsonl"
```

在任何其他调度器上运行同样的作业，只需要该标志和这两个环境变量；`TRACEPARENT` 是可选的，缺省时按进程生成，此时一次运行中的语句仍然共享同一个 trace id。
