---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi LOWER di StackQL: mengubah string menjadi huruf kecil."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string input dalam huruf kecil.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Tipe data ]](/language-spec/data-types)

* * * 

## Sintaksis {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> Fungsi `lower()` hanya bekerja untuk karakter ASCII.

## Argumen {#arguments}

__*character_expression*__  
String ASCII.

## Nilai yang dikembalikan {#return-values}
Mengembalikan nilai string.

* * *

## Contoh {#examples}

### Mengembalikan string dalam huruf kecil {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
