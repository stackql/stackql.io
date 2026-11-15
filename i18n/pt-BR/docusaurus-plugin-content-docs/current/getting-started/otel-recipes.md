---
title: Receitas de backends OTel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - inventário de nuvem
  - detecção de desvios
description: Inventário de nuvem, desvio e histórico de direitos de acesso a partir da saída otel do StackQL no ClickHouse e no Datadog, com um exemplo de CI agendada
image: "/img/stackql-featured-image.png"
---
Veja também:  
[[` Modos de saída `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` transforma uma consulta em um snapshot com carimbo de data/hora: um registro de log do OpenTelemetry por linha, um registro de conclusão por instrução e as chaves de correlação `stackql.snapshot.id`, `stackql.query.hash` e `stackql.row.fingerprint` em cada registro (consulte [Saída `otel`](/getting-started/output-modes#otel-output)). Esta página acompanha esses registros de ponta a ponta nos dois backends para os quais o formato foi projetado: uma tabela de inventário tipada no ClickHouse (incluindo o ClickStack), e consultas de logs e monitores no Datadog. A última seção é um job de CI agendado que marca cada execução com um ID de trace e atributos de recurso.

As receitas usam duas consultas recorrentes, um inventário de instâncias do Compute Engine e as vinculações do IAM de um projeto desaninhadas em uma linha por principal e role. Ambas projetam uma coluna `identity`, a chave pela qual o backend acompanha um recurso (`id`, `arn`, `selfLink` ou uma concatenação para uma chave composta); a impressão digital é o valor.

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

Cada consulta tem um `stackql.query.hash` estável, e as receitas filtram por ele para que uma série seja uma única consulta recorrente e nada mais. Use `{query_hash:String}` como parâmetro de consulta no ClickHouse, ou cole o hash de um registro de conclusão.

## ClickHouse {#clickhouse}

O exportador ClickHouse do OpenTelemetry Collector, que o ClickStack inclui, grava os registros de log em `otel_logs` com os atributos como mapas de strings (`ResourceAttributes`, `LogAttributes`) junto com `Timestamp`, `Body`, `TraceId` e `ScopeName`. Aponte o exportador para a ingestão OTLP do ClickStack ou para um Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Tabela de inventário tipada {#typed-inventory-table}

Consultar `otel_logs` diretamente funciona, mas cada pergunta passa a pagar pelas buscas nos mapas. Uma view materializada transfere os registros de linha para uma tabela tipada com a chave `(query_hash, identity, snapshot_time)`:

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

Os registros de conclusão vão para uma pequena tabela própria. Ela define o que "o snapshot mais recente" significa em todas as receitas abaixo: um snapshot só conta depois que o seu registro de conclusão chega, de modo que uma execução que falha no meio do caminho nunca é confundida com um parque menor.

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

Uma view materializada só vê as linhas inseridas depois da sua criação. Preencha retroativamente os registros que chegaram antes com um `INSERT INTO ... SELECT` que use o mesmo select da view.

### Estado mais recente por recurso {#latest-state-per-resource}

O parque atual é o conjunto de linhas do snapshot completo mais recente:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Para obter o último estado conhecido de cada recurso já visto, incluindo os recursos que desapareceram desde então, agregue com `argMax`:

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

### Desvio entre dois snapshots {#drift-between-two-snapshots}

Faça o join dos dois snapshots pela identidade. Uma impressão digital diferente é uma alteração; um lado ausente é um recurso que apareceu ou desapareceu:

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

Substitua por quaisquer dois IDs de snapshot para comparar pontos arbitrários no tempo. Quando só um dos lados interessa, a mesma pergunta é um anti-join:

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

A coluna `change` é o escopo de desvio da projeção: colunas deixadas de fora do `SELECT` não podem causar uma alteração, portanto uma consulta que projeta apenas `name` e `status` relata alterações de estado e nada mais.

### Histórico de direitos de acesso {#entitlement-history}

Na consulta de direitos de acesso, a identidade é o par de principal e role, portanto a linha do tempo de um par na tabela de inventário é o tempo de vida desse direito de acesso:

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

`first_seen` e `last_seen` reúnem em um único intervalo uma concessão que foi revogada e depois concedida novamente; quando as lacunas importam, `groupArray(snapshot_time)` no mesmo agrupamento lista cada snapshot em que o par esteve presente. Todo o restante desta página se aplica sem alterações: a receita de desvio aplicada à consulta de direitos de acesso lista as concessões e revogações entre duas execuções.

## Datadog {#datadog}

Envie os registros para o receptor OTLP de um Datadog Agent (habilite o receptor HTTP no `otlp_config` do Agent, porta `4318`) ou de um OpenTelemetry Collector que execute o exportador do Datadog. A própria ingestão OTLP do Datadog recebe os registros diretamente, com a chave de API como cabeçalho; obtenha a URL regional na documentação de ingestão OTLP do Datadog.

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

### Mapeamento de atributos {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Atributo de recurso `service.name`|Tag `service` (`stackql` ou `OTEL_SERVICE_NAME`)|
|Atributo de recurso `deployment.environment`|Tag `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` e outros atributos de recurso|Atributos de log, por exemplo `@cloud.account.id`|
|Atributos de log `stackql.*`|Atributos de log, por exemplo `@stackql.snapshot.id`; adicione facets para os que você usa como filtro|
|Colunas da linha (`identity`, `name`, `status`, ...)|Atributos de log com o nome da coluna, por exemplo `@status`|
|Corpo do registro|`message`, a linha como JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, que correlacionam todas as instruções de uma execução|

<br />

### Consultas de logs {#log-queries}

O Datadog responde a perguntas de presença e de histórico; os joins entre snapshots são feitos no ClickHouse, ou fazendo o fan-out do mesmo fluxo para os dois por meio de um Collector.

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

Monitores sobre os registros de conclusão vigiam o próprio pipeline: um monitor de logs em `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` detecta um inventário que voltou vazio, e um monitor com uma condição "no data" em `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` detecta uma execução agendada que não aconteceu.

## CI agendada {#scheduled-ci}

Um job agendado gera a série. O exemplo executa as duas consultas a cada hora no GitHub Actions: o exportador é configurado uma vez em `OTEL_CONFIG` e passado para `--otel.config`, `TRACEPARENT` dá a todas as instruções de uma execução o mesmo ID de trace, `OTEL_RESOURCE_ATTRIBUTES` marca o ambiente, o nome do job e o ID da execução em cada registro, e os arquivos `.otlp.jsonl` são mantidos como artefatos para que um envio com falha ainda deixe uma cópia.

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

O mesmo job em qualquer outro agendador precisa apenas da opção e das duas variáveis de ambiente; `TRACEPARENT` é opcional: sem ele, cada processo `stackql exec` gera o próprio ID de trace, que só as instruções executadas por esse processo compartilham, de modo que as duas instruções da execução acima teriam IDs de trace diferentes.
