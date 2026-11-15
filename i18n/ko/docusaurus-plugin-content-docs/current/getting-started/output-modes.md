---
title: 출력 모드
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL 출력 모드: table, json, jsonl, csv, otel의 선택 방법과 shell, exec, 서버 사용 시 각 모드의 출력 내용."
image: "/img/stackql-featured-image.png"
---
참고 항목:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL 쿼리에서 반환된 결과는 표 형식(table)뿐 아니라 json, jsonl(줄 바꿈으로 구분된 JSON), csv, text 또는 otel(OpenTelemetry 로그 레코드) 형식으로 지정할 수 있습니다. JSON, JSONL, CSV 형식은 다른 프로그램과 데이터를 교환하는 데 사용할 수 있으며, `otel` 형식은 결과 집합을 타임스탬프가 지정된 스냅샷으로 OpenTelemetry 백엔드에 전송합니다. 원하는 출력 형식은 StackQL의 `--output` 플래그로 구성합니다.

### `table` 출력 형식 {#table-output-format}
아래와 같이 `--output` 매개 변수에 `table` 값(StackQL 대화형 셸의 기본값)을 사용하면 결과를 표 형식으로 지정할 수 있습니다:

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

### `json` 출력 {#json-output}
아래와 같이 `--output` 매개 변수에 `json` 값을 사용하면 결과를 JSON 형식으로 반환할 수 있습니다. 이 출력 형식은 쿼리 결과를 외부 프로세스나 스크립트에 전달해야 하는 경우에 유용합니다:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### `jsonl` 출력 {#jsonl-output}
아래와 같이 `--output` 매개 변수에 `jsonl` 값을 사용하면 결과를 줄 바꿈으로 구분된 JSON(JSON Lines) 형식으로 반환할 수 있습니다(`ndjson` 값도 별칭으로 허용됩니다). 각 행은 한 줄에 하나의 JSON 객체로 기록되고 기록되는 즉시 플러시되므로, 이 출력 형식은 줄 단위로 처리하는 도구로 결과를 스트리밍하거나 큰 결과 집합을 다른 프로그램으로 파이프하는 데 유용합니다:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### `csv` 출력 {#csv-output}
아래와 같이 `--output` 매개 변수에 `csv` 값을 사용하면 결과를 CSV 형식으로 반환할 수 있습니다. 이 출력 형식은 Excel에서 결과를 구문 분석하거나 다른 시스템에 데이터 인터페이스를 제공하는 데 유용합니다:

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

#### Csv 출력 예제(대체 구분 기호 사용) {#csv-output-example-with-alternative-delimiter}
아래와 같이 `-d` 스위치를 사용하여 `csv` 출력 형식에 대체 구분 기호를 지정할 수 있습니다:

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

### `otel` 출력 {#otel-output}
`--output` 매개 변수에 `otel` 값을 사용하면 결과를 OpenTelemetry 로그 레코드로 내보낼 수 있습니다. 각 행은 한 줄에 하나의 OTLP/JSON `LogsData` 객체로 기록되고 `jsonl` 형식처럼 기록되는 즉시 플러시되며, 그 뒤에 해당 문의 완료 레코드 1개가 이어집니다. 이 출력 형식은 쿼리를 텔레메트리 형태의 클라우드 구성 스냅샷으로 변환합니다. 인벤토리 쿼리를 일정에 따라 실행하고, 레코드를 OTLP를 지원하는 백엔드(ClickHouse, Datadog 또는 다른 백엔드 앞에 둔 OpenTelemetry Collector)로 전송한 다음, 그곳에서 "현재 자산은 어떤 상태인가", "마지막 실행 이후 무엇이 변경되었나", "누가 어떤 사용 권한을 언제부터 가지고 있었나"에 답할 수 있습니다. StackQL `v0.11.660` 이상에서 사용할 수 있습니다.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

다음은 보기 좋게 출력한 행 레코드 1개입니다(파일에는 각 레코드가 한 줄에 저장됩니다):

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

#### 스냅샷 {#snapshots}

하나의 문은 하나의 스냅샷입니다. 문의 모든 레코드는 동일한 `timeUnixNano` 값(문 시작 시간), 동일한 `stackql.snapshot.id` 값, 동일한 `spanId` 값을 가지므로, 행이 여러 페이지에 걸쳐 몇 초 동안 도착하더라도 특정 시점 기준 쿼리, 차이 비교, 최신 상태 쿼리가 작동합니다. `observedTimeUnixNano` 값은 각 레코드가 실제로 기록된 시간을 나타냅니다. 한 번의 호출에 포함된 모든 문은 하나의 `traceId` 값을 공유합니다. 이 값은 `TRACEPARENT` 환경 변수가 설정되어 있으면 그 값을 사용하고(CI 파이프라인의 관례), 그렇지 않으면 생성됩니다.

문의 마지막 레코드는 완료 레코드입니다. 완료 레코드의 본문은 `snapshot complete <provider.service.resource>` 형식이며, `stackql.snapshot.complete=true`, `stackql.rows_returned`, `stackql.duration_ms` 속성을 포함합니다. 이 레코드는 스냅샷이 완전하다는 것을 백엔드에 알립니다. 행을 반환하지 않는 문도 이 레코드를 내보내므로, 사라진 리소스는 아무 신호 없이 사라지는 대신 드리프트로 나타납니다. 오류 표시 방식이 `record`인 경우 실패한 문은 `error.type` 속성과 `error.message` 속성을 포함하는 `ERROR` 레코드를 생성합니다. 기본적으로 오류는 다른 모든 출력 형식과 마찬가지로 stderr로 출력됩니다.

#### 레코드 형태 {#record-shape}

본문은 중첩된 객체를 그대로 유지한 압축 JSON 형식의 전체 행이며, 전체 문서의 단일 정보 출처입니다. 속성은 각 열을 해당 열 이름으로 전달하며, 값이 스칼라인 경우 형식이 지정됩니다(`stringValue`, `intValue`, `doubleValue`, `boolValue`). 중첩된 열은 JSON 문자열로 전달되므로 기본 속성 스키마가 문자열 값인 백엔드 간에 이식할 수 있으며, null 열은 생략됩니다. 예약된 `stackql.*` 속성은 다음과 같습니다:

| 속성 | 레코드 | 값 |
|--|--|--|
|`stackql.snapshot.id`|모두|문 실행마다 생성되는 UUID|
|`stackql.query`|모두|제출된 쿼리 텍스트(원문 그대로)|
|`stackql.query.hash`|모두|쿼리 텍스트의 SHA-256으로, 반복 작업에서 SQL 자체보다 비용이 적게 드는 시리즈 키|
|`stackql.provider`, `stackql.service`, `stackql.resource`|모두|문의 기본 테이블이며, 여러 테이블에 걸친 조인과 뷰에서는 생략됨|
|`stackql.row.index`|행|결과 집합에서 0부터 시작하는 위치|
|`stackql.row.fingerprint`|행|정규화된 행 JSON의 `sha256:` 값으로, 모든 속성을 비교하는 대신 identity당 한 번만 비교하면 드리프트를 확인할 수 있음|
|`stackql.snapshot.complete`|완료|`true`|
|`stackql.rows_returned`|완료|문의 행 수|
|`stackql.duration_ms`|완료|문 시작부터 경과한 실제 시간|

<br />

내보내는 속성 집합은 버전이 지정된 인터페이스입니다. 계측 범위는 버전 `1.0.0`의 `github.com/stackql/stackql/internal/stackql/output`이며, `schemaUrl` 값은 안정 규칙을 `https://opentelemetry.io/schemas/1.44.0`으로 고정합니다.

리소스 ID는 유추되지 않습니다. 지문은 값이고, 키는 백엔드가 선택합니다(`id`, `arn`, `selfLink` 또는 열의 튜플). 사용 권한을 엔터티로 추적하려면 SQL에서 바인딩을 보안 주체 및 역할당 1행으로 펼치세요. 텔레메트리는 결과의 세분성을 따릅니다.

#### 리소스 속성 {#resource-attributes}

리소스에는 `service.name` 속성(`stackql` 또는 `OTEL_SERVICE_NAME` 값)과 `service.version` 속성이 포함됩니다. 또한 단일 테이블 `SELECT` 문에서 도출할 수 있는 경우 OpenTelemetry 클라우드 규칙의 속성(프로바이더에서 `cloud.provider`; `WHERE` 절의 `project` 또는 `subscriptionId` 등호 조건에서 `cloud.account.id`; `region`에서 `cloud.region`; `zone`에서 `cloud.availability_zone`)과 표준 `OTEL_RESOURCE_ATTRIBUTES` 환경 변수의 모든 쌍이 포함됩니다. 따라서 파이프라인 단계에서 추가 플래그 없이 `deployment.environment`, 작업 이름 또는 계정 태그를 기록할 수 있습니다:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### 레코드 전송 {#shipping-the-records}

파일에는 한 줄에 하나의 `LogsData` 객체가 있으며, 이는 OpenTelemetry Collector의 `otlp_json_file` 리시버가 변환 프로세서 없이 수집하는 형태입니다:

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

각 줄은 또한 바이트 단위까지 OTLP/HTTP `POST /v1/logs` 요청의 본문과 동일하며, 이것이 기본 제공 푸시 익스포터가 보내는 내용입니다.

#### OTLP 엔드포인트로 푸시 {#pushing-to-an-otlp-endpoint}

`--otel.config` 플래그에 익스포터를 구성하면 레코드를 파일이나 stdout에 기록하는 것과 함께 OTLP/HTTP 로그 엔드포인트로 직접 푸시할 수 있습니다. 이 플래그는 다른 구성 플래그와 마찬가지로 JSON 또는 YAML 문자열입니다(StackQL `v0.12.732` 이상에서 사용할 수 있습니다):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| 키 | 용도 |
|--|--|
|`exporter.endpoint`|로그 엔드포인트의 전체 URL(예: `http://localhost:4318/v1/logs`). 익스포터를 활성화하려면 필수|
|`exporter.headers`|내보내기마다 전송되는 요청 헤더로, 일반적으로 수집 토큰 또는 API 키|
|`exporter.timeout_ms`|요청 시간 제한(밀리초), 기본값 `10000`|
|`exporter.batch_size`|요청당 레코드 수, 기본값 `512`|

<br />

각 문은 하나의 배치입니다. 문의 행 레코드와 완료 레코드는 하나의 OTLP/JSON `LogsData` 객체(리소스 및 범위 엔벨로프 1개, 레코드당 `logRecords` 항목 1개)로 전송됩니다. 큰 결과 집합은 행이 스트리밍되는 동안 최대 `batch_size`개 레코드의 요청으로 분할되며, 마지막 요청은 완료 레코드 뒤에 전송됩니다. `429` 및 `5xx` 응답과 연결 실패는 500ms부터 두 배씩 늘어나는 백오프를 적용하여 최대 4회까지 시도하며, 서버가 지연 시간(초)을 지정한 `Retry-After` 헤더를 보내면 이를 따릅니다(최대 30초). 다른 오류는 재시도하지 않습니다. 파일 또는 스트림은 그와 관계없이 기록되므로, 푸시에 실패해도 로컬 사본과 stderr의 오류 줄이 남습니다. 익스포터는 HTTP를 통해 OTLP/JSON으로 통신합니다. protobuf만 허용하는 엔드포인트에는 앞단에 Collector가 필요합니다.

`OTEL_EXPORTER_OTLP_*` 환경 변수는 읽지 않습니다. 모든 익스포터 설정은 `--otel.config` 플래그에서 가져옵니다. `--otel.config` 값의 형식이 잘못되었거나 `exporter` 객체에 `endpoint` 키가 없으면 시작에 실패합니다.

이 기능은 ClickStack의 OTLP 수집 엔드포인트(`http://<host>:4318/v1/logs`, 헤더 `authorization: <ingestion API key>`), Datadog Agent의 OTLP 리시버 또는 Datadog OTLP 수집 엔드포인트(`dd-api-key: <key>`), 그리고 모든 OpenTelemetry Collector `otlp` 리시버를 대상으로 합니다. 수신 측의 인벤토리, 드리프트, 사용 권한 쿼리는 [OTel 백엔드 레시피](/getting-started/otel-recipes)를 참조하세요.
