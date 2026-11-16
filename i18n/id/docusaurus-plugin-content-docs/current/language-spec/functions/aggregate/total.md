---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi agregat TOTAL di StackQL: total nilai yang bukan NULL dalam kolom atau pengelompokan, selalu dikembalikan sebagai bilangan floating point."
image: "/img/stackql-featured-image.png"
---
Mengembalikan jumlah semua nilai yang bukan `NULL` dalam kolom atau pengelompokan kolom.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Sintaksis {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumen {#arguments}

__*columnExpression*__  
Kolom atau ekspresi.

> Jika semua nilai dalam *columnExpression* bernilai `NULL`, `TOTAL` mengembalikan `0.0`.  

__*groupByColumn*__  
Satu atau beberapa kolom yang digunakan untuk melakukan operasi ringkasan atau agregat.  Klausa `GROUP BY` mengembalikan satu baris untuk setiap pengelompokan kolom.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai floating point yang merepresentasikan jumlah *columnExpression*.

* * *

## Contoh {#examples}

### Mengembalikan jumlah ekspresi kolom pada seluruh sumber daya {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan jumlah ekspresi kolom yang dikelompokkan berdasarkan ekspresi kolom lain {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
