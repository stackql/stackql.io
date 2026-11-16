---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung arkus tangen (invers tangen) suatu bilangan, yang mengembalikan sudut dalam radian, menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung arkus tangen (invers tangen) suatu bilangan, yaitu sudut dalam radian yang tangennya sama dengan bilangan yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik yang arkus tangennya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan arkus tangen dari nilai yang ditentukan dalam radian.

* * *

## Contoh {#examples}

### Menghitung arkus tangen 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Kueri ini mengembalikan π/4 atau kira-kira `0.785`, karena arkus tangen 1 adalah π/4 radian.

### Menghitung arkus tangen 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Kueri ini menghitung arkus tangen 0, yang menghasilkan `0`, karena tangen 0 radian adalah 0.
