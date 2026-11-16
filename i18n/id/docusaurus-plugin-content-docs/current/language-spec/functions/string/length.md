---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi LENGTH di StackQL: jumlah karakter dalam string, atau jumlah byte dalam BLOB."
image: "/img/stackql-featured-image.png"
---
Mengembalikan jumlah karakter dalam ekspresi input string sebelum karakter `NULL` pertama, atau jumlah byte dalam ekspresi BLOB.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Untuk mendapatkan panjang array JSON, gunakan fungsi [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Sintaksis {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expression*__  
String ASCII atau BLOB.

> Mengembalikan `NULL` jika *expression* adalah `NULL`.

> Jika *expression* bernilai numerik, yang dikembalikan adalah panjang representasi string dari *expression*.

## Nilai yang dikembalikan {#return-values}
Mengembalikan bilangan bulat.

* * *

## Contoh {#examples}

### Mengembalikan panjang kolom string {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
