---
title: Рецепти для бекендів OTel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - інвентаризація хмарних ресурсів
  - виявлення дрейфу
description: Інвентаризація хмарних ресурсів, дрейф та історія прав доступу з виводу otel StackQL у ClickHouse і Datadog із прикладом CI за розкладом
image: "/img/stackql-featured-image.png"
---
Див. також:  
[[` Режими виводу `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

Команда `stackql exec --output otel` перетворює запит на знімок із позначкою часу: один запис журналу OpenTelemetry на кожен рядок, один запис про завершення на кожну інструкцію, а також ключі кореляції `stackql.snapshot.id`, `stackql.query.hash` і `stackql.row.fingerprint` у кожному записі (див. [Вивід `otel`](/getting-started/output-modes#otel-output)).  На цій сторінці ці записи простежено від початку до кінця у двох бекендах, для яких розроблено цей формат: типізована таблиця інвентаризації в ClickHouse (зокрема ClickStack), а також запити до журналів і монітори в Datadog.  Останній розділ - це завдання CI за розкладом, яке позначає кожен запуск ідентифікатором трасування й атрибутами ресурсу.

У рецептах використано два повторювані запити: інвентаризацію екземплярів Compute Engine і прив'язки IAM проєкту, розгорнуті до одного рядка для кожної пари principal і ролі.  Обидва запити включають у проєкцію стовпець `identity` - ключ, за яким бекенд відстежує ресурс (`id`, `arn`, `selfLink` або конкатенація для складеного ключа); відбиток - це значення.

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

Кожен запит має стабільне значення `stackql.query.hash`, і рецепти фільтрують за ним, щоб серія складалася з одного повторюваного запиту й нічого більше.  Використовуйте `{query_hash:String}` як параметр запиту в ClickHouse або вставте хеш із запису про завершення.

## ClickHouse {#clickhouse}

Експортер ClickHouse з OpenTelemetry Collector, який постачається в складі ClickStack, зберігає записи журналу в `otel_logs`, де атрибути представлено як словники рядкових значень (`ResourceAttributes`, `LogAttributes`) поряд із `Timestamp`, `Body`, `TraceId` і `ScopeName`.  Спрямуйте експортер на кінцеву точку приймання OTLP у ClickStack або на Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Типізована таблиця інвентаризації {#typed-inventory-table}

Запити безпосередньо до `otel_logs` працюють, але тоді кожне запитання витрачає ресурси на пошук у словниках.  Матеріалізоване подання переносить записи рядків у типізовану таблицю з ключем `(query_hash, identity, snapshot_time)`:

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

Записи про завершення потрапляють в окрему невелику таблицю.  Вона визначає, що означає "останній знімок" у кожному рецепті нижче: знімок зараховується, щойно надійде його запис про завершення, тому запуск, що завершився помилкою на півдорозі, ніколи не сприймається як менше середовище.

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

Матеріалізоване подання бачить лише рядки, вставлені після його створення.  Записи, що надійшли раніше, дозаповніть за допомогою `INSERT INTO ... SELECT` з тим самим запитом select, що й у поданні.

### Останній стан кожного ресурсу {#latest-state-per-resource}

Поточне середовище - це набір рядків останнього повного знімка:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Щоб отримати останній відомий стан кожного ресурсу, який будь-коли спостерігався, зокрема ресурсів, що відтоді зникли, виконайте агрегацію за допомогою `argMax`:

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

### Дрейф між двома знімками {#drift-between-two-snapshots}

З'єднайте два знімки за ідентичністю.  Нерівність відбитків означає зміну; відсутня сторона означає ресурс, який з'явився або зник:

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

Підставте будь-які два ідентифікатори знімків, щоб порівняти довільні моменти часу.  Коли потрібна лише одна сторона, те саме запитання розв'язує антиз'єднання:

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

Стовпець `change` відображає дрейф в області, яку визначає проєкція: стовпці, не включені в `SELECT`, не можуть спричинити зміну, тому запит, який включає в проєкцію лише `name` і `status`, повідомляє про зміни стану й ні про що інше.

### Історія прав доступу {#entitlement-history}

Для запиту прав доступу ідентичність - це пара principal і роль, тому часова шкала пари в таблиці інвентаризації - це час існування цього права доступу:

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

Стовпці `first_seen` і `last_seen` об'єднують в один проміжок право, яке було відкликано, а пізніше надано знову; якщо прогалини важливі, `groupArray(snapshot_time)` з тим самим групуванням перелічує кожен знімок, у якому була присутня пара.  Усе інше на цій сторінці застосовується без змін: рецепт дрейфу для запиту прав доступу перелічує надання й відкликання прав між двома запусками.

## Datadog {#datadog}

Надсилайте записи на приймач OTLP у Datadog Agent (увімкніть приймач HTTP у `otlp_config` агента, порт `4318`) або в OpenTelemetry Collector, у якому працює експортер Datadog.  Власна кінцева точка приймання OTLP у Datadog приймає записи безпосередньо, з ключем API в заголовку; регіональну адресу URL візьміть із документації Datadog про приймання даних OTLP.

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

### Зіставлення атрибутів {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Атрибут ресурсу `service.name`|Тег `service` (`stackql` або `OTEL_SERVICE_NAME`)|
|Атрибут ресурсу `deployment.environment`|Тег `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` та інші атрибути ресурсу|Атрибути журналу, наприклад `@cloud.account.id`|
|Атрибути журналу `stackql.*`|Атрибути журналу, наприклад `@stackql.snapshot.id`; додайте facets для тих атрибутів, за якими ви фільтруєте|
|Стовпці рядка (`identity`, `name`, `status`, ...)|Атрибути журналу з назвою стовпця, наприклад `@status`|
|Тіло запису|`message`, рядок у форматі JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, що пов'язують усі інструкції одного запуску|

<br />

### Запити до журналів {#log-queries}

Datadog відповідає на запитання про наявність та історію; з'єднання між знімками виконують у ClickHouse або розсилаючи той самий потік в обидва бекенди через Collector.

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

Монітори на записах про завершення стежать за самим конвеєром: монітор журналів на `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` виявляє інвентаризацію, яка повернулася порожньою, а монітор з умовою "no data" на `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` виявляє запуск за розкладом, який не відбувся.

## CI за розкладом {#scheduled-ci}

Серію створює завдання за розкладом.  У прикладі обидва запити запускаються щогодини з GitHub Actions: експортер налаштовано один раз у змінній `OTEL_CONFIG` і передано прапорцю `--otel.config`, змінна `TRACEPARENT` надає всім інструкціям запуску той самий ідентифікатор трасування, змінна `OTEL_RESOURCE_ATTRIBUTES` позначає кожен запис середовищем, назвою завдання та ідентифікатором запуску, а файли `.otlp.jsonl` зберігаються як артефакти, тож навіть у разі невдалого надсилання залишається копія.

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

Тому самому завданню в будь-якому іншому планувальнику потрібні лише прапорець і дві змінні середовища; змінна `TRACEPARENT` необов'язкова: без неї кожен процес `stackql exec` генерує власний ідентифікатор трасування, спільний лише для інструкцій, які виконує цей процес, тому дві інструкції запуску вище мали б різні ідентифікатори трасування.
