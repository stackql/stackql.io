---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: Alat MCP yang mencantumkan provider untuk layanan cloud dan SaaS yang tersedia bagi server MCP StackQL
image: "/img/stackql-featured-image.png"
---

Mengembalikan provider yang sudah diunduh ke cache StackQL lokal - tingkat teratas [hierarki sumber daya](/getting-started/resource-hierarchy) StackQL.  Gunakan alat ini sebagai titik awal saat agen perlu menemukan apa saja yang dapat dikueri.

Untuk provider yang tersedia di *registri* (termasuk yang belum diunduh), lihat [`list_registry`](/mcp/list_registry).

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Input {#inputs}

Tidak ada.

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| Tabel (tabel markdown untuk LLM) | Satu baris per provider |

<br />

Setiap baris memuat nama provider dan metadata versi sebagaimana dipublikasikan oleh registri.

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server (`read_only`, `safe`, `delete_safe`, `full_access`).  Hanya baca.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
Which cloud providers are available for you to query right now?
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
