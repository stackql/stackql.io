---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi agregat MAX di StackQL: nilai terbesar dalam kolom atau pengelompokan kolom."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai maksimum berdasarkan input kolom atau pengelompokan kolom.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

### Fungsi agregat {#aggregate-function}

__*columnExpression*__  
Kolom atau ekspresi, termasuk operator (`+`, `-`, ...).

> Fungsi `MAX` mengembalikan nilai terbesar untuk kolom yang diberikan berdasarkan tipe data kolom tersebut; misalnya, operasi `MAX` pada kolom bilangan bulat mengembalikan bilangan bulat terbesar untuk kolom atau pengelompokan kolom tersebut, sedangkan operasi `MAX` pada kolom atau pengelompokan kolom string mengembalikan nilai yang berada di urutan terakhir, dengan membandingkan string menggunakan fungsi kolasi `BINARY` (byte demi byte, sehingga huruf kapital diurutkan sebelum huruf kecil).

__*groupByColumn*__  
Satu atau beberapa kolom yang digunakan untuk melakukan operasi ringkasan atau agregat.  Klausa `GROUP BY` mengembalikan satu baris untuk setiap pengelompokan kolom.

> Fungsi `MAX` mengabaikan nilai `NULL`.

> Fungsi `MAX` mengembalikan `NULL` jika semua nilai dalam kelompok bernilai `NULL`.

### Fungsi skalar {#scalar-function}

__*scalarExpression*__  
Daftar kolom atau ekspresi (2 atau lebih) yang nilai terbesarnya ditentukan dan dikembalikan. 

> Fungsi skalar `MAX` mengembalikan `NULL` jika ada argumen yang bernilai `NULL`. 

> Fungsi skalar atau multiargumen `MAX` menelusuri argumennya dari kiri ke kanan untuk menemukan argumen yang mendefinisikan fungsi kolasi, lalu menggunakan fungsi kolasi tersebut untuk semua perbandingan string. 

> Jika tidak ada argumen fungsi skalar `MAX` yang mendefinisikan fungsi kolasi, fungsi kolasi `BINARY` akan digunakan. 

> Jika hanya satu argumen yang diberikan, fungsi agregat `MAX` akan dipanggil.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai maksimum berdasarkan tipe data input (atau pengelompokan).

* * *

## Contoh {#examples}

### Mengembalikan nilai maksimum untuk suatu kolom {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengembalikan nilai maksimum untuk setiap nilai dalam suatu kolom {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Mengembalikan nilai maksimum dari daftar nilai menggunakan fungsi skalar `max` {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Menggunakan `MAX` sebagai fungsi jendela untuk menemukan nilai maksimum dalam partisi {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) atau [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
