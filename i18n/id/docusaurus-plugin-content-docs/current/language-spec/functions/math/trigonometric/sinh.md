---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung sinus hiperbolik suatu bilangan dan mengembalikan hasilnya menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung sinus hiperbolik dari nilai yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik yang sinus hiperboliknya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan sinus hiperbolik dari nilai yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung sinus hiperbolik 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Kueri ini mengembalikan kira-kira `1.1752`, karena sinus hiperbolik dari 1 adalah sekitar 1.1752.

### Menghitung sinus hiperbolik 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Kueri ini menghitung sinus hiperbolik dari 0, dengan hasil `0`, karena sinus hiperbolik dari 0 tepat sama dengan 0.
