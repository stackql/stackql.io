---
title: Flag global
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Flag global untuk biner stackql: autentikasi, format output, lokasi registri, log, dan pengaturan backend SQL, yang tersedia di setiap perintah."
image: "/img/stackql-featured-image.png"
---

Flag global diterima oleh perintah StackQL, tetapi sebagian hanya berlaku untuk mode tertentu. Tetapkan flag ini di baris perintah atau, jika didukung, di file inisialisasi StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)). Flag `--pgsrv.*` tercantum di [opsi server PostgreSQL](/command-line-usage/srv#server-options).

| Flag | Tipe data | Deskripsi | Default |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Konfigurasi akses pemanggil, objek pembungkus JSON `{"path": "/path/to/access.json"}` (jalur file biasa juga diterima).  Mengaktifkan [manajemen akses](/command-line-usage/access-management): pemanggil diautentikasi dengan JWT, token bearer statis, TLS mutual, atau identitas OS lokal, dan pernyataan diotorisasi berdasarkan aturan kebijakan.  Kredensial token lokal dibaca dari `STACKQL_CALLER_TOKEN`.  Tersedia mulai StackQL `v1.0`. | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Mengizinkan sesi memilih penyimpanan rahasia OS persisten (macOS Keychain, Linux Secret Service, atau Windows Credential Manager) dengan `SET "$.secret_store"`.  Penyimpanan berbasis file datar tetap ditolak meskipun flag ini digunakan.  Lihat [Sesi provider dan rahasia](/getting-started/provider-sessions#persistent-os-secret-stores).  Tersedia mulai StackQL `v1.0`. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Mengizinkan nilai rahasia dan kredensial didereferensi dalam ekspresi SQL (`"$secrets.name"`, `"$auth.provider.field"`), dengan konsekuensi nilai tersebut dapat masuk ke hasil, log, dan artefak kueri yang disimpan.  Tidak diperlukan untuk autentikasi provider biasa.  Lihat [Nilai rahasia di SQL](/getting-started/provider-sessions#secret-values-in-sql).  Tersedia mulai StackQL `v1.0`. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Batas waktu permintaan API dalam detik, 0 berarti tanpa batas waktu | `45` |
| <span class="nowrap">`--approot`</span> | string | Jalur root konfigurasi dan cache aplikasi | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Mode file konfigurasi dan cache aplikasi | `493` |
| <span class="nowrap">`--auth`</span> | string | Pasangan kunci-nilai konteks `auth` dalam bentuk json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Jumlah kunci awal cache | `100` |
| <span class="nowrap">`--configfile`</span> | string | Jalur lengkap file konfigurasi | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | File `cpuprofile`, tidak ada jika kosong | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | integer | Kedalaman maksimum dependensi dataflow untuk kueri tertentu | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | integer | Jumlah maksimum komponen dataflow yang terhubung lemah untuk kueri tertentu | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | String JSON / YAML untuk mengonfigurasi penanganan kueri pemeliharaan DBMS | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Pembatas untuk output csv. Hanya satu karakter.  Diabaikan untuk semua output selain csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | Flag `dryrun`; hanya menjalankan praprosesor, output hasil template akan dikembalikan | `false` |
| <span class="nowrap">`--env.file`</span> | string | File opsional bergaya dotenv yang dimuat ke lingkungan proses saat startup (semua perintah); alat MCP [`reload_credentials`](/mcp/reload_credentials) memuatnya ulang sesuai permintaan. File yang tidak ada ditoleransi; kunci dengan nilai kosong dilewati; variabel yang sudah ada diganti. Tidak dimuat dari `.stackqlrc`. Tersedia di rilis StackQL setelah `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | integer | Batas konkurensi untuk eksekusi kueri | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Prefiks alias ekspor (namespace atau skema) | |
| <span class="nowrap">`--gc`</span> | string | String JSON / YAML yang merepresentasikan konfigurasi GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Menonaktifkan header kolom, hanya valid dengan `--output csv` atau `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Menampilkan info permintaan http di terminal | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Host proxy (kosongkan jika tanpa proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Kata sandi proxy | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Port proxy, angka apa pun yang kurang dari atau sama dengan 0 akan menghasilkan port default untuk skema tertentu (misalnya `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Skema proxy, `http` atau `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Pengguna proxy | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Jumlah hasil maksimum per permintaan http, angka apa pun yang kurang dari atau sama dengan 0 berarti tanpa batasan | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Jumlah halaman hasil maksimum yang akan dikembalikan per sumber daya, angka apa pun yang kurang dari atau sama dengan 0 berarti tanpa batasan | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Kedalaman maksimum untuk kueri tidak langsung: tampilan dan subkueri | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | File input (file IQL) tempat kueri dibaca | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | File konteks (data) untuk penggunaan template (file `json` atau `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | Level log, harus salah satu dari \{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} (`warning` diterima sebagai alias untuk `warn`) | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | Objek konfigurasi JSON / YAML untuk [server MCP](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` dan `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Pengodean log audit MCP, `jsonl` atau `otel` (catatan log OTLP/JSON); mengganti `server.audit.format` di `--mcp.config`.  Lihat [Format log](/command-line-usage/mcp#log-format).  Tersedia mulai StackQL `v0.11.660`. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Revisi protokol MCP terbaru yang dinyatakan, `auto`, `2026-07-28` (hanya tanpa sesi), atau revisi yang lebih lama seperti `2025-11-25`; mengganti `server.protocol_version` di `--mcp.config`.  Lihat [Mengunci revisi](/command-line-usage/mcp#pinning-the-revision).  Tersedia mulai StackQL `v0.12.732`. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | Tipe server MCP, salah satu dari \{`http`, `stdio`, `reverse_proxy`\}.  Lihat [Tipe server MCP](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL untuk dokumen metadata dalam cache, dalam detik | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | String JSON / YAML yang merepresentasikan namespace untuk cache, tampilan, dan sebagainya | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Bekerja secara offline menggunakan data cache | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | File output tempat hasil ditulis | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Format output, harus salah satu dari \{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} (`ndjson` diterima sebagai alias untuk `jsonl`).  `otel` menghasilkan catatan log OpenTelemetry, lihat [Mode output](/getting-started/output-modes#otel-output); tersedia mulai StackQL `v0.11.660`.  Dengan exporter yang dikonfigurasi di `--otel.config`, catatan `otel` juga dikirim ke endpoint tersebut | `table` |
| <span class="nowrap">`--otel.config`</span> | string | String JSON / YAML yang mengonfigurasi format output `otel`; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms`, dan `exporter.batch_size` mengirim catatan ke endpoint log OTLP/HTTP, lihat [Mengirim langsung ke endpoint OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  Tersedia mulai StackQL `v0.12.732` | `{}` |
| <span class="nowrap">`--preview`</span> | string | String JSON yang mengaktifkan fungsionalitas pratinjau.  `{"unstable": true}` mengekspos relasi `stackql_unstable_<provider>.<service>.<resource>` berbasis dokumen, yang diselesaikan dan dijalankan oleh `omnisdk` langsung dari dokumen provider dengan baris yang dialirkan segera setelah dihasilkan; kunci lainnya (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) menyesuaikan backend tersebut.  Fungsionalitas pratinjau tidak stabil: perilaku, sintaksis, dan output dapat berubah atau rusak di antara rilis tanpa pemberitahuan, jadi jangan mengandalkannya di lingkungan produksi.  Mulai `v1.0`, aktivasi eksplisit yang sama mengekspos relasi `stackql_preview.iac.run` dan `stackql_preview.iac.destroy`, yang menerapkan dan menghapus kumpulan sumber daya yang dideklarasikan melalui `omnisdk` dengan status yang disimpan di bawah root aplikasi.  Lihat [Fungsionalitas pratinjau](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | Provider default awal. Misalnya, dengan `--provider github`, `repos.repos` di-resolve menjadi `github.repos.repos` tanpa pernyataan `USE` sebelumnya. Tidak melakukan autentikasi ke provider. | tidak ada |
| <span class="nowrap">`--querycachesize`</span> | integer | Ukuran cache LRU untuk rencana kueri, dalam jumlah entri | `10000` |
| <span class="nowrap">`--registry`</span> | string | String JSON / YAML yang mengonfigurasi lokasi registri provider dan verifikasi tanda tangan dokumen provider | lihat [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | String JSON / YAML yang merepresentasikan konfigurasi sesi | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | String JSON / YAML yang merepresentasikan konfigurasi sistem backend SQL | lihat [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | String JSON / YAML yang merepresentasikan konfigurasi penyimpanan Txn | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Jalur ke bundle CA; jika tidak ditentukan, default sistem akan digunakan |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Mengizinkan kepercayaan terhadap sertifikat yang tidak aman (tidak disarankan) |  |
| <span class="nowrap">`--upstream.errors`</span> | string | Kebijakan JSON / YAML untuk menoleransi status respons HTTP secara eksplisit pada operasi baca provider. Lihat [`--upstream.errors`](#--upstream-errors). | `{}` |
| <span class="nowrap">`--var`</span> | string | Variabel eksternal yang diberikan sebagai pasangan kunci/nilai untuk dimasukkan ke kueri StackQL melalui file data `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Output verbose | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Bantuan sesuai konteks untuk stackql | |
| <span class="nowrap">`--version`</span> | | Menampilkan versi program StackQL | |

Flag `--colorscheme` tidak digunakan lagi dan sudah tidak mengubah warna shell. Flag `--usenonpreferredapis` diterima, tetapi tidak mengubah pemilihan API di mesin saat ini.

## `--registry` {#--registry}

Flag `--registry` mengonfigurasi dari mana dokumen provider dibaca dan apakah tanda tangannya diverifikasi.  Nilainya berupa string JSON dengan kunci berikut:

| Kunci | Deskripsi | Default |
| -- | -- | -- |
| `url` | URL registri.  Registri provider StackQL publik digunakan jika tidak ditentukan. | registri publik |
| `localDocRoot` | Direktori lokal yang berisi dokumen provider, yang dibaca sebagai pengganti pengunduhan dari registri.  Digunakan saat [mengembangkan provider](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Prefiks jalur di bawah root registri tempat dokumen sumber provider berada. | `src` |
| `verifyConfig.nopVerify` | `true` melewati verifikasi tanda tangan.  Tetapkan ke `false` untuk mewajibkan tanda tangan ML-DSA-65 (FIPS 204) tersemat pada setiap dokumen provider dan layanan yang dibaca, termasuk relasi pratinjau berbasis dokumen. | `true` |
| `verifyConfig.publicKeys` | Objek JSON yang memetakan ID kunci ke kunci publik ML-DSA-65 yang dikodekan hex, untuk registri privat.  Objek ini menggantikan kunci produksi tersemat; objek kosong yang eksplisit merupakan error. | kunci tersemat |

### Contoh: mewajibkan dokumen provider yang ditandatangani {#example-require-signed-provider-documents}

Mengaktifkan verifikasi untuk setiap dokumen provider dan layanan yang dibaca dari registri publik, yang diperiksa terhadap kunci produksi tersemat:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Contoh: membaca dokumen tanpa tanda tangan dari direktori lokal {#example-read-unsigned-documents-from-a-local-directory}

Mengarahkan registri ke direktori berisi dokumen provider yang sedang dikembangkan.  Dokumen tersebut tidak ditandatangani, sehingga verifikasi tetap nonaktif:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

Verifikasi tanda tangan tersedia mulai StackQL `v1.0` dan nonaktif secara default.  Tanda tangan mencakup byte YAML yang persis dari sebuah dokumen, sehingga setiap pengeditan, termasuk perubahan format atau baris baru, membuat tanda tangan tidak valid; tandatangani ulang dengan alat `pqsig sign-doc` milik registri.  Dokumen pengembangan lokal yang tidak ditandatangani harus tetap menggunakan `nopVerify: true`.  Opsi sertifikat sebelumnya dan file sidecar `.sig` tidak lagi digunakan, dan arsip yang dipublikasikan hanya dengan sidecar harus dipublikasikan ulang sebelum digunakan dengan verifikasi.  Verifikasi mengautentikasi dokumen saat dokumen dibaca; pengunduhan dan ekstraksi arsip tidak tercakup.

:::

## `--sqlBackend` {#--sqlbackend}

Flag `--sqlBackend` mengonfigurasi backend SQL eksternal untuk StackQL, sehingga Anda dapat menggunakan mesin database eksternal (seperti PostgreSQL) sebagai pengganti mesin SQL tersemat default. Ini berguna untuk memanfaatkan database eksternal untuk pemrosesan kueri, penyimpanan persisten, atau integrasi dengan infrastruktur database yang sudah ada.

Nilainya berupa string JSON dengan kunci berikut:

| Kunci | Deskripsi | Contoh nilai |
| -- | -- | -- |
| `dbEngine` | Tipe mesin database | `postgres_tcp` |
| `sqlDialect` | Dialek SQL yang digunakan | `postgres` |
| `dsn` | Nama sumber data (string koneksi) untuk database | Lihat contoh di bawah |

### Contoh: menggunakan backend PostgreSQL {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Contoh: menggunakan antarmuka PostgreSQL Databricks {#example-using-databricks-postgresql-interface}

Databricks menyediakan antarmuka yang kompatibel dengan PostgreSQL yang dapat digunakan sebagai backend SQL StackQL. DSN menggunakan format string koneksi PostgreSQL standar dengan kredensial Databricks Anda:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Karakter khusus dalam nama pengguna atau kata sandi (seperti `@`) harus dikodekan URL di DSN (misalnya, `@` menjadi `%40`).
:::

## `--upstream.errors` {#--upstream-errors}

Gunakan `http.ignoreStatusCodes` untuk mencantumkan status respons HTTP yang harus ditoleransi secara eksplisit oleh operasi baca provider. Entri berupa string: gunakan kode persis seperti `"503"` atau kelas seperti `"5xx"`. Nilai default `{}` tidak menambahkan pengecualian apa pun pada penanganan normal respons upstream oleh mesin. Pengaturan ini tidak mencoba ulang permintaan yang gagal dan tidak berlaku untuk mutasi atau pertukaran autentikasi.

Respons yang diabaikan tidak menghasilkan baris. Operasi baca lain dalam kueri tetap dapat mengembalikan baris, sehingga kueri yang berhasil dapat mengembalikan hasil yang tidak lengkap. StackQL melaporkan status HTTP yang diabaikan sebagai diagnostik.

Misalnya, [pengujian join GitHub dengan API mock](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104) menggunakan opsi ini:

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

Dalam pengujian tersebut, permintaan kolaborator untuk satu repositori mengembalikan HTTP `500`. Kueri tetap mengembalikan baris kolaborator untuk repositori lainnya, dan stderr berisi `ignoring upstream response: 500 INTERNAL SERVER ERROR`. Utamakan kode status persis jika hanya satu kegagalan yang diperkirakan.
