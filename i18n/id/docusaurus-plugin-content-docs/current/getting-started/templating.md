---
title: Membuat template infrastruktur
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Menghasilkan template INSERT untuk sumber daya apa pun dengan SHOW INSERT di StackQL, lengkap atau hanya parameter wajib, sebagai titik awal definisi infrastruktur."
image: "/img/stackql-featured-image.png"
---
Lihat juga:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Menggunakan variabel ]](/getting-started/variables)

Sumber daya cloud di-deploy dengan StackQL menggunakan pernyataan `INSERT`.  Template `INSERT` untuk berbagai sumber daya cloud dapat dihasilkan menggunakan perintah `SHOW INSERT`.

### Menghasilkan template `INSERT` menggunakan perintah `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Contoh pembuatan template ditunjukkan di sini:

```sql
SHOW INSERT INTO google.compute.addresses;
```
Perintah ini dapat dijalankan melalui `shell` atau menggunakan perintah `exec` seperti yang ditunjukkan di sini:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Mengembalikan:__*  

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

> Template ini kemudian dapat digunakan bersama file data `json` atau `jsonnet` untuk menyediakan nilai saat runtime; untuk informasi selengkapnya, lihat [Menggunakan variabel](/getting-started/variables).

### Menghasilkan template `INSERT` hanya dengan parameter wajib {#generating-an-insert-template-with-only-required-parameters}

Untuk menghasilkan template `INSERT` StackQL yang hanya berisi atribut wajib, gunakan petunjuk kueri `/*+ REQUIRED */` seperti yang ditunjukkan di sini:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Perintah ini dapat dijalankan melalui `shell` atau menggunakan perintah `exec` seperti yang ditunjukkan di sini:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__Mengembalikan:__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
