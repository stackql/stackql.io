---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: Alat MCP yang mengembalikan identitas dan runtime server MCP StackQL
image: "/img/stackql-featured-image.png"
---

Mengembalikan identitas dan runtime server MCP StackQL: versi, mesin SQL yang mendasari, lokasi registri provider, mode server, dan flag hanya baca lama.  Panggil alat ini sesuai kebutuhan, misalnya untuk mendiagnosis operasi yang ditolak atau dikendalikan, atau untuk memeriksa perilaku yang bergantung pada versi; alat ini tidak perlu dipanggil di awal sesi atau sebelum kueri.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Input {#inputs}

Tidak ada.

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| KV (blok kunci/nilai markdown untuk LLM) | Payload terstruktur dengan field di bawah ini |

<br/><br/>

| Field | Deskripsi |
|--|--|
|`version`|Versi build StackQL.|
|`commit`|Hash singkat commit Git.|
|`build_date`|Stempel waktu build dalam format RFC3339.|
|`platform`|`os/arch` (misalnya, `linux/amd64`).|
|`transport`|`http` atau `stdio`.|
|`sql_backend`|Mesin SQL yang mendasari (misalnya, `sqlite3`).|
|`provider_registry`|URL registri provider.|
|`mode`|Mode server yang aktif - `read_only`, `safe`, `delete_safe`, atau `full_access`.|
|`is_read_only`|Flag boolean lama (`true` jika dan hanya jika `mode == read_only`).|

* * *

## Pengendalian {#gating}

Selalu diizinkan di semua mode server.  Tidak diaudit sebagai mutasi.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
