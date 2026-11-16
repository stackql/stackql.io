---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi agregat MIN di StackQL: nilai terkecil dalam kolom atau pengelompokan kolom."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai minimum berdasarkan input kolom atau pengelompokan kolom.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Sintaksis {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

### Fungsi agregat {#aggregate-function}

__*columnExpression*__  
Kolom atau ekspresi, termasuk operator (`+`, `-`, ...).

> Fungsi `MIN` mengembalikan nilai terkecil untuk kolom yang diberikan berdasarkan tipe data kolom tersebut; misalnya, operasi `MIN` pada kolom bilangan bulat mengembalikan bilangan bulat terkecil untuk kolom atau pengelompokan kolom tersebut, sedangkan operasi `MIN` pada kolom atau pengelompokan kolom string mengembalikan nilai yang berada di urutan pertama, dengan membandingkan string menggunakan fungsi kolasi `BINARY` (byte demi byte, sehingga huruf kapital diurutkan sebelum huruf kecil).

__*groupByColumn*__  
Satu atau beberapa kolom yang digunakan untuk melakukan operasi ringkasan atau agregat.  Klausa `GROUP BY` mengembalikan satu baris untuk setiap pengelompokan kolom.

> Fungsi `MIN` mengabaikan nilai `NULL`.

> Fungsi `MIN` mengembalikan `NULL` jika semua nilai dalam kelompok bernilai `NULL`.

### Fungsi skalar {#scalar-function}

__*scalarExpression*__  
Daftar kolom atau ekspresi (2 atau lebih) yang nilai terkecilnya ditentukan dan dikembalikan. 

> Fungsi skalar `MIN` mengembalikan `NULL` jika ada argumen yang bernilai `NULL`. 

> Fungsi skalar atau multiargumen `MIN` menelusuri argumennya dari kiri ke kanan untuk menemukan argumen yang mendefinisikan fungsi kolasi, lalu menggunakan fungsi kolasi tersebut untuk semua perbandingan string. 

> Jika tidak ada argumen fungsi skalar `MIN` yang mendefinisikan fungsi kolasi, fungsi kolasi `BINARY` akan digunakan. 

> Jika hanya satu argumen yang diberikan, fungsi agregat `MIN` akan dipanggil.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai minimum berdasarkan tipe data input (atau pengelompokan).

* * *

## Contoh {#examples}

### Mengembalikan nilai minimum untuk suatu kolom {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Mengembalikan nilai minimum untuk setiap nilai dalam suatu kolom {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Mengembalikan nilai minimum dari daftar nilai menggunakan fungsi skalar `min` {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Menggunakan `MIN` sebagai fungsi jendela untuk menemukan nilai minimum dalam partisi {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) atau [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
