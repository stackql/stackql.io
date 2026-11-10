---
title: OTel-Backend-Rezepte
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - cloud-inventar
  - drift-erkennung
description: Cloud-Inventar, Drift- und Berechtigungshistorie aus der otel-Ausgabe von StackQL in ClickHouse und Datadog, mit einem Beispiel für geplante CI-Läufe
image: "/img/stackql-featured-image.png"
---
Siehe auch:  
[[` Output Modes `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` macht aus einer Abfrage einen Snapshot mit Zeitstempel: ein OpenTelemetry-Log-Record pro Zeile, ein Abschluss-Record pro Anweisung und die Korrelationsschlüssel `stackql.snapshot.id`, `stackql.query.hash` und `stackql.row.fingerprint` auf jedem Record (siehe [`otel`-Ausgabe](/getting-started/output-modes#otel-output)). Diese Seite führt diese Records durchgängig durch die beiden Backends, für die das Format entworfen wurde: eine typisierte Inventartabelle in ClickHouse (einschließlich ClickStack) sowie Log-Abfragen und Monitore in Datadog. Der letzte Abschnitt ist ein geplanter CI-Job, der jeden Lauf mit einer Trace-ID und Ressourcenattributen versieht.

Die Rezepte verwenden zwei wiederkehrende Abfragen: ein Inventar der Compute Engine-Instanzen und die IAM-Bindings eines Projekts, aufgelöst in eine Zeile pro Principal und Rolle. Beide projizieren eine Spalte `identity`, den Schlüssel, anhand dessen das Backend eine Ressource verfolgt (`id`, `arn`, `selfLink` oder eine Verkettung für einen zusammengesetzten Schlüssel); der Fingerprint ist der Wert.

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

Jede Abfrage hat einen stabilen `stackql.query.hash`, und die Rezepte filtern darauf, sodass eine Serie genau eine wiederkehrende Abfrage ist und nichts anderes. Verwenden Sie `{query_hash:String}` als Abfrageparameter in ClickHouse oder fügen Sie den Hash aus einem Abschluss-Record ein.

## ClickHouse {#clickhouse}

Der ClickHouse-Exporter des OpenTelemetry Collectors, den ClickStack mitliefert, legt Log-Records in `otel_logs` ab, mit den Attributen als String-Maps (`ResourceAttributes`, `LogAttributes`) neben `Timestamp`, `Body`, `TraceId` und `ScopeName`. Richten Sie den Exporter auf den OTLP-Intake von ClickStack oder auf einen Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Typisierte Inventartabelle {#typed-inventory-table}

`otel_logs` direkt abzufragen funktioniert, aber jede Frage bezahlt dann für die Map-Lookups. Eine materialisierte View hebt die Zeilen-Records in eine typisierte Tabelle mit dem Schlüssel `(query_hash, identity, snapshot_time)`:

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

Die Abschluss-Records kommen in eine eigene kleine Tabelle. Sie definiert, was "der neueste Snapshot" in jedem Rezept unten bedeutet: Ein Snapshot zählt, sobald sein Abschluss-Record eingetroffen ist, sodass ein Lauf, der unterwegs fehlschlägt, nie für einen kleineren Bestand gehalten wird.

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

Eine materialisierte View sieht nur Zeilen, die nach ihrer Erstellung eingefügt werden. Füllen Sie Records, die früher eingetroffen sind, mit einem `INSERT INTO ... SELECT` nach, das dasselbe Select wie die View verwendet.

### Letzter Zustand pro Ressource {#latest-state-per-resource}

Der aktuelle Bestand ist die Zeilenmenge des neuesten vollständigen Snapshots:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Für den letzten bekannten Zustand jeder jemals gesehenen Ressource, einschließlich Ressourcen, die inzwischen verschwunden sind, aggregieren Sie mit `argMax`:

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

### Drift zwischen zwei Snapshots {#drift-between-two-snapshots}

Verknüpfen Sie die beiden Snapshots über die Identität. Ein ungleicher Fingerprint ist eine Änderung; eine fehlende Seite ist eine Ressource, die erschienen oder verschwunden ist:

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

Setzen Sie zwei beliebige Snapshot-IDs ein, um beliebige Zeitpunkte zu vergleichen. Wenn nur eine Seite gewünscht ist, ist dieselbe Frage ein Anti-Join:

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

Die Spalte `change` ist der Drift-Umfang der Projektion: Spalten, die im `SELECT` weggelassen werden, können keine Änderung verursachen, sodass eine Abfrage, die nur `name` und `status` projiziert, Zustandsänderungen meldet und nichts anderes.

### Berechtigungshistorie {#entitlement-history}

Bei der Berechtigungsabfrage ist die Identität das Paar aus Principal und Rolle, sodass die Zeitachse eines Paars in der Inventartabelle die Lebensdauer dieser Berechtigung ist:

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

`first_seen` und `last_seen` fassen eine Zuweisung, die entzogen und später erneut erteilt wurde, zu einer Spanne zusammen; `groupArray(snapshot_time)` auf derselben Gruppierung listet jeden Snapshot auf, in dem das Paar vorhanden war, wenn die Lücken relevant sind. Alles andere auf dieser Seite gilt unverändert: Das Drift-Rezept auf der Berechtigungsabfrage listet Erteilungen und Entzüge zwischen zwei Läufen auf.

## Datadog {#datadog}

Senden Sie die Records an den OTLP-Receiver eines Datadog Agents (aktivieren Sie den HTTP-Receiver in der `otlp_config` des Agents, Port `4318`) oder eines OpenTelemetry Collectors mit dem Datadog-Exporter. Der eigene OTLP-Intake von Datadog nimmt die Records direkt mit dem API-Schlüssel als Header entgegen; die regionale URL entnehmen Sie der Datadog-Dokumentation zur OTLP-Ingestion.

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

### Attributzuordnung {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Ressourcenattribut `service.name`|Tag `service` (`stackql` oder `OTEL_SERVICE_NAME`)|
|Ressourcenattribut `deployment.environment`|Tag `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` und weitere Ressourcenattribute|Log-Attribute, zum Beispiel `@cloud.account.id`|
|`stackql.*`-Log-Attribute|Log-Attribute, zum Beispiel `@stackql.snapshot.id`; legen Sie Facetten für diejenigen an, nach denen Sie filtern|
|Zeilenspalten (`identity`, `name`, `status`, ...)|Log-Attribute unter dem Spaltennamen, zum Beispiel `@status`|
|Record-Body|`message`, die Zeile als JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, korrelieren jede Anweisung eines Laufs|

<br />

### Log-Abfragen {#log-queries}

Datadog beantwortet Fragen zu Vorhandensein und Historie; Joins zwischen Snapshots erfolgen in ClickHouse oder indem derselbe Stream über einen Collector an beide verteilt wird.

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

Monitore auf den Abschluss-Records überwachen die Pipeline selbst: Ein Log-Monitor auf `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` fängt ein Inventar ab, das leer zurückkam, und ein Monitor mit einer "no data"-Bedingung auf `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` fängt einen geplanten Lauf ab, der nicht stattgefunden hat.

## Geplante CI {#scheduled-ci}

Ein geplanter Job erzeugt die Serie. Das Beispiel führt beide Abfragen stündlich aus GitHub Actions aus: Der Exporter wird einmal in `OTEL_CONFIG` konfiguriert und an `--otel.config` übergeben, `TRACEPARENT` gibt jeder Anweisung eines Laufs dieselbe Trace-ID, `OTEL_RESOURCE_ATTRIBUTES` versieht jeden Record mit der Umgebung, dem Jobnamen und der Run-ID, und die `.otlp.jsonl`-Dateien werden als Artefakte aufbewahrt, sodass ein fehlgeschlagener Push trotzdem eine Kopie hinterlässt.

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

Derselbe Job auf einem beliebigen anderen Scheduler benötigt nur das Flag und die beiden Umgebungsvariablen; `TRACEPARENT` ist optional und wird pro Prozess generiert, wenn es fehlt, wobei die Anweisungen eines Laufs weiterhin eine gemeinsame Trace-ID haben.
