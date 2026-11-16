---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi LTRIM di StackQL: menghapus spasi, atau karakter mana pun dari kumpulan karakter tertentu, dari sisi kiri string."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string yang dibentuk dengan menghapus spasi, atau karakter mana pun dari kumpulan karakter tertentu, dari sisi kiri string input.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*character_expression*__  
String literal atau kolom string.

__*character*__  
String karakter opsional: setiap karakter di dalamnya dihapus dari awal *character_expression*.  Jika tidak diberikan, yang dihapus adalah spasi (bukan tab atau baris baru).

## Nilai yang dikembalikan {#return-values}
Mengembalikan string.

* * *

## Contoh {#examples}

### Menghapus spasi awal dari kolom string {#remove-leading-spaces-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
