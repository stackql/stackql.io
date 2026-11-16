---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Menghitung akar kuadrat suatu bilangan menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Menghitung akar kuadrat dari ekspresi numerik nonnegatif.

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi numerik nonnegatif yang akar kuadratnya akan dihitung.

## Nilai yang dikembalikan {#return-values}
Mengembalikan akar kuadrat dari nilai numerik nonnegatif yang ditentukan.

* * *

## Contoh {#examples}

### Menghitung akar kuadrat suatu bilangan {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
