---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi IFNULL di StackQL: mengembalikan argumen pertama yang bukan NULL dari tepat dua argumen, bentuk dua argumen dari COALESCE."
image: "/img/stackql-featured-image.png"
---
Menerima tepat dua argumen dan mengembalikan argumen pertama yang bukan `NULL`; dapat digunakan untuk mengganti nilai `NULL` dengan nilai default.  

> Fungsi `IFNULL` setara dengan fungsi [`COALESCE`](/language-spec/functions/general/coalesce) yang hanya memiliki dua argumen; fungsi [`COALESCE`](/language-spec/functions/general/coalesce) dapat memiliki lebih dari dua argumen.

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Sintaksis {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expression*__  
Dua ekspresi dengan tipe data apa pun yang menghasilkan nilai `NULL` atau bukan `NULL`.

> Jika kedua argumen *expression* bernilai `NULL`, fungsi `IFNULL` mengembalikan `NULL`.


## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai pertama yang bukan `NULL` dari kedua argumen *expression*. 

* * *

## Contoh {#examples}

### Mengganti nilai `NULL` dengan nilai default {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
