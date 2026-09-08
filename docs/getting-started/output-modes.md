---
title: Output Modes
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---
See also:  
[[` exec `]](/docs/command-line-usage/exec) [[` shell `]](/docs/command-line-usage/shell)

Results returned from StackQL queries can be formatted in tabular (table) format, as well as json, jsonl (newline delimited JSON), csv, text or otel (OpenTelemetry log records) format.  JSON, JSONL and CSV formats can be used to interchange data with other programs; the `otel` format ships a result set to an OpenTelemetry backend as a timestamped snapshot.  The desired output format is configured using the `--output` StackQL flag.

### `table` Output Format
Results can be formatted in a table format using the `table` value for the `--output` parameter as shown below (this is the default value for the StackQL interactive shell):

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

### `json` Output
Results can be returned in JSON format using the `json` value for the `--output` parameter as shown below, this output format is useful if the results of a query need to be passed to an external process or script:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### `jsonl` Output
Results can be returned in newline delimited JSON (JSON Lines) format using the `jsonl` value for the `--output` parameter as shown below (`ndjson` is accepted as an alias).  Each row is written as a single JSON object on its own line and rows are flushed as they are written, making this output format useful for streaming results to line-oriented tools or piping large result sets to other programs:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### `csv` Output
Results can be returned in JSON format using the `csv` value for the `--output` parameter as shown below, this output format is useful for parsing results in Excel or providing a data interface to another system:

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

#### Csv Output Example (with alternative delimiter)
An alternatate delimiter can be specified for the `csv` output format using the `-d` switch as shown below:

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

### `otel` Output
Results can be emitted as OpenTelemetry log records using the `otel` value for the `--output` parameter.  Each row is written as one OTLP/JSON `LogsData` object on its own line, flushed as it is written like `jsonl`, followed by one completion record for the statement.  This output format turns a query into a cloud configuration snapshot as telemetry: run an inventory query on a schedule, ship the records to an OTLP-capable backend (ClickHouse, Datadog, or an OpenTelemetry Collector in front of anything else), and answer "what does the estate look like now", "what changed since the last run", and "who has had which entitlement, and since when" there.  Available in StackQL releases from `v0.11.660`.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

One row record, pretty-printed (the file holds each record on one line):

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

#### Snapshots

One statement is one snapshot.  Every record of the statement carries the same `timeUnixNano` (the statement start time), the same `stackql.snapshot.id` and the same `spanId`, so as-of queries, diffs and latest-state queries work even though rows arrive across pages over several seconds; `observedTimeUnixNano` records when each record was actually written.  All statements of one invocation share a `traceId`, taken from a `TRACEPARENT` environment variable when one is set (the convention for CI pipelines) and generated otherwise.

The last record of a statement is the completion record: body `snapshot complete <provider.service.resource>`, with `stackql.snapshot.complete=true`, `stackql.rows_returned` and `stackql.duration_ms`.  It tells the backend the snapshot is whole, and a statement that returns no rows still emits it, so a resource that has disappeared shows up as drift rather than as silence.  A failed statement produces an `ERROR` record carrying `error.type` and `error.message` when the error presentation is `record`; by default errors go to stderr as for every other output format.

#### Record shape

The body is the complete row as compact JSON, nested objects intact, and is the source of truth for the full document.  The attributes carry each column under its own name, typed where the value is a scalar (`stringValue`, `intValue`, `doubleValue`, `boolValue`); nested columns are carried as JSON strings, which is portable across backends whose default attribute schema is string-valued, and null columns are omitted.  The reserved `stackql.*` attributes are:

| Attribute | Record | Value |
|--|--|--|
|`stackql.snapshot.id`|all|UUID generated per statement execution|
|`stackql.query`|all|The submitted query text, verbatim|
|`stackql.query.hash`|all|SHA-256 of the query text; a cheaper series key than the SQL for a recurring job|
|`stackql.provider`, `stackql.service`, `stackql.resource`|all|The statement's primary table; omitted for joins and views that span tables|
|`stackql.row.index`|row|Zero-based position in the result set|
|`stackql.row.fingerprint`|row|`sha256:` of the canonical row JSON, so drift is one comparison per identity instead of a comparison over every attribute|
|`stackql.snapshot.complete`|completion|`true`|
|`stackql.rows_returned`|completion|Row count for the statement|
|`stackql.duration_ms`|completion|Wall-clock duration from statement start|

<br />

The emitted attribute set is a versioned interface: the instrumentation scope is `github.com/stackql/stackql/internal/stackql/output` at version `1.0.0`, and `schemaUrl` pins the stable conventions to `https://opentelemetry.io/schemas/1.44.0`.

No resource identity is inferred: the fingerprint is the value and the backend chooses the key (`id`, `arn`, `selfLink`, or a tuple of columns).  To track entitlements as entities, explode bindings to one row per principal and role in SQL; the telemetry follows the result grain.

#### Resource attributes

The resource carries `service.name` (`stackql`, or the value of `OTEL_SERVICE_NAME`) and `service.version`, the OpenTelemetry cloud conventions where they can be derived from a single-table `SELECT` (`cloud.provider` from the provider; `cloud.account.id` from a `project` or `subscriptionId` equality in the `WHERE` clause; `cloud.region` from `region`; `cloud.availability_zone` from `zone`), and every pair in the standard `OTEL_RESOURCE_ATTRIBUTES` environment variable, so a pipeline step can stamp `deployment.environment`, a job name or account tags with no additional flags:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--keyfilepath stackql-demo.json --output otel -f instances.otlp.jsonl
```

#### Shipping the records

The file is one `LogsData` per line, the shape the OpenTelemetry Collector's `otlp_json_file` receiver ingests with no transform processor:

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

Each line is also, byte for byte, the body of an OTLP/HTTP `POST /v1/logs` request, so the records can be pushed directly to an OTLP endpoint by any HTTP client.
