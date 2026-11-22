---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi JSON_EXTRACT di StackQL: mengambil satu atau beberapa nilai dari objek atau array JSON berdasarkan jalur, alat utama untuk meratakan field respons API bersarang."
image: "/img/stackql-featured-image.png"
---
Mengembalikan satu atau beberapa nilai dari objek atau array JSON yang berformat benar.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipe data ]](/language-spec/data-types)

* * * 

:::tip 

Gunakan pernyataan [**DESCRIBE**](/language-spec/describe) untuk menemukan tipe data `array` atau `object` yang dapat digunakan dengan fungsi JSON StackQL. Gunakan fungsi [**JSON_TYPE**](/language-spec/functions/json/json_type) untuk menentukan tipe data objek bersarang atau elemen array.

:::

:::note 

Fungsi `json_extract` diimplementasikan sebagai fungsi `json_extract_path_text` jika Anda menggunakan stackql dengan backend SQL `postgres`.  Jalur dipisahkan pada `.` dan `$` di awal dihapus, sehingga `'$.a.b'` menjadi kunci `'a'` dan `'b'`; subskrip array seperti `$[0]` diteruskan sebagai kunci objek dan tidak memilih elemen array di backend tersebut.

:::


## Sintaksis {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*json_array_or_object*__  
Array atau objek JSON.

__*json_path*__  
Jalur JSON ke array atau objek yang terdapat di dalam *json_array_or_object*.  Lihat [jsonpath.com](https://jsonpath.com/).

> Jika satu *json_path* diberikan, fungsi mengembalikan hasil skalar dengan tipe data dari jalur yang ditemukan.

> Jika beberapa argumen *json_path* diberikan, hasilnya berupa array yang tersusun dari objek yang ditemukan oleh masing-masing jalur.

> Menentukan `'$'` untuk argumen *json_path* akan mengembalikan seluruh objek JSON dari kolom yang direpresentasikan oleh argumen *json_array_or_object*.

> CATATAN: Anda dapat menggunakan alat seperti [`json-viewer`](https://aarrange.com/json-viewer) untuk mengembangkan dan menguji ekspresi jalur JSON

## Nilai yang dikembalikan {#return-values}
String, bilangan bulat, objek, array, boolean, atau null, bergantung pada *json_array_or_object* dan *json_path* yang diberikan sebagai input.

* * *

## Contoh {#examples}

### Mengembalikan objek disk yang terkait dengan setiap instance Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan nama perangkat dan ukuran disk pertama yang terpasang pada setiap instance Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan tingkat jaringan dari NIC pertama yang terpasang pada setiap instance Google Compute Engine {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan daftar cakupan dari akun layanan pertama yang terkait dengan setiap instance Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
