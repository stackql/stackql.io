---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi INSTR di StackQL: posisi (dimulai dari satu) kemunculan pertama suatu string di dalam string lain, atau 0 jika tidak ditemukan."
image: "/img/stackql-featured-image.png"
---
Mengembalikan posisi karakter dari kemunculan pertama suatu string di dalam string lain, atau 0 jika string yang dicari tidak ditemukan.

Lihat juga:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*string*__  
String sumber tempat pencarian dilakukan.

__*searchstring*__  
Substring yang dicari.

> Jika *string* dan *searchstring* keduanya BLOB, `instr(<string>, <searchstring>)` mengembalikan jumlah byte sebelum kemunculan pertama *searchstring* ditambah satu, atau 0 jika *searchstring* tidak muncul di mana pun di dalam *string*. Jika kedua argumen bukan `NULL` dan bukan BLOB, keduanya diinterpretasikan sebagai string. 

## Nilai yang dikembalikan {#return-values}
Mengembalikan bilangan bulat.

> Jika salah satu nilai *string* atau *searchstring* adalah `NULL`, hasilnya adalah `NULL`.

* * *

## Contoh {#examples}

### Mencari string dalam field {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
