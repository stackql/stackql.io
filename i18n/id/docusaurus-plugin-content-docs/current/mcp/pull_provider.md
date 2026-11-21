---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: Alat MCP yang menginstal provider StackQL dari registri ke cache lokal
image: "/img/stackql-featured-image.png"
---

Menginstal satu provider dari registri StackQL ke cache approot lokal.  Jika `version` tidak diberikan, versi terbaru yang dipublikasikan akan diunduh.

Hanya menulis status cache lokal - tidak ada efek pada bidang kontrol atau bidang data cloud, sehingga alat ini diizinkan di semua mode server (termasuk `read_only`).  Gunakan [`list_registry`](/mcp/list_registry) untuk menemukan nama provider dan versi yang tersedia sebelum mengunduh.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Perintah registry ]](/command-line-usage/registry)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`provider`|Ya|Nama provider sebagaimana dipublikasikan di registri (misalnya, `aws`, `google`, `cloudflare`).|
|`version`|Tidak|Versi yang dipublikasikan untuk diunduh.  Jika tidak diberikan, versi terbaru yang dipublikasikan akan diinstal.|

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| KV | Konfirmasi bahwa provider (dan versinya) telah diinstal ke cache lokal |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server.  Tidak ada efek di sisi cloud; hanya status cache lokal yang berubah.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang telah mendaftarkan server MCP StackQL:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
