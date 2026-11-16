---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Perintah stackql shell meluncurkan shell perintah StackQL interaktif untuk menjalankan kueri dan perintah provider terhadap akun cloud dan SaaS Anda."
image: "/img/stackql-featured-image.png"
---

Perintah yang digunakan untuk meluncurkan shell perintah StackQL interaktif.  Untuk informasi selengkapnya, lihat [Menggunakan shell interaktif StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Sintaksis {#syntax}

`stackql shell [flags]`

* * *

### Flag {#flags}

| Flag | Deskripsi |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Menampilkan informasi bantuan|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Menjalankan kueri dalam mode verbose dengan output tambahan yang dikirim ke stdout; jika flag global `-f` (`--outfile`) ditetapkan, informasi log tambahan ini akan ditulis ke file output bersama hasil kueri|
<br/>
<br/>
> lihat [Flag global](/command-line-usage/global-flags) untuk opsi tambahan

* * *

### Contoh {#examples}

Meluncurkan shell StackQL, menggunakan file kunci akun layanan untuk autentikasi.
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
