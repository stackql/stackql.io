---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: Alat MCP yang mengambil template kueri StackQL dan memvalidasi parameter yang diberikan untuk mengembalikan SQL yang dirender tanpa menjalankannya
image: "/img/stackql-featured-image.png"
---

Mengambil entri [Query Library](pathname:///docs/query-library/) berdasarkan `id` yang dikembalikan oleh [`query_library_search`](/mcp/query_library_search).

Jika `params` dihilangkan atau kosong, alat ini mengembalikan template mentah, deklarasi parameter, dan catatan. Dengan objek `params` yang tidak kosong, server memvalidasi nilai tersebut dan menyubstitusikannya ke dalam template. Alat ini mengembalikan SQL hanya jika perenderan berhasil. Model tidak perlu menyubstitusikan nilai sendiri.

Alat ini tidak menjalankan SQL dan tidak memeriksa kredensial cloud. Validasi parameter yang dilakukannya berbeda dari [`validate_select_query`](/mcp/validate_select_query), yang mengurai dan merencanakan pernyataan `SELECT` terhadap provider yang terinstal.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`id`|Ya|ID kueri dari hasil pencarian, misalnya `aws/s3/buckets-list`.|
|`params`|Tidak|Objek yang memetakan nama parameter yang dideklarasikan ke nilai. Hilangkan objek ini atau berikan `{}` untuk memeriksa template tanpa merendernya.|
|`format`|Tidak|Perenderan teks: `markdown` atau `json`. Jika dihilangkan, pengaturan render server yang digunakan (`markdown` secara default).|

Parameter yang tidak dikenal ditolak. Parameter wajib yang tidak diberi nilai dan tidak memiliki nilai default dilaporkan di `missing_params`. Nilai yang diberikan diperiksa terhadap tipe dan batasan yang dideklarasikan; placeholder yang tidak terselesaikan dianggap error.

* * *

## Output {#output}

Tampilan teks default berupa blok KV yang berisi identitas, status mutasi, alat berikutnya, validitas, tautan sumber, serta template, SQL yang dirender, atau error jika ada. Gunakan payload terstruktur atau `format: "json"` untuk metadata lengkap:

| Field | Deskripsi |
|--|--|
|`id`, `title`, `description`|Identitas dan ringkasan entri.|
|`mutation`, `verb`|Apakah entri mengubah sumber daya dan, jika dideklarasikan, tipe operasi entri tersebut (`select`, `mutation`, atau `lifecycle`).|
|`next_tool`|Alat eksekusi untuk entri: `run_select_query`, `run_mutation_query`, atau `run_lifecycle_operation`. Pengambilan entri tidak memanggil alat tersebut.|
|`params`|Deklarasi parameter beserta nama, tipe, flag wajib, serta nilai default, deskripsi, contoh, enum, atau pola jika ada.|
|`template`, `notes`|Template mentah dan catatan penulis, dikembalikan jika `params` dihilangkan atau kosong.|
|`rendered`, `valid`|Apakah perenderan dicoba dan berhasil. Keduanya bernilai `false` saat memeriksa template mentah.|
|`sql`|SQL yang dirender, hanya ada setelah perenderan berhasil.|
|`missing_params`, `unknown_params`, `errors`|Detail validasi jika perenderan gagal. Jangan jalankan SQL sebelum masalah ini diselesaikan.|
|`auth`, `permissions`|Jika dideklarasikan, variabel lingkungan kredensial dan izin asli provider yang diperlukan untuk eksekusi. Pengambilan entri itu sendiri tidak memerlukan keduanya.|
|`cost`, `outputs`, `related`|Jika dideklarasikan, petunjuk fan-out dan biaya, deskripsi kolom hasil, dan ID entri terkait.|
|`doc_url`|Halaman library yang dapat dibaca manusia.|
|`source_tier`, `source_url`, `stale`|Asal konten: `primary`, `fallback`, atau `snapshot`, URL atau jalur tersematnya, dan apakah konten disajikan dari cache atau snapshot setelah pengambilan gagal.|

ID yang tidak dikenal atau formatnya salah mengembalikan error. Gunakan `query_library_search` untuk menemukan ID yang valid.

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server, dengan tetap tunduk pada daftar izin alat yang dikonfigurasi. Mengambil atau merender template mutasi atau siklus hidup tidak berdampak apa pun di sisi cloud dan tidak memerlukan persetujuan. Menjalankan SQL yang dikembalikan merupakan panggilan terpisah yang diatur oleh [mode](/command-line-usage/mcp#server-modes) server.

* * *

## Contoh {#example}

Periksa entri terlebih dahulu:

```json
{
  "id": "aws/s3/buckets-list"
}
```

Lalu berikan region perutean yang diwajibkan oleh entri tersebut:

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

Setelah memeriksa `rendered` dan `valid`, gunakan [`validate_select_query`](/mcp/validate_select_query) untuk memvalidasi pernyataan `SELECT` yang dikembalikan. Jalankan pernyataan tersebut dengan alat yang disebutkan di `next_tool`, dengan meneruskan `sql` yang dikembalikan dan menetapkan `source` ke `id` entri library untuk atribusi. Kredensial dan izin provider diperlukan saat eksekusi.
