---
title: Рецепты для бэкендов OTel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - инвентаризация облачных ресурсов
  - обнаружение дрейфа
description: Инвентаризация облачных ресурсов, дрейф и история прав доступа в ClickHouse и Datadog на основе вывода otel из StackQL, с примером задания CI по расписанию
image: "/img/stackql-featured-image.png"
---
См. также:  
[[` Режимы вывода `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

Команда `stackql exec --output otel` превращает запрос в снимок с меткой времени: одна запись журнала OpenTelemetry на каждую строку, одна запись о завершении на каждую инструкцию и ключи корреляции `stackql.snapshot.id`, `stackql.query.hash` и `stackql.row.fingerprint` в каждой записи (см. [Вывод `otel`](/getting-started/output-modes#otel-output)).  На этой странице показан полный путь этих записей в двух бэкендах, для которых разработан этот формат: типизированная таблица инвентаризации в ClickHouse (включая ClickStack), а также запросы к журналам и мониторы в Datadog.  Последний раздел - задание CI по расписанию, которое помечает каждый запуск идентификатором трассировки и атрибутами ресурса.

В рецептах используются два повторяющихся запроса: инвентаризация экземпляров Compute Engine и привязки IAM проекта, развернутые в одну строку на каждую пару субъекта и роли.  Оба запроса включают в проекцию столбец `identity` - ключ, по которому бэкенд отслеживает ресурс (`id`, `arn`, `selfLink` или конкатенация для составного ключа); отпечаток - это значение.

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

У каждого запроса стабильное значение `stackql.query.hash`, и рецепты фильтруют по нему, чтобы серия состояла из одного повторяющегося запроса и ничего больше.  Используйте `{query_hash:String}` как параметр запроса в ClickHouse или вставьте хэш из записи о завершении.

## ClickHouse {#clickhouse}

Экспортер ClickHouse из OpenTelemetry Collector, который входит в состав ClickStack, сохраняет записи журнала в таблицу `otel_logs`, где атрибуты хранятся в виде словарей строк (`ResourceAttributes`, `LogAttributes`) рядом с `Timestamp`, `Body`, `TraceId` и `ScopeName`.  Направьте экспортер на конечную точку приема OTLP в ClickStack или на Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Типизированная таблица инвентаризации {#typed-inventory-table}

Запросы непосредственно к таблице `otel_logs` работают, но тогда каждый вопрос требует затрат на поиск по словарям.  Материализованное представление переносит записи строк в типизированную таблицу с ключом `(query_hash, identity, snapshot_time)`:

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

Записи о завершении попадают в отдельную небольшую таблицу.  Она определяет, что означает "последний снимок" во всех рецептах ниже: снимок учитывается, только когда поступила его запись о завершении, поэтому запуск, прервавшийся на полпути, никогда не принимается ошибочно за среду меньшего размера.

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

Материализованное представление видит только строки, вставленные после его создания.  Записи, поступившие раньше, загрузите задним числом с помощью инструкции `INSERT INTO ... SELECT` с той же выборкой, что и в представлении.

### Последнее состояние каждого ресурса {#latest-state-per-resource}

Текущая среда - это набор строк последнего завершенного снимка:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Чтобы получить последнее известное состояние каждого когда-либо замеченного ресурса, включая ресурсы, которые с тех пор исчезли, выполните агрегирование с помощью функции `argMax`:

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

### Дрейф между двумя снимками {#drift-between-two-snapshots}

Соедините два снимка по столбцу identity.  Неравенство отпечатков означает изменение; отсутствующая сторона означает ресурс, который появился или исчез:

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

Подставьте любые два идентификатора снимков, чтобы сравнить произвольные моменты времени.  Если нужна только одна сторона, тот же вопрос решается антисоединением:

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

Столбец `change` охватывает дрейф только в пределах проекции: столбцы, не включенные в `SELECT`, не могут вызвать изменение, поэтому запрос, который проецирует только `name` и `status`, сообщает об изменениях состояния и ни о чем больше.

### История прав доступа {#entitlement-history}

В запросе прав доступа identity - это пара из субъекта и роли, поэтому временная шкала пары в таблице инвентаризации - это срок существования этого права доступа:

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

`first_seen` и `last_seen` объединяют право, которое было отозвано, а затем предоставлено снова, в один интервал; если пропуски важны, `groupArray(snapshot_time)` с той же группировкой выводит список всех снимков, в которых присутствовала пара.  Остальные рецепты на этой странице применяются без изменений: рецепт дрейфа для запроса прав доступа выводит список предоставлений и отзывов прав между двумя запусками.

## Datadog {#datadog}

Отправляйте записи в приемник OTLP агента Datadog (включите HTTP-приемник в разделе `otlp_config` агента, порт `4318`) или OpenTelemetry Collector с экспортером Datadog.  Собственная точка приема OTLP в Datadog принимает записи напрямую, а API-ключ передается в заголовке; региональный URL-адрес возьмите из документации Datadog по приему OTLP.

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

### Сопоставление атрибутов {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Атрибут ресурса `service.name`|Тег `service` (`stackql` или `OTEL_SERVICE_NAME`)|
|Атрибут ресурса `deployment.environment`|Тег `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` и другие атрибуты ресурса|Атрибуты журнала, например `@cloud.account.id`|
|Атрибуты журнала `stackql.*`|Атрибуты журнала, например `@stackql.snapshot.id`; добавьте facets для тех атрибутов, по которым вы фильтруете|
|Столбцы строки (`identity`, `name`, `status`, ...)|Атрибуты журнала с именем столбца, например `@status`|
|Тело записи|`message`, строка в формате JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, связывающие все инструкции одного запуска|

<br />

### Запросы к журналам {#log-queries}

Datadog отвечает на вопросы о наличии и истории; соединения между снимками выполняются в ClickHouse или путем разветвления одного и того же потока в оба бэкенда через Collector.

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

Мониторы для записей о завершении следят за самим конвейером: монитор журналов по запросу `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` обнаруживает инвентаризацию, вернувшую пустой результат, а монитор с условием "no data" по запросу `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` обнаруживает запуск по расписанию, который не состоялся.

## CI по расписанию {#scheduled-ci}

Серию формирует задание по расписанию.  В примере оба запроса выполняются каждый час из GitHub Actions: экспортер настраивается один раз в переменной `OTEL_CONFIG` и передается во флаг `--otel.config`, переменная `TRACEPARENT` дает всем инструкциям одного запуска один и тот же идентификатор трассировки, переменная `OTEL_RESOURCE_ATTRIBUTES` добавляет в каждую запись среду, имя задания и идентификатор запуска, а файлы `.otlp.jsonl` сохраняются как артефакты, чтобы при неудачной отправке оставалась копия.

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

Для того же задания в любом другом планировщике нужны только флаг и две переменные среды; переменная `TRACEPARENT` необязательна: без нее каждый процесс `stackql exec` генерирует собственный идентификатор трассировки, общий только для инструкций, выполненных этим процессом, поэтому две инструкции запуска выше получили бы разные идентификаторы трассировки.
