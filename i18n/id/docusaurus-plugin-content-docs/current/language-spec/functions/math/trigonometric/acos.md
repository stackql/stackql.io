---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung arkus kosinus (invers kosinus) suatu bilangan, yang mengembalikan sudut dalam radian, menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung arkus kosinus (invers kosinus) suatu bilangan, yaitu sudut dalam radian yang kosinusnya sama dengan bilangan yang ditentukan.

Lihat juga:  
[[`SELECT`]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*value*__  
Ekspresi numerik dalam rentang -1 hingga 1, inklusif, yang arkus kosinusnya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan arkus kosinus dari nilai yang ditentukan dalam radian.

* * *

## Contoh {#examples}

### Menghitung arkus kosinus 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Kueri ini mengembalikan kira-kira `1.047`, karena arkus kosinus 0.5 adalah π/3 radian (60 derajat), yaitu sekitar 1.047 radian.

### Menghitung arkus kosinus 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Kueri ini menghitung arkus kosinus 1, yang menghasilkan `0`, karena kosinus 0 radian adalah 1.
