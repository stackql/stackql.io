---
title: Công thức backend OTel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - kiểm kê đám mây
  - phát hiện sai lệch
description: Kiểm kê đám mây, sai lệch và lịch sử quyền sử dụng từ đầu ra otel của StackQL trong ClickHouse và Datadog, kèm ví dụ CI định kỳ
image: "/img/stackql-featured-image.png"
---
Xem thêm:  
[[` Chế độ đầu ra `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` biến một truy vấn thành một ảnh chụp nhanh có dấu thời gian: một bản ghi nhật ký OpenTelemetry cho mỗi hàng, một bản ghi hoàn tất cho mỗi câu lệnh và các khóa tương quan `stackql.snapshot.id`, `stackql.query.hash` và `stackql.row.fingerprint` trên mọi bản ghi (xem [Đầu ra `otel`](/getting-started/output-modes#otel-output)).  Trang này đưa các bản ghi đó đi từ đầu đến cuối trong hai backend mà định dạng này được thiết kế để phục vụ: một bảng kiểm kê có kiểu trong ClickHouse (bao gồm ClickStack), cùng các truy vấn nhật ký và trình giám sát trong Datadog.  Phần cuối là một job CI định kỳ gắn cho mỗi lần chạy một ID trace và các thuộc tính tài nguyên.

Các công thức sử dụng hai truy vấn định kỳ, một truy vấn kiểm kê các instance Compute Engine và một truy vấn lấy các liên kết IAM của một dự án, được trải phẳng thành một hàng cho mỗi principal và vai trò.  Cả hai đều chiếu ra một cột `identity`, là khóa mà backend dùng để theo dấu một tài nguyên (`id`, `arn`, `selfLink` hoặc một phép nối chuỗi cho khóa tổng hợp); dấu vân tay là giá trị.

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

Mỗi truy vấn có một `stackql.query.hash` ổn định, và các công thức lọc theo giá trị này để một chuỗi thời gian chỉ gồm một truy vấn định kỳ và không gì khác.  Dùng `{query_hash:String}` làm tham số truy vấn trong ClickHouse, hoặc dán hash từ một bản ghi hoàn tất.

## ClickHouse {#clickhouse}

Exporter ClickHouse của OpenTelemetry Collector, được ClickStack đóng gói sẵn, ghi các bản ghi nhật ký vào `otel_logs` với các thuộc tính ở dạng map chuỗi (`ResourceAttributes`, `LogAttributes`) cùng với `Timestamp`, `Body`, `TraceId` và `ScopeName`.  Trỏ exporter đến điểm tiếp nhận OTLP của ClickStack hoặc đến một Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Bảng kiểm kê có kiểu {#typed-inventory-table}

Truy vấn trực tiếp `otel_logs` vẫn hoạt động, nhưng khi đó mọi câu hỏi đều phải trả chi phí tra cứu map.  Một dạng xem vật chất hóa đưa các bản ghi hàng vào một bảng có kiểu với khóa là `(query_hash, identity, snapshot_time)`:

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

Các bản ghi hoàn tất được đưa vào một bảng nhỏ riêng.  Bảng này định nghĩa "ảnh chụp nhanh mới nhất" nghĩa là gì trong mọi công thức bên dưới: một ảnh chụp nhanh chỉ được tính khi bản ghi hoàn tất của nó đã đến, vì vậy một lần chạy thất bại giữa chừng không bao giờ bị nhầm thành một môi trường nhỏ hơn.

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

Một dạng xem vật chất hóa chỉ thấy các hàng được chèn sau khi nó được tạo.  Nạp bù các bản ghi đã đến trước đó bằng một `INSERT INTO ... SELECT` sử dụng cùng truy vấn select như dạng xem.

### Trạng thái mới nhất của mỗi tài nguyên {#latest-state-per-resource}

Môi trường hiện tại là tập hàng của ảnh chụp nhanh hoàn tất mới nhất:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Để có trạng thái được biết gần nhất của mọi tài nguyên từng xuất hiện, bao gồm cả những tài nguyên đã biến mất sau đó, hãy tổng hợp bằng hàm `argMax`:

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

### Sai lệch giữa hai ảnh chụp nhanh {#drift-between-two-snapshots}

Nối hai ảnh chụp nhanh theo identity.  Dấu vân tay khác nhau là một thay đổi; thiếu một phía là một tài nguyên đã xuất hiện hoặc đã biến mất:

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

Thay bằng hai ID ảnh chụp nhanh bất kỳ để so sánh các thời điểm tùy ý.  Khi chỉ cần một phía, cùng câu hỏi đó là một phép anti-join:

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

Cột `change` phản ánh phạm vi sai lệch của phép chiếu: các cột không có trong `SELECT` không thể gây ra thay đổi, vì vậy một truy vấn chỉ chiếu ra `name` và `status` sẽ báo cáo các thay đổi trạng thái và không gì khác.

### Lịch sử quyền sử dụng {#entitlement-history}

Với truy vấn quyền sử dụng, identity là cặp principal và vai trò, vì vậy dòng thời gian của một cặp trong bảng kiểm kê chính là thời gian tồn tại của quyền sử dụng đó:

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

`first_seen` và `last_seen` gộp một quyền đã bị thu hồi rồi sau đó được cấp lại thành một khoảng duy nhất; khi các khoảng trống là quan trọng, `groupArray(snapshot_time)` trên cùng cách nhóm sẽ liệt kê mọi ảnh chụp nhanh mà cặp đó có mặt.  Mọi phần khác trên trang này vẫn áp dụng nguyên vẹn: công thức sai lệch chạy trên truy vấn quyền sử dụng sẽ liệt kê các lần cấp quyền và thu hồi quyền giữa hai lần chạy.

## Datadog {#datadog}

Gửi các bản ghi đến receiver OTLP của một Datadog Agent (bật receiver HTTP trong `otlp_config` của Agent, cổng `4318`) hoặc của một OpenTelemetry Collector chạy exporter Datadog.  Điểm tiếp nhận OTLP của chính Datadog nhận trực tiếp các bản ghi với khóa API làm header; lấy URL theo khu vực từ tài liệu về tiếp nhận OTLP của Datadog.

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

### Ánh xạ thuộc tính {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Thuộc tính tài nguyên `service.name`|Tag `service` (`stackql` hoặc `OTEL_SERVICE_NAME`)|
|Thuộc tính tài nguyên `deployment.environment`|Tag `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone` và các thuộc tính tài nguyên khác|Thuộc tính nhật ký, ví dụ `@cloud.account.id`|
|Thuộc tính nhật ký `stackql.*`|Thuộc tính nhật ký, ví dụ `@stackql.snapshot.id`; thêm facet cho những thuộc tính bạn lọc theo|
|Các cột của hàng (`identity`, `name`, `status`, ...)|Thuộc tính nhật ký mang tên cột, ví dụ `@status`|
|Phần thân bản ghi|`message`, hàng ở dạng JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, liên kết tương quan mọi câu lệnh của một lần chạy|

<br />

### Truy vấn nhật ký {#log-queries}

Datadog trả lời các câu hỏi về sự hiện diện và lịch sử; phép nối giữa các ảnh chụp nhanh được thực hiện trong ClickHouse, hoặc bằng cách fan-out cùng một luồng dữ liệu đến cả hai thông qua một Collector.

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

Các trình giám sát đặt trên bản ghi hoàn tất sẽ giám sát chính pipeline: một trình giám sát nhật ký trên `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` phát hiện một lần kiểm kê trả về rỗng, và một trình giám sát có điều kiện "no data" trên `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` phát hiện một lần chạy định kỳ đã không diễn ra.

## CI định kỳ {#scheduled-ci}

Một job định kỳ tạo ra chuỗi thời gian.  Ví dụ này chạy cả hai truy vấn mỗi giờ từ GitHub Actions: exporter được cấu hình một lần trong `OTEL_CONFIG` và được truyền cho `--otel.config`; `TRACEPARENT` gán cùng một ID trace cho mọi câu lệnh của một lần chạy; `OTEL_RESOURCE_ATTRIBUTES` gắn môi trường, tên job và ID lần chạy lên mọi bản ghi; các tệp `.otlp.jsonl` được giữ lại dưới dạng artifact để một lần đẩy dữ liệu thất bại vẫn để lại một bản sao.

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

Cùng job này trên bất kỳ trình lập lịch nào khác chỉ cần cờ và hai biến môi trường; `TRACEPARENT` là không bắt buộc: nếu không có biến này, mỗi tiến trình `stackql exec` tự tạo ID trace riêng, chỉ được chia sẻ giữa các câu lệnh do tiến trình đó chạy, vì vậy hai câu lệnh của lần chạy ở trên sẽ mang các ID trace khác nhau.
