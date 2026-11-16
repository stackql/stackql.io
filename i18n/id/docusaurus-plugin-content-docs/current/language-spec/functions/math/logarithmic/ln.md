---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung logaritma natural suatu bilangan menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung logaritma natural (logaritma dengan basis e) dari bilangan yang diberikan.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi numerik positif yang logaritma naturalnya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan logaritma natural dari nilai numerik positif yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung logaritma natural dari 2.71828 (kira-kira e) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Kueri ini mengembalikan nilai yang mendekati `1`, karena logaritma natural dari e (kira-kira 2.71828) adalah 1.

### Menghitung logaritma natural dari bilangan yang lebih besar {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Kueri ini menghitung logaritma natural dari 10, yang memberikan nilai logaritmik 10 dengan basis e.
