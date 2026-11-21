---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: Alat MCP yang menjalankan SELECT StackQL terhadap provider
image: "/img/stackql-featured-image.png"
---

Menjalankan pernyataan `SELECT` terhadap provider yang dikonfigurasi dan mengembalikan baris hasil.  Hanya baca - mutasi dan operasi siklus hidup tidak diterima; untuk keduanya, gunakan [`run_mutation_query`](/mcp/run_mutation_query) atau [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).

Baris hasil dikembalikan ke klien, tetapi sengaja dikecualikan dari log audit.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT di spesifikasi bahasa ]](/language-spec/select)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`sql`|Ya|Pernyataan `SELECT`.  Pernyataan metadata (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) digolongkan sebagai hanya baca oleh gerbang kebijakan dan juga diterima; `DESC` dikenali mulai StackQL `v0.12.732`.|
|`row_limit`|Tidak|Batas jumlah baris yang dikembalikan ke klien.  `0` (default) berarti tidak ada batas di sisi klien.|
|`source`|Tidak|ID entri Query Library jika SQL berasal dari [`query_library_get`](/mcp/query_library_get). Hanya untuk atribusi; tidak mengubah eksekusi atau persetujuan.|
|`format`|Tidak|Perenderan teks: `markdown` atau `json`. Jika dihilangkan, pengaturan render server yang digunakan.|

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| Tabel | `{rows: [...]}` - satu objek per baris, dengan nama kolom sebagai kunci |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server (`read_only`, `safe`, `delete_safe`, `full_access`) jika setiap pernyataan dalam payload bersifat hanya baca.  Mulai `v1.0`, alat ini menolak `INSERT`, `UPDATE`, `REPLACE`, `DELETE`, dan `EXEC` di semua mode, termasuk `full_access`, dengan error yang menyebutkan [`run_mutation_query`](/mcp/run_mutation_query) atau [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).  Payload yang berisi beberapa pernyataan digolongkan menurut pernyataan dengan hak istimewa tertinggi di dalamnya, sehingga mutasi yang didahului pernyataan `SELECT` juga ditolak dengan cara yang sama.  Alat ini menyatakan `destructiveHint: false` dan `openWorldHint: true`.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang telah mendaftarkan server MCP StackQL:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
