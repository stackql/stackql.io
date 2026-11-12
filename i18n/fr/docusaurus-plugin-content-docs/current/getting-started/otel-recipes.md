---
title: Recettes pour backends OTel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - inventaire cloud
  - détection de dérive
description: Inventaire cloud, dérive et historique des droits d'accès à partir de la sortie otel de StackQL dans ClickHouse et Datadog, avec un exemple de CI planifiée
image: "/img/stackql-featured-image.png"
---
Voir aussi :  
[[` Modes de sortie `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` transforme une requête en instantané horodaté : un enregistrement de journal OpenTelemetry par ligne, un enregistrement de fin par instruction, et les clés de corrélation `stackql.snapshot.id`, `stackql.query.hash` et `stackql.row.fingerprint` sur chaque enregistrement (consultez [Sortie `otel`](/getting-started/output-modes#otel-output)). Cette page suit ces enregistrements de bout en bout dans les deux backends pour lesquels le format a été conçu : une table d'inventaire typée dans ClickHouse (ClickStack compris), et des requêtes de journaux et des monitors dans Datadog. La dernière section est une tâche CI planifiée qui appose sur chaque exécution un identifiant de trace et des attributs de ressource.

Les recettes utilisent deux requêtes récurrentes : un inventaire des instances Compute Engine et les liaisons IAM d'un projet, désimbriquées à raison d'une ligne par principal et par rôle. Les deux projettent une colonne `identity`, la clé par laquelle le backend suit une ressource (`id`, `arn`, `selfLink`, ou une concaténation pour une clé composite) ; l'empreinte est la valeur.

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

Chaque requête a un `stackql.query.hash` stable, et les recettes filtrent sur cette valeur, de sorte qu'une série correspond à une seule requête récurrente et à rien d'autre. Utilisez `{query_hash:String}` comme paramètre de requête dans ClickHouse, ou collez le hash issu d'un enregistrement de fin.

## ClickHouse {#clickhouse}

L'exportateur ClickHouse de l'OpenTelemetry Collector, fourni avec ClickStack, dépose les enregistrements de journal dans `otel_logs`, avec les attributs sous forme de maps de chaînes de caractères (`ResourceAttributes`, `LogAttributes`) aux côtés de `Timestamp`, `Body`, `TraceId` et `ScopeName`. Faites pointer l'exportateur vers le point d'ingestion OTLP de ClickStack ou vers un Collector :

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Table d'inventaire typée {#typed-inventory-table}

Interroger directement `otel_logs` fonctionne, mais chaque question paie alors le coût des recherches dans les maps. Une vue matérialisée extrait les enregistrements de ligne vers une table typée dont la clé est `(query_hash, identity, snapshot_time)` :

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

Les enregistrements de fin vont dans une petite table qui leur est propre. Elle définit ce que signifie "le dernier instantané" dans toutes les recettes ci-dessous : un instantané compte dès que son enregistrement de fin est arrivé, de sorte qu'une exécution qui échoue en cours de route n'est jamais prise pour un parc plus petit.

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

Une vue matérialisée ne voit que les lignes insérées après sa création. Chargez rétroactivement les enregistrements arrivés auparavant avec un `INSERT INTO ... SELECT` utilisant le même select que la vue.

### Dernier état par ressource {#latest-state-per-resource}

Le parc actuel est le jeu de lignes du dernier instantané complet :

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Pour obtenir le dernier état connu de chaque ressource jamais observée, y compris celles qui ont disparu depuis, agrégez avec `argMax` :

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

### Dérive entre deux instantanés {#drift-between-two-snapshots}

Joignez les deux instantanés sur l'identité. Une différence d'empreinte est un changement ; un côté manquant est une ressource apparue ou disparue :

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

Substituez deux identifiants d'instantané quelconques pour comparer des points arbitraires dans le temps. Lorsqu'un seul côté est souhaité, la même question devient une anti-jointure :

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

La colonne `change` a pour périmètre de dérive la projection : les colonnes laissées hors du `SELECT` ne peuvent pas provoquer de changement, de sorte qu'une requête qui ne projette que `name` et `status` signale les changements d'état et rien d'autre.

### Historique des droits d'accès {#entitlement-history}

Pour la requête sur les droits d'accès, l'identité est la paire principal et rôle, de sorte que la chronologie d'une paire dans la table d'inventaire correspond à la durée de vie de ce droit d'accès :

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

`first_seen` et `last_seen` ramènent à un seul intervalle une attribution révoquée puis accordée de nouveau ; lorsque les interruptions comptent, `groupArray(snapshot_time)` sur le même regroupement liste tous les instantanés dans lesquels la paire était présente. Tout le reste de cette page s'applique sans changement : la recette de dérive appliquée à la requête sur les droits d'accès liste les attributions et les révocations entre deux exécutions.

## Datadog {#datadog}

Envoyez les enregistrements au récepteur OTLP d'un Datadog Agent (activez le récepteur HTTP dans le `otlp_config` de l'Agent, port `4318`) ou d'un OpenTelemetry Collector exécutant l'exportateur Datadog. Le point d'ingestion OTLP propre à Datadog accepte directement les enregistrements, avec la clé d'API en en-tête ; prenez l'URL régionale dans la documentation de Datadog sur l'ingestion OTLP.

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

### Correspondance des attributs {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Attribut de ressource `service.name`|Tag `service` (`stackql`, ou `OTEL_SERVICE_NAME`)|
|Attribut de ressource `deployment.environment`|Tag `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` et autres attributs de ressource|Attributs de journal, par exemple `@cloud.account.id`|
|Attributs de journal `stackql.*`|Attributs de journal, par exemple `@stackql.snapshot.id` ; ajoutez des facettes pour ceux sur lesquels vous filtrez|
|Colonnes de la ligne (`identity`, `name`, `status`, ...)|Attributs de journal portant le nom de la colonne, par exemple `@status`|
|Corps de l'enregistrement|`message`, la ligne au format JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, qui corrèlent toutes les instructions d'une même exécution|

<br />

### Requêtes de journaux {#log-queries}

Datadog répond aux questions de présence et d'historique ; les jointures entre instantanés se font dans ClickHouse, ou par fan-out du même flux vers les deux via un Collector.

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

Les monitors sur les enregistrements de fin surveillent le pipeline lui-même : un monitor de journaux sur `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` détecte un inventaire revenu vide, et un monitor avec une condition "no data" sur `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` détecte une exécution planifiée qui n'a pas eu lieu.

## CI planifiée {#scheduled-ci}

Une tâche planifiée produit la série. L'exemple exécute les deux requêtes toutes les heures depuis GitHub Actions : l'exportateur est configuré une seule fois dans `OTEL_CONFIG` et transmis à `--otel.config`, `TRACEPARENT` donne le même identifiant de trace à toutes les instructions d'une exécution, `OTEL_RESOURCE_ATTRIBUTES` appose l'environnement, le nom de la tâche et l'identifiant d'exécution sur chaque enregistrement, et les fichiers `.otlp.jsonl` sont conservés comme artefacts, de sorte qu'un push en échec laisse tout de même une copie.

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

La même tâche sur tout autre ordonnanceur ne nécessite que l'option et les deux variables d'environnement ; `TRACEPARENT` est facultatif et est généré par processus lorsqu'il est absent, auquel cas les instructions d'une même exécution partagent tout de même un identifiant de trace.
