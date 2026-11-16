---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung logaritma basis 2 suatu bilangan menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung logaritma basis 2 dari bilangan yang diberikan, dengan mengembalikan pangkat yang harus dikenakan pada bilangan 2 untuk memperoleh nilai tersebut.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi numerik positif yang akan dihitung logaritmanya dengan basis 2.

## Nilai yang dikembalikan {#return-values}
Mengembalikan logaritma basis 2 dari nilai numerik yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung logaritma basis 2 dari 16 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Kueri ini mengembalikan `4`, karena 2^4 = 16.

### Menghitung logaritma basis 2 dari 32 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Kueri ini menghitung logaritma basis 2 dari 32, yang menghasilkan `5`, karena 2^5 = 32.
