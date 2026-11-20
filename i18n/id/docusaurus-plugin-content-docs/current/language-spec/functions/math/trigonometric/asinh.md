---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung arkus sinus hiperbolik (invers sinus hiperbolik) suatu bilangan menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung arkus sinus hiperbolik (invers sinus hiperbolik) suatu bilangan, yaitu nilai yang sinus hiperboliknya sama dengan bilangan yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik yang arkus sinus hiperboliknya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan arkus sinus hiperbolik dari nilai yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung arkus sinus hiperbolik 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Kueri ini mengembalikan kira-kira `0.4812`, karena arkus sinus hiperbolik 0.5 adalah sekitar 0.4812.

### Menghitung arkus sinus hiperbolik -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Kueri ini menghitung arkus sinus hiperbolik -1, yang menghasilkan kira-kira `-0.8814`, karena itulah nilai yang sinus hiperboliknya adalah -1.
