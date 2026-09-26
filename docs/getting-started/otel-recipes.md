---
title: OTel Backend Recipes
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - cloud inventory
  - drift detection
description: Cloud inventory, drift and entitlement history from StackQL otel output in ClickHouse and Datadog, with a scheduled CI example
image: "/img/stackql-featured-image.png"
---
See also:  
[[` Output Modes `]](/docs/getting-started/output-modes#otel-output) [[` exec `]](/docs/command-line-usage/exec)

`stackql exec --output otel` turns a query into a timestamped snapshot: one OpenTelemetry log record per row, a completion record per statement, and the correlation keys `stackql.snapshot.id`, `stackql.query.hash` and `stackql.row.fingerprint` on every record (see [`otel` Output](/docs/getting-started/output-modes#otel-output)).  This page takes those records end to end in the two backends the format was designed for: a typed inventory table in ClickHouse (including ClickStack), and log queries and monitors in Datadog.  The last section is a scheduled CI job that stamps each run with a trace id and resource attributes.

The recipes use two recurring queries, an inventory of Compute Engine instances and the IAM bindings of a project unnested to one row per principal and role.  Both project an `identity` column, the key the backend tracks a resource by (`id`, `arn`, `selfLink`, or a concatenation for a composite key); the fingerprint is the value.

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

Each query has a stable `stackql.query.hash`, and the recipes filter on it so a series is one recurring query and nothing else.  Use `{query_hash:String}` as a query parameter in ClickHouse, or paste the hash from a completion record.

## ClickHouse

The OpenTelemetry Collector's ClickHouse exporter, which ClickStack ships, lands log records in `otel_logs` with the attributes as string maps (`ResourceAttributes`, `LogAttributes`) alongside `Timestamp`, `Body`, `TraceId` and `ScopeName`.  Point the exporter at ClickStack's OTLP intake or at a Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Typed inventory table

Querying `otel_logs` directly works, but every question then pays for the map lookups.  A materialized view lifts the row records into a typed table keyed by `(query_hash, identity, snapshot_time)`:

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

The completion records go to a small table of their own.  It defines what "the latest snapshot" means in every recipe below: a snapshot counts once its completion record has arrived, so a run that fails part way is never mistaken for a smaller estate.

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

A materialized view only sees rows inserted after it is created.  Backfill records that landed earlier with an `INSERT INTO ... SELECT` using the same select as the view.

### Latest state per resource

The current estate is the row set of the latest complete snapshot:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

For the last known state of every resource ever seen, including resources that have since disappeared, aggregate with `argMax`:

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

### Drift between two snapshots

Join the two snapshots on identity.  A fingerprint inequality is a change; a missing side is a resource that appeared or disappeared:

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

Substitute any two snapshot ids to diff arbitrary points in time.  When only one side is wanted, the same question is an anti-join:

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

The `change` column is the drift scope of the projection: columns left out of the `SELECT` cannot cause a change, so a query that projects only `name` and `status` reports state changes and nothing else.

### Entitlement history

For the entitlement query the identity is the principal and role pair, so a pair's timeline in the inventory table is the lifetime of that entitlement:

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

`first_seen` and `last_seen` collapse a grant that was revoked and later re-granted into one span; `groupArray(snapshot_time)` on the same grouping lists every snapshot the pair was present in when the gaps matter.  Everything else on this page applies unchanged: the drift recipe on the entitlement query lists grants and revocations between two runs.

## Datadog

Send the records to the OTLP receiver of a Datadog Agent (enable the HTTP receiver in the Agent's `otlp_config`, port `4318`) or of an OpenTelemetry Collector running the Datadog exporter.  Datadog's own OTLP intake takes the records directly with the API key as a header; take the regional URL from Datadog's OTLP ingestion documentation.

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

### Attribute mapping

| OpenTelemetry | Datadog |
|--|--|
|`service.name` resource attribute|`service` tag (`stackql`, or `OTEL_SERVICE_NAME`)|
|`deployment.environment` resource attribute|`env` tag|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` and other resource attributes|Log attributes, for example `@cloud.account.id`|
|`stackql.*` log attributes|Log attributes, for example `@stackql.snapshot.id`; add facets for the ones you filter on|
|Row columns (`identity`, `name`, `status`, ...)|Log attributes under the column name, for example `@status`|
|Record body|`message`, the row as JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, correlating every statement of one run|

<br />

### Log queries

Datadog answers presence and history questions; joins between snapshots are done in ClickHouse, or by fanning the same stream out to both through a Collector.

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

Monitors on the completion records watch the pipeline itself: a log monitor on `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` catches an inventory that came back empty, and a monitor with a "no data" condition on `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` catches a scheduled run that did not happen.

## Scheduled CI

A scheduled job makes the series.  The example runs both queries hourly from GitHub Actions: the exporter is configured once in `OTEL_CONFIG` and passed to `--otel.config`, `TRACEPARENT` gives every statement of a run the same trace id, `OTEL_RESOURCE_ATTRIBUTES` stamps the environment, the job name and the run id on every record, and the `.otlp.jsonl` files are kept as artifacts so a failed push still leaves a copy.

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

The same job on any other scheduler needs only the flag and the two environment variables; `TRACEPARENT` is optional and is generated per process when absent, in which case the statements of one run still share a trace id.
