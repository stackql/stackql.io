---
title: Recetas para backends de OTel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - inventario de nube
  - detección de desviaciones
description: Inventario de nube, desviación e historial de derechos de acceso a partir de la salida otel de StackQL en ClickHouse y Datadog, con un ejemplo de CI programada
image: "/img/stackql-featured-image.png"
---
Consulte también:  
[[` Modos de salida `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` convierte una consulta en una instantánea con marca de tiempo: un registro de log de OpenTelemetry por fila, un registro de finalización por instrucción y las claves de correlación `stackql.snapshot.id`, `stackql.query.hash` y `stackql.row.fingerprint` en cada registro (consulte [Salida `otel`](/getting-started/output-modes#otel-output)). Esta página sigue esos registros de extremo a extremo en los dos backends para los que se diseñó el formato: una tabla de inventario tipada en ClickHouse (incluido ClickStack), y consultas de logs y monitores en Datadog. La última sección es un trabajo de CI programado que marca cada ejecución con un identificador de traza y atributos de recurso.

Las recetas usan dos consultas recurrentes, un inventario de instancias de Compute Engine y las vinculaciones de IAM de un proyecto desanidadas en una fila por principal y rol. Ambas proyectan una columna `identity`, la clave con la que el backend sigue un recurso (`id`, `arn`, `selfLink` o una concatenación para una clave compuesta); la huella digital es el valor.

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

Cada consulta tiene un `stackql.query.hash` estable, y las recetas filtran por él para que una serie sea una consulta recurrente y nada más. Use `{query_hash:String}` como parámetro de consulta en ClickHouse, o pegue el hash de un registro de finalización.

## ClickHouse {#clickhouse}

El exportador de ClickHouse de OpenTelemetry Collector, que ClickStack incluye, deposita los registros de log en `otel_logs` con los atributos como mapas de cadenas (`ResourceAttributes`, `LogAttributes`) junto con `Timestamp`, `Body`, `TraceId` y `ScopeName`. Apunte el exportador a la ingesta OTLP de ClickStack o a un Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Tabla de inventario tipada {#typed-inventory-table}

Consultar `otel_logs` directamente funciona, pero entonces cada pregunta paga las búsquedas en los mapas. Una vista materializada lleva los registros de fila a una tabla tipada cuya clave es `(query_hash, identity, snapshot_time)`:

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

Los registros de finalización van a una pequeña tabla propia. Esta define qué significa "la instantánea más reciente" en todas las recetas siguientes: una instantánea cuenta una vez que ha llegado su registro de finalización, de modo que una ejecución que falla a mitad de camino nunca se confunde con un parque más pequeño.

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

Una vista materializada solo ve las filas insertadas después de su creación. Rellene retroactivamente los registros que llegaron antes con un `INSERT INTO ... SELECT` que use el mismo select que la vista.

### Estado más reciente por recurso {#latest-state-per-resource}

El parque actual es el conjunto de filas de la instantánea completa más reciente:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Para obtener el último estado conocido de cada recurso visto alguna vez, incluidos los recursos que han desaparecido desde entonces, realice la agregación con `argMax`:

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

### Desviación entre dos instantáneas {#drift-between-two-snapshots}

Haga un join de las dos instantáneas por identidad. Una desigualdad de huellas digitales es un cambio; un lado ausente es un recurso que apareció o desapareció:

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

Sustituya dos identificadores de instantánea cualesquiera para comparar puntos arbitrarios en el tiempo. Cuando solo se quiere un lado, la misma pregunta es un anti-join:

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

La columna `change` es el ámbito de desviación de la proyección: las columnas que quedan fuera del `SELECT` no pueden causar un cambio, por lo que una consulta que proyecta solo `name` y `status` informa de los cambios de estado y de nada más.

### Historial de derechos de acceso {#entitlement-history}

En la consulta de derechos de acceso la identidad es el par de principal y rol, por lo que la línea de tiempo de un par en la tabla de inventario es el tiempo de vida de ese derecho de acceso:

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

`first_seen` y `last_seen` reducen a un solo intervalo una concesión que se revocó y más tarde se volvió a conceder; `groupArray(snapshot_time)` sobre la misma agrupación enumera todas las instantáneas en las que el par estuvo presente, para cuando los huecos importan. Todo lo demás de esta página se aplica sin cambios: la receta de desviación sobre la consulta de derechos de acceso enumera las concesiones y revocaciones entre dos ejecuciones.

## Datadog {#datadog}

Envíe los registros al receptor OTLP de un Datadog Agent (habilite el receptor HTTP en el `otlp_config` del Agent, puerto `4318`) o de un OpenTelemetry Collector que ejecute el exportador de Datadog. La ingesta OTLP propia de Datadog recibe los registros directamente con la clave de API como encabezado; tome la URL regional de la documentación de ingesta OTLP de Datadog.

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

### Correspondencia de atributos {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Atributo de recurso `service.name`|Tag `service` (`stackql` u `OTEL_SERVICE_NAME`)|
|Atributo de recurso `deployment.environment`|Tag `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` y otros atributos de recurso|Atributos de log, por ejemplo `@cloud.account.id`|
|Atributos de log `stackql.*`|Atributos de log, por ejemplo `@stackql.snapshot.id`; agregue facetas para aquellos por los que filtre|
|Columnas de fila (`identity`, `name`, `status`, ...)|Atributos de log con el nombre de la columna, por ejemplo `@status`|
|Cuerpo del registro|`message`, la fila como JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, que correlacionan todas las instrucciones de una ejecución|

<br />

### Consultas de logs {#log-queries}

Datadog responde preguntas de presencia e historial; los joins entre instantáneas se hacen en ClickHouse, o mediante un fan-out del mismo flujo hacia ambos a través de un Collector.

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

Los monitores sobre los registros de finalización supervisan el propio pipeline: un monitor de logs sobre `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` detecta un inventario que volvió vacío, y un monitor con una condición "no data" sobre `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` detecta una ejecución programada que no se produjo.

## CI programada {#scheduled-ci}

Un trabajo programado genera la serie. El ejemplo ejecuta ambas consultas cada hora desde GitHub Actions: el exportador se configura una vez en `OTEL_CONFIG` y se pasa a `--otel.config`, `TRACEPARENT` da a todas las instrucciones de una ejecución el mismo identificador de traza, `OTEL_RESOURCE_ATTRIBUTES` marca cada registro con el entorno, el nombre del trabajo y el identificador de ejecución, y los archivos `.otlp.jsonl` se conservan como artefactos para que un envío fallido deje igualmente una copia.

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

El mismo trabajo en cualquier otro planificador solo necesita la opción y las dos variables de entorno; `TRACEPARENT` es opcional y, cuando falta, se genera por proceso, en cuyo caso las instrucciones de una ejecución siguen compartiendo un identificador de traza.
