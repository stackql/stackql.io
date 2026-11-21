---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: Alat MCP yang mengurai dan merencanakan SELECT StackQL tanpa menjalankannya
image: "/img/stackql-featured-image.png"
---

Mengurai dan merencanakan satu pernyataan `SELECT` tanpa menjalankannya.  Mengembalikan `{valid, errors}`.  Berguna saat agen ingin memastikan kuerinya dapat dikompilasi dan diikat ke metode yang ditemukan sebelum melakukan panggilan API sungguhan.

:::info

Hanya `SELECT`, dan tepat satu pernyataan: payload yang berisi lebih dari satu pernyataan ditolak dengan `validate_select_query accepts exactly one statement`.

:::

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`sql`|Ya|Satu pernyataan `SELECT`.|

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| KV | `valid` (bool) dan `errors` (array string, kosong jika valid) |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server.  Tidak ada panggilan API yang dilakukan.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang telah mendaftarkan server MCP StackQL:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
