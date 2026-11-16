---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi FLOOR di StackQL: membulatkan argumen numerik ke bawah ke bilangan bulat terdekat."
image: "/img/stackql-featured-image.png"
---
Membulatkan argumen numerik ke bawah ke bilangan bulat terdekat.  

Lihat juga:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi dari kategori tipe data numerik eksak atau numerik perkiraan.

## Nilai yang dikembalikan {#return-values}
Mengembalikan bilangan bulat terbesar yang lebih kecil dari atau sama dengan ekspresi numerik yang ditentukan.

* * *

## Contoh {#examples}

### Membulatkan bilangan floating point ke bawah ke bilangan bulat terdekat (yang lebih kecil) {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_floor
FROM google.storage.buckets WHERE project = 'stackql';
```
