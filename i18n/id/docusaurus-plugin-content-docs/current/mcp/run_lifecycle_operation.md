---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: Alat MCP yang menjalankan operasi siklus hidup EXEC di StackQL
image: "/img/stackql-featured-image.png"
---

Menjalankan operasi siklus hidup `EXEC` StackQL - tindakan provider non-CRUD (memulai/menghentikan VM, merotasi kunci, memicu workflow, dan sebagainya).  **Efek samping nyata.**

Dikendalikan oleh [mode](/command-line-usage/mcp#server-modes) server:

| Mode | `EXEC` siklus hidup |
|--|--|
|`read_only`|ditolak|
|`safe` (default)|perlu persetujuan (elisitasi)|
|`delete_safe`|perlu persetujuan|
|`full_access`|diizinkan|

<br/>

:::info

Payload yang berisi beberapa pernyataan dikendalikan oleh pernyataan dengan hak istimewa tertinggi di dalamnya

:::

Jika persetujuan diperlukan dan klien tidak menyatakan kemampuan elisitasi, panggilan ditolak.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC di spesifikasi bahasa ]](/language-spec/exec)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`sql`|Ya|Pernyataan `EXEC` StackQL yang menargetkan metode siklus hidup.|
|`source`|Tidak|ID entri Query Library jika SQL berasal dari [`query_library_get`](/mcp/query_library_get). Hanya untuk atribusi; tidak mengubah eksekusi atau persetujuan.|
|`format`|Tidak|Perenderan teks: `markdown` atau `json`. Jika dihilangkan, pengaturan render server yang digunakan.|

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

Klien MCP akan menampilkan permintaan persetujuan sebelum operasi dikirim, kecuali jika server berjalan dalam mode `full_access`.

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
