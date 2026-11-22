---
title: 輸出模式
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 輸出模式：table、json、jsonl、csv、text 與 otel，如何選取這些模式，以及各模式在 shell、exec 與伺服器用途中的輸出內容。"
image: "/img/stackql-featured-image.png"
---
另請參閱：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL 查詢傳回的結果可以格式化為表格（table）格式，也可以使用 json、jsonl（以換行分隔的 JSON）、csv、text 或 otel（OpenTelemetry 記錄）格式。JSON、JSONL 和 CSV 格式可用來與其他程式交換資料；`otel` 格式則會將結果集以帶有時間戳記的快照形式傳送到 OpenTelemetry 後端。所需的輸出格式透過 StackQL 的 `--output` 旗標設定。

### `table` 輸出格式 {#table-output-format}
如下所示，將 `--output` 參數的值設為 `table`，即可將結果格式化為表格格式（這是 StackQL 互動式 shell 的預設值）：

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

### `json` 輸出 {#json-output}
如下所示，將 `--output` 參數的值設為 `json`，即可以 JSON 格式傳回結果；若查詢結果需要傳給外部處理程序或指令碼，這種輸出格式很實用：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### `jsonl` 輸出 {#jsonl-output}
如下所示，將 `--output` 參數的值設為 `jsonl`，即可以換行分隔的 JSON（JSON Lines）格式傳回結果（也接受別名 `ndjson`）。每個資料列會寫成獨佔一行的單一 JSON 物件，且資料列在寫入時即會排清，因此這種輸出格式適合將結果串流傳送到逐行處理的工具，或將大型結果集透過管道傳給其他程式：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### `csv` 輸出 {#csv-output}
如下所示，將 `--output` 參數的值設為 `csv`，即可以 CSV 格式傳回結果；這種輸出格式適合在 Excel 中剖析結果，或為其他系統提供資料介面：

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

#### Csv 輸出範例（使用替代分隔符號） {#csv-output-example-with-alternative-delimiter}
如下所示，可以使用 `-d` 旗標為 `csv` 輸出格式指定替代分隔符號：

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

### `text` 輸出 {#text-output}
將 `--output` 參數的值設為 `text`，即可以純文字傳回結果。每個資料列會寫成以逗號分隔的值；除非設定了 `-H`（`--hideheaders`），否則前面會有一個標題列。與 `csv` 不同，值不會加上引號或逸出，且會忽略 `-d` 分隔符號，因此含有逗號或雙引號的值會原樣寫出：

```shell
stackql exec "SELECT 'a,b' AS letters, 'say \"hi\"' AS greeting" --output text

letters,greeting
a,b,say "hi"
```

若使用 `--output csv`，相同的查詢會傳回 `"a,b","say ""hi"""` 作為其資料列。

### `otel` 輸出 {#otel-output}
將 `--output` 參數的值設為 `otel`，即可將結果以 OpenTelemetry 記錄的形式發出。每個資料列會寫成獨佔一行的一個 OTLP/JSON `LogsData` 物件，並與 `jsonl` 一樣在寫入時即排清，最後再加上該陳述式的一筆完成記錄。這種輸出格式會將查詢轉換為以遙測形式呈現的雲端設定快照：依排程執行資源清單查詢，將記錄傳送到支援 OTLP 的後端（ClickHouse、Datadog，或置於任何其他後端前方的 OpenTelemetry Collector），再在該處回答「資源環境現在是什麼樣子」、「自上次執行以來有哪些變更」以及「誰擁有過哪些權利，從何時開始」。適用於 `v0.11.660` 起的 StackQL 發行版本。

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

一筆資料列記錄，以易讀格式排版（檔案中每筆記錄各佔一行）：

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

#### 快照 {#snapshots}

一個陳述式就是一個快照。該陳述式的每筆記錄都帶有相同的 `timeUnixNano`（陳述式的開始時間）、相同的 `stackql.snapshot.id` 和相同的 `spanId`，因此即使資料列分散在多個分頁中、於數秒內陸續抵達，時間點查詢、差異比較與最新狀態查詢仍可正常運作；`observedTimeUnixNano` 則記錄每筆記錄實際寫入的時間。同一次叫用中的所有陳述式共用一個 `traceId`，若已設定 `TRACEPARENT` 環境變數（CI 管線的慣例），就取自該變數，否則會自動產生。

陳述式的最後一筆記錄是完成記錄：本文為 `snapshot complete <provider.service.resource>`，並帶有 `stackql.snapshot.complete=true`、`stackql.rows_returned` 和 `stackql.duration_ms`。它告訴後端快照已完整，且未傳回任何資料列的陳述式仍會發出此記錄，因此已消失的資源會顯示為漂移，而不是毫無訊息。當 StackQL 以 `--errorpresentation record` 執行時，失敗的陳述式會產生一筆帶有 `error.type` 和 `error.message` 的 `ERROR` 記錄；預設情況下，錯誤會與其他所有輸出格式一樣寫入 stderr。

#### 記錄結構 {#record-shape}

本文是以精簡 JSON 表示的完整資料列，保留巢狀物件，是完整文件的事實來源。屬性以各資料行自己的名稱存放每個資料行，值為純量時會加上型別（`stringValue`、`intValue`、`doubleValue`、`boolValue`）；巢狀資料行以 JSON 字串存放，可在預設屬性 schema 為字串值的後端之間通用，而 null 資料行則會省略。保留的 `stackql.*` 屬性如下：

| 屬性 | 記錄 | 值 |
|--|--|--|
|`stackql.snapshot.id`|全部|每次執行陳述式時產生的 UUID|
|`stackql.query`|全部|提交的查詢文字，原文照錄|
|`stackql.query.hash`|全部|查詢文字的 SHA-256；對週期性作業而言，是比 SQL 本身更省成本的序列鍵|
|`stackql.provider`, `stackql.service`, `stackql.resource`|全部|陳述式的主要資料表；跨多個資料表的聯結與檢視會省略此屬性|
|`stackql.row.index`|資料列|在結果集中從零起算的位置|
|`stackql.row.fingerprint`|資料列|標準化資料列 JSON 的 `sha256:` 雜湊值，因此漂移只需對每個識別值比較一次，而不必比較每個屬性|
|`stackql.snapshot.complete`|完成|`true`|
|`stackql.rows_returned`|完成|陳述式的資料列數|
|`stackql.duration_ms`|完成|自陳述式開始起算的實際經過時間|

<br />

發出的屬性集合是有版本控制的介面：檢測範圍為 `github.com/stackql/stackql/internal/stackql/output`，版本為 `1.0.0`，而 `schemaUrl` 將穩定版慣例固定為 `https://opentelemetry.io/schemas/1.44.0`。

不會推斷任何資源識別：指紋是值，鍵則由後端選擇（`id`、`arn`、`selfLink`，或多個資料行組成的元組）。若要將權利當作實體追蹤，請在 SQL 中將繫結展開為每個主體與角色一個資料列；遙測資料會依循結果的粒度。

#### 資源屬性 {#resource-attributes}

資源帶有 `service.name`（`stackql` 或 `OTEL_SERVICE_NAME` 的值）和 `service.version`；可從單一資料表 `SELECT` 推導出來時，也帶有 OpenTelemetry 雲端慣例屬性（`cloud.provider` 取自 Provider；`cloud.account.id` 取自 `WHERE` 子句中 `project` 或 `subscriptionId` 的等式條件；`cloud.region` 取自 `region`；`cloud.availability_zone` 取自 `zone`）；此外還帶有標準 `OTEL_RESOURCE_ATTRIBUTES` 環境變數中的每個鍵值對，因此管線步驟不需額外旗標，即可加上 `deployment.environment`、作業名稱或帳戶標籤：

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### 傳送記錄 {#shipping-the-records}

檔案中每行一個 `LogsData`，這正是 OpenTelemetry Collector 的 `otlp_json_file` 接收器不需轉換處理器即可擷取的格式：

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

每一行也是完整的 OTLP/JSON `LogsData`，OTLP/HTTP `POST /v1/logs` 端點會接受它作為要求本文。內建推送匯出工具傳送的是相同的編碼，但會將記錄分批傳送，如下文所述。

#### 推送到 OTLP 端點 {#pushing-to-an-otlp-endpoint}

除了寫入檔案或 stdout 之外，也可以在 `--otel.config` 旗標中設定匯出工具，將記錄直接推送到 OTLP/HTTP 記錄端點；此旗標與其他設定旗標一樣，接受 JSON 或 YAML 字串（適用於 `v0.12.732` 起的 StackQL 發行版本）：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| 鍵 | 用途 |
|--|--|
|`exporter.endpoint`|記錄端點的完整 URL，例如 `http://localhost:4318/v1/logs`；啟用匯出工具時為必要項目|
|`exporter.headers`|每次匯出時傳送的要求標頭，通常是擷取權杖或 API 金鑰|
|`exporter.timeout_ms`|要求逾時（毫秒），預設為 `10000`|
|`exporter.batch_size`|每個要求的記錄數，預設為 `512`|

<br />

每個陳述式是一個批次：其資料列記錄與完成記錄會以單一 OTLP/JSON `LogsData` 傳送（一個資源與範圍封套，`logRecords` 中每筆記錄一個項目）；大型結果集會在資料列串流時分割為每個最多 `batch_size` 筆記錄的要求，最後一個要求則在完成記錄產生之後送出。`429` 與 `5xx` 回應以及連線失敗會以從 500ms 起每次加倍的退避時間重試，最多嘗試四次；若伺服器傳送以秒數表示延遲的 `Retry-After` 標頭，則會遵循該標頭（上限為 30 秒）；其他錯誤不會重試。無論如何都會寫入檔案或串流，因此推送失敗時仍會留下本機副本，並在 stderr 上留下一行錯誤訊息。匯出工具透過 HTTP 使用 OTLP/JSON；只接受 protobuf 的端點需要在其前方加上 Collector。

不會讀取任何 `OTEL_EXPORTER_OTLP_*` 環境變數；所有匯出工具設定都來自 `--otel.config`。格式錯誤的 `--otel.config` 值，或缺少 `endpoint` 的 `exporter` 物件，都會導致啟動失敗。

適用的目標包括 ClickStack 的 OTLP 擷取端點（`http://<host>:4318/v1/logs`，標頭為 `authorization: <ingestion API key>`）、Datadog Agent 的 OTLP 接收器或 Datadog OTLP 擷取端點（`dd-api-key: <key>`），以及任何 OpenTelemetry Collector 的 `otlp` 接收器。如需接收端的資源清單、漂移與權利查詢，請參閱 [OTel 後端設定範例](/getting-started/otel-recipes)。
