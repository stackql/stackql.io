---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung logaritma basis 10 suatu bilangan menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung logaritma basis 10 dari bilangan yang diberikan, dengan menentukan pangkat yang harus dikenakan pada bilangan 10 untuk mencapai nilai tersebut.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi numerik positif yang akan dihitung logaritmanya dengan basis 10.

## Nilai yang dikembalikan {#return-values}
Mengembalikan logaritma basis 10 dari nilai numerik yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung logaritma basis 10 dari 100 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Kueri ini mengembalikan `2`, karena 10^2 = 100.

### Menghitung logaritma basis 10 dari 1000 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Kueri ini menghitung logaritma basis 10 dari 1000, yang menghasilkan `3`, karena 10^3 = 1000.
