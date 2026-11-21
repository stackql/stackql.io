---
title: Chế độ đầu ra
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Các chế độ đầu ra của StackQL: table, json, jsonl, csv, text và otel, cách chọn chúng và đầu ra của mỗi chế độ khi dùng với shell, exec và máy chủ."
image: "/img/stackql-featured-image.png"
---
Xem thêm:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Kết quả trả về từ các truy vấn StackQL có thể được trình bày ở dạng bảng (table), cũng như ở định dạng json, jsonl (JSON phân cách bằng dòng mới), csv, text hoặc otel (bản ghi nhật ký OpenTelemetry).  Các định dạng JSON, JSONL và CSV có thể được dùng để trao đổi dữ liệu với các chương trình khác; định dạng `otel` gửi một tập kết quả đến một backend OpenTelemetry dưới dạng ảnh chụp nhanh có dấu thời gian.  Định dạng đầu ra mong muốn được cấu hình bằng cờ `--output` của StackQL.

### Định dạng đầu ra `table` {#table-output-format}
Kết quả có thể được định dạng thành bảng bằng giá trị `table` cho tham số `--output` như minh họa dưới đây (đây là giá trị mặc định của shell tương tác StackQL):

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

### Đầu ra `json` {#json-output}
Kết quả có thể được trả về ở định dạng JSON bằng giá trị `json` cho tham số `--output` như minh họa dưới đây, định dạng đầu ra này hữu ích nếu kết quả của một truy vấn cần được chuyển cho một tiến trình hoặc tập lệnh bên ngoài:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Đầu ra `jsonl` {#jsonl-output}
Kết quả có thể được trả về ở định dạng JSON phân cách bằng dòng mới (JSON Lines) bằng giá trị `jsonl` cho tham số `--output` như minh họa dưới đây (`ndjson` được chấp nhận làm bí danh).  Mỗi hàng được ghi thành một đối tượng JSON duy nhất trên một dòng riêng và các hàng được đẩy ra ngay khi được ghi, nên định dạng đầu ra này hữu ích để truyền phát kết quả đến các công cụ xử lý theo dòng hoặc chuyển các tập kết quả lớn qua pipe đến các chương trình khác:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Đầu ra `csv` {#csv-output}
Kết quả có thể được trả về ở định dạng CSV bằng giá trị `csv` cho tham số `--output` như minh họa dưới đây, định dạng đầu ra này hữu ích để phân tích kết quả trong Excel hoặc cung cấp một giao diện dữ liệu cho hệ thống khác:

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

#### Ví dụ đầu ra CSV (với dấu phân cách thay thế) {#csv-output-example-with-alternative-delimiter}
Có thể chỉ định một dấu phân cách thay thế cho định dạng đầu ra `csv` bằng cờ `-d` như minh họa dưới đây:

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

### Đầu ra `text` {#text-output}
Kết quả có thể được trả về dưới dạng văn bản thuần bằng giá trị `text` cho tham số `--output`.  Mỗi hàng được ghi thành các giá trị của nó nối với nhau bằng dấu phẩy, đứng sau một hàng tiêu đề trừ khi đặt `-H` (`--hideheaders`).  Khác với `csv`, các giá trị không được đặt trong dấu ngoặc kép hay được thoát và dấu phân cách `-d` được bỏ qua, nên một giá trị chứa dấu phẩy hoặc dấu ngoặc kép được ghi nguyên như vậy:

```shell
stackql exec "SELECT 'a,b' AS letters, 'say \"hi\"' AS greeting" --output text

letters,greeting
a,b,say "hi"
```

Với `--output csv`, cùng truy vấn đó trả về `"a,b","say ""hi"""` làm hàng dữ liệu.

### Đầu ra `otel` {#otel-output}
Kết quả có thể được xuất ra dưới dạng bản ghi nhật ký OpenTelemetry bằng giá trị `otel` cho tham số `--output`.  Mỗi hàng được ghi thành một đối tượng OTLP/JSON `LogsData` trên một dòng riêng, được đẩy ra ngay khi được ghi giống như `jsonl`, theo sau là một bản ghi hoàn tất cho câu lệnh.  Định dạng đầu ra này biến một truy vấn thành ảnh chụp nhanh cấu hình đám mây dưới dạng dữ liệu telemetry: chạy một truy vấn kiểm kê theo lịch, gửi các bản ghi đến một backend hỗ trợ OTLP (ClickHouse, Datadog hoặc một OpenTelemetry Collector đặt trước bất kỳ hệ thống nào khác) và trả lời tại đó các câu hỏi "môi trường hiện trông như thế nào", "điều gì đã thay đổi kể từ lần chạy trước" và "ai đã có quyền sử dụng nào, và từ khi nào".  Có từ StackQL `v0.11.660` trở đi.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

Một bản ghi hàng, được định dạng cho dễ đọc (tệp lưu mỗi bản ghi trên một dòng):

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

#### Ảnh chụp nhanh {#snapshots}

Một câu lệnh là một ảnh chụp nhanh.  Mọi bản ghi của câu lệnh đều mang cùng `timeUnixNano` (thời điểm bắt đầu câu lệnh), cùng `stackql.snapshot.id` và cùng `spanId`, nên các truy vấn as-of, so sánh khác biệt và truy vấn trạng thái mới nhất đều hoạt động dù các hàng đến qua nhiều trang trong vài giây; `observedTimeUnixNano` ghi lại thời điểm từng bản ghi thực sự được ghi.  Mọi câu lệnh của một lần gọi dùng chung một `traceId`, lấy từ biến môi trường `TRACEPARENT` khi biến này được đặt (quy ước cho các pipeline CI) và được tạo mới trong trường hợp ngược lại.

Bản ghi cuối cùng của một câu lệnh là bản ghi hoàn tất: phần thân `snapshot complete <provider.service.resource>`, kèm `stackql.snapshot.complete=true`, `stackql.rows_returned` và `stackql.duration_ms`.  Bản ghi này cho backend biết ảnh chụp nhanh đã đầy đủ, và một câu lệnh không trả về hàng nào vẫn phát ra nó, nên một tài nguyên đã biến mất sẽ hiện ra dưới dạng sai lệch thay vì im lặng.  Một câu lệnh bị lỗi tạo ra một bản ghi `ERROR` mang `error.type` và `error.message` khi cách trình bày lỗi là `record`; theo mặc định, lỗi được ghi ra stderr như với mọi định dạng đầu ra khác.

#### Dạng bản ghi {#record-shape}

Phần thân là toàn bộ hàng dưới dạng JSON thu gọn, giữ nguyên các đối tượng lồng nhau, và là nguồn dữ liệu gốc cho tài liệu đầy đủ.  Các thuộc tính mang từng cột dưới tên riêng của cột đó, có kiểu khi giá trị là vô hướng (`stringValue`, `intValue`, `doubleValue`, `boolValue`); các cột lồng nhau được mang dưới dạng chuỗi JSON, cách này dùng được trên các backend có lược đồ thuộc tính mặc định chỉ chứa giá trị chuỗi, và các cột null được lược bỏ.  Các thuộc tính `stackql.*` dành riêng là:

| Thuộc tính | Bản ghi | Giá trị |
|--|--|--|
|`stackql.snapshot.id`|tất cả|UUID được tạo cho mỗi lần thực thi câu lệnh|
|`stackql.query`|tất cả|Văn bản truy vấn đã gửi, nguyên văn|
|`stackql.query.hash`|tất cả|SHA-256 của văn bản truy vấn; một khóa chuỗi (series key) rẻ hơn so với câu SQL cho một job định kỳ|
|`stackql.provider`, `stackql.service`, `stackql.resource`|tất cả|Bảng chính của câu lệnh; được lược bỏ đối với các phép nối và dạng xem trải trên nhiều bảng|
|`stackql.row.index`|hàng|Vị trí trong tập kết quả, bắt đầu từ 0|
|`stackql.row.fingerprint`|hàng|`sha256:` của JSON chuẩn tắc của hàng, nên việc phát hiện sai lệch chỉ cần một phép so sánh cho mỗi danh tính thay vì so sánh trên mọi thuộc tính|
|`stackql.snapshot.complete`|hoàn tất|`true`|
|`stackql.rows_returned`|hoàn tất|Số hàng của câu lệnh|
|`stackql.duration_ms`|hoàn tất|Thời lượng đo theo đồng hồ thực tính từ lúc câu lệnh bắt đầu|

<br />

Tập thuộc tính được phát ra là một giao diện có phiên bản: phạm vi instrumentation là `github.com/stackql/stackql/internal/stackql/output` ở phiên bản `1.0.0`, và `schemaUrl` ghim các quy ước ổn định vào `https://opentelemetry.io/schemas/1.44.0`.

Danh tính tài nguyên không được suy ra: dấu vân tay là giá trị và backend chọn khóa (`id`, `arn`, `selfLink` hoặc một bộ các cột).  Để giám sát quyền sử dụng như các thực thể, hãy tách các liên kết thành một hàng cho mỗi principal và vai trò trong SQL; dữ liệu telemetry tuân theo độ chi tiết của kết quả.

#### Thuộc tính tài nguyên {#resource-attributes}

Tài nguyên mang `service.name` (`stackql`, hoặc giá trị của `OTEL_SERVICE_NAME`) và `service.version`, các quy ước đám mây của OpenTelemetry khi chúng có thể được suy ra từ một câu lệnh `SELECT` trên một bảng (`cloud.provider` từ provider; `cloud.account.id` từ một phép so sánh bằng trên `project` hoặc `subscriptionId` trong mệnh đề `WHERE`; `cloud.region` từ `region`; `cloud.availability_zone` từ `zone`), và mọi cặp trong biến môi trường chuẩn `OTEL_RESOURCE_ATTRIBUTES`, nên một bước pipeline có thể gắn `deployment.environment`, tên job hoặc tag tài khoản mà không cần thêm cờ nào:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### Gửi các bản ghi {#shipping-the-records}

Tệp chứa một `LogsData` trên mỗi dòng, đúng dạng mà receiver `otlp_json_file` của OpenTelemetry Collector tiếp nhận mà không cần bộ xử lý biến đổi (transform processor) nào:

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

Mỗi dòng cũng là một `LogsData` OTLP/JSON hoàn chỉnh mà một điểm cuối OTLP/HTTP `POST /v1/logs` chấp nhận làm phần thân yêu cầu.  Khi đẩy, exporter tích hợp sẵn gửi cùng cách mã hóa này nhưng gom các bản ghi thành lô, như mô tả bên dưới.

#### Đẩy đến một điểm cuối OTLP {#pushing-to-an-otlp-endpoint}

Các bản ghi có thể được đẩy thẳng đến một điểm cuối nhật ký OTLP/HTTP, đồng thời vẫn được ghi vào tệp hoặc stdout, bằng cách cấu hình một exporter trong cờ `--otel.config`, một chuỗi JSON hoặc YAML giống như các cờ cấu hình khác (có từ StackQL `v0.12.732` trở đi):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| Khóa | Mục đích |
|--|--|
|`exporter.endpoint`|URL đầy đủ của điểm cuối nhật ký, ví dụ `http://localhost:4318/v1/logs`; bắt buộc để bật exporter|
|`exporter.headers`|Các header của yêu cầu được gửi kèm mỗi lần xuất, thường là một token tiếp nhận dữ liệu hoặc khóa API|
|`exporter.timeout_ms`|Thời gian chờ của yêu cầu tính bằng mili giây, mặc định `10000`|
|`exporter.batch_size`|Số bản ghi trong mỗi yêu cầu, mặc định `512`|

<br />

Mỗi câu lệnh là một lô: các bản ghi hàng và bản ghi hoàn tất của câu lệnh được gửi dưới dạng một `LogsData` OTLP/JSON duy nhất (một lớp bao tài nguyên và phạm vi, một mục trong `logRecords` cho mỗi bản ghi), và một tập kết quả lớn được chia thành các yêu cầu chứa tối đa `batch_size` bản ghi trong khi các hàng được truyền đến, với yêu cầu cuối cùng đi sau bản ghi hoàn tất.  Các phản hồi `429` và `5xx` cùng các lỗi kết nối được thử lại, tối đa bốn lần thử, với thời gian chờ tăng gấp đôi bắt đầu từ 500ms, tuân theo header `Retry-After` dạng số giây chờ (giới hạn ở 30 giây) khi máy chủ gửi header này; các lỗi khác không được thử lại.  Tệp hoặc luồng vẫn được ghi trong mọi trường hợp, nên một lần đẩy thất bại sẽ để lại một bản sao cục bộ và một dòng lỗi trên stderr.  Exporter dùng OTLP/JSON qua HTTP; một điểm cuối chỉ chấp nhận protobuf cần có một Collector đứng trước.

Không biến môi trường `OTEL_EXPORTER_OTLP_*` nào được đọc; mọi thiết lập exporter đều đến từ `--otel.config`.  Một giá trị `--otel.config` sai định dạng, hoặc một đối tượng `exporter` không có `endpoint`, sẽ khiến quá trình khởi động thất bại.

Cách này nhắm đến điểm tiếp nhận OTLP của ClickStack (`http://<host>:4318/v1/logs`, header `authorization: <ingestion API key>`), receiver OTLP của Datadog Agent hoặc điểm tiếp nhận OTLP của Datadog (`dd-api-key: <key>`) và mọi receiver `otlp` của OpenTelemetry Collector.  Xem [Công thức backend OTel](/getting-started/otel-recipes) để biết các truy vấn kiểm kê, sai lệch và quyền sử dụng ở phía nhận.
