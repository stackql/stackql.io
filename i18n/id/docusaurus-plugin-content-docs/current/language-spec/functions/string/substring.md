---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi SUBSTRING di StackQL (alias SUBSTR): mengembalikan bagian dari string atau nilai biner berdasarkan posisi awal dan panjang."
image: "/img/stackql-featured-image.png"
---
Mengembalikan bagian dari ekspresi karakter atau biner.  `SUBSTR` adalah alias untuk `SUBSTRING`.

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[ Tipe data ]](/language-spec/data-types)

* * * 

:::info

Untuk mengekstrak field dari objek JSON, gunakan fungsi [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Sintaksis {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expression*__  
String atau BLOB.

> Jika *expression* adalah string, indeks karakter merujuk ke karakter UTF-8 yang sebenarnya. Jika *expression* adalah BLOB, indeks merujuk ke byte.

__*start*__  
Bilangan bulat yang menyatakan karakter atau byte pertama dari bagian ekspresi input yang akan dikembalikan.  Jika *start* negatif, karakter pertama substring ditemukan dengan menghitung dari kanan, bukan dari kiri.

> Karakter paling kiri dari *expression* adalah nomor 1.

__*length*__  
Nilai bilangan bulat opsional yang menyatakan panjang bagian yang akan dikembalikan setelah nilai *start*.  Jika *length* dihilangkan, semua karakter hingga akhir string atau BLOB yang ditentukan oleh *expression* dikembalikan.

> Jika *length* negatif, karakter yang mendahului *start* dikembalikan, sebanyak nilai absolut *length*: `SUBSTR('abcdef', 4, -2)` mengembalikan `bc`.

## Nilai yang dikembalikan {#return-values}

Mengembalikan string atau nilai BLOB, bergantung pada tipe *expression*.

* * *

## Contoh {#examples}

### Mengembalikan *n* karakter pertama dari kolom {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Memotong *n* karakter pertama dari kolom {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan *n* karakter terakhir dari kolom {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
