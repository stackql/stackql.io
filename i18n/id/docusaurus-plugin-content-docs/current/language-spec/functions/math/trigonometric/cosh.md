---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung kosinus hiperbolik suatu bilangan dan mengembalikan hasilnya menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung kosinus hiperbolik dari nilai yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik yang kosinus hiperboliknya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan kosinus hiperbolik dari nilai yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung kosinus hiperbolik 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Kueri ini mengembalikan kira-kira `1.5431`, karena kosinus hiperbolik 1 adalah sekitar 1.5431.

### Menghitung kosinus hiperbolik 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Kueri ini menghitung kosinus hiperbolik 0, yang menghasilkan `1`, karena kosinus hiperbolik 0 tepat bernilai 1.
