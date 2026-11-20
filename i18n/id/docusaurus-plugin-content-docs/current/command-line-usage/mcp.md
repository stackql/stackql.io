---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Perintah stackql mcp menjalankan StackQL sebagai server Model Context Protocol untuk agen AI: mode deployment, mode keamanan, pemuatan ulang kredensial, dan log audit."
image: "/img/stackql-featured-image.png"
---

Perintah yang digunakan untuk menjalankan StackQL sebagai server Model Context Protocol (MCP), sehingga agen dan asisten AI dapat berinteraksi dengan infrastruktur cloud menggunakan kemampuan kueri StackQL.

:::info[Apa itu MCP?]

[Model Context Protocol (MCP)](https://modelcontextprotocol.io/) adalah protokol terbuka yang menstandarkan cara aplikasi AI berinteraksi dengan sumber data dan alat eksternal.  Dengan menjalankan StackQL sebagai server MCP, Anda dapat memungkinkan agen AI (Claude, Cursor, Continue, agen kustom, dll.) mengkueri dan mengelola infrastruktur cloud di beberapa provider menggunakan bahasa alami.

:::

* * *

### Sintaksis {#syntax}

`stackql mcp [flags]` untuk server MCP mandiri.

`stackql srv [flags]` untuk menjalankan server MCP bersama server protokol komunikasi PostgreSQL dalam proses yang sama.

* * *

### Mode deployment {#deployment-modes}

Server MCP StackQL dapat di-deploy dalam tiga konfigurasi berbeda untuk memenuhi berbagai kebutuhan arsitektur.  Contoh di bawah ini mengaktifkan secara eksplisit layanan tanpa autentikasi dengan `"allow_unauthenticated": true`, yang cocok untuk pengembangan lokal; untuk apa pun yang dapat dijangkau oleh proses atau host lain, konfigurasikan token bearer sebagai gantinya (lihat [Autentikasi klien HTTP](#http-client-authentication)).

#### 1. Server MCP mandiri {#1-standalone-mcp-server}

Jalankan StackQL sebagai server MCP khusus pada port tertentu.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**Kasus penggunaan:** saat Anda hanya memerlukan akses protokol MCP dan tidak memerlukan kompatibilitas protokol komunikasi PostgreSQL.

#### 2. Server MCP + PostgreSQL (dalam memori) {#2-mcp--postgresql-server-in-memory}

Jalankan server MCP dan PostgreSQL secara bersamaan dengan komunikasi dalam memori di antara keduanya.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**Kasus penggunaan:** saat Anda memerlukan akses protokol MCP untuk agen AI sekaligus protokol komunikasi PostgreSQL untuk klien database tradisional, dengan performa maksimal melalui komunikasi dalam memori.

#### 3. Server MCP + PostgreSQL (reverse proxy) {#3-mcp--postgresql-server-reverse-proxy}

Jalankan kedua server dengan komunikasi berbasis TCP, yang mendukung deployment terdistribusi dan enkripsi TLS.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Dengan enkripsi TLS:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS mengenkripsi transport tetapi tidak mengautentikasi klien, sehingga contoh ini juga mewajibkan token bearer yang disimpan di `STACKQL_MCP_TOKEN`.

**Kasus penggunaan:** saat Anda perlu memisahkan beban kerja MCP dan PostgreSQL ke proses atau host yang berbeda, atau saat Anda memerlukan enkripsi TLS untuk endpoint MCP.

* * *

### Opsi konfigurasi {#configuration-options}

#### Tipe server MCP {#mcp-server-type}

| Tipe | Deskripsi |
|--|--|
|`http`|Mode server HTTP langsung - permintaan MCP ditangani di dalam proses oleh mesin StackQL.|
|`reverse_proxy`|Mode reverse proxy - permintaan MCP diteruskan ke server PostgreSQL yang mendasari melalui DSN yang dikonfigurasi.  Digunakan dengan `stackql srv`.|
|`stdio`|Transport input/output standar - server membaca permintaan dari `stdin` dan menulis respons ke `stdout`.  Digunakan oleh klien MCP yang tersemat di editor (Claude Desktop, Cursor, dll.).|

#### Format log MCP {#mcp-log-format}

Flag `--mcp.log.format` memilih pengodean log audit: `jsonl` (default) atau `otel` (catatan log OTLP/JSON OpenTelemetry).  Flag ini mengganti nilai `server.audit.format` di objek konfigurasi.  Lihat [Format log](#log-format).

#### Versi protokol MCP {#mcp-protocol-version}

Flag `--mcp.protocol.version` menetapkan revisi protokol terbaru yang dinyatakan oleh server: `auto` (default, semua revisi yang didukung), `2026-07-28` (hanya tanpa sesi), atau revisi yang lebih lama seperti `2025-11-25` (revisi tersebut dan sebelumnya).  Flag ini mengganti nilai `server.protocol_version` di objek konfigurasi.  Lihat [Mengunci revisi](#pinning-the-revision).

#### Objek konfigurasi MCP {#mcp-configuration-object}

Flag `--mcp.config` menerima objek JSON dengan struktur berikut.

##### Konfigurasi server {#server-configuration}

| Field | Deskripsi | Wajib |
|--|--|--|
|`server.transport`|Protokol transport - `http` atau `stdio`.|Ya|
|`server.address`|Alamat dan port tempat server MCP diikat (misalnya, `127.0.0.1:9912`).  Khusus transport HTTP.|Untuk `http`|
|`server.tls_cert_file`|Jalur ke file sertifikat TLS untuk HTTPS.|Tidak|
|`server.tls_key_file`|Jalur ke file kunci privat TLS untuk HTTPS.|Tidak|
|`server.auth_token_env_var`|Nama variabel lingkungan yang menyimpan token bearer yang dibagikan sebelumnya.  Jika ditetapkan, setiap permintaan HTTP harus menyertakan `Authorization: Bearer <token>`; server menolak untuk dimulai jika variabel tersebut tidak ditetapkan atau kosong.  Lihat [Autentikasi klien HTTP](#http-client-authentication).|Salah satu dari ini atau `allow_unauthenticated`, untuk `http`|
|`server.allow_unauthenticated`|Aktivasi eksplisit untuk melayani transport HTTP tanpa autentikasi klien.  Nilai default: `false`.|Salah satu dari ini atau `auth_token_env_var`, untuk `http`|
|`server.mode`|Kontrak keamanan yang mengendalikan operasi mutasi dan siklus hidup.  Salah satu dari `read_only`, `safe` (default), `delete_safe`, `full_access`.  Lihat [Mode server](#server-modes).|Tidak|
|`server.read_only`|Alias boolean **lama** untuk `mode: read_only`.  Jika kedua field ditetapkan, `mode` yang berlaku.|Tidak|
|`server.stateless`|Melayani Streamable HTTP tanpa sesi (tanpa `Mcp-Session-Id`), yang merupakan cara revisi protokol `2026-07-28` dilayani melalui HTTP.  Nilai default: `false`.  Diabaikan untuk `stdio`.  Lihat [Dukungan revisi protokol](#protocol-revision-support).|Tidak|
|`server.protocol_version`|Revisi protokol terbaru yang dinyatakan: `auto` (default), `2026-07-28`, atau revisi yang lebih lama seperti `2025-11-25`.  `2026-07-28` menyiratkan `stateless` untuk Streamable HTTP.  Lihat [Mengunci revisi](#pinning-the-revision).|Tidak|
|`server.audit`|Konfigurasi subsistem audit, termasuk `audit.format` (`jsonl` atau `otel`).  Lihat [Log audit](#audit-log).|Tidak|

##### Konfigurasi backend (khusus mode reverse proxy) {#backend-configuration-reverse-proxy-mode-only}

| Field | Deskripsi | Wajib |
|--|--|--|
|`backend.dsn`|String koneksi PostgreSQL untuk server StackQL backend.|Ya (untuk `reverse_proxy`)|

<br />

:::info

DSN backend sebaiknya menyertakan parameter `default_query_exec_mode=simple_protocol` untuk kompatibilitas optimal.

:::

##### Daftar izin {#allowlists}

| Field | Deskripsi |
|--|--|
|`enabled_tools`|Array nama alat yang diizinkan untuk dipublikasikan oleh server.  Jika dihilangkan atau kosong, semua alat bawaan didaftarkan.  Digunakan untuk mengekspos subset yang sempit (misalnya, server inventaris hanya baca).|
|`enabled_prompts`|Array nama prompt.  Semantiknya sama dengan `enabled_tools`, tetapi untuk prompt.|

<br/>

Contoh - server yang hanya memublikasikan `server_info` dan `list_providers`:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### Dukungan revisi protokol {#protocol-revision-support}

Revisi protokol `2026-07-28`, `server.stateless`, dan format log audit `otel` tersedia mulai StackQL `v0.11.660`.  `server.protocol_version` dan flag `--mcp.protocol.version` tersedia mulai StackQL `v0.12.732`.

Server mendukung semua revisi Model Context Protocol yang didukung oleh [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) dan bernegosiasi per klien, sehingga sekumpulan klien campuran dapat bekerja dengan satu server.

| Revisi | Siklus hidup | Permintaan persetujuan (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (dinyatakan)|Tanpa handshake: `server/discover`, lalu setiap permintaan membawa `io.modelcontextprotocol/protocolVersion` dan `io.modelcontextprotocol/clientCapabilities` di `_meta` (ditambah header `Mcp-Protocol-Version`, `Mcp-Method`, dan `Mcp-Name` pada HTTP).|Hasil `input_required`, yang dicoba ulang oleh klien dengan `inputResponses` (SEP-2322).|
|`2025-11-25`, `2025-06-18`|Handshake `initialize` / `notifications/initialized`.|Permintaan `elicitation/create` yang dimulai oleh server.|
|`2025-03-26`, `2024-11-05`|Sama seperti di atas.|Sama seperti di atas.|

<br />

**stdio** melayani semua revisi dalam satu proses: permintaan pertama dari klien `2026-07-28` dilayani tanpa handshake dan `initialize` dari klien yang lebih lama tetap berfungsi.  Tidak ada yang perlu dikonfigurasi untuk Claude Desktop, peluncur npm / PyPI, atau image Docker.

**Streamable HTTP** secara default menggunakan model dengan status, satu sesi per klien (`Mcp-Session-Id`), yang dilayani SDK untuk revisi hingga `2025-11-25`.  Permintaan `2026-07-28` dijawab dengan error JSON-RPC `-32022` (versi protokol tidak didukung; HTTP 400 biasa di rilis hingga `v0.12.718`) dengan `data.supported` yang mencantumkan revisi handshake, dan `server/discover` menyatakan hal yang sama, sehingga klien menegosiasikan revisi yang lebih rendah dan integrasi HTTP yang sudah ada mempertahankan sesi dan permintaan persetujuannya tanpa perubahan.  Tetapkan `server.stateless` (atau kunci revisi ke `2026-07-28`, [di bawah](#pinning-the-revision)) untuk melayani `2026-07-28` secara native melalui HTTP:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

Server tanpa sesi tidak menerbitkan `Mcp-Session-Id`, menjawab `GET` dan `DELETE` dengan 405 (tidak ada sesi yang perlu diakhiri), menjaga `tools/list`, `prompts/list`, dan `resources/list` tetap sama untuk setiap koneksi, dan menjalankan alur bolak-balik persetujuan melalui `input_required`.  Server ini tetap menerima `initialize` dari klien yang lebih lama dan melayani operasi baca untuknya, tetapi tidak dapat menyimpan kemampuan elisitasi yang dinyatakan klien tersebut saat inisialisasi (setiap permintaan mendapat sesi sementara), sehingga klien yang lebih lama tidak dapat menyetujui operasi tulis yang memerlukan persetujuan di server tanpa sesi.  Pilih `stateless` untuk host dengan revisi terkini dan default untuk sekumpulan klien yang masih mencakup klien yang lebih lama.

Server tidak menyimpan status lintas panggilan: mode, audit, dan kredensial provider merupakan konfigurasi tingkat proses, sehingga tidak ada yang perlu dipindahkan ke balik handle eksplisit.

#### Mengunci revisi {#pinning-the-revision}

Secara default, server menyatakan semua revisi yang didukung dan bernegosiasi per klien.  Flag `--mcp.protocol.version`, atau `server.protocol_version` di objek konfigurasi (flag yang berlaku), menetapkan revisi terbaru yang dinyatakan:

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| Nilai | Dinyatakan | Efek |
|--|--|--|
|`auto` (default) atau tidak ditetapkan|Semua revisi yang didukung.|Negosiasi seperti di atas: revisi tertinggi yang didukung kedua pihak.|
|`2026-07-28`|Hanya `2026-07-28`.|Hanya tanpa sesi, dan `stateless` tersirat untuk Streamable HTTP.  `initialize` dari klien yang lebih lama dijawab dengan `2025-11-25`, yang menjadi isyarat bagi klien tersebut untuk memutuskan koneksi alih-alih membaca jawaban itu sebagai siklus hidup baru.|
|`2025-11-25` (atau revisi yang lebih lama)|Revisi tersebut dan semua revisi sebelumnya.|Hanya siklus hidup handshake: permintaan `2026-07-28` mendapat error JSON-RPC `-32022` dengan `data.supported` yang mencantumkan revisi yang dinyatakan, sehingga klien dapat bernegosiasi ulang.  Klien handshake yang lebih lama tetap menggunakan revisinya sendiri.|

<br />

Nilai lain apa pun gagal dalam validasi konfigurasi saat dimulai, dan error yang dihasilkan menyebutkan nilai yang valid.

#### Batas transport {#transport-limits}

Isi permintaan Streamable HTTP dibatasi hingga 4 MiB (HTTP 413 jika melebihinya), JSON yang bersarang lebih dari 1000 tingkat ditolak sebelum diurai, dan satu frame `stdio` dibatasi hingga 8 MiB.  Tidak satu pun dari batas ini dapat dikonfigurasi; semuanya jauh di atas pernyataan SQL mana pun yang diterima server.

* * *

### Pemuatan (ulang) kredensial (`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` dan alat `reload_credentials` tersedia di rilis StackQL setelah `v0.10.542`.

:::

Blok lingkungan suatu proses bersifat tetap sejak proses tersebut dibuat.  Hal ini menimbulkan dua masalah bagi server MCP:

- Server MCP `stdio` yang dijalankan oleh klien desktop (Claude Desktop, Cursor, dll.) tanpa variabel lingkungan kredensial tidak akan pernah dapat melihatnya - klien meneruskan lingkungannya sendiri ke subproses.
- Kredensial berumur pendek yang dirotasi tidak pernah sampai ke sesi server yang berjalan lama.

Flag `--env.file` menjembatani kedua celah tersebut dengan menunjuk file bergaya dotenv yang berfungsi sebagai penyimpanan kredensial yang dapat diubah:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### Semantik pemuatan {#sourcing-semantics}

File ini dimuat ke lingkungan proses saat dimulai untuk setiap titik masuk (`exec`, `shell`, `srv`, `registry`, `mcp`):

- Jika flag kosong atau tidak ditetapkan, tidak ada yang terjadi - kredensial di-resolve secara tertunda dari lingkungan proses pada saat permintaan, persis seperti perilaku sebelumnya.
- File yang tidak ada di jalur yang diberikan ditoleransi dan tidak melakukan apa pun (file dapat dibuat kemudian).
- File yang formatnya salah atau tidak dapat dibaca menyebabkan kegagalan fatal saat dimulai.
- Hanya kunci dengan nilai yang tidak kosong yang ditetapkan; variabel lingkungan proses yang sudah ada **akan** ditimpa (file adalah sumber kebenaran); tidak ada yang pernah dihapus.
- Flag ini hanya tersedia di baris perintah - flag ini tidak dibaca dari `.stackqlrc`.

Format file adalah satu `KEY=VALUE` per baris.  Komentar `#`, baris kosong, prefiks `export ` opsional, tanda kutip tunggal atau ganda opsional di sekitar nilai, dan akhir baris CRLF semuanya ditoleransi:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

Alat [`reload_credentials`](/mcp/reload_credentials) memuat ulang file `--env.file` ke lingkungan proses di tengah sesi, lalu melaporkan status resolusi kredensial per provider.  Nilai rahasia tidak pernah dikembalikan, dicatat di log, atau diaudit - hanya nama dan status.  Tanpa `--env.file` yang dikonfigurasi, alat ini hanya berfungsi sebagai pemeriksa status.

Jika kueri gagal pada resolusi kredensial, error MCP membawa petunjuk yang mengarahkan agen untuk memanggil `reload_credentials` dan mencoba lagi, sehingga klien berbasis agen dapat pulih sendiri: tulis kredensial ke file, minta agen memuat ulang, dan kueri yang gagal akan berhasil saat dicoba lagi - tanpa perlu memulai ulang server.

`reload_credentials` tidak didukung dalam mode backend `reverse_proxy` - kueri dijalankan di proses server stackql jarak jauh, sehingga alat ini mengembalikan error yang mengarahkan pengguna untuk memuat ulang di server yang mendasari.

#### Windows dan Claude Desktop {#windows-and-claude-desktop}

`setx` menulis ke registri, bukan ke proses yang sedang berjalan, dan Claude Desktop meneruskan lingkungannya sendiri (yang sudah usang) ke subproses MCP - sehingga perubahan kredensial biasanya memerlukan Claude Desktop dimulai ulang sepenuhnya.  Dengan `--env.file`, file kredensial dapat dibuat atau dirotasi kapan saja dan `reload_credentials` membacanya tanpa perlu memulai ulang apa pun.  Lihat [Menggunakan StackQL dengan Claude Desktop](/getting-started/claude-desktop) untuk contoh konfigurasi lengkap.

* * *

### Autentikasi klien HTTP {#http-client-authentication}

:::note

`server.auth_token_env_var` dan `server.allow_unauthenticated` tersedia mulai StackQL `v0.12.742`.

:::

Transport HTTP memerlukan autentikasi klien.  `server.auth_token_env_var` menyebutkan nama variabel lingkungan yang menyimpan token bearer yang dibagikan sebelumnya; setiap permintaan harus menyertakan `Authorization: Bearer <token>` atau akan dijawab dengan `401`.  Token dibaca dari lingkungan, sehingga tidak pernah muncul di `--mcp.config` atau argumen proses.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[Autentikasi server wajib]

Melayani tanpa token memerlukan aktivasi eksplisit: tetapkan `"allow_unauthenticated": true` di `server`.  Tanpa salah satu dari kedua kunci tersebut, server keluar saat dimulai dengan pesan yang menyebutkan keduanya; server juga keluar jika variabel token tidak ditetapkan atau kosong.  Mulai rilis `v0.12.742`, Anda harus menambahkan `auth_token_env_var` atau `"allow_unauthenticated": true` ke `server` saat menggunakan transport `http`.

:::

TLS (`tls_cert_file` / `tls_key_file`) mengenkripsi transport tetapi tidak mengautentikasi klien; gabungkan dengan token untuk listener apa pun di luar loopback.  Permintaan browser lintas origin (`Origin` yang tidak cocok, atau `Sec-Fetch-Site` lintas situs) ditolak dengan `403` terlepas dari autentikasi.  `stackql_mcp_client` yang disertakan tidak mengirim header `Authorization`, sehingga server yang dihubunginya harus mengaktifkan secara eksplisit layanan tanpa autentikasi.

Mulai `v1.0`, menjalankan server dengan [`--access.config`](/command-line-usage/access-management) mengganti gerbang token bersama dengan autentikasi per pemanggil: setiap permintaan HTTPS diverifikasi dengan skema JWT, bearer statis, atau mutual TLS yang dikonfigurasi dan diotorisasi berdasarkan aturan kebijakan.  Dalam hal ini, TLS server wajib, autentikasi `local_os` ditolak melalui HTTP, dan permintaan tanpa kredensial tidak pernah beralih ke token lingkungan server.  Manajemen akses tidak tersedia dengan tipe server `reverse_proxy`.

* * *

### Mode server {#server-modes}

`server.mode` memilih salah satu dari empat kontrak keamanan.  Keempatnya mengizinkan `SELECT` dan pembacaan metadata; perbedaannya terletak pada cara menangani mutasi dan operasi siklus hidup.

| Mode | `SELECT` / metadata | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (siklus hidup) |
|--|--|--|--|--|
|`read_only`|izinkan|tolak|tolak|tolak|
|`safe` (default)|izinkan|perlu persetujuan|perlu persetujuan|perlu persetujuan|
|`delete_safe`|izinkan|izinkan|perlu persetujuan|perlu persetujuan|
|`full_access`|izinkan|izinkan|izinkan|izinkan|

<br />

**Tolak** langsung mengembalikan error ke klien.

**Perlu persetujuan** menggunakan alur elisitasi MCP, yang bentuknya ditentukan oleh [revisi protokol](#protocol-revision-support) hasil negosiasi:

- Jika klien menyatakan kemampuan elisitasi (saat inisialisasi, atau dalam kemampuan klien `_meta` per permintaan pada `2026-07-28`), server meminta pengguna untuk menyetujui tindakan dengan pesan singkat (nama alat, kelas kueri, SQL).  Pada `2026-07-28`, permintaan persetujuan dikembalikan sebagai hasil `input_required` yang membawa permintaan input `elicitation/create` dengan ID `stackql_approval`, dan klien mencoba ulang panggilan dengan jawaban di `inputResponses`; pada revisi sebelumnya, server sendiri yang mengirim permintaan `elicitation/create` di tengah panggilan.  Pengguna menerima, menolak, atau membatalkan.
- Jika klien **tidak** menyatakan elisitasi, alat ditolak dengan pesan yang menjelaskan kekurangan tersebut dan mengarahkan operator ke mode `full_access`.

Persetujuan dijawab oleh klien, sehingga persetujuan melindungi dari agen yang bertindak tanpa sepengetahuan penggunanya, bukan dari pemanggil yang tidak tepercaya.  Gunakan transport HTTP bersama [autentikasi klien](#http-client-authentication) jika pemanggil tidak tepercaya.

Mode berlaku global per server.  Tidak ada penggantian per alat.

Mulai `v1.0`, [`run_select_query`](/mcp/run_select_query) menolak `INSERT`, `UPDATE`, `REPLACE`, `DELETE`, dan `EXEC` di semua mode, termasuk `full_access`; error yang dihasilkan menyebutkan `run_mutation_query` atau `run_lifecycle_operation`.  Setiap alat juga membawa `title` dan empat petunjuk perilaku MCP sebagai boolean eksplisit (`readOnlyHint`, `destructiveHint`, `idempotentHint`, `openWorldHint`), yang diturunkan dari klasifikasi yang sama dengan yang diterapkan oleh gerbang kebijakan.  `openWorldHint` bernilai `true` untuk `list_registry`, `pull_provider`, `run_select_query`, `run_mutation_query`, dan `run_lifecycle_operation`, yang menjangkau endpoint registri atau provider, dan `false` untuk alat yang hanya membaca status lokal.  Petunjuk ini bersifat saran untuk UX klien; penegakannya tetap dilakukan oleh gerbang kebijakan.

:::info[Pengendalian payload]

Mulai rilis `v0.12.742`, payload yang berisi beberapa pernyataan dikendalikan berdasarkan pernyataan dengan hak istimewa tertinggi di dalamnya di semua mode, sehingga `SELECT 1; DELETE ...` diperlakukan sebagai `DELETE` dan catatan audit membawa `query_class` `mutation_delete`.  `validate_select_query` hanya menerima tepat satu pernyataan.

:::

#### Perubahan perilaku mode default {#default-mode-behaviour-change}

Rilis sebelumnya menerapkan keamanan hanya melalui satu flag `read_only: true/false` dengan nilai default "tanpa penegakan".  Default saat ini (`mode: safe`) membuat mutasi memerlukan persetujuan pengguna sejak awal.  Operator yang menjalankan klien yang mendukung elisitasi (Claude Desktop, Cursor, Continue, dll.) akan melihat satu permintaan persetujuan per mutasi.  Operator yang menjalankan otomatisasi, atau `stackql_mcp_client` yang disertakan (yang tidak menyatakan elisitasi), harus secara eksplisit mengaktifkan `mode: full_access` untuk menjalankan mutasi dan operasi siklus hidup.

Kunci JSON / YAML lama `read_only: true` masih diterima demi kompatibilitas mundur dan diperlakukan setara dengan `mode: read_only`.

#### Contoh {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### Log audit {#audit-log}

Setiap panggilan alat menulis satu catatan ke tujuan audit yang dikonfigurasi, dalam format JSONL secara default atau sebagai catatan log OpenTelemetry (lihat [Format log](#log-format)).  Audit **aktif secara default**.  Audit menjawab "apa yang dilakukan agen", bukan "apa yang dilihat agen" - baris hasil dari pernyataan `SELECT` sengaja tidak dicatat, dalam format mana pun.

#### Apa yang dicatat {#what-gets-recorded}

| Field | Deskripsi |
|--|--|
|`timestamp`|Waktu jam dinding saat panggilan dimulai (RFC3339).|
|`tool`|Nama alat (misalnya, `run_select_query`).|
|`mode`|Mode server yang berlaku saat panggilan.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`.  Untuk payload yang berisi beberapa pernyataan, kelas dari pernyataan dengan hak istimewa tertinggi.|
|`sql`|String SQL untuk alat kueri (`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`).|
|`args`|Field hierarki untuk alat metadata (`list_*`, `describe_method`); SQL + `row_limit` untuk alat kueri.|
|`duration_ms`|Durasi waktu jam dinding dari gerbang + handler.|
|`error`|Pesan error jika alat mengalami error atau ditolak.|

<br />

Baris hasil dari pernyataan `SELECT` sengaja dikecualikan - ukurannya dapat sangat besar dan mungkin berisi data sensitif.

#### Tujuan file {#file-sink}

Satu-satunya tujuan yang tersedia saat ini adalah `file`.  Satu objek JSON per baris, di-fsync setelah setiap catatan, dengan rotasi gaya lumberjack berdasarkan ukuran / usia / jumlah cadangan.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

Ada dua cara untuk menentukan lokasi:

- `audit.file.path` - jalur file lengkap (absolut, atau relatif terhadap cwd).
- `audit.file.dir` - sebuah direktori; tujuan memilih nama dasar file `stackql_mcp_server_<RFC3339-utc-second>.log` di dalamnya.

Jika keduanya tidak ditetapkan di `mcp.config`, server MCP menggunakan cwd sebagai nilai default `dir` sehingga operator yang sudah ada melihat perilaku yang sama seperti sebelumnya.  Paket tujuan generik yang mendasarinya sendiri menolak memilih direktori secara diam-diam - keputusan "ke mana log disimpan" selalu dibuat oleh pemanggil.

Jalur absolut hasil resolusi dicatat ke stderr saat dimulai sebagai `sink file: /path/to/file.log`.

#### Format log {#log-format}

Aliran audit memiliki dua pengodean, yang ditulis ke tujuan file yang sama:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

Flag ini mengganti nilai `server.audit.format` di `mcp.config` (`"audit": {"format": "otel"}`).  `jsonl` kompatibel per byte dengan catatan yang dijelaskan di atas.  `otel` menulis satu objek OTLP/JSON `LogsData` per baris, bentuk yang diterima apa adanya oleh receiver `otlp_json_file` milik OpenTelemetry Collector (diverifikasi dengan otelcol-contrib 0.160.0), sehingga aliran ini dapat mencapai pipeline OTLP mana pun tanpa processor transformasi:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Setiap panggilan alat menghasilkan satu catatan log; panggilan yang melewati gerbang persetujuan menghasilkan catatan kedua untuk keputusan elisitasi.  Catatan dari satu panggilan berbagi `gen_ai.tool.call.id` dan ID trace yang sama.  `traceparent` W3C yang diberikan oleh pemanggil di `params._meta` akan dihormati; jika tidak ada, satu ID trace dibuat per sesi MCP sehingga catatan dari satu sesi agen saling berkorelasi.

Kumpulan atribut yang dihasilkan merupakan antarmuka berversi: cakupan instrumentasinya adalah `github.com/stackql/stackql/pkg/mcp_server/audit` pada versi `1.0.0`, dan `schemaUrl` mengunci konvensi stabil ke `https://opentelemetry.io/schemas/1.44.0`.  Atribut `gen_ai.*` dan `mcp.*` mengikuti konvensi semantik [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) dan [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md), yang masih berstatus Development; versi cakupan dinaikkan setiap kali kumpulan atribut berubah.

| Atribut | Catatan | Nilai |
|--|--|--|
|`service.name`, `service.version`|sumber daya|`stackql`, versi StackQL|
|`gen_ai.operation.name`|panggilan alat|`execute_tool`|
|`mcp.method.name`|keduanya|`tools/call` pada catatan panggilan alat, `elicitation/create` pada catatan keputusan|
|`gen_ai.tool.name`|keduanya|Nama alat|
|`gen_ai.tool.call.id`|keduanya|Dibuat per panggilan, digunakan bersama oleh kedua catatan|
|`mcp.protocol.version`|keduanya|Revisi hasil negosiasi|
|`mcp.session.id`|keduanya|ID sesi (tidak ada untuk permintaan HTTP tanpa sesi)|
|`stackql.mode`|keduanya|Mode server|
|`stackql.decision`|keduanya|Nilai `decision` dari tabel di atas|
|`stackql.query`|panggilan alat|SQL apa adanya, untuk alat kueri|
|`stackql.query_class`|panggilan alat|Nilai `query_class`|
|`stackql.provider`|panggilan alat|Provider, untuk alat metadata|
|`stackql.query.source`|panggilan alat|ID entri Query Library, jika diberikan|
|`stackql.duration_ms`|panggilan alat|Durasi waktu jam dinding|
|`stackql.rows_returned`|panggilan alat|Jumlah baris, untuk alat yang mengembalikan baris|
|`error.type`, `error.message`|panggilan alat|Saat gagal: keputusan penolakan, atau `tool_error` setelah melewati gerbang|

<br />

Body berisi `execute_tool <tool>` / `elicitation <tool>`; tingkat keparahannya `INFO`, atau `ERROR` jika panggilan gagal atau ditolak.  Kontrak redaksi identik di kedua format: nilai hasil tidak pernah diserialisasi, hanya pernyataan yang menghasilkannya.

#### Mode kegagalan {#failure-modes}

Jika tujuan mengembalikan error, perilaku respons bergantung pada `audit.failure_mode`.

| failure_mode | Efek |
|--|--|
|`strict` (default)|Panggilan alat mengembalikan error audit ke klien meskipun alat yang mendasarinya berhasil.  Disengaja: lebih baik respons klien yang ambigu daripada `DELETE` yang tidak terdeteksi.|
|`strict_mutations`|Pembacaan `SELECT` / metadata tetap berjalan dengan catatan di stderr; mutasi dan operasi siklus hidup memunculkan error audit.|
|`best_effort`|Selalu mencatat ke stderr dan melanjutkan.|

#### Urutan {#sequencing}

Penulisan audit terjadi **setelah** alat dijalankan (atau ditolak oleh pengendalian) tetapi **sebelum** respons dikembalikan ke klien.  Dalam mode strict, kegagalan penulisan audit pada DELETE yang berhasil berarti baris sudah terhapus tetapi klien menerima error - ini memang dirancang demikian, agar tidak ada mutasi yang lolos tanpa diaudit.

#### Menonaktifkan audit {#disabling-audit}

Untuk menonaktifkan audit sepenuhnya, seperti pada rilis sebelum `v0.10.474` yang belum memiliki audit:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### Alat MCP yang tersedia {#available-mcp-tools}

Saat berjalan sebagai server MCP, StackQL menyediakan alat berikut, sesuai dengan daftar izin `enabled_tools` yang dikonfigurasi. Setiap alat mengembalikan tampilan teks yang telah dirender (untuk LLM) sekaligus payload terstruktur bertipe (untuk klien terprogram). Rendering teks default berupa tabel markdown untuk hasil multibaris yang seragam atau blok KV markdown untuk hasil yang jarang / satu catatan. Alat yang memiliki argumen menerima penggantian `format` opsional (`markdown` atau `json`); jika tidak, pengaturan render server yang digunakan. Input di bawah mencantumkan argumen khusus tugas; `?` menandai input opsional.

Klik nama alat mana pun untuk membuka halaman referensi lengkapnya, termasuk input, perilaku pengendalian, dan contoh prompt.

| Alat | Perender | Deskripsi | Input |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|Identitas dan runtime server: versi stackql, mesin SQL yang mendasari, lokasi registri provider, mode, flag hanya baca.  Panggil sesuai kebutuhan, misalnya untuk mendiagnosis operasi yang ditolak atau dikendalikan, atau untuk memeriksa perilaku yang bergantung pada versi.|tidak ada|
|[`list_providers`](/mcp/list_providers)|Tabel|Provider yang sudah diunduh ke cache lokal - puncak hierarki.|tidak ada|
|[`list_services`](/mcp/list_services)|Tabel|Layanan di bawah sebuah provider.|`provider`|
|[`list_resources`](/mcp/list_resources)|Tabel|Sumber daya di bawah `provider`.`service`.|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|Tabel|Metode akses (operasi HTTP) untuk sebuah sumber daya, beserta kata kerja SQL yang dipetakan ke masing-masing metode dan parameter wajibnya.  Panggil sebelum menulis kueri apa pun - dari sinilah parameter `WHERE` yang wajib disimpulkan.|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|Kontrak I/O lengkap untuk satu metode (selalu EXTENDED): input wajib dan opsional, serta field output yang dapat dirujuk oleh `SELECT`.|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|Tabel|Menemukan template kueri terkurasi berdasarkan maksud dalam bahasa alami. Hanya baca; entri mutasi memerlukan aktivasi eksplisit dan dikecualikan dalam mode `read_only`.|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|Mengambil template beserta deklarasi parameternya. Dengan `params` yang tidak kosong, memvalidasi nilai dan mengembalikan SQL yang telah dirender beserta `next_tool`, tanpa menjalankannya.|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|Mengurai dan merencanakan satu `SELECT` tanpa menjalankannya.  Mengembalikan `{valid, errors}`.  Hanya `SELECT`; tepat satu pernyataan.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|Tabel|Menjalankan `SELECT`.  Mengembalikan `{rows}`.  Hanya membaca.|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|Menjalankan `INSERT`/`UPDATE`/`REPLACE`/`DELETE` terhadap provider.  **Efek samping nyata.** Mengembalikan `{messages, timestamp}`.  Dikendalikan oleh [mode](#server-modes) server.|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|Menjalankan operasi siklus hidup `EXEC` stackql.  Mengembalikan `{messages, timestamp}`.  Dikendalikan oleh [mode](#server-modes) server.|`sql`|
|[`list_registry`](/mcp/list_registry)|Tabel|Provider (beserta versinya) yang tersedia di registri yang dikonfigurasi.  Berbeda dari `list_providers`, yang hanya mencantumkan provider yang sudah diunduh.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|Menginstal satu provider dari registri ke cache lokal.  Hanya memengaruhi status cache lokal - tidak ada efek pada bidang kontrol atau bidang data cloud.|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|Tabel|Memuat ulang kredensial dari file dotenv `--env.file` ke lingkungan proses dan melaporkan status resolusi per provider (`ok`, `unresolved`, `not_checked`).  Tidak pernah mengembalikan nilai rahasia.  Diizinkan di semua mode.|`provider?`|

Gunakan `query_library_search` sebelum menulis SQL dari awal, lalu gunakan `query_library_get` untuk memeriksa dan merender template yang cocok. Alat kueri juga menerima `source` opsional yang berisi ID entri library hanya untuk atribusi; eksekusi tetap tunduk pada mode server dan kebijakan persetujuan yang biasa.

:::note

`describe_resource` dihentikan di StackQL `v0.12.732`; klien MCP, prompt, atau instruksi agen yang memanggilnya sebaiknya memanggil [`list_methods`](/mcp/list_methods) lalu `describe_method` sebagai gantinya.  Sumber daya tidak memiliki satu daftar field tunggal, karena setiap metode akses mengembalikan bentuknya sendiri, sehingga nama kolom berasal dari [`describe_method`](/mcp/describe_method) untuk metode yang menjadi tujuan kueri.  Pernyataan SQL [`DESCRIBE`](/language-spec/describe) tidak berubah.

:::

### Prompt MCP yang tersedia {#available-mcp-prompts}

Satu prompt statis dipublikasikan.

| Prompt | Deskripsi |
|--|--|
|`write_safe_select`|Panduan untuk menulis kueri `SELECT` yang aman terhadap sumber daya stackql.  Menjelaskan cara menggunakan `SHOW METHODS IN <provider>.<service>.<resource>` untuk menemukan metode baca yang tepat dan parameter `WHERE` yang wajib.|

* * *

### Flag {#flags}

| Flag | Deskripsi |
|--|--|
|`--mcp.server.type`|Tipe server MCP: `http`, `stdio`, atau `reverse_proxy` (yang terakhir digunakan dengan `stackql srv`).|
|`--mcp.config`|Objek konfigurasi JSON untuk server MCP.  YAML juga diterima.|
|`--mcp.protocol.version`|Revisi protokol terbaru yang dinyatakan: `auto` (default), `2026-07-28`, atau revisi yang lebih lama seperti `2025-11-25`.  Lihat [Mengunci revisi](#pinning-the-revision).|
|`--env.file`|File kredensial bergaya dotenv yang dimuat saat dimulai dan dimuat ulang sesuai permintaan oleh alat `reload_credentials`.  Lihat [Pemuatan (ulang) kredensial](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|Port TCP untuk server protokol komunikasi PostgreSQL (digunakan dengan `stackql srv`).|
|`-h`, `--help`|Menampilkan informasi bantuan.|
|`-v`, `--verbose`|Menjalankan dalam mode verbose dengan output tambahan.|

&nbsp;
&nbsp;
> lihat [Flag global](/command-line-usage/global-flags) untuk opsi tambahan

:::info

Anda perlu menetapkan variabel lingkungan yang diperlukan untuk autentikasi provider sebelum memulai server MCP, atau menunjuk file kredensial bergaya dotenv dengan [`--env.file`](#credential-resourcing---envfile--reload_credentials).  Lihat [Menggunakan provider](/getting-started/using-a-provider) untuk informasi selengkapnya.

:::

* * *

### Contoh {#examples-1}

#### Server MCP mandiri dasar {#basic-standalone-mcp-server}

Jalankan server MCP mandiri dengan autentikasi provider dan token bearer untuk kliennya:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Server MCP dengan server PostgreSQL (dalam memori) {#mcp-server-with-postgresql-server-in-memory}

Jalankan server MCP dan PostgreSQL sekaligus untuk akses dua protokol:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Server inventaris hanya baca dengan audit ke jalur yang diketahui {#read-only-inventory-server-with-audit-to-a-known-path}

Berguna untuk agen pemindai inventaris yang tidak boleh melakukan mutasi:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Server otomatisasi dengan akses penuh {#full-access-automation-server}

Untuk pipeline otomatisasi tepercaya yang memerlukan mutasi dan tidak dapat merespons permintaan elisitasi.  Pipeline menyertakan token bearer yang disimpan di `STACKQL_MCP_TOKEN`:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Server MCP aman dengan TLS {#secure-mcp-server-with-tls}

Jalankan server MCP dengan enkripsi TLS dalam mode reverse proxy:

Pertama, buat sertifikat TLS (ditandatangani sendiri untuk pengembangan):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

Kemudian jalankan server:

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Menguji server MCP Anda {#testing-your-mcp-server}

Repositori ini menyertakan klien MCP pengembangan (`stackql_mcp_client`) untuk penulisan skrip dan uji regresi.  Bangun klien tersebut dengan:

```bash
python cicd/python/build.py --build-mcp-client
```

Perintah ini menghasilkan `./build/stackql_mcp_client`.

Perhatikan bahwa `stackql_mcp_client` tidak mengirim header `Authorization`, sehingga server yang ditujunya harus menetapkan `"allow_unauthenticated": true` (seperti pada contoh di atas); server yang dilindungi token menjawabnya dengan `401`.  Klien ini juga **tidak** menyatakan kemampuan elisitasi MCP - klien ini ditujukan untuk skrip noninteraktif.  Terhadap server `safe`, klien ini akan menerima penolakan "client does not support elicitation" pada panggilan mutasi dan siklus hidup; terhadap server `delete_safe`, pada panggilan `DELETE` dan `EXEC`.  Untuk pengujian interaktif dengan permintaan elisitasi, gunakan klien MCP yang tersemat di editor (Claude Desktop, Cursor, Continue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation - succeeds only against a full_access server, or against
# delete_safe (for INSERT/UPDATE) or safe with an elicitation-capable client.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### Contoh respons {#example-responses}

`server_info` (terhadap server dengan mode default, StackQL `v1.0.888` di Windows melalui stdio):

```json
{
  "version": "1.0.888",
  "commit": "f7b4116",
  "build_date": "2026-11-05T00:26:03Z",
  "platform": "Windows",
  "transport": "stdio",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.app/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query` (terhadap server `full_access`):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

Contoh baris log audit untuk mutasi yang sama:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### Integrasi dengan asisten AI {#integration-with-ai-assistants}

Untuk mengintegrasikan server MCP StackQL dengan asisten AI, daftarkan `stackql` sebagai server MCP di konfigurasi asisten.  Sebagian besar klien MCP yang tersemat di editor menjalankan server melalui `stdio`; untuk klien tersebut, gunakan `--mcp.server.type=stdio` dan asisten akan meluncurkan proses secara langsung.  Agen mandiri yang menggunakan HTTP dapat terhubung ke proses `stackql mcp --mcp.server.type=http` yang berjalan lama.

Untuk Claude Desktop - termasuk entri di Anthropic Connector Directory (metode instalasi yang disarankan), MCP Bundle (`.mcpb`) yang dapat diunduh, dan konfigurasi manual - lihat [Menggunakan StackQL dengan Claude Desktop](/getting-started/claude-desktop).

:::note

Lokasi file konfigurasi berbeda-beda menurut sistem operasi dan asisten AI.  Lihat dokumentasi asisten AI Anda untuk jalur dan format yang benar.

:::

* * *

### Pertimbangan arsitektur {#architecture-considerations}

Saat memilih mode deployment, pertimbangkan hal berikut:

1. **Mandiri (`stackql mcp`)**: penyiapan paling sederhana, ideal untuk pengembangan dan untuk klien yang tersemat di editor melalui `stdio`.
2. **Server ganda dalam memori (`stackql srv` dengan `mcp.server.type=http`)**: satu proses melayani protokol MCP sekaligus protokol komunikasi PostgreSQL.  Performa terbaik, cocok untuk sebagian besar deployment produksi.
3. **Reverse proxy (`stackql srv` dengan `mcp.server.type=reverse_proxy`)**:
   - memungkinkan pemisahan beban kerja di beberapa proses atau host
   - mendukung enkripsi TLS untuk endpoint MCP yang aman
   - memungkinkan penskalaan antarmuka MCP dan PostgreSQL secara independen
   - memberikan fleksibilitas untuk integrasi CA perusahaan (catatan: dukungan CA perusahaan masih eksperimental)

Secara terpisah, terkait keamanan:

- Mulai dengan `mode: safe` default.  Klien yang mendukung elisitasi (Claude Desktop, Cursor, Continue) akan meminta persetujuan pengguna sebelum mutasi apa pun.
- Kunci server pada `mode: read_only` untuk agen inventaris atau analitik yang tidak boleh melakukan operasi tulis.
- Kunci server pada `mode: delete_safe` jika Anda menginginkan kebebasan membuat/memperbarui tetapi ingin ada manusia yang terlibat untuk operasi destruktif.
- Gunakan `mode: full_access` hanya untuk pipeline otomatisasi tepercaya, dan hanya bersama log audit yang ditinjau oleh seseorang.
- Tetapkan `server.auth_token_env_var` pada listener HTTP mana pun yang dapat dijangkau oleh proses atau host lain; `allow_unauthenticated` ditujukan untuk pengembangan lokal.  TLS saja tidak mengautentikasi klien.

:::caution

Saat menggunakan TLS dengan otoritas sertifikat (CA) perusahaan, konfigurasi tambahan mungkin diperlukan.  Fungsionalitas ini masih eksperimental dan mungkin memerlukan penyesuaian berdasarkan implementasi CA spesifik Anda.

:::

* * *

### Bacaan lebih lanjut {#further-reading}

- Referensi untuk pengembang paket adalah [README `pkg/mcp_server`](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) di repositori stackql.
- Dokumen teknis untuk kontributor tersedia di [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) di repositori stackql dan berisi contoh respons lengkap untuk setiap alat.
