---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung tangen hiperbolik suatu bilangan dan mengembalikan hasilnya menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung tangen hiperbolik dari nilai yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik yang tangen hiperboliknya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan tangen hiperbolik dari nilai yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung tangen hiperbolik 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Kueri ini mengembalikan kira-kira `0.7616`, karena tangen hiperbolik dari 1 adalah sekitar 0.7616.

### Menghitung tangen hiperbolik 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Kueri ini menghitung tangen hiperbolik dari 0, dengan hasil `0`, karena tangen hiperbolik dari 0 tepat sama dengan 0.
