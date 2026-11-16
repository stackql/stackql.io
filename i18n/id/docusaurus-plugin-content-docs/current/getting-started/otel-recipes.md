---
title: Resep backend OTel
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - opentelemetry
  - clickhouse
  - datadog
  - inventaris cloud
  - deteksi penyimpangan
description: Inventaris cloud, penyimpangan, dan riwayat hak akses dari output otel StackQL di ClickHouse dan Datadog, dengan contoh CI terjadwal
image: "/img/stackql-featured-image.png"
---
Lihat juga:  
[[` Mode output `]](/getting-started/output-modes#otel-output) [[` exec `]](/command-line-usage/exec)

`stackql exec --output otel` mengubah kueri menjadi snapshot yang diberi stempel waktu: satu catatan log OpenTelemetry per baris, satu catatan penyelesaian per pernyataan, dan kunci korelasi `stackql.snapshot.id`, `stackql.query.hash`, dan `stackql.row.fingerprint` pada setiap catatan (lihat [Output `otel`](/getting-started/output-modes#otel-output)). Halaman ini membawa catatan tersebut dari awal hingga akhir di dua backend yang menjadi sasaran rancangan format ini: tabel inventaris bertipe di ClickHouse (termasuk ClickStack), serta kueri log dan monitor di Datadog. Bagian terakhir berisi job CI terjadwal yang menandai setiap eksekusi dengan ID trace dan atribut sumber daya.

Resep ini menggunakan dua kueri berulang, yaitu inventaris instance Compute Engine dan binding IAM sebuah proyek yang dipecah menjadi satu baris per prinsipal dan peran. Keduanya memproyeksikan kolom `identity`, yaitu kunci yang digunakan backend untuk melacak sumber daya (`id`, `arn`, `selfLink`, atau penggabungan untuk kunci komposit); sidik jari adalah nilainya.

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

Setiap kueri memiliki `stackql.query.hash` yang stabil, dan resep ini memfilter berdasarkan nilai tersebut sehingga satu seri hanya berisi satu kueri berulang dan tidak ada yang lain. Gunakan `{query_hash:String}` sebagai parameter kueri di ClickHouse, atau tempelkan hash dari catatan penyelesaian.

## ClickHouse {#clickhouse}

Exporter ClickHouse milik OpenTelemetry Collector, yang disertakan dalam ClickStack, menyimpan catatan log di `otel_logs` dengan atribut sebagai map string (`ResourceAttributes`, `LogAttributes`) bersama `Timestamp`, `Body`, `TraceId`, dan `ScopeName`. Arahkan exporter ke endpoint penerimaan OTLP milik ClickStack atau ke Collector:

```shell
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }' \
"select id as identity, name, status, machineType, zone from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'"
```

### Tabel inventaris bertipe {#typed-inventory-table}

Mengkueri `otel_logs` secara langsung dapat dilakukan, tetapi setiap pertanyaan kemudian menanggung biaya pencarian map. Tampilan terwujud mengangkat catatan baris ke tabel bertipe dengan kunci `(query_hash, identity, snapshot_time)`:

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

Catatan penyelesaian masuk ke tabel kecil tersendiri. Tabel ini menentukan arti "snapshot terbaru" di setiap resep di bawah: sebuah snapshot baru dihitung setelah catatan penyelesaiannya tiba, sehingga eksekusi yang gagal di tengah jalan tidak pernah disangka sebagai lingkungan yang lebih kecil.

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

Tampilan terwujud hanya melihat baris yang disisipkan setelah tampilan tersebut dibuat. Untuk catatan yang tiba lebih awal, isi tabel secara susulan dengan `INSERT INTO ... SELECT` yang menggunakan kueri select yang sama dengan tampilan tersebut.

### Status terbaru per sumber daya {#latest-state-per-resource}

Lingkungan saat ini adalah kumpulan baris dari snapshot lengkap terbaru:

```sql
WITH (SELECT snapshot_id FROM stackql_snapshots WHERE query_hash = {query_hash:String}
      ORDER BY snapshot_time DESC LIMIT 1) AS latest
SELECT identity, attributes['name'] AS name, attributes['status'] AS status, document
FROM stackql_inventory
WHERE snapshot_id = latest
ORDER BY identity;
```

Untuk status terakhir yang diketahui dari setiap sumber daya yang pernah terlihat, termasuk sumber daya yang kemudian menghilang, lakukan agregasi dengan `argMax`:

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

### Penyimpangan antara dua snapshot {#drift-between-two-snapshots}

Lakukan join pada kedua snapshot berdasarkan identitas. Sidik jari yang tidak sama berarti perubahan; sisi yang tidak ada berarti sumber daya yang muncul atau menghilang:

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

Gunakan dua ID snapshot mana pun sebagai gantinya untuk membandingkan dua titik waktu sembarang. Jika hanya satu sisi yang diperlukan, pertanyaan yang sama dijawab dengan anti-join:

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

Kolom `change` adalah cakupan penyimpangan dari proyeksi: kolom yang tidak disertakan dalam `SELECT` tidak dapat menyebabkan perubahan, sehingga kueri yang hanya memproyeksikan `name` dan `status` melaporkan perubahan status dan tidak ada yang lain.

### Riwayat hak akses {#entitlement-history}

Untuk kueri hak akses, identitasnya adalah pasangan prinsipal dan peran, sehingga linimasa suatu pasangan di tabel inventaris adalah masa berlaku hak akses tersebut:

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

`first_seen` dan `last_seen` meringkas pemberian hak yang dicabut lalu diberikan kembali menjadi satu rentang; jika celahnya penting, `groupArray(snapshot_time)` pada pengelompokan yang sama mencantumkan setiap snapshot yang memuat pasangan tersebut. Semua hal lain di halaman ini berlaku tanpa perubahan: resep penyimpangan pada kueri hak akses mencantumkan pemberian dan pencabutan hak antara dua eksekusi.

## Datadog {#datadog}

Kirim catatan ke receiver OTLP milik Datadog Agent (aktifkan receiver HTTP di `otlp_config` milik Agent, port `4318`) atau milik OpenTelemetry Collector yang menjalankan exporter Datadog. Endpoint penerimaan OTLP milik Datadog sendiri menerima catatan secara langsung dengan kunci API sebagai header; ambil URL regional dari dokumentasi penyerapan OTLP Datadog.

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

### Pemetaan atribut {#attribute-mapping}

| OpenTelemetry | Datadog |
|--|--|
|Atribut sumber daya `service.name`|Tag `service` (`stackql`, atau `OTEL_SERVICE_NAME`)|
|Atribut sumber daya `deployment.environment`|Tag `env`|
|`cloud.provider`, `cloud.account.id`, `cloud.region`, `cloud.availability_zone`, dan atribut sumber daya lainnya|Atribut log, misalnya `@cloud.account.id`|
|Atribut log `stackql.*`|Atribut log, misalnya `@stackql.snapshot.id`; tambahkan facet untuk atribut yang Anda gunakan sebagai filter|
|Kolom baris (`identity`, `name`, `status`, ...)|Atribut log dengan nama kolom tersebut, misalnya `@status`|
|Isi catatan|`message`, baris sebagai JSON|
|`traceId`, `spanId`|`trace_id`, `span_id`, yang mengorelasikan setiap pernyataan dari satu eksekusi|

<br />

### Kueri log {#log-queries}

Datadog menjawab pertanyaan tentang keberadaan dan riwayat; join antara snapshot dilakukan di ClickHouse, atau dengan melakukan fan-out aliran yang sama ke keduanya melalui Collector.

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

Monitor pada catatan penyelesaian mengawasi pipeline itu sendiri: monitor log pada `service:stackql @stackql.snapshot.complete:true @stackql.rows_returned:0` mendeteksi inventaris yang kembali kosong, dan monitor dengan kondisi "no data" pada `service:stackql @stackql.snapshot.complete:true @stackql.job:gce-inventory` mendeteksi eksekusi terjadwal yang tidak terjadi.

## CI terjadwal {#scheduled-ci}

Job terjadwal menghasilkan seri tersebut. Contoh ini menjalankan kedua kueri setiap jam dari GitHub Actions: exporter dikonfigurasi sekali di `OTEL_CONFIG` dan diteruskan ke `--otel.config`, `TRACEPARENT` memberikan ID trace yang sama kepada setiap pernyataan dalam satu eksekusi, `OTEL_RESOURCE_ATTRIBUTES` menandai lingkungan, nama job, dan ID eksekusi pada setiap catatan, dan file `.otlp.jsonl` disimpan sebagai artefak sehingga pengiriman yang gagal tetap meninggalkan salinan.

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

Job yang sama di penjadwal lain hanya memerlukan flag tersebut dan kedua variabel lingkungan; `TRACEPARENT` bersifat opsional: tanpa variabel ini, setiap proses `stackql exec` menghasilkan ID trace sendiri, yang hanya dimiliki bersama oleh pernyataan yang dijalankan oleh proses tersebut, sehingga kedua pernyataan dalam eksekusi di atas akan membawa ID trace yang berbeda.
