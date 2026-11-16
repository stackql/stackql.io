---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Perintah stackql srv menjalankan StackQL sebagai server protokol komunikasi PostgreSQL agar psql, alat BI, dan driver bahasa dapat mengkueri API cloud dan SaaS dengan SQL."
image: "/img/stackql-featured-image.png"
---

Perintah yang digunakan untuk menjalankan StackQL sebagai layanan, yang kemudian dapat diakses oleh klien [protokol komunikasi Postgres](https://www.postgresql.org/docs/current/protocol.html) yang diautentikasi menggunakan mTLS. Perintah `srv` juga dapat menjalankan server [Model Context Protocol (MCP)](/command-line-usage/mcp) bersama server protokol komunikasi PostgreSQL, sehingga memungkinkan akses melalui dua protokol bagi klien database tradisional maupun agen AI.

* * * 

### Sintaksis {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Meskipun server StackQL menggunakan protokol komunikasi Postgres, server ini bukan server Postgres dan juga tidak memerlukannya. Protokol komunikasi digunakan oleh klien untuk berkomunikasi dengan server, yang menggunakan StackQL untuk menjalankan kueri atau operasi DML terhadap penyedia cloud dan SaaS jarak jauh dan mengembalikan hasilnya ke klien.

:::

Parameter yang dikirim melalui protokol kueri yang diperluas (`Parse` / `Bind` / `Execute`, seperti yang digunakan oleh `psycopg` 3, `pgx`, dan JDBC) diikat di sisi server sebagai literal string. Mulai `v0.12.742`, placeholder `$n` di dalam literal string, pengidentifikasi yang diapit tanda kutip, atau komentar dibiarkan apa adanya, garis miring terbalik dalam nilai parameter di-escape, dan nilai teks `NULL` tetap menjadi string `NULL` (`NULL` SQL dibawa secara terpisah).

### Opsi server {#server-options}

| Opsi | Deskripsi |
|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Alamat tempat server mendengarkan (biasanya `0.0.0.0`)|
|<span class="nowrap">`--pgsrv.port`</span>|Port tempat server mendengarkan (misalnya `5444`)|
|<span class="nowrap">`--pgsrv.tls`</span>|Objek konfigurasi mTLS, lihat [contoh](#examples) di bawah|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Level log (default `WARN`)|

<br/>

> lihat [Flag global](/command-line-usage/global-flags) untuk opsi tambahan

### Opsi server MCP {#mcp-server-options}

Saat menjalankan perintah `srv` dengan dukungan MCP, opsi tambahan berikut tersedia:

| Opsi | Deskripsi |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|Tipe server MCP: `http` (dalam memori) atau `reverse_proxy` (berbasis TCP)|
|<span class="nowrap">`--mcp.config`</span>|Objek konfigurasi JSON untuk server MCP (lihat [dokumentasi MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Pengodean log audit MCP: `jsonl` (default) atau `otel` (catatan log OTLP/JSON); mengganti nilai `server.audit.format` di objek konfigurasi|
|<span class="nowrap">`--mcp.protocol.version`</span>|Revisi protokol MCP terbaru yang dinyatakan: `auto` (default), `2026-07-28` (hanya tanpa sesi), atau revisi yang lebih lama seperti `2025-11-25`; mengganti nilai `server.protocol_version` di objek konfigurasi|

<br/>

:::info[Perubahan yang tidak kompatibel di v0.12.742]

Mulai `v0.12.742`, transport HTTP MCP (`--mcp.server.type=http` atau `reverse_proxy`) menolak untuk berjalan kecuali objek konfigurasi menyebutkan token bearer dengan `server.auth_token_env_var` atau secara eksplisit memilih untuk tidak menggunakan autentikasi dengan `"allow_unauthenticated": true`. Lihat [Autentikasi klien HTTP](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Lihat [dokumentasi perintah MCP](/command-line-usage/mcp) untuk informasi terperinci tentang cara mengonfigurasi dan menggunakan server MCP, termasuk mode deployment, opsi konfigurasi, dan integrasi dengan asisten AI.

:::

:::info

Anda perlu mengatur variabel lingkungan yang diperlukan untuk autentikasi provider sebelum memulai server, lihat [Menggunakan provider](/getting-started/using-a-provider) untuk informasi selengkapnya.

:::

Untuk server yang berjalan lama, flag [`--env.file`](/command-line-usage/global-flags) memungkinkan Anda menempatkan atau merotasi kredensial dalam file bergaya dotenv di disk sebelum server dijalankan, alih-alih mengekspor variabel ke shell:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Untuk perintah `stackql srv`, file dibaca sekali saat server dijalankan; cara memuat ulang kredensial di tengah sesi adalah melalui alat MCP [`reload_credentials`](/mcp/reload_credentials).

### Flag {#flags}

| Flag | Deskripsi |
|--|--|
|`-h,--help`|Menampilkan informasi bantuan|
|`-v,--verbose`|Menjalankan kueri dalam mode verbose dengan output tambahan yang dikirim ke stdout; jika flag global `-f` (`--outfile`) ditetapkan,<br/>informasi log tambahan ini akan ditulis ke file output bersama hasil kueri|

<br/>

> lihat [Flag global](/command-line-usage/global-flags) untuk opsi tambahan

* * *

### Contoh {#examples}

Jalankan proses server StackQL dan sambungkan menggunakan klien baris perintah `psql`.  

Pertama, unduh file konfigurasi `openssl.cnf` dari [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

Selanjutnya, buat file sertifikat dan kunci yang akan digunakan oleh server dan klien untuk autentikasi TLS timbal balik (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Sekarang atur variabel lingkungan; jika klien dan server berada di host yang sama, variabel ini dapat digunakan bersama.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Siapkan objek autentikasi untuk provider apa pun yang perlu diakses oleh server, misalnya untuk GitHub.  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Sekarang jalankan server di satu terminal.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

Di terminal lain, sambungkan ke server menggunakan klien baris perintah `psql`.  

```bash
psql 127.0.0.1
```

Sekarang jalankan kueri StackQL terhadap provider yang telah diautentikasi.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

127.0.0.1=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

127.0.0.1=>
```
