---
title: OTel 백엔드 레시피
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - 클라우드 인벤토리
  - 드리프트 감지
description: ClickHouse와 Datadog에서 StackQL otel 출력을 활용한 클라우드 인벤토리, 드리프트, 사용 권한 이력과 예약된 CI 예제
image: "/img/stackql-featured-image.png"
---
참고 항목:  
[[` 출력 모드 `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` 명령은 쿼리를 타임스탬프가 지정된 스냅샷으로 변환합니다. 행당 1개의 OpenTelemetry 로그 레코드와 문당 1개의 완료 레코드가 생성되며, 모든 레코드에 `stackql.snapshot.id`, `stackql.query.hash`, `stackql.row.fingerprint` 상관 키가 포함됩니다. [`otel` 출력](/getting-started/output-modes#otel-output)을 참조하세요. 이 페이지에서는 이 형식이 염두에 두고 설계된 두 백엔드에서 해당 레코드를 처음부터 끝까지 다룹니다: ClickHouse(ClickStack 포함)의 형식이 지정된 인벤토리 테이블, 그리고 Datadog의 로그 쿼리와 모니터입니다. 마지막 섹션은 각 실행에 트레이스 ID와 리소스 속성을 부여하는 예약된 CI 작업입니다.

레시피에서는 두 가지 반복 쿼리를 사용합니다. 하나는 Compute Engine 인스턴스의 인벤토리이고, 다른 하나는 프로젝트의 IAM 바인딩을 보안 주체 및 역할당 1행으로 펼친 것입니다. 두 쿼리 모두 `identity` 열을 프로젝션합니다. 이 열은 백엔드가 리소스를 추적하는 키(`id`, `arn`, `selfLink` 또는 복합 키의 경우 연결한 값)이며, 지문은 값입니다.

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

각 쿼리에는 안정적인 `stackql.query.hash` 값이 있으며, 레시피는 이 값으로 필터링하므로 하나의 시리즈는 하나의 반복 쿼리로만 구성됩니다. ClickHouse에서는 `{query_hash:String}` 형식의 쿼리 매개 변수를 사용하거나, 완료 레코드의 해시를 붙여 넣으세요.

## ClickHouse {#clickhouse}

ClickStack에 포함된 OpenTelemetry Collector의 ClickHouse 익스포터는 로그 레코드를 `otel_logs` 테이블에 저장합니다. 이때 속성은 `Timestamp`, `Body`, `TraceId`, `ScopeName` 열과 함께 문자열 맵(`ResourceAttributes`, `LogAttributes`)으로 저장됩니다. 익스포터의 대상을 ClickStack의 OTLP 수집 엔드포인트 또는 Collector로 지정하세요:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### 형식이 지정된 인벤토리 테이블 {#typed-inventory-table}

`otel_logs` 테이블을 직접 쿼리해도 되지만, 그러면 모든 질문에서 맵 조회 비용이 발생합니다. 구체화된 뷰는 행 레코드를 `(query_hash, identity, snapshot_time)` 키를 사용하는 형식이 지정된 테이블로 옮깁니다:

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

완료 레코드는 별도의 작은 테이블에 저장됩니다. 이 테이블은 아래의 모든 레시피에서 "최신 스냅샷"의 의미를 정의합니다. 스냅샷은 완료 레코드가 도착한 후에야 유효한 것으로 간주되므로, 도중에 실패한 실행을 자산이 줄어든 것으로 오인하는 일이 없습니다.

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

구체화된 뷰는 생성된 후에 삽입된 행만 인식합니다. 그 전에 저장된 레코드는 뷰와 동일한 SELECT 쿼리를 사용하는 `INSERT INTO ... SELECT` 문으로 백필하세요.

### 리소스별 최신 상태 {#latest-state-per-resource}

현재 자산은 최신 완료 스냅샷의 행 집합입니다:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

이후 사라진 리소스를 포함하여 한 번이라도 확인된 모든 리소스의 마지막으로 알려진 상태를 구하려면 `argMax` 함수로 집계하세요:

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

### 두 스냅샷 간 드리프트 {#drift-between-two-snapshots}

두 스냅샷을 identity 기준으로 조인합니다. 지문이 다르면 변경이고, 한쪽이 없으면 나타나거나 사라진 리소스입니다:

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

임의의 두 스냅샷 ID로 바꾸면 임의의 두 시점을 비교할 수 있습니다. 한쪽만 필요한 경우 같은 질문은 안티 조인이 됩니다:

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

`change` 열은 프로젝션 범위 안의 드리프트를 나타냅니다. `SELECT` 문에서 제외된 열은 변경을 일으킬 수 없으므로, `name` 열과 `status` 열만 프로젝션하는 쿼리는 상태 변경만 보고합니다.

### 사용 권한 이력 {#entitlement-history}

사용 권한 쿼리에서 identity는 보안 주체와 역할의 쌍이므로, 인벤토리 테이블에서 한 쌍의 타임라인이 곧 해당 사용 권한의 수명입니다:

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

`first_seen` 열과 `last_seen` 열은 해지되었다가 나중에 다시 부여된 권한을 하나의 기간으로 합칩니다. 공백이 중요한 경우 같은 그룹화에 `groupArray(snapshot_time)` 함수를 사용하면 해당 쌍이 존재했던 모든 스냅샷이 나열됩니다. 이 페이지의 다른 모든 내용은 그대로 적용됩니다. 사용 권한 쿼리에 드리프트 레시피를 적용하면 두 실행 사이의 권한 부여와 해지가 나열됩니다.

## Datadog {#datadog}

레코드를 Datadog Agent의 OTLP 리시버(Agent의 `otlp_config` 설정에서 HTTP 리시버를 활성화, 포트 `4318`) 또는 Datadog 익스포터를 실행하는 OpenTelemetry Collector의 OTLP 리시버로 보내세요. Datadog 자체 OTLP 수집 엔드포인트는 API 키를 헤더로 받아 레코드를 직접 수신합니다. 리전별 URL은 Datadog의 OTLP 수집 문서를 참조하세요.

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

### 속성 매핑 {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|`service.name` 리소스 속성|`service` 태그(`stackql` 또는 `OTEL_SERVICE_NAME`)|
|`deployment.environment` 리소스 속성|`env` 태그|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` 및 기타 리소스 속성|로그 속성(예: `@cloud.account.id`)|
|`stackql.*` 로그 속성|로그 속성(예: `@stackql.snapshot.id`). 필터링에 사용하는 속성에는 패싯을 추가하세요|
|행의 열(`identity`, `name`, `status`, ...)|열 이름을 사용한 로그 속성(예: `@status`)|
|레코드 본문|`message`, JSON 형식의 행|
|`traceId`, `spanId`|`trace_id`, `span_id`: 한 번의 실행에 속한 모든 문을 연결|

<br />

### 로그 쿼리 {#log-queries}

Datadog은 존재 여부와 이력에 관한 질문에 답합니다. 스냅샷 간 조인은 ClickHouse에서 수행하거나, Collector를 통해 같은 스트림을 두 백엔드 모두에 팬아웃하여 수행합니다.

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

완료 레코드에 대한 모니터는 파이프라인 자체를 모니터링합니다. `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` 쿼리에 대한 로그 모니터는 비어 있는 인벤토리를 감지하고, `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` 쿼리에 "no data" 조건을 설정한 모니터는 실행되지 않은 예약 실행을 감지합니다.

## 예약된 CI {#scheduled-ci}

예약된 작업이 시리즈를 만듭니다. 이 예제는 GitHub Actions에서 두 쿼리를 매시간 실행합니다. 익스포터는 `OTEL_CONFIG` 변수에 한 번 구성되어 `--otel.config` 플래그로 전달되고, `TRACEPARENT` 변수는 한 실행의 모든 문에 동일한 트레이스 ID를 부여하며, `OTEL_RESOURCE_ATTRIBUTES` 변수는 모든 레코드에 환경, 작업 이름, 실행 ID를 기록합니다. 또한 `.otlp.jsonl` 파일은 아티팩트로 보관되므로 푸시가 실패해도 사본이 남습니다.

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

다른 스케줄러에서 같은 작업을 실행할 때도 플래그 하나와 환경 변수 2개만 있으면 됩니다. `TRACEPARENT` 변수는 선택 사항이며, 없으면 프로세스마다 생성됩니다. 이 경우에도 한 실행의 문들은 동일한 트레이스 ID를 공유합니다.
