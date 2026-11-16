---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: Alat MCP yang mencantumkan layanan di bawah provider StackQL
image: "/img/stackql-featured-image.png"
---

Mengembalikan layanan yang diekspos oleh satu provider - tingkat kedua [hierarki sumber daya](/getting-started/resource-hierarchy) StackQL.  Panggil alat ini setelah [`list_providers`](/mcp/list_providers) untuk mempersempit area yang ditelusuri sebelum masuk lebih dalam ke sumber daya.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`provider`|Ya|Nama provider (misalnya, `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Pengidentifikasi tidak boleh berisi spasi kosong, titik koma, atau garis miring terbalik; input seperti itu ditolak sebelum pernyataan apa pun dibangun.

:::

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| Tabel | Satu baris per layanan di bawah provider yang diberikan |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server.  Hanya baca.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
List the services available in Google Cloud
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
