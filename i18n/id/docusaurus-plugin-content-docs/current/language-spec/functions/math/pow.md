---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung hasil perpangkatan satu bilangan dengan bilangan lain menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung *x* dipangkatkan *y*.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*x*__  
Bilangan pokok, berupa ekspresi numerik.

__*y*__  
Eksponen yang menjadi pangkat bilangan pokok *x*, juga berupa ekspresi numerik.

## Nilai yang dikembalikan {#return-values}
Mengembalikan hasil *x* dipangkatkan *y*.

* * *

## Contoh {#examples}

### Menghitung x dipangkatkan y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
