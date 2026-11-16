---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi COALESCE di StackQL: mengembalikan argumen pertama yang bukan NULL dari dua argumen atau lebih, biasanya untuk mengganti nilai yang hilang dengan nilai default."
image: "/img/stackql-featured-image.png"
---
Mengembalikan argumen pertama yang bukan `NULL` dari serangkaian dua argumen atau lebih; dapat digunakan untuk mengganti nilai `NULL` dengan nilai default.  

> Fungsi `COALESCE` setara dengan fungsi `NVL` di Oracle atau fungsi `IFNULL` di MySQL.

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Sintaksis {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expression*__  
Dua ekspresi atau lebih dengan tipe data apa pun yang menghasilkan nilai `NULL` atau bukan `NULL`.

> Jika semua argumen *expression* bernilai `NULL`, fungsi `COALESCE` mengembalikan `NULL`.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai pertama yang bukan `NULL` dari serangkaian argumen *expression*.

* * *

## Contoh {#examples}

### Mengganti nilai `NULL` dengan nilai default {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
