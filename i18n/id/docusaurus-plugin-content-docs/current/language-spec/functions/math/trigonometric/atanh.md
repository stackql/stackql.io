---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung arkus tangen hiperbolik (invers tangen hiperbolik) suatu bilangan, yang mengembalikan sudut dalam radian, menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung arkus tangen hiperbolik (invers tangen hiperbolik) suatu bilangan, yaitu nilai dalam radian yang tangen hiperboliknya sama dengan bilangan yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik dalam rentang -1 hingga 1 (eksklusif), yang arkus tangen hiperboliknya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan arkus tangen hiperbolik dari nilai yang ditentukan dalam radian.

* * *

## Contoh {#examples}

### Menghitung arkus tangen hiperbolik 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Kueri ini mengembalikan kira-kira `0.5493`, karena arkus tangen hiperbolik 0.5 adalah sekitar 0.5493 radian.

### Menghitung arkus tangen hiperbolik -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Kueri ini menghitung arkus tangen hiperbolik -0.5, yang menghasilkan kira-kira `-0.5493`, karena itulah nilai dalam radian yang tangen hiperboliknya adalah -0.5.
