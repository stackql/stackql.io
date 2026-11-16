---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Mengagregasi pasangan kunci-nilai menjadi objek JSON menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Mengagregasi pasangan kunci-nilai menjadi objek JSON, dengan pengelompokan berdasarkan kolom tertentu. Fungsi `JSON_GROUP_OBJECT()` mengumpulkan semua pasangan kunci-nilai untuk setiap grup pada kolom yang ditentukan dan menyatukannya menjadi satu objek JSON untuk setiap grup.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Argumen {#arguments}

__*key_column*__  
Kolom yang menyediakan kunci untuk objek JSON.

__*value_column*__  
Kolom yang menyediakan nilai untuk objek JSON.

__*grouping_column*__  
Kolom yang menjadi dasar pengelompokan hasil.

## Nilai yang dikembalikan {#return-values}
Mengembalikan objek JSON yang berisi semua pasangan kunci-nilai teragregasi dari kolom yang ditentukan, yang dikelompokkan menurut `grouping_column`.

* * *

## Contoh {#examples}

### Mengagregasi ukuran repositori berdasarkan bahasa pemrograman {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

Kueri ini mengembalikan objek JSON untuk setiap bahasa pemrograman, dan setiap objek berisi nama repositori beserta ukurannya sebagai pasangan kunci-nilai. Berikut contoh output yang diharapkan:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

Output ini berguna untuk memvisualisasikan distribusi ukuran repositori di berbagai bahasa pemrograman dalam suatu organisasi, dengan data yang jelas dan terorganisasi yang dikelompokkan berdasarkan bahasa.
