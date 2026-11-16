---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Perintah stackql registry mencantumkan, mengunduh, dan mengelola provider dari registri provider StackQL melalui baris perintah."
image: "/img/stackql-featured-image.png"
---

Perintah yang digunakan untuk berinteraksi dengan StackQL Provider Registry.  Lokasi registri, dan apakah tanda tangan dokumen provider diverifikasi, dikonfigurasi dengan flag global [`--registry`](/command-line-usage/global-flags#--registry).  Perintah `registry` tidak tersedia selama [manajemen akses](/command-line-usage/access-management) diaktifkan; sebagai gantinya, gunakan `REGISTRY PULL` dalam sesi `exec` atau `shell` yang terlindungi.

* * * 

### Sintaksis {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Argumen {#arguments}

| Argumen | Deskripsi | Contoh |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Operasi registri yang akan dilakukan (`list` atau `pull`) | `list` atau `pull` |    
|<span class="nowrap">`provider`</span>|Provider yang akan diinstal menggunakan subperintah `pull`| `okta` |
|<span class="nowrap">`version`</span>|Versi provider opsional yang akan diinstal menggunakan subperintah `pull` *(versi dokumen provider StackQL)* | `v23.03.00121` |


### Flag {#flags}

| Flag | Deskripsi |
|--|--|
|<span class="nowrap">`-h, --help`</span>|Menampilkan informasi bantuan|
<br/>
<br/>
> lihat [Flag global](/command-line-usage/global-flags) untuk opsi tambahan

* * *

### Contoh {#examples}

Mencantumkan provider yang tersedia di registri publik:
```shell
stackql registry list
```

Mengunduh versi terbaru sebuah provider dari registri publik:
```shell
stackql registry pull okta
```
