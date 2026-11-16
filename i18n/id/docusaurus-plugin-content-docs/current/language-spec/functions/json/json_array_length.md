---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi JSON_ARRAY_LENGTH di StackQL: jumlah elemen dalam array JSON, di tingkat atas atau pada suatu jalur di dalam nilai JSON."
image: "/img/stackql-featured-image.png"
---
Mengembalikan jumlah elemen dalam array JSON.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Tipe data ]](/language-spec/data-types)

* * * 

:::tip 

Gunakan pernyataan [**DESCRIBE**](/language-spec/describe) untuk menemukan tipe data `array` atau `object` yang dapat digunakan dengan fungsi JSON StackQL. Gunakan fungsi [**JSON_TYPE**](/language-spec/functions/json/json_type) untuk menentukan tipe data objek bersarang atau elemen array.

:::

## Sintaksis {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*json_array_or_object*__  
Array atau objek JSON.

> Jika *json_array_or_object* berupa objek JSON (bukan array) dan *json_path* tidak ditentukan, fungsi mengembalikan `0`.

__*json_path*__  
Jalur JSON opsional ke array yang terdapat di dalam objek.  Lihat [jsonpath.com](https://jsonpath.com/).

> Jika *json_path* diberikan, fungsi `json_array_length` mencari jalur tersebut di dalam *json_array_or_object* dan mengembalikan panjang array, atau `0` jika *json_path* berupa objek (bukan array).  Jika *json_path* tidak dapat ditemukan di dalam *json_array_or_object*, fungsi mengembalikan `NULL`.

> Jika *json_array_or_object* bukan JSON yang berformat benar, atau *json_path* bukan jalur JSON yang berformat benar, fungsi menghasilkan error.

## Nilai yang dikembalikan {#return-values}
Mengembalikan bilangan bulat.

* * *

## Contoh {#examples}

### Mengembalikan jumlah disk yang terpasang pada instance Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan jumlah antarmuka jaringan yang terpasang pada instance Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan jumlah ACL yang terkait dengan bucket Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Mengembalikan jumlah cakupan yang terkait dengan akun layanan {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
