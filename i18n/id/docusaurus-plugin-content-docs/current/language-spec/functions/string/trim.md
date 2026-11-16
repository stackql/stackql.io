---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi TRIM di StackQL: menghapus spasi, atau karakter apa pun dari sekumpulan karakter tertentu, dari kedua ujung string."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string yang dibentuk dengan menghapus spasi, atau karakter apa pun dari sekumpulan karakter tertentu, dari kedua ujung string input.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*character_expression*__  
String literal atau kolom string.

__*character*__  
String karakter opsional: setiap karakter di dalamnya dihapus dari awal dan akhir *character_expression*.  Jika dihilangkan, yang dihapus adalah spasi (bukan tab atau baris baru).

## Nilai yang dikembalikan {#return-values}
Mengembalikan string.

* * *

## Contoh {#examples}

### Menghapus spasi di awal dan akhir kolom string {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
