---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi NULLIF di StackQL: mengembalikan NULL jika dua argumen sama, dan jika tidak, mengembalikan argumen pertama."
image: "/img/stackql-featured-image.png"
---
Menerima dua argumen dan mengembalikan nilai `NULL` jika keduanya sama; jika tidak, argumen pertama akan dikembalikan.  

> Fungsi `NULLIF` setara dengan `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> `NULLIF` berguna dengan fungsi agregat seperti [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum), dan [`COUNT`](/language-spec/functions/aggregate/count) jika kumpulan hasil berisi nilai khusus yang bukan `NULL`, tetapi ingin Anda perlakukan sebagai `NULL` untuk keperluan agregasi.

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Sintaksis {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expression*__  
Dua ekspresi dengan tipe data apa pun yang menghasilkan nilai `NULL` atau bukan `NULL`.

> Fungsi `NULLIF` menelusuri argumennya dari kiri ke kanan untuk menemukan argumen yang mendefinisikan fungsi kolasi, lalu menggunakan fungsi kolasi tersebut untuk semua perbandingan string.  Jika tidak ada argumen yang mendefinisikan fungsi kolasi, kolasi `BINARY` akan digunakan.

## Nilai yang dikembalikan {#return-values}

Mengembalikan `NULL` jika kedua argumen *expression* menghasilkan nilai yang sama; jika tidak, nilai pertama akan dikembalikan. 

* * *

## Contoh {#examples}

### Menghitung baris dengan field yang tidak sama dengan nilai tertentu {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name != 'controller-0';
*/
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
