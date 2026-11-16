---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi REPLACE di StackQL: mengganti setiap kemunculan substring di dalam string dengan string lain."
image: "/img/stackql-featured-image.png"
---
Mengembalikan salinan string dengan setiap kemunculan suatu substring diganti dengan string lain.

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*character_expression*__  
String literal atau kolom string tempat Anda ingin melakukan penggantian.

__*pattern*__  
Substring yang dicari dalam string asli.

> Jika *pattern* berupa string kosong atau tidak ditemukan dalam *character_expression*, *character_expression* dikembalikan tanpa perubahan. 

__*replacement*__  
String pengganti.

> Jika *replacement* pada awalnya bukan string, nilai tersebut dikonversi menjadi string UTF-8 sebelum diproses.

> Urutan kolasi `BINARY` digunakan untuk perbandingan.

## Nilai yang dikembalikan {#return-values}
Mengembalikan string.

* * *

## Contoh {#examples}

### Mengganti pola string dalam kolom {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
