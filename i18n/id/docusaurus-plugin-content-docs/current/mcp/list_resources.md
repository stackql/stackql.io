---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: Alat MCP yang mencantumkan sumber daya di bawah provider.service StackQL
image: "/img/stackql-featured-image.png"
---

Mengembalikan sumber daya di bawah suatu `provider.service` - tingkat ketiga [hierarki sumber daya](/getting-started/resource-hierarchy) StackQL.  Sumber daya adalah target yang muncul di klausa `FROM` pada kueri StackQL.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`provider`|Ya|Nama provider.|
|`service`|Ya|Layanan di bawah provider tersebut.|

<br/>

:::info

Pengidentifikasi tidak boleh berisi spasi kosong, titik koma, atau garis miring terbalik; input seperti itu ditolak sebelum pernyataan apa pun dibangun.

:::

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| Tabel | Satu baris per sumber daya di bawah `provider.service` |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server.  Hanya baca.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
List the resources in the google.storage service
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
