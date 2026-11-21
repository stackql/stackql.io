---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi bernilai tabel JSON_EACH di StackQL: memecah array atau objek JSON menjadi satu baris per elemen atau anggota, untuk melakukan join dan memfilter respons API bersarang."
image: "/img/stackql-featured-image.png"
---
Fungsi bernilai tabel yang mengembalikan tabel berisi satu baris untuk setiap elemen array atau anggota objek.

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Tipe data ]](/language-spec/data-types)

* * * 

:::tip 

Gunakan pernyataan [**DESCRIBE**](/language-spec/describe) untuk menemukan tipe data `array` yang dapat digunakan dengan fungsi `json_each`.

:::

:::note 

Fungsi `json_each` diimplementasikan sebagai fungsi `json_array_elements_text` jika Anda menggunakan stackql dengan backend SQL `postgres`

:::

## Sintaksis {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object [, json_path]);
```
## Argumen {#arguments}

__*json_array_or_object*__  
Array atau objek JSON.

__*json_path*__  
Opsional. Jalur JSON ke array atau objek yang terdapat di dalam *json_array_or_object*.  Lihat [jsonpath.com](https://jsonpath.com/).

> Jika *json_path* tidak ada di dalam *json_array_or_object*, fungsi tidak mengembalikan baris apa pun.

> Fungsi `json_each()` menghasilkan error jika *json_array_or_object* bukan JSON yang berformat benar.

## Nilai yang dikembalikan {#return-values}
Tabel berisi satu baris untuk setiap elemen array atau anggota objek.

* * *

## Contoh {#examples}

### Memecah array string menjadi tabel yang di-join dengan field terkait {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

sebagai konteks:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

mengembalikan...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

Kueri `stackql` berikut menggunakan fungsi `json_each` untuk memecah array `members` menjadi tabel yang di-join dengan field `role` dan, secara opsional, field `condition`.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

akan mengembalikan...

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
