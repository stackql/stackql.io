---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: Alat MCP yang mencantumkan provider dan versi yang tersedia di registri provider StackQL
image: "/img/stackql-featured-image.png"
---

Mengembalikan provider (beserta versinya) yang tersedia di registri provider StackQL yang dikonfigurasi.  Berbeda dengan [`list_providers`](/mcp/list_providers), yang hanya melaporkan provider yang sudah diunduh ke cache lokal.

Berikan argumen opsional `provider` untuk mencantumkan versi yang dipublikasikan untuk satu provider.  Gunakan bersama [`pull_provider`](/mcp/pull_provider) saat agen perlu menginstal sesuatu yang belum ada secara lokal.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Perintah registry ]](/command-line-usage/registry)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`provider`|Tidak|Nama provider.  Jika diberikan, hasil dipersempit ke versi yang dipublikasikan untuk provider tersebut; jika tidak diberikan, semua entri registri dikembalikan.|

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| Tabel | Satu baris per entri registri (provider beserta metadata versi) |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server.  Tidak ada efek pada bidang kontrol atau bidang data cloud - hanya registri yang dibaca.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
