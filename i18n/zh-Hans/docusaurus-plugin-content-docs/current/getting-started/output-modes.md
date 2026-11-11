---
title: 输出模式
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 输出模式：table、json、jsonl、csv 和 otel，如何选择，以及各模式在 shell、exec 和服务器用法下输出的内容。"
image: "/img/stackql-featured-image.png"
---
另请参阅：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL 查询返回的结果可以格式化为表格（table）格式，也可以格式化为 json、jsonl（换行分隔的 JSON）、csv、text 或 otel（OpenTelemetry 日志记录）格式。JSON、JSONL 和 CSV 格式可用于与其他程序交换数据；`otel` 格式将结果集作为带时间戳的快照发送到 OpenTelemetry 后端。所需的输出格式通过 StackQL 的 `--output` 标志配置。

### `table` 输出格式 {#table-output-format}
如下所示，将 `--output` 参数的值设为 `table`，即可将结果格式化为表格格式（这是 StackQL 交互式 shell 的默认值）：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output table

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

### `json` 输出 {#json-output}
如下所示，将 `--output` 参数的值设为 `json`，即可以 JSON 格式返回结果。如果需要将查询结果传递给外部进程或脚本，这种输出格式很有用：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### `jsonl` 输出 {#jsonl-output}
如下所示，将 `--output` 参数的值设为 `jsonl`，即可以换行分隔的 JSON（JSON Lines）格式返回结果（`ndjson` 可作为别名使用）。每一行作为单个 JSON 对象独占一行写出，并且行在写出时即被刷新，因此这种输出格式适合将结果流式传输给面向行的工具，或通过管道将大型结果集传给其他程序：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### `csv` 输出 {#csv-output}
如下所示，将 `--output` 参数的值设为 `csv`，即可以 JSON 格式返回结果。这种输出格式适合在 Excel 中解析结果，或为其他系统提供数据接口：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv

id,name
1257085253691867467,demo-instance-1
2586731219281477960,demo-instance-2
5584456226809282885,demo-instance-3
```

#### CSV 输出示例（使用其他分隔符） {#csv-output-example-with-alternative-delimiter}
如下所示，可以使用 `-d` 开关为 `csv` 输出格式指定其他分隔符：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output csv -d "|"

id|name
1257085253691867467|demo-instance-1
2586731219281477960|demo-instance-2
5584456226809282885|demo-instance-3
```

### `otel` 输出 {#otel-output}
将 `--output` 参数的值设为 `otel`，即可将结果作为 OpenTelemetry 日志记录输出。每一行作为一个 OTLP/JSON `LogsData` 对象独占一行写出，与 `jsonl` 一样在写出时即被刷新，随后是该语句的一条完成记录。这种输出格式将查询转换为以遥测数据形式呈现的云配置快照：按计划运行资源清单查询，将记录发送到支持 OTLP 的后端（ClickHouse、Datadog，或置于其他任何系统之前的 OpenTelemetry Collector），然后在那里回答“资产现在是什么样子”“自上次运行以来发生了什么变化”以及“谁拥有过哪项权限分配，从何时开始”。从 `v0.11.660` 起的 StackQL 发布版本中可用。

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

一条行记录，已美化输出（文件中每条记录占一行）：

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

一条语句就是一个快照。该语句的每条记录都带有相同的 `timeUnixNano`（语句开始时间）、相同的 `stackql.snapshot.id` 和相同的 `spanId`，因此即使各行在数秒内跨多个分页陆续到达，时间点查询、差异比较和最新状态查询也都能正常工作；`observedTimeUnixNano` 记录每条记录实际写出的时间。一次调用中的所有语句共享一个 `traceId`，设置了 `TRACEPARENT` 环境变量时取自该变量（CI 流水线的惯例），否则自动生成。

语句的最后一条记录是完成记录：正文为 `snapshot complete <provider.service.resource>`，并带有 `stackql.snapshot.complete=true`、`stackql.rows_returned` 和 `stackql.duration_ms`。它告诉后端该快照是完整的，不返回任何行的语句也会输出它，因此已消失的资源会表现为漂移，而不是无声无息。当错误呈现方式为 `record` 时，失败的语句会产生一条带有 `error.type` 和 `error.message` 的 `ERROR` 记录；默认情况下，错误与其他所有输出格式一样写入 stderr。

#### 记录结构 {#record-shape}

正文是以紧凑 JSON 表示的完整行，嵌套对象保持原样，它是完整文档的事实来源。属性以各列自己的名称携带每一列，值为标量时带有类型（`stringValue`、`intValue`、`doubleValue`、`boolValue`）；嵌套列以 JSON 字符串形式携带，这样可以在默认属性 schema 为字符串值的后端之间移植，null 列则被省略。保留的 `stackql.*` 属性如下：

| 属性 | 记录 | 值 |
|--|--|--|
|`stackql.snapshot.id`|全部|每次语句执行生成的 UUID|
|`stackql.query`|全部|提交的查询文本，原样保留|
|`stackql.query.hash`|全部|查询文本的 SHA-256；对于周期性作业，是比 SQL 本身开销更低的序列键|
|`stackql.provider`、`stackql.service`、`stackql.resource`|全部|语句的主表；对于跨表的连接和视图则省略|
|`stackql.row.index`|行|在结果集中从零开始的位置|
|`stackql.row.fingerprint`|行|规范化行 JSON 的 `sha256:`，因此漂移只需对每个标识比较一次，而不必比较每个属性|
|`stackql.snapshot.complete`|完成|`true`|
|`stackql.rows_returned`|完成|该语句的行数|
|`stackql.duration_ms`|完成|自语句开始起的挂钟时长|

<br />

输出的属性集是一个带版本的接口：插桩作用域为 `github.com/stackql/stackql/internal/stackql/output`，版本为 `1.0.0`，`schemaUrl` 将稳定约定固定为 `https://opentelemetry.io/schemas/1.44.0`。

不会推断任何资源标识：指纹是值，键由后端选择（`id`、`arn`、`selfLink`，或由多列组成的元组）。要将权限分配作为实体来跟踪，请在 SQL 中将绑定展开为每个主体和角色一行；遥测数据遵循结果的粒度。

#### 资源属性 {#resource-attributes}

资源带有 `service.name`（`stackql`，或 `OTEL_SERVICE_NAME` 的值）和 `service.version`，以及能够从单表 `SELECT` 推导出的 OpenTelemetry 云约定（`cloud.provider` 来自 Provider；`cloud.account.id` 来自 `WHERE` 子句中 `project` 或 `subscriptionId` 的等值条件；`cloud.region` 来自 `region`；`cloud.availability_zone` 来自 `zone`），还有标准 `OTEL_RESOURCE_ATTRIBUTES` 环境变量中的每个键值对，因此流水线步骤无需额外的标志即可标记 `deployment.environment`、作业名称或账号标签：

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

#### 发送记录 {#shipping-the-records}

文件每行一个 `LogsData`，这正是 OpenTelemetry Collector 的 `otlp_json_file` 接收器无需 transform 处理器即可摄取的结构：

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

每一行也与 OTLP/HTTP `POST /v1/logs` 请求的正文逐字节相同，内置的推送导出器发送的正是它。

#### 推送到 OTLP 端点 {#pushing-to-an-otlp-endpoint}

除了写入文件或 stdout 之外，还可以通过在 `--otel.config` 标志中配置导出器，将记录直接推送到 OTLP/HTTP 日志端点；该标志与其他配置标志一样是一个 JSON 或 YAML 字符串（从 `v0.12.732` 起的 StackQL 发布版本中可用）：

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }\'
```

| 键 | 用途 |
|--|--|
|`exporter.endpoint`|日志端点的完整 URL，例如 `http://localhost:4318/v1/logs`；启用导出器时必填|
|`exporter.headers`|随每次导出发送的请求头，通常是摄取令牌或 API 密钥|
|`exporter.timeout_ms`|请求超时时间（毫秒），默认 `10000`|
|`exporter.batch_size`|每个请求的记录数，默认 `512`|

<br />

每条语句是一个批次：其行记录和完成记录作为单个 OTLP/JSON `LogsData` 发送（一个资源和作用域信封，每条记录在 `logRecords` 中对应一个条目）；大型结果集会随着行的流式输出被拆分为每个最多包含 `batch_size` 条记录的请求，最后一个请求在完成记录之后发出。`429` 和 `5xx` 响应以及连接失败会重试，最多尝试 4 次，退避时间从 500ms 起逐次加倍，如果服务器发送了延迟秒数形式的 `Retry-After` 响应头则遵循该值（上限为 30 秒）；其他错误不会重试。无论如何都会写入文件或流，因此推送失败时会留下一份本地副本，并在 stderr 上留下一行错误信息。导出器通过 HTTP 使用 OTLP/JSON；只接受 protobuf 的端点需要在其前面放置一个 Collector。

不会读取任何 `OTEL_EXPORTER_OTLP_*` 环境变量；导出器的每项设置都来自 `--otel.config`。格式错误的 `--otel.config` 值，或没有 `endpoint` 的 `exporter` 对象，都会导致启动失败。

它面向 ClickStack 的 OTLP 接入端点（`http://<host>:4318/v1/logs`，请求头 `authorization: <ingestion API key>`）、Datadog Agent 的 OTLP 接收器或 Datadog OTLP 接入端点（`dd-api-key: <key>`），以及任何 OpenTelemetry Collector 的 `otlp` 接收器。接收端的资源清单、漂移和权限分配查询，请参阅 [OTel 后端方案](/getting-started/otel-recipes)。
