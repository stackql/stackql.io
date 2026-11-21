---
title: Mode output
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Mode output StackQL: table, json, jsonl, csv, text, dan otel, cara memilihnya, dan apa yang dihasilkan masing-masing untuk penggunaan shell, exec, dan server."
image: "/img/stackql-featured-image.png"
---
Lihat juga:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

Hasil yang dikembalikan oleh kueri StackQL dapat diformat dalam format tabular (table), serta dalam format json, jsonl (JSON yang dibatasi baris baru), csv, text, atau otel (catatan log OpenTelemetry).  Format JSON, JSONL, dan CSV dapat digunakan untuk bertukar data dengan program lain; format `otel` mengirimkan kumpulan hasil ke backend OpenTelemetry sebagai snapshot dengan stempel waktu.  Format output yang diinginkan dikonfigurasi menggunakan flag StackQL `--output`.

### Format output `table` {#table-output-format}
Hasil dapat diformat sebagai tabel menggunakan nilai `table` untuk parameter `--output` seperti yang ditunjukkan di bawah ini (ini adalah nilai default untuk shell interaktif StackQL):

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

### Output `json` {#json-output}
Hasil dapat dikembalikan dalam format JSON menggunakan nilai `json` untuk parameter `--output` seperti yang ditunjukkan di bawah ini; format output ini berguna jika hasil kueri perlu diteruskan ke proses atau skrip eksternal:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output json

[{"id":"5584456226809282885","name":"demo-instance-3"}
,{"id":"1257085253691867467","name":"demo-instance-1"}
,{"id":"2586731219281477960","name":"demo-instance-2"}]
```


### Output `jsonl` {#jsonl-output}
Hasil dapat dikembalikan dalam format JSON yang dibatasi baris baru (JSON Lines) menggunakan nilai `jsonl` untuk parameter `--output` seperti yang ditunjukkan di bawah ini (`ndjson` diterima sebagai alias).  Setiap baris hasil ditulis sebagai satu objek JSON di baris teksnya sendiri, dan baris langsung di-flush saat ditulis, sehingga format output ini berguna untuk mengalirkan hasil ke alat yang berorientasi baris atau menyalurkan kumpulan hasil yang besar ke program lain:

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output jsonl

{"id":"5584456226809282885","name":"demo-instance-3"}
{"id":"1257085253691867467","name":"demo-instance-1"}
{"id":"2586731219281477960","name":"demo-instance-2"}
```


### Output `csv` {#csv-output}
Hasil dapat dikembalikan dalam format CSV menggunakan nilai `csv` untuk parameter `--output` seperti yang ditunjukkan di bawah ini; format output ini berguna untuk mengurai hasil di Excel atau menyediakan antarmuka data ke sistem lain:

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

#### Contoh output CSV (dengan pembatas alternatif) {#csv-output-example-with-alternative-delimiter}
Pembatas alternatif dapat ditentukan untuk format output `csv` menggunakan flag `-d` seperti yang ditunjukkan di bawah ini:

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

### Output `text` {#text-output}
Hasil dapat dikembalikan sebagai teks biasa menggunakan nilai `text` untuk parameter `--output`.  Setiap baris hasil ditulis sebagai nilai-nilainya yang digabungkan dengan koma, didahului oleh baris header kecuali `-H` (`--hideheaders`) ditetapkan.  Tidak seperti `csv`, nilai tidak diapit tanda kutip atau di-escape dan pembatas `-d` diabaikan, sehingga nilai yang berisi koma atau tanda kutip ganda ditulis apa adanya:

```shell
stackql exec "SELECT 'a,b' AS letters, 'say \"hi\"' AS greeting" --output text

letters,greeting
a,b,say "hi"
```

Dengan `--output csv`, kueri yang sama mengembalikan `"a,b","say ""hi"""` sebagai baris datanya.

### Output `otel` {#otel-output}
Hasil dapat dikeluarkan sebagai catatan log OpenTelemetry menggunakan nilai `otel` untuk parameter `--output`.  Setiap baris hasil ditulis sebagai satu objek OTLP/JSON `LogsData` di baris teksnya sendiri, di-flush saat ditulis seperti `jsonl`, lalu diikuti oleh satu catatan penyelesaian untuk pernyataan tersebut.  Format output ini mengubah kueri menjadi snapshot konfigurasi cloud dalam bentuk telemetri: jalankan kueri inventaris secara terjadwal, kirim catatan ke backend yang mendukung OTLP (ClickHouse, Datadog, atau OpenTelemetry Collector di depan backend lain), lalu jawab "seperti apa kondisi lingkungan cloud saat ini", "apa yang berubah sejak terakhir kali dijalankan", dan "siapa yang memiliki hak akses apa, dan sejak kapan" di sana.  Tersedia mulai StackQL `v0.11.660`.

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

Satu catatan baris, diformat agar mudah dibaca (file menyimpan setiap catatan dalam satu baris):

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

#### Snapshot {#snapshots}

Satu pernyataan adalah satu snapshot.  Setiap catatan dari pernyataan tersebut membawa `timeUnixNano` yang sama (waktu mulai pernyataan), `stackql.snapshot.id` yang sama, dan `spanId` yang sama, sehingga kueri as-of, diff, dan kueri status terbaru tetap berfungsi meskipun baris tiba melalui beberapa halaman selama beberapa detik; `observedTimeUnixNano` mencatat kapan setiap catatan benar-benar ditulis.  Semua pernyataan dalam satu pemanggilan berbagi satu `traceId`, yang diambil dari variabel lingkungan `TRACEPARENT` jika variabel tersebut ditetapkan (konvensi untuk pipeline CI) dan dibuat jika tidak.

Catatan terakhir dari sebuah pernyataan adalah catatan penyelesaian: body `snapshot complete <provider.service.resource>`, dengan `stackql.snapshot.complete=true`, `stackql.rows_returned`, dan `stackql.duration_ms`.  Catatan ini memberi tahu backend bahwa snapshot sudah lengkap, dan pernyataan yang tidak mengembalikan baris apa pun tetap mengeluarkannya, sehingga sumber daya yang telah hilang muncul sebagai penyimpangan, bukan sebagai kekosongan.  Pernyataan yang gagal menghasilkan catatan `ERROR` yang membawa `error.type` dan `error.message` jika penyajian error adalah `record`; secara default, error dikirim ke stderr seperti pada semua format output lainnya.

#### Bentuk catatan {#record-shape}

Body berisi baris lengkap sebagai JSON ringkas, dengan objek bersarang tetap utuh, dan merupakan sumber kebenaran untuk dokumen lengkap.  Atribut membawa setiap kolom dengan namanya sendiri, bertipe jika nilainya skalar (`stringValue`, `intValue`, `doubleValue`, `boolValue`); kolom bertingkat dibawa sebagai string JSON, yang portabel di berbagai backend dengan skema atribut default yang bernilai string, dan kolom null dihilangkan.  Atribut `stackql.*` yang dicadangkan adalah:

| Atribut | Catatan | Nilai |
|--|--|--|
|`stackql.snapshot.id`|semua|UUID yang dibuat per eksekusi pernyataan|
|`stackql.query`|semua|Teks kueri yang dikirim, apa adanya|
|`stackql.query.hash`|semua|SHA-256 dari teks kueri; kunci seri yang lebih ringan daripada SQL untuk job berulang|
|`stackql.provider`, `stackql.service`, `stackql.resource`|semua|Tabel utama pernyataan; dihilangkan untuk join dan tampilan yang mencakup beberapa tabel|
|`stackql.row.index`|baris|Posisi berbasis nol dalam kumpulan hasil|
|`stackql.row.fingerprint`|baris|`sha256:` dari JSON baris kanonis, sehingga penyimpangan cukup diperiksa dengan satu perbandingan per identitas, bukan perbandingan atas setiap atribut|
|`stackql.snapshot.complete`|penyelesaian|`true`|
|`stackql.rows_returned`|penyelesaian|Jumlah baris untuk pernyataan|
|`stackql.duration_ms`|penyelesaian|Durasi waktu nyata sejak pernyataan dimulai|

<br />

Kumpulan atribut yang dikeluarkan merupakan antarmuka berversi: cakupan instrumentasi adalah `github.com/stackql/stackql/internal/stackql/output` pada versi `1.0.0`, dan `schemaUrl` mengunci konvensi stabil ke `https://opentelemetry.io/schemas/1.44.0`.

Identitas sumber daya tidak disimpulkan: sidik jari adalah nilainya dan backend yang memilih kuncinya (`id`, `arn`, `selfLink`, atau tuple kolom).  Untuk melacak hak akses sebagai entitas, pecah binding menjadi satu baris per prinsipal dan peran di SQL; telemetri mengikuti granularitas hasil.

#### Atribut sumber daya {#resource-attributes}

Sumber daya membawa `service.name` (`stackql`, atau nilai `OTEL_SERVICE_NAME`) dan `service.version`, konvensi cloud OpenTelemetry jika dapat diturunkan dari `SELECT` satu tabel (`cloud.provider` dari provider; `cloud.account.id` dari kondisi kesamaan `project` atau `subscriptionId` di klausa `WHERE`; `cloud.region` dari `region`; `cloud.availability_zone` dari `zone`), serta setiap pasangan dalam variabel lingkungan standar `OTEL_RESOURCE_ATTRIBUTES`, sehingga langkah pipeline dapat membubuhkan `deployment.environment`, nama job, atau tag akun tanpa flag tambahan:

```shell
TRACEPARENT=00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01 \
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=prod,stackql.job=gce-inventory \
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl
```

#### Mengirim catatan {#shipping-the-records}

File berisi satu `LogsData` per baris, bentuk yang diterima oleh receiver `otlp_json_file` dari OpenTelemetry Collector tanpa transform processor:

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

Setiap baris juga merupakan objek OTLP/JSON `LogsData` lengkap yang diterima endpoint OTLP/HTTP `POST /v1/logs` sebagai isi permintaan.  Exporter push bawaan mengirim pengodean yang sama tetapi mengelompokkan catatan ke dalam batch, seperti yang dijelaskan di bawah.

#### Mengirim langsung ke endpoint OTLP {#pushing-to-an-otlp-endpoint}

Catatan dapat dikirim langsung ke endpoint log OTLP/HTTP, selain ditulis ke file atau stdout, dengan mengonfigurasi exporter di flag `--otel.config`, yaitu string JSON atau YAML seperti flag konfigurasi lainnya (tersedia mulai StackQL `v0.12.732`):

```shell
stackql exec "select id, name from google.compute.instances \
where project = 'stackql-demo' and zone = 'australia-southeast1-a'" \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "stackql-demo.json" } }' --output otel -f instances.otlp.jsonl \
--otel.config '{ "exporter": { "endpoint": "http://clickstack.example:4318/v1/logs", "headers": { "authorization": "'"$CLICKSTACK_INGESTION_KEY"'" } } }'
```

| Kunci | Kegunaan |
|--|--|
|`exporter.endpoint`|URL lengkap endpoint log, misalnya `http://localhost:4318/v1/logs`; wajib untuk mengaktifkan exporter|
|`exporter.headers`|Header permintaan yang dikirim bersama setiap ekspor, biasanya token penyerapan atau kunci API|
|`exporter.timeout_ms`|Batas waktu permintaan dalam milidetik, default `10000`|
|`exporter.batch_size`|Jumlah catatan per permintaan, default `512`|

<br />

Setiap pernyataan adalah satu batch: catatan baris dan catatan penyelesaiannya dikirim sebagai satu `LogsData` OTLP/JSON (satu amplop sumber daya dan cakupan, satu entri di `logRecords` per catatan), dan kumpulan hasil yang besar dipecah menjadi permintaan berisi paling banyak `batch_size` catatan selama baris mengalir, dengan permintaan terakhir dikirim setelah catatan penyelesaian.  Respons `429` dan `5xx` serta kegagalan koneksi dicoba ulang hingga empat kali percobaan dengan backoff yang berlipat dua mulai dari 500ms, dengan mematuhi header `Retry-After` berformat detik penundaan (dibatasi hingga 30 detik) jika server mengirimkannya; error lain tidak dicoba ulang.  File atau stream tetap ditulis, sehingga pengiriman yang gagal meninggalkan salinan lokal dan satu baris error di stderr.  Exporter menggunakan OTLP/JSON melalui HTTP; endpoint yang hanya menerima protobuf memerlukan Collector di depannya.

Tidak ada variabel lingkungan `OTEL_EXPORTER_OTLP_*` yang dibaca; setiap pengaturan exporter berasal dari `--otel.config`.  Nilai `--otel.config` yang tidak berformat benar, atau objek `exporter` tanpa `endpoint`, menyebabkan startup gagal.

Exporter ini ditujukan untuk endpoint penerimaan OTLP ClickStack (`http://<host>:4318/v1/logs`, header `authorization: <ingestion API key>`), receiver OTLP dari Datadog Agent atau endpoint penerimaan OTLP Datadog (`dd-api-key: <key>`), dan receiver `otlp` OpenTelemetry Collector mana pun.  Lihat [Resep backend OTel](/getting-started/otel-recipes) untuk kueri inventaris, penyimpangan, dan hak akses di sisi penerima.
