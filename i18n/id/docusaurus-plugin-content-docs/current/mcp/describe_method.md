---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: Alat MCP yang mengembalikan kontrak I/O lengkap untuk satu metode akses StackQL
image: "/img/stackql-featured-image.png"
---

Mengembalikan kontrak I/O lengkap untuk satu metode akses - selalu EXTENDED, yang berarti input, skema isi permintaan, dan bentuk output lengkap.  Dari sinilah agen mendapatkan nama kolom: setiap baris membawa `param_type` bernilai `input_required` (predikat `WHERE` wajib dengan pencocokan persis), `input_optional`, atau `output` (field yang dapat direferensikan oleh `SELECT` atau `RETURNING`).  Sumber daya tidak memiliki satu daftar field tunggal, karena setiap metode mengembalikan bentuknya sendiri, jadi panggil alat ini untuk metode yang dipilih dari [`list_methods`](/mcp/list_methods) sebelum kueri pertama terhadap metode tersebut.

Lihat juga:
[[ Ringkasan MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Input {#inputs}

| Argumen | Wajib | Deskripsi |
|--|--|--|
|`provider`|Ya|Nama provider.|
|`service`|Ya|Layanan di bawah provider tersebut.|
|`resource`|Ya|Sumber daya di bawah `provider.service`.|
|`method`|Ya|Nama metode sebagaimana dikembalikan oleh [`list_methods`](/mcp/list_methods).|

<br/>

:::info

Pengidentifikasi tidak boleh berisi spasi kosong, titik koma, atau garis miring terbalik; input seperti itu ditolak sebelum pernyataan apa pun disusun.

:::

* * *

## Output {#output}

| Perender | Bentuk |
|--|--|
| KV | Satu baris per field input dan output: nama, tipe, `param_type` (`input_required`, `input_optional`, atau `output`), bentuk, dan deskripsi |

* * *

## Pengendalian {#gating}

Diizinkan di semua mode server.  Hanya baca.

* * *

## Contoh {#example}

Coba prompt ini di klien MCP mana pun yang mendukung elisitasi dan telah mendaftarkan server MCP StackQL:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Berikut contohnya di Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
