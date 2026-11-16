---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi ABS di StackQL: nilai absolut dari argumen numerik."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai absolut dari argumen numerik.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `abs(X)` mengembalikan `NULL` jika `X` bernilai `NULL`. `abs(X)` mengembalikan `0.0` jika `X` adalah string atau blob yang tidak dapat dikonversi menjadi nilai numerik. Jika `X` adalah bilangan bulat -9223372036854775808, `abs(X)` menghasilkan error overflow bilangan bulat karena tidak ada nilai positif padanannya dalam representasi komplemen dua 64-bit.

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi dari kategori tipe data numerik eksak atau numerik perkiraan.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai numerik positif.

* * *

## Contoh {#examples}

### Mengembalikan nilai absolut suatu bilangan {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_mathfunc.html](https://www.sqlite.org/lang_mathfunc.html)
