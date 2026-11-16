---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: Alat MCP yang mencantumkan metode akses untuk sumber daya StackQL
image: "/img/stackql-featured-image.png"
---

Mengembalikan metode akses (operasi HTTP) yang terikat pada suatu sumber daya - metode baca, mutasi, dan operasi siklus hidup yang tersedia.  Ini adalah alat yang harus dipanggil agen **sebelum menulis kueri apa pun**, karena parameter `WHERE` yang wajib disimpulkan dari metode baca yang dipilih.  Pilih metode yang verba SQL-nya sesuai dengan maksud dan yang parameter wajibnya dapat diberikan, lalu panggil [`describe_method`](/mcp/describe_method) pada metode tersebut untuk mendapatkan kontrak lengkapnya, termasuk field output.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`provider`|Ya|Nama provider.|
|`service`|Ya|Layanan di bawah provider tersebut.|
|`resource`|Ya|Sumber daya di bawah `provider.service`.|

<br/>

:::info

Pengidentifikasi tidak boleh berisi spasi kosong, titik koma, atau garis miring terbalik; input seperti itu ditolak sebelum pernyataan apa pun dibangun.

:::

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| Tabel | Satu baris per metode akses, termasuk verba SQL yang dipetakan ke metode tersebut dan parameter yang wajib |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server.  Hanya baca.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
