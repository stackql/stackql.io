---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi UPPER di StackQL: mengubah string menjadi huruf kapital."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string input dalam huruf kapital.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Tipe data ]](/language-spec/data-types)

* * * 

## Sintaksis {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> Fungsi `upper()` hanya bekerja untuk karakter ASCII.

## Argumen {#arguments}

__*character_expression*__  
String ASCII.

## Nilai yang dikembalikan {#return-values}
Mengembalikan nilai string.

* * *

## Contoh {#examples}

### Mengembalikan string dalam huruf kapital {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
