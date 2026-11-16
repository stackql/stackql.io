---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: Alat MCP yang mencari template kueri StackQL yang dikurasi berdasarkan maksud dalam bahasa alami, provider, layanan, dan tag
image: "/img/stackql-featured-image.png"
---

Mencari template yang cocok dengan maksud dalam bahasa alami di [Query Library StackQL](pathname:///docs/query-library/). Gunakan alat ini sebelum menyusun SQL dari awal, lalu teruskan `id` dari salah satu hasil ke [`query_library_get`](/mcp/query_library_get) untuk memeriksa template dan memvalidasi parameternya.

Alat ini hanya mengambil konten library. Alat ini tidak menjalankan SQL, tidak memanggil API cloud, dan tidak memerlukan kredensial provider.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`intent`|Ya|Deskripsi tugas dalam bahasa alami.|
|`provider`|Tidak|Filter berdasarkan provider, misalnya `aws`.|
|`service`|Tidak|Filter berdasarkan layanan, misalnya `s3`.|
|`tags`|Tidak|Array tag. Entri harus cocok dengan setiap tag yang diberikan.|
|`include_mutations`|Tidak|Menyertakan template mutasi. Nilai default: `false`; diabaikan dalam mode `read_only`, yang selalu mengecualikan entri mutasi.|
|`limit`|Tidak|Jumlah maksimum hasil. Nilai default: `5` jika dihilangkan atau tidak positif; dibatasi hingga `20`.|
|`format`|Tidak|Perenderan teks: `markdown` atau `json`. Jika dihilangkan, pengaturan render server yang digunakan (`markdown` secara default).|

* * *

## Output {#output}

Tampilan teks default berupa tabel berperingkat yang berisi `id`, `title`, `mutation`, `required_params`, dan `score`, diikuti kutipan sumber dan panduan jika pencarian tidak menemukan kecocokan. Payload terstruktur berisi:

| Field | Deskripsi |
|--|--|
|`hits`|Entri berperingkat dengan `id`, `title`, `description` opsional, `providers`, `services`, dan `required_params`, ditambah `mutation` dan `score`. Lanjutkan dengan `query_library_get` untuk mendapatkan deklarasi parameter yang lengkap.|
|`miss`|`true` jika tidak ada hasil atau skor tertinggi berada di bawah ambang kecocokan. Kecocokan terdekat dengan skor rendah tetap dapat dikembalikan.|
|`guidance`|Jika pencarian tidak menemukan kecocokan, mengarahkan agen ke instruksi server, alat penemuan, dan `validate_select_query` untuk menyusun kueri.|
|`source_tier`|Sumber konten: `primary`, `fallback`, atau `snapshot`.|
|`source_url`|URL atau jalur tersemat dari katalog yang digunakan, untuk kutipan.|
|`stale`|Jika `true`, konten disajikan dari cache atau snapshot tersemat setelah pengambilan gagal.|

Entri yang sudah usang dikecualikan. Server menyimpan katalog dalam cache dan dapat beralih dari library yang di-hosting ke sumbernya di GitHub dan ke snapshot tersemat.

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server, dengan tetap tunduk pada daftar izin alat yang dikonfigurasi. Tidak memerlukan persetujuan maupun kredensial provider. Mencari template mutasi tidak mengotorisasi eksekusi template tersebut.

* * *

## Contoh {#example}

Argumen alat:

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

Gunakan `id` dari `hits`, misalnya `aws/s3/buckets-list`, dengan [`query_library_get`](/mcp/query_library_get). Jika `miss` bernilai `true`, periksa template terdekat sebagai panduan atau gunakan [`list_methods`](/mcp/list_methods) dan [`describe_method`](/mcp/describe_method) sebelum menyusun SQL.
