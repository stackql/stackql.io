---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: Alat MCP yang memuat ulang kredensial dari file dotenv --env.file dan melaporkan status resolusi per provider
image: "/img/stackql-featured-image.png"
---

Memuat ulang file bergaya dotenv yang ditunjuk oleh flag [`--env.file`](/command-line-usage/global-flags) ke lingkungan proses server, lalu melaporkan status resolusi kredensial untuk setiap provider.  Ini mengatasi masalah lingkungan yang sudah tetap sejak proses dimulai: kredensial yang ditulis atau dirotasi di disk di tengah sesi sampai ke server yang sedang berjalan tanpa perlu memulai ulang server.

File env melapisi lingkungan proses alih-alih menggantikannya: resolusi kredensial selalu membaca lingkungan proses yang aktif pada saat permintaan, sehingga variabel yang sudah ada saat proses dimulai (dari shell atau blok `env` milik klien) tetap berfungsi dengan atau tanpa `--env.file`.  Saat pemuatan ulang, hanya kunci yang ada di file dengan nilai yang tidak kosong yang ditulis dan tidak ada variabel yang pernah dihapus, sehingga pemuatan ulang yang gagal atau parsial tetap mempertahankan kredensial yang sebelumnya berfungsi.

Nilai rahasia tidak pernah dikembalikan, dicatat dalam log, atau diaudit - alat ini hanya melaporkan nama variabel dan status.  Tanpa `--env.file` yang dikonfigurasi, alat ini hanya berfungsi sebagai pemeriksa status.  Saat kueri gagal pada resolusi kredensial, error MCP menyertakan petunjuk yang mengarahkan agen untuk memanggil `reload_credentials` dan mencoba lagi, sehingga klien agentik dapat memulihkan diri sendiri.

:::note

Tersedia di rilis StackQL setelah `v0.10.542`.

:::

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ Pemuatan (ulang) kredensial ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`provider`|Tidak|Membatasi cakupan laporan status ke satu provider.  Jika dihilangkan, semua provider dilaporkan.|

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| Tabel | Hasil pemuatan file env ditambah satu baris per provider dengan status resolusi kredensial |

<br/>

Payload terstruktur berisi:

| Field | Deskripsi |
|--|--|
|`env_file`|Jalur `--env.file` yang dikonfigurasi (kosong jika tidak ada yang dikonfigurasi).|
|`env_file_sourced`|`true` jika file ditemukan dan dimuat pada panggilan ini.|
|`sourced_vars`|Nama variabel lingkungan yang ditetapkan dari file - hanya nama, tidak pernah nilai.|
|`providers`|Satu baris per provider (lihat di bawah).|

<br/>

Setiap baris `providers` berisi:

| Kolom | Deskripsi |
|--|--|
|`provider`|Nama provider.|
|`auth_type`|Tipe autentikasi yang dikonfigurasi untuk provider.|
|`sourced_from`|Asal resolusi kredensial, misalnya `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline`, atau `none`.|
|`status`|`ok`, `unresolved`, atau `not_checked`.|
|`detail`|Teks error jika `unresolved`.|

<br/>

Tipe autentikasi yang melakukan autentikasi di luar jalur byte kredensial (gcloud interaktif, Azure CLI, kredensial klien oauth2, rantai pengambilan peran, penandatanganan OCI, null) melaporkan `not_checked`.

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server, termasuk `read_only` - alat ini tidak mengubah apa pun selain lingkungan proses server itu sendiri.

Tidak didukung dalam mode backend `reverse_proxy`: kueri dijalankan di proses server stackql jarak jauh, sehingga alat ini mengembalikan error yang mengarahkan pengguna untuk memuat ulang kredensial di server yang mendasari.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang telah mendaftarkan server MCP StackQL (dimulai dengan `--env.file`):

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
