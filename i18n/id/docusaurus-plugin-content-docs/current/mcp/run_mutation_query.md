---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: Alat MCP yang menjalankan INSERT, UPDATE, REPLACE, atau DELETE terhadap provider melalui StackQL
image: "/img/stackql-featured-image.png"
---

Menjalankan pernyataan `INSERT`, `UPDATE`, `REPLACE`, atau `DELETE` terhadap provider.  **Efek samping nyata** - panggilan yang berhasil mengubah status cloud.

Dikendalikan oleh [mode](/command-line-usage/mcp#server-modes) server:

| Mode | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|ditolak|ditolak|
|`safe` (default)|perlu persetujuan (elisitasi)|perlu persetujuan|
|`delete_safe`|diizinkan|perlu persetujuan|
|`full_access`|diizinkan|diizinkan|

<br/>

:::info

Payload yang berisi beberapa pernyataan dikendalikan oleh pernyataan dengan hak istimewa tertinggi di dalamnya

:::

Jika mutasi memerlukan persetujuan dan klien tidak menyatakan kemampuan elisitasi MCP, panggilan ditolak.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT di spesifikasi bahasa ]](/language-spec/insert) [[ DELETE di spesifikasi bahasa ]](/language-spec/delete)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`sql`|Ya|Pernyataan `INSERT`, `UPDATE`, `REPLACE`, atau `DELETE`.|
|`source`|Tidak|ID entri Query Library jika SQL berasal dari [`query_library_get`](/mcp/query_library_get). Hanya untuk atribusi; tidak mengubah eksekusi atau persetujuan.|
|`format`|Tidak|Perenderan teks: `markdown` atau `json`. Jika dihilangkan, pengaturan render server yang digunakan.|

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| KV | `{messages: [...], timestamp}` - konfirmasi pengiriman, bukan isi respons API |

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

Klien MCP akan menampilkan permintaan persetujuan sebelum penghapusan dikirim, kecuali jika server berjalan dalam mode `full_access`.

Klip berikut menunjukkan alur elisitasi saat server MCP StackQL berjalan dalam mode `safe` (default) - klien diminta menyetujui pernyataan `DELETE` sebelum perubahan apa pun di sisi cloud dikirim.  Anda dapat mereproduksinya terhadap server MCP StackQL Anda sendiri dengan [MCP Inspector](https://github.com/modelcontextprotocol/inspector) resmi menggunakan `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
