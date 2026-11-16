---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Mencocokkan pola dalam string dengan ekspresi reguler menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Memeriksa apakah string sumber cocok dengan pola ekspresi reguler yang diberikan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*source_string*__  
String tempat pola dicari.

__*pattern_expression*__  
Pola ekspresi reguler yang dicocokkan dengan string sumber.

## Nilai yang dikembalikan {#return-values}
Mengembalikan nilai boolean: `TRUE` jika string sumber cocok dengan pola ekspresi reguler, atau `FALSE` jika tidak.

* * *

## Contoh {#examples}

### Memeriksa apakah string berisi angka {#check-if-a-string-contains-numbers}

Contoh ini menunjukkan cara memeriksa apakah string 'the year is 2021' berisi satu atau lebih karakter numerik:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

Kueri ini mengembalikan `TRUE` karena string tersebut berisi substring '2021', yang cocok dengan pola `[0-9]+`, yaitu satu atau lebih digit.

### Memvalidasi format email {#validate-email-format}

Untuk memeriksa apakah string memiliki format email yang valid:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

Kueri ini mengembalikan `TRUE`, yang menunjukkan bahwa 'user@example.com' berformat seperti alamat email pada umumnya dan cocok dengan pola ekspresi reguler yang ditentukan.
