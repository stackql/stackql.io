---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi SIGN di StackQL: mengembalikan -1, 0, atau 1 bergantung pada apakah argumen numerik bernilai negatif, nol, atau positif."
image: "/img/stackql-featured-image.png"
---
Mengembalikan -1 jika nilai input negatif, 0 jika nilai input 0, dan 1 jika nilai input positif.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*numeric_expression*__  
Ekspresi dari kategori tipe data numerik eksak atau numerik perkiraan.

> Jika *numeric_expression* bernilai `NULL` atau merupakan `string` atau BLOB yang tidak dapat dikonversi menjadi bilangan tanpa kehilangan informasi, fungsi akan mengembalikan `NULL`.

## Nilai yang dikembalikan {#return-values}

Mengembalikan `-1`, `0`, atau `1`.

* * *

## Contoh {#examples}

### Menentukan tanda suatu bilangan {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
