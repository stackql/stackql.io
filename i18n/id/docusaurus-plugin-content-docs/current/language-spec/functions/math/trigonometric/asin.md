---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung arkus sinus (invers sinus) suatu bilangan, yang mengembalikan sudut dalam radian, menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung arkus sinus (invers sinus) suatu bilangan, yaitu sudut dalam radian yang sinusnya sama dengan bilangan yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik dalam rentang -1 hingga 1, inklusif, yang arkus sinusnya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan arkus sinus dari nilai yang ditentukan dalam radian.

* * *

## Contoh {#examples}

### Menghitung arkus sinus 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Kueri ini mengembalikan π/6 atau kira-kira `0.5236`, karena arkus sinus 0.5 sesuai dengan sudut π/6 radian.

### Menghitung arkus sinus -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Kueri ini menghitung arkus sinus -1, yang menghasilkan -π/2 atau kira-kira `-1.5708`, karena sinus -π/2 adalah -1.
