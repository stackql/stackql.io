---
title: Режимы вывода
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Режимы вывода StackQL: table, json, jsonl, csv, text и otel, как их выбрать и что выдает каждый из них в shell, exec и в режиме сервера."
image: "/img/stackql-featured-image.png"
---
См. также:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Результаты запросов StackQL можно выводить в табличном формате (table), а также в форматах json, jsonl (JSON, разделенный символами новой строки), csv, text или otel (записи журнала OpenTelemetry).  Форматы JSON, JSONL и CSV можно использовать для обмена данными с другими программами; формат `otel` отправляет результирующий набор в бэкенд OpenTelemetry в виде снимка с меткой времени.  Нужный формат вывода задается флагом StackQL `--output`.

### Формат вывода `table` {#table-output-format}
Результаты можно вывести в табличном формате с помощью значения `table` параметра `--output`, как показано ниже (это значение по умолчанию для интерактивной оболочки StackQL):

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

### Вывод `json` {#json-output}
Результаты можно получить в формате JSON с помощью значения `json` параметра `--output`, как показано ниже. Этот формат вывода удобен, если результаты запроса нужно передать внешнему процессу или скрипту:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Вывод `jsonl` {#jsonl-output}
Результаты можно получить в формате JSON, разделенного символами новой строки (JSON Lines), с помощью значения `jsonl` параметра `--output`, как показано ниже (в качестве псевдонима принимается `ndjson`).  Каждая строка результата записывается как отдельный JSON-объект в собственной строке вывода, и строки сбрасываются в вывод сразу по мере записи, поэтому этот формат вывода удобен для потоковой передачи результатов инструментам, которые обрабатывают данные построчно, или для передачи больших результирующих наборов другим программам через конвейер:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Вывод `csv` {#csv-output}
Результаты можно получить в формате CSV с помощью значения `csv` параметра `--output`, как показано ниже. Этот формат вывода удобен для разбора результатов в Excel или для предоставления интерфейса данных другой системе:

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

#### Пример вывода CSV (с другим разделителем) {#csv-output-example-with-alternative-delimiter}
Другой разделитель для формата вывода `csv` можно задать флагом `-d`, как показано ниже:

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

### Вывод `text` {#text-output}
Результаты можно получить в виде обычного текста с помощью значения `text` параметра `--output`.  Каждая строка записывается в виде своих значений через запятую, а перед строками выводится строка заголовков, если не задан флаг `-H` (`--hideheaders`).  В отличие от `csv`, значения не заключаются в кавычки и не экранируются, а разделитель `-d` игнорируется, поэтому значение, содержащее запятую или двойную кавычку, записывается как есть:

```shell
stackql exec "SELECT 'a,b' AS letters, 'say \"hi\"' AS greeting" --output text

letters,greeting
a,b,say "hi"
```

С флагом `--output csv` тот же запрос возвращает строку данных `"a,b","say ""hi"""`.

### Вывод `otel` {#otel-output}
Результаты можно выдавать в виде записей журнала OpenTelemetry с помощью значения `otel` параметра `--output`.  Каждая строка результата записывается как один объект OTLP/JSON `LogsData` в отдельной строке вывода и, как в `jsonl`, сбрасывается в вывод сразу при записи; после строк следует одна запись о завершении для инструкции.  Этот формат вывода превращает запрос в снимок облачной конфигурации в виде телеметрии: выполняйте запрос инвентаризации по расписанию, отправляйте записи в бэкенд с поддержкой OTLP (ClickHouse, Datadog или OpenTelemetry Collector перед любым другим бэкендом) и отвечайте там на вопросы "как сейчас выглядит среда", "что изменилось с последнего запуска" и "у кого было какое право доступа и с какого времени".  Доступно в StackQL `v0.11.660` и более поздних версиях.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

Одна запись строки в отформатированном виде (в файле каждая запись занимает одну строку):

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

#### Снимки {#snapshots}

Одна инструкция - один снимок.  Каждая запись инструкции содержит один и тот же `timeUnixNano` (время начала инструкции), один и тот же `stackql.snapshot.id` и один и тот же `spanId`, поэтому запросы состояния на определенный момент, сравнения и запросы последнего состояния работают, даже если строки поступают постранично в течение нескольких секунд; `observedTimeUnixNano` фиксирует, когда каждая запись была фактически записана.  Все инструкции одного вызова используют общий `traceId`, который берется из переменной среды `TRACEPARENT`, если она задана (так принято в конвейерах CI), а иначе генерируется.

Последняя запись инструкции - запись о завершении: тело `snapshot complete <provider.service.resource>` с атрибутами `stackql.snapshot.complete=true`, `stackql.rows_returned` и `stackql.duration_ms`.  Она сообщает бэкенду, что снимок полный, и выдается даже инструкцией, которая не возвращает ни одной строки, поэтому исчезнувший ресурс проявляется как дрейф, а не как молчание.  Если StackQL запущен с флагом `--errorpresentation record`, инструкция, завершившаяся ошибкой, создает запись `ERROR` с атрибутами `error.type` и `error.message`; по умолчанию ошибки выводятся в stderr, как и для любого другого формата вывода.

#### Структура записи {#record-shape}

Тело - это полная строка в виде компактного JSON с сохранением вложенных объектов, и оно служит источником истины для всего документа.  Атрибуты передают каждый столбец под его собственным именем, с типом, если значение скалярное (`stringValue`, `intValue`, `doubleValue`, `boolValue`); вложенные столбцы передаются как JSON-строки, что переносимо между бэкендами, у которых схема атрибутов по умолчанию строковая, а столбцы со значением null опускаются.  Зарезервированные атрибуты `stackql.*`:

| Атрибут | Запись | Значение |
|--|--|--|
|`stackql.snapshot.id`|все|UUID, генерируемый для каждого выполнения инструкции|
|`stackql.query`|все|Текст отправленного запроса без изменений|
|`stackql.query.hash`|все|SHA-256 текста запроса; для повторяющегося задания более дешевый ключ временного ряда, чем SQL|
|`stackql.provider`, `stackql.service`, `stackql.resource`|все|Основная таблица инструкции; опускаются для соединений и представлений, охватывающих несколько таблиц|
|`stackql.row.index`|строка|Позиция в результирующем наборе, начиная с нуля|
|`stackql.row.fingerprint`|строка|`sha256:` канонического JSON строки, поэтому дрейф определяется одним сравнением на каждое удостоверение вместо сравнения по всем атрибутам|
|`stackql.snapshot.complete`|завершение|`true`|
|`stackql.rows_returned`|завершение|Число строк для инструкции|
|`stackql.duration_ms`|завершение|Фактическая длительность от начала инструкции|

<br />

Выдаваемый набор атрибутов - это версионируемый интерфейс: область инструментирования - `github.com/stackql/stackql/internal/stackql/output` версии `1.0.0`, а `schemaUrl` закрепляет стабильные соглашения за `https://opentelemetry.io/schemas/1.44.0`.

Удостоверение ресурса не выводится автоматически: отпечаток - это значение, а ключ выбирает бэкенд (`id`, `arn`, `selfLink` или кортеж столбцов).  Чтобы отслеживать права доступа как сущности, разложите в SQL привязки на отдельные строки, по одной на каждую пару субъекта и роли; телеметрия следует гранулярности результата.

#### Атрибуты ресурса {#resource-attributes}

Ресурс содержит `service.name` (`stackql` или значение `OTEL_SERVICE_NAME`) и `service.version`, облачные соглашения OpenTelemetry там, где их можно вывести из инструкции `SELECT` по одной таблице (`cloud.provider` из провайдера; `cloud.account.id` из условия равенства для `project` или `subscriptionId` в предложении `WHERE`; `cloud.region` из `region`; `cloud.availability_zone` из `zone`), а также каждую пару из стандартной переменной среды `OTEL_RESOURCE_ATTRIBUTES`, поэтому шаг конвейера может проставить `deployment.environment`, имя задания или теги учетной записи без дополнительных флагов:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### Доставка записей {#shipping-the-records}

Файл содержит один объект `LogsData` на строку - в такой форме его принимает приемник `otlp_json_file` в OpenTelemetry Collector без процессора преобразования:

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

Каждая строка - это также полный объект OTLP/JSON `LogsData`, который конечная точка OTLP/HTTP `POST /v1/logs` принимает как тело запроса.  Встроенный экспортер отправки использует ту же кодировку, но группирует записи в пакеты, как описано ниже.

#### Отправка в конечную точку OTLP {#pushing-to-an-otlp-endpoint}

Помимо сохранения в файл или вывода в stdout, записи можно отправлять напрямую в конечную точку журналов OTLP/HTTP, настроив экспортер во флаге `--otel.config` - строке JSON или YAML, как и у других флагов конфигурации (доступно в StackQL `v0.12.732` и более поздних версиях):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| Ключ | Назначение |
|--|--|
|`exporter.endpoint`|Полный URL-адрес конечной точки журналов, например `http://localhost:4318/v1/logs`; обязателен для включения экспортера|
|`exporter.headers`|Заголовки запроса, отправляемые при каждом экспорте, обычно токен приема данных или API-ключ|
|`exporter.timeout_ms`|Тайм-аут запроса в миллисекундах, по умолчанию `10000`|
|`exporter.batch_size`|Число записей в одном запросе, по умолчанию `512`|

<br />

Каждая инструкция - один пакет: ее записи строк и запись о завершении отправляются как один объект OTLP/JSON `LogsData` (один конверт ресурса и области, по одному элементу в `logRecords` на каждую запись), а большой результирующий набор по мере поступления строк разбивается на запросы, содержащие не более `batch_size` записей, причем последний запрос отправляется вслед за записью о завершении.  При ответах `429` и `5xx`, а также при сбоях подключения выполняется до четырех попыток с удвоением интервала ожидания начиная с 500ms; если сервер передает заголовок `Retry-After` с задержкой в секундах, он учитывается (но не более 30 секунд); при других ошибках повторные попытки не выполняются.  Файл или поток записывается в любом случае, поэтому при неудачной отправке остаются локальная копия и строка с ошибкой в stderr.  Экспортер работает по протоколу OTLP/JSON поверх HTTP; конечной точке, которая принимает только protobuf, нужен Collector перед ней.

Переменные среды `OTEL_EXPORTER_OTLP_*` не читаются; все настройки экспортера берутся из `--otel.config`.  Неправильно сформированное значение `--otel.config` или объект `exporter` без `endpoint` приводят к ошибке при запуске.

Экспортер рассчитан на прием OTLP в ClickStack (`http://<host>:4318/v1/logs`, заголовок `authorization: <ingestion API key>`), приемник OTLP в Datadog Agent или прием OTLP в Datadog (`dd-api-key: <key>`), а также любой приемник `otlp` в OpenTelemetry Collector.  Запросы инвентаризации, дрейфа и прав доступа на принимающей стороне см. на странице [Рецепты для бэкендов OTel](/getting-started/otel-recipes).
