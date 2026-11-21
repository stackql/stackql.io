---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Perintah stackql exec menjalankan satu pernyataan StackQL atau skrip berisi beberapa pernyataan, dengan flag untuk format output, autentikasi, dan registri provider."
image: "/img/stackql-featured-image.png"
---

Perintah yang digunakan untuk menjalankan kueri StackQL, baik sebagai satu pernyataan maupun sebagai batch pernyataan dari skrip StackQL (file IQL).  

* * * 

### Sintaksis {#syntax}

`stackql exec [query] [flags]`

* * *

### Argumen {#arguments}

| Argumen | Deskripsi | Contoh |
|--|--|--|
|`query`|Kueri StackQL yang akan diproses<br/>atau perintah yang akan dijalankan | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_Perhatikan bahwa kueri harus diapit tanda kutip tunggal atau ganda_ |    


### Flag {#flags}

| Flag | Deskripsi |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Mode output, nilai yang valid meliputi: `table` (default), `json`, `jsonl` (JSON yang dipisahkan baris baru, `ndjson` diterima sebagai alias), `csv`, `text`, dan `otel` (catatan log OpenTelemetry, lihat [Mode output](/getting-started/output-modes#otel-output)), contoh: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Menjalankan kueri dari file yang ditentukan jika diperlukan; jika flag ini ditentukan, kueri apa pun yang diberikan sebagai argumen akan diabaikan, contoh: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Data sumber untuk perintah menggunakan file `json` atau `jsonnet`, contoh: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Flag uji coba; hanya praprosesor yang akan berjalan dan output akan dikembalikan |
|<span class="nowrap">`--var`</span>|Variabel eksternal (dapat berupa variabel lingkungan atau rahasia) yang digunakan oleh file jsonnet yang ditentukan dengan flag `--iqldata`, contoh `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|File tujuan penulisan hasil jika diperlukan (perilaku default adalah menampilkan hasil hanya di konsol), contoh `--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|Menampilkan informasi bantuan|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Menjalankan kueri dalam mode verbose dengan output tambahan yang dikirim ke stdout; jika opsi `-f` dipilih, informasi log tambahan ini akan ditulis ke file output bersama hasil kueri|
<br/>
<br/>
> lihat [Flag global](/command-line-usage/global-flags) untuk opsi tambahan


* * *

### Contoh {#examples}

Menampilkan hasil kueri di konsol dalam format tabel:
```shell
stackql exec "SHOW services IN google"
```

Menampilkan hasil kueri di konsol dalam format json:
```shell
stackql exec "SHOW services IN google" -o json
```

Menulis hasil kueri ke file dalam format json:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Menjalankan kueri dari file input (file IQL) dan menulis hasilnya ke file dalam format json:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Status keluar {#exit-status}

Perintah `exec` keluar dengan status `0` setelah input selesai diproses; error dari setiap pernyataan ditulis ke konsol, sehingga skrip harus memeriksa output.  Mulai `v1.0`, perintah ini keluar dengan status `1` ketika pernyataan konfigurasi [`SET`](/language-spec/set) atau [`UNSET`](/language-spec/unset) gagal, ketika eksekusi IaC pratinjau melaporkan sumber daya yang gagal, atau, jika [manajemen akses](/command-line-usage/access-management) diaktifkan, ketika pernyataan apa pun gagal atau ditolak.
