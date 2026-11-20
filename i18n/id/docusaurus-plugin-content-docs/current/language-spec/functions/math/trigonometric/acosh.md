---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung arkus kosinus hiperbolik (invers kosinus hiperbolik) suatu bilangan menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung arkus kosinus hiperbolik (invers kosinus hiperbolik) suatu bilangan, yaitu nilai nonnegatif yang kosinus hiperboliknya sama dengan bilangan yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik yang lebih besar dari atau sama dengan 1, yang arkus kosinus hiperboliknya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan arkus kosinus hiperbolik dari nilai yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung arkus kosinus hiperbolik 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Kueri ini mengembalikan `0`, karena arkus kosinus hiperbolik 1 adalah 0.

### Menghitung arkus kosinus hiperbolik 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Kueri ini menghitung arkus kosinus hiperbolik 10, yang menghasilkan kira-kira `2.9932`, karena itulah nilai yang kosinus hiperboliknya adalah 10.
