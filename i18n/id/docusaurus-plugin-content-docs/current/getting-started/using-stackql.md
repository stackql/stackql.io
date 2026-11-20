---
title: Menggunakan StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Menggunakan StackQL: menjalankan kueri secara interaktif di shell, noninteraktif dengan exec, atau sebagai server, sambil menemukan provider, layanan, dan sumber daya."
image: "/img/stackql-featured-image.png"
---
Lihat juga:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL memiliki beberapa mode penggunaan, yaitu: shell interaktif StackQL, baris perintah StackQL dengan pernyataan StackQL, atau menjalankan satu atau beberapa pernyataan StackQL dalam file input (file IQL). Setiap mode dijelaskan lebih rinci di bawah ini:

- Menggunakan shell interaktif StackQL ([`stackql shell`](/command-line-usage/shell))
- Menggunakan utilitas baris perintah StackQL ([`stackql exec`](/command-line-usage/exec))
- Menggunakan file input (file IQL) ([`stackql exec -i`](/command-line-usage/exec))

### Menggunakan shell interaktif StackQL {#using-the-stackql-interactive-shell}
Shell StackQL menyediakan lingkungan pemrograman interaktif bagi engineer untuk men-deploy dan berinteraksi dengan sumber daya cloud, serta memungkinkan analisis interaktif (menggunakan pernyataan [`SELECT`](/language-spec/select)) atas lingkungan cloud mereka. Secara fungsional, shell ini mirip dengan shell MySQL atau shell REPL (Read Evaluate Print Loop) lainnya. Shell menerima pernyataan StackQL yang valid dan mengembalikan hasil kepada pengguna. Pernyataan dapat mencakup beberapa baris dan dijalankan setelah titik koma penutup dimasukkan.

Shell StackQL dapat dipanggil menggunakan perintah `shell` seperti yang ditunjukkan di bawah ini (lihat [StackQL Provider Registry](/providers) untuk informasi tentang autentikasi provider):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Setelah berada di shell yang terautentikasi ke penyedia cloud, Anda dapat mulai menjalankan kueri StackQL (kueri `SELECT` atau operasi DDL, yaitu infrastruktur sebagai kode).

Secara default, shell StackQL mengembalikan hasil dalam bentuk tabel, tetapi format output `json`, `jsonl`, `csv`, dan `text` tersedia melalui flag `--output`, lihat [Mode output](/getting-started/output-modes).

### File inisialisasi `.stackqlrc` {#the-stackqlrc-initialization-file}

Nilai default untuk opsi perintah StackQL, seperti format output, dapat diatur menggunakan file inisialisasi `.stackqlrc`. Jika file ini ada di direktori saat ini yang digunakan oleh file executable StackQL, file tersebut akan dimuat secara otomatis, atau sebagai alternatif dapat dirujuk menggunakan flag `--configfile` seperti yang ditunjukkan di bawah ini:

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Informasi selengkapnya tentang opsi perintah tersedia di [Flag global](/command-line-usage/global-flags).

### Menggunakan utilitas baris perintah StackQL {#using-the-stackql-command-line-utility}
Perintah StackQL dapat dijalankan secara noninteraktif menggunakan perintah exec dari file executable StackQL. Lebih dari satu perintah dapat dijalankan dalam satu batch asalkan setiap perintah diakhiri dengan titik koma (`;`). Flag perintah, seperti flag `--output`, dapat ditentukan di baris perintah atau menggunakan file inisialisasi `.stackqlrc` yang dibahas sebelumnya.

Output dari file executable StackQL dikirim ke stdout pada sistem yang digunakan, dan output ini dapat diurai dalam skrip BASH atau PowerShell.

Berikut contoh skrip Windows PowerShell yang berisi perintah StackQL:

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$env:GOOGLE_CREDENTIALS = Get-Content -Raw "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from google.compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
Perintah PowerShell di atas menunjukkan cara memilih instance Compute Engine dan menampilkan output menggunakan `Out-GridView`.

### Menggunakan file input (file IQL) {#using-an-input-file-iql-file}
File input StackQL dapat digunakan untuk menjalankan beberapa perintah yang diakhiri dengan titik koma. Ini adalah opsi lain untuk pemrograman infrastruktur noninteraktif atau batch. File input ditentukan dengan opsi `-i` atau `--infile` seperti yang ditunjukkan di bawah ini:

```shell
# create an iql file
echo "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "/mnt/c/tmp/stackql-demo.json" } }' \
--output json | jq '.[] | .name'
```
