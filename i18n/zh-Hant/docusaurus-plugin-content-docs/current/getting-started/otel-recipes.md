---
title: OTel 後端設定範例
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - 雲端資源清單
  - 漂移偵測
description: 在 ClickHouse 與 Datadog 中以 StackQL otel 輸出取得雲端資源清單、漂移與權利歷程，並附排程 CI 範例
image: "/img/stackql-featured-image.png"
---
另請參閱：  
[[` 輸出模式 `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` 會將查詢轉換為帶有時間戳記的快照：每個資料列一筆 OpenTelemetry 記錄、每個陳述式一筆完成記錄，且每筆記錄都帶有關聯鍵 `stackql.snapshot.id`、`stackql.query.hash` 和 `stackql.row.fingerprint`（請參閱 [`otel` 輸出](/getting-started/output-modes#otel-output)）。本頁在此格式設計所針對的兩個後端中，端對端地處理這些記錄：ClickHouse（包括 ClickStack）中具型別的資源清單資料表，以及 Datadog 中的記錄查詢與監控。最後一節是一個排程的 CI 作業，會為每次執行加上追蹤 ID 與資源屬性。

這些範例使用兩個週期性查詢：Compute Engine 執行個體的資源清單，以及展開為每個主體與角色一個資料列的專案 IAM 繫結。兩者都會投影出 `identity` 資料行，作為後端追蹤資源所用的鍵（`id`、`arn`、`selfLink`，或複合鍵的串連值）；指紋則是值。

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

每個查詢都有穩定的 `stackql.query.hash`，範例會以它進行篩選，讓一個序列只對應一個週期性查詢，不含其他內容。在 ClickHouse 中請將 `{query_hash:String}` 當作查詢參數使用，或從完成記錄中貼上雜湊。

## ClickHouse {#clickhouse}

OpenTelemetry Collector 的 ClickHouse 匯出工具（ClickStack 隨附此工具）會將記錄寫入 `otel_logs`，屬性以字串對應（`ResourceAttributes`、`LogAttributes`）存放，並與 `Timestamp`、`Body`、`TraceId` 和 `ScopeName` 並列。請將匯出工具指向 ClickStack 的 OTLP 擷取端點或 Collector：

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### 具型別的資源清單資料表 {#typed-inventory-table}

直接查詢 `otel_logs` 也可行，但每個問題都得付出對應查閱的成本。具體化檢視會將資料列記錄提升到以 `(query_hash, identity, snapshot_time)` 為鍵的具型別資料表中：

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

完成記錄則寫入專屬的小型資料表。它定義了下列每個範例中「最新快照」的意義：快照在其完成記錄抵達後才算數，因此中途失敗的執行絕不會被誤認為規模較小的資源環境。

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

具體化檢視只會看到在其建立之後插入的資料列。對於先前已寫入的記錄，請使用與檢視相同的 select，以 `INSERT INTO ... SELECT` 進行回填。

### 每個資源的最新狀態 {#latest-state-per-resource}

目前的資源環境就是最新完整快照的資料列集合：

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

若要取得曾經出現過的每個資源最後已知的狀態（包括之後已消失的資源），請以 `argMax` 進行彙總：

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

### 兩個快照之間的漂移 {#drift-between-two-snapshots}

以識別值聯結兩個快照。指紋不相等代表變更；缺少一側則代表資源出現或消失：

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

代入任意兩個快照 ID，即可比較任意兩個時間點的差異。若只需要其中一側，同一個問題可以寫成反聯結：

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

`change` 資料行的漂移範圍取決於投影：未列在 `SELECT` 中的資料行不會造成變更，因此只投影 `name` 和 `status` 的查詢只會回報狀態變更，不含其他內容。

### 權利歷程 {#entitlement-history}

對權利查詢而言，識別值是主體與角色的組合，因此某個組合在資源清單資料表中的時間軸，就是該權利的存續期間：

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

`first_seen` 和 `last_seen` 會將曾被撤銷、之後又重新授予的授權合併為一個時段；若間隔很重要，可在相同分組上使用 `groupArray(snapshot_time)`，列出該組合出現過的每個快照。本頁的其他內容皆可直接套用：將漂移範例用於權利查詢，即可列出兩次執行之間的授予與撤銷。

## Datadog {#datadog}

將記錄傳送到 Datadog Agent 的 OTLP 接收器（在 Agent 的 `otlp_config` 中啟用 HTTP 接收器，連接埠 `4318`），或傳送到執行 Datadog 匯出工具之 OpenTelemetry Collector 的 OTLP 接收器。Datadog 本身的 OTLP 擷取端點可直接接收記錄，並以 API 金鑰作為標頭；區域 URL 請參閱 Datadog 的 OTLP 擷取文件。

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

### 屬性對應 {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|`service.name` 資源屬性|`service` 標籤（`stackql` 或 `OTEL_SERVICE_NAME`）|
|`deployment.environment` 資源屬性|`env` 標籤|
|`cloud.provider`、`cloud.account.id`、`cloud.region`、`cloud.availability_zone` 及其他資源屬性|記錄屬性，例如 `@cloud.account.id`|
|`stackql.*` 記錄屬性|記錄屬性，例如 `@stackql.snapshot.id`；請為您用來篩選的屬性新增 facet|
|資料列的資料行（`identity`、`name`、`status`、...）|以資料行名稱存放的記錄屬性，例如 `@status`|
|記錄本文|`message`，即 JSON 格式的資料列|
|`traceId`, `spanId`|`trace_id`、`span_id`，用來關聯同一次執行的每個陳述式|

<br />

### 記錄查詢 {#log-queries}

Datadog 可回答資源是否存在與歷程方面的問題；快照之間的聯結則在 ClickHouse 中進行，或透過 Collector 將同一串流扇出到兩者。

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

針對完成記錄的監控可監看管線本身：針對 `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` 的記錄監控可偵測傳回空結果的資源清單，而針對 `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` 設定「no data」條件的監控，則可偵測未執行的排程作業。

## 排程 CI {#scheduled-ci}

序列由排程作業產生。此範例每小時從 GitHub Actions 執行這兩個查詢：匯出工具只在 `OTEL_CONFIG` 中設定一次，再傳給 `--otel.config`；`TRACEPARENT` 讓同一次執行的每個陳述式都具有相同的追蹤 ID；`OTEL_RESOURCE_ATTRIBUTES` 會在每筆記錄上加上環境、作業名稱與執行 ID；`.otlp.jsonl` 檔案則保留為成品，因此即使推送失敗，仍會留下一份副本。

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

在其他任何排程器上執行相同的作業，只需要此旗標與這兩個環境變數；`TRACEPARENT` 是選用的：若未設定，每個 `stackql exec` 處理程序都會產生自己的追蹤 ID，只有該處理程序執行的陳述式會共用此 ID，因此上述執行中的兩個陳述式會帶有不同的追蹤 ID。
