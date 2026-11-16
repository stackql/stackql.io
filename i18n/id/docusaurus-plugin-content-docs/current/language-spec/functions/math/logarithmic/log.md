---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung logaritma suatu bilangan untuk basis apa pun menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung logaritma suatu bilangan dengan basis yang ditentukan, yang merupakan bentuk fungsi logaritma yang lebih umum dibandingkan LN, yang menggunakan basis e.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*base*__  
Basis logaritma, berupa ekspresi numerik positif selain 1.

__*numeric_expression*__  
Ekspresi numerik positif yang logaritmanya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan logaritma nilai numerik yang ditentukan dengan basis yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung logaritma 1000 dengan basis 10 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Kueri ini mengembalikan `3`, karena 10^3 = 1000.

### Menghitung logaritma 16 dengan basis 2 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Kueri ini menghitung logaritma 16 dengan basis 2, yang menghasilkan `4`, karena 2^4 = 16.
