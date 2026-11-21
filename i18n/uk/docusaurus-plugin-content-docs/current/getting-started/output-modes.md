---
title: Режими виводу
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Режими виводу StackQL: table, json, jsonl, csv, text і otel, як їх вибрати та що виводить кожен із них в оболонці, у команді exec і на сервері."
image: "/img/stackql-featured-image.png"
---
Див. також:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Результати запитів StackQL можна виводити в табличному форматі (table), а також у форматах json, jsonl (JSON, розділений символами нового рядка), csv, text або otel (записи журналу OpenTelemetry).  Формати JSON, JSONL і CSV можна використовувати для обміну даними з іншими програмами; формат `otel` надсилає набір результатів у бекенд OpenTelemetry як знімок із позначкою часу.  Потрібний формат виводу вказують за допомогою прапорця StackQL `--output`.

### Формат виводу `table` {#table-output-format}
Результати можна відформатувати як таблицю за допомогою значення `table` параметра `--output`, як показано нижче (це значення за замовчуванням для інтерактивної оболонки StackQL):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output table

|---------------------|-----------------|
|         ID          |      NAME       |
|---------------------|-----------------|
| 1257085253691867467 | demo-instance-1 |
|---------------------|-----------------|
| 2586731219281477960 | demo-instance-2 |
|---------------------|-----------------|
| 5584456226809282885 | demo-instance-3 |
|---------------------|-----------------|
```

### Вивід `json` {#json-output}
Результати можна отримати у форматі JSON за допомогою значення `json` параметра `--output`, як показано нижче; цей формат виводу зручний, якщо результати запиту потрібно передати зовнішньому процесу або скрипту:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Вивід `jsonl` {#jsonl-output}
Результати можна отримати у форматі JSON, розділеному символами нового рядка (JSON Lines), за допомогою значення `jsonl` параметра `--output`, як показано нижче (також приймається псевдонім `ndjson`).  Кожен рядок результату виводиться як один об'єкт JSON в окремому рядку, а рядки скидаються у вивід одразу під час запису, тому цей формат виводу зручний для потокової передачі результатів у рядково-орієнтовані інструменти або для передавання великих наборів результатів іншим програмам через канал:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Вивід `csv` {#csv-output}
Результати можна отримати у форматі CSV за допомогою значення `csv` параметра `--output`, як показано нижче; цей формат виводу зручний для обробки результатів в Excel або для надання інтерфейсу даних іншій системі:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output csv

id,name
1257085253691867467,demo-instance-1
2586731219281477960,demo-instance-2
5584456226809282885,demo-instance-3
```

#### Приклад виводу CSV (з альтернативним роздільником) {#csv-output-example-with-alternative-delimiter}
Для формату виводу `csv` можна вказати інший роздільник за допомогою прапорця `-d`, як показано нижче:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output csv -d "|"

id|name
1257085253691867467|demo-instance-1
2586731219281477960|demo-instance-2
5584456226809282885|demo-instance-3
```

### Вивід `text` {#text-output}
Результати можна отримати як звичайний текст за допомогою значення `text` параметра `--output`.  Кожен рядок результату виводиться як його значення через кому, а перед рядками виводиться рядок заголовків, якщо не вказано `-H` (`--hideheaders`).  На відміну від `csv`, значення не беруться в лапки й не екрануються, а роздільник `-d` ігнорується, тому значення, що містить кому або подвійну лапку, виводиться як є:

```shell
stackql exec "SELECT 'a,b' AS letters, 'say \"hi\"' AS greeting" --output text

letters,greeting
a,b,say "hi"
```

Із параметром `--output csv` той самий запит повертає як рядок даних `"a,b","say ""hi"""`.

### Вивід `otel` {#otel-output}
Результати можна виводити як записи журналу OpenTelemetry за допомогою значення `otel` параметра `--output`.  Кожен рядок результату записується в окремий рядок виводу як один об'єкт OTLP/JSON `LogsData` і скидається у вивід одразу під час запису, як у форматі `jsonl`; після них іде один запис про завершення інструкції.  Цей формат виводу перетворює запит на знімок хмарної конфігурації у формі телеметрії: запускайте запит інвентаризації за розкладом, надсилайте записи в бекенд із підтримкою OTLP (ClickHouse, Datadog або OpenTelemetry Collector перед будь-яким іншим бекендом) і вже там відповідайте на запитання "який вигляд має середовище зараз", "що змінилося після останнього запуску" і "хто мав яке право доступу і відколи".  Доступно в StackQL `v0.11.660` і новіших версіях.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

Один запис рядка у відформатованому вигляді (у файлі кожен запис займає один рядок):

```json
{"resourceLogs":[{"resource":{"attributes":[
  {"key":"service.name","value":{"stringValue":"stackql"}},
  {"key":"service.version","value":{"stringValue":"0.11.660"}},
  {"key":"cloud.provider","value":{"stringValue":"gcp"}},
  {"key":"cloud.account.id","value":{"stringValue":"stackql-demo"}},
  {"key":"cloud.availability_zone","value":{"stringValue":"australia-southeast1-a"}}
]},"scopeLogs":[{"scope":{"name":"github.com/stackql/stackql/internal/stackql/output","version":"1.0.0"},
"logRecords":[{
  "timeUnixNano":"1788739200000000000","observedTimeUnixNano":"1788739203412000000",
  "severityNumber":9,"severityText":"INFO",
  "body":{"stringValue":"{\"id\":\"1257085253691867467\",\"name\":\"demo-instance-1\"}"},
  "attributes":[
    {"key":"id","value":{"stringValue":"1257085253691867467"}},
    {"key":"name","value":{"stringValue":"demo-instance-1"}},
    {"key":"stackql.snapshot.id","value":{"stringValue":"1e0d4a6c-4b0f-4b4e-9f7d-2a6c9a1f3b21"}},
    {"key":"stackql.query","value":{"stringValue":"select id, name from google.compute.instances where project = 'stackql-demo' and zone = 'australia-southeast1-a'"}},
    {"key":"stackql.query.hash","value":{"stringValue":"9c1d2e..."}},
    {"key":"stackql.provider","value":{"stringValue":"google"}},
    {"key":"stackql.service","value":{"stringValue":"compute"}},
    {"key":"stackql.resource","value":{"stringValue":"instances"}},
    {"key":"stackql.row.index","value":{"intValue":"0"}},
    {"key":"stackql.row.fingerprint","value":{"stringValue":"sha256:7f3a..."}}
  ],
  "traceId":"4bf92f3577b34da6a3ce929d0e0e4736","spanId":"a1b2c3d4e5f60718"
}],"schemaUrl":"https://opentelemetry.io/schemas/1.44.0"}],"schemaUrl":"https://opentelemetry.io/schemas/1.44.0"}]}
```

#### Знімки {#snapshots}

Одна інструкція - один знімок.  Кожен запис інструкції містить те саме значення `timeUnixNano` (час початку інструкції), той самий `stackql.snapshot.id` і той самий `spanId`, тому запити стану на певний момент часу, порівняння й запити останнього стану працюють, навіть якщо рядки надходять кількома сторінками протягом кількох секунд; `observedTimeUnixNano` фіксує, коли кожен запис було фактично записано.  Усі інструкції одного виклику мають спільний `traceId`, який береться зі змінної середовища `TRACEPARENT`, якщо її встановлено (так прийнято в конвеєрах CI), а інакше генерується.

Останній запис інструкції - запис про завершення: тіло `snapshot complete <provider.service.resource>` з атрибутами `stackql.snapshot.complete=true`, `stackql.rows_returned` і `stackql.duration_ms`.  Він повідомляє бекенду, що знімок повний, і навіть інструкція, яка не повертає рядків, виводить його, тому ресурс, що зник, видно як дрейф, а не як відсутність даних.  Інструкція, що завершилася помилкою, створює запис `ERROR` з атрибутами `error.type` і `error.message`, якщо StackQL запущено з прапорцем `--errorpresentation record`; за замовчуванням помилки, як і для всіх інших форматів виводу, надходять у stderr.

#### Форма запису {#record-shape}

Тіло - це повний рядок як компактний JSON зі збереженими вкладеними об'єктами; воно є джерелом істини для повного документа.  Атрибути містять кожен стовпець під його власним ім'ям, із типом, якщо значення скалярне (`stringValue`, `intValue`, `doubleValue`, `boolValue`); вкладені стовпці передаються як рядки JSON, що сумісно з бекендами, схема атрибутів яких за замовчуванням рядкова, а стовпці зі значенням null пропускаються.  Зарезервовані атрибути `stackql.*` такі:

| Атрибут | Запис | Значення |
|--|--|--|
|`stackql.snapshot.id`|усі|UUID, що генерується для кожного виконання інструкції|
|`stackql.query`|усі|Надісланий текст запиту дослівно|
|`stackql.query.hash`|усі|SHA-256 тексту запиту; дешевший ключ серії, ніж SQL, для регулярного завдання|
|`stackql.provider`, `stackql.service`, `stackql.resource`|усі|Основна таблиця інструкції; пропускається для з'єднань і подань, що охоплюють кілька таблиць|
|`stackql.row.index`|рядок|Позиція в наборі результатів, починаючи з нуля|
|`stackql.row.fingerprint`|рядок|`sha256:` канонічного JSON рядка, тож дрейф визначається одним порівнянням на кожну ідентичність замість порівняння всіх атрибутів|
|`stackql.snapshot.complete`|завершення|`true`|
|`stackql.rows_returned`|завершення|Кількість рядків інструкції|
|`stackql.duration_ms`|завершення|Фактична тривалість від початку інструкції|

<br />

Набір атрибутів у виводі - це версіонований інтерфейс: область інструментування - `github.com/stackql/stackql/internal/stackql/output` версії `1.0.0`, а `schemaUrl` закріплює стабільні конвенції на `https://opentelemetry.io/schemas/1.44.0`.

Ідентичність ресурсу не визначається автоматично: відбиток - це значення, а ключ вибирає бекенд (`id`, `arn`, `selfLink` або кортеж стовпців).  Щоб відстежувати права доступу як сутності, розкладіть прив'язки в SQL на окремі рядки, один рядок на кожну пару principal і роль; телеметрія відповідає гранулярності результату.

#### Атрибути ресурсу {#resource-attributes}

Ресурс містить `service.name` (`stackql` або значення `OTEL_SERVICE_NAME`) і `service.version`, хмарні конвенції OpenTelemetry, якщо їх можна вивести з інструкції `SELECT` з однією таблицею (`cloud.provider` - з провайдера; `cloud.account.id` - з умови рівності для `project` або `subscriptionId` у реченні `WHERE`; `cloud.region` - з `region`; `cloud.availability_zone` - з `zone`), а також кожну пару зі стандартної змінної середовища `OTEL_RESOURCE_ATTRIBUTES`, тож крок конвеєра може додати `deployment.environment`, назву завдання або теги облікового запису без додаткових прапорців:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### Доставлення записів {#shipping-the-records}

Файл містить один `LogsData` на рядок - саме в такій формі приймач `otlp_json_file` з OpenTelemetry Collector приймає дані без процесора transform:

```yaml
receivers:
  otlp_json_file:
    include: [/var/lib/stackql/inventory/*.otlp.jsonl]
exporters:
  otlphttp:
    endpoint: https://otlp.example.com
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [otlphttp]
```

Кожен рядок також є повним об'єктом OTLP/JSON `LogsData`, який кінцева точка OTLP/HTTP `POST /v1/logs` приймає як тіло запиту.  Вбудований експортер для надсилання використовує те саме кодування, але групує записи в пакети, як описано нижче.

#### Надсилання до кінцевої точки OTLP {#pushing-to-an-otlp-endpoint}

Записи можна надсилати безпосередньо в кінцеву точку журналів OTLP/HTTP, а також записувати у файл або stdout, налаштувавши експортер у прапорці `--otel.config` - рядку JSON або YAML, як і в інших прапорцях конфігурації (доступно в StackQL `v0.12.732` і новіших версіях):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| Ключ | Призначення |
|--|--|
|`exporter.endpoint`|Повна адреса URL кінцевої точки журналів, наприклад `http://localhost:4318/v1/logs`; обов'язковий для ввімкнення експортера|
|`exporter.headers`|Заголовки запиту, що надсилаються з кожним експортом, зазвичай токен приймання даних або ключ API|
|`exporter.timeout_ms`|Тайм-аут запиту в мілісекундах, за замовчуванням `10000`|
|`exporter.batch_size`|Кількість записів на запит, за замовчуванням `512`|

<br />

Кожна інструкція - один пакет: її записи рядків і запис про завершення надсилаються як один об'єкт OTLP/JSON `LogsData` (одна обгортка ресурсу та області, один елемент у `logRecords` на кожен запис), а великий набір результатів під час потокового надходження рядків розбивається на запити, кожен з яких містить не більше ніж `batch_size` записів, причому останній запит надсилається після запису про завершення.  Для відповідей `429` і `5xx` та в разі збоїв з'єднання виконується до чотирьох спроб із подвоєнням затримки, починаючи з 500ms; якщо сервер надсилає заголовок `Retry-After` із затримкою в секундах, його враховують (з обмеженням у 30 секунд); інші помилки не повторюються.  Файл або потік записується в будь-якому разі, тому невдале надсилання залишає локальну копію та рядок помилки в stderr.  Експортер працює з OTLP/JSON через HTTP; кінцевій точці, яка приймає лише protobuf, потрібен Collector перед нею.

Змінні середовища `OTEL_EXPORTER_OTLP_*` не зчитуються; усі налаштування експортера беруться з прапорця `--otel.config`.  Неправильно сформоване значення `--otel.config` або об'єкт `exporter` без `endpoint` призводить до помилки під час запуску.

Цей експортер розраховано на приймання OTLP у ClickStack (`http://<host>:4318/v1/logs`, заголовок `authorization: <ingestion API key>`), приймач OTLP у Datadog Agent або приймання OTLP у Datadog (`dd-api-key: <key>`) і будь-який приймач `otlp` в OpenTelemetry Collector.  Запити інвентаризації, дрейфу й прав доступу на стороні приймання див. на сторінці [Рецепти для бекендів OTel](/getting-started/otel-recipes).
