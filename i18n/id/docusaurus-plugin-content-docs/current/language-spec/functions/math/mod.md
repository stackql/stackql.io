---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung sisa pembagian satu bilangan dengan bilangan lain menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung sisa pembagian *x* dengan *y*, dengan mengembalikan modulus. Ini mirip dengan penggunaan operator '%', tetapi kompatibel dengan argumen yang bukan bilangan bulat.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*x*__  
Bilangan yang dibagi, berupa ekspresi numerik.

__*y*__  
Pembagi, juga berupa ekspresi numerik.

## Nilai yang dikembalikan {#return-values}
Mengembalikan sisa pembagian *x* dengan *y*.

* * *

## Contoh {#examples}

### Menghitung modulus 10 dibagi 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
