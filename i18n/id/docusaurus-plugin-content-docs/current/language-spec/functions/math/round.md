---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi ROUND di StackQL: membulatkan argumen numerik ke sejumlah tempat desimal tertentu, dengan nilai default nol."
image: "/img/stackql-featured-image.png"
---
Membulatkan argumen numerik.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi dari kategori tipe data numerik eksak atau numerik perkiraan.

__*precision*__  
Jumlah digit di sebelah kanan titik desimal untuk pembulatan *numeric_expression*.

> Jika argumen *precision* dihilangkan, nilainya dianggap 0.

## Nilai yang dikembalikan {#return-values}
Mengembalikan nilai numerik.

* * *

## Contoh {#examples}

### Membulatkan bilangan floating point ke dua tempat desimal {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
