---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi agregat SUM di StackQL: total nilai yang bukan NULL dalam kolom atau pengelompokan, dikembalikan sebagai bilangan bulat jika setiap input berupa bilangan bulat."
image: "/img/stackql-featured-image.png"
---
Mengembalikan jumlah semua nilai yang bukan `NULL` dalam kolom atau pengelompokan kolom.  

Lihat juga:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Gunakan fungsi [**TOTAL**](/language-spec/functions/aggregate/total) untuk menjumlahkan bilangan floating point atau nilai long.

:::

## Sintaksis {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumen {#arguments}

__*columnExpression*__  
Kolom atau ekspresi.

> Jika semua nilai dalam *columnExpression* bernilai `NULL`, `SUM` mengembalikan `NULL`.  

> `SUM` akan menghasilkan pengecualian "integer overflow" jika terjadi luapan bilangan bulat pada titik mana pun selama komputasi.  Fungsi [`TOTAL`](/language-spec/functions/aggregate/total) tidak pernah menghasilkan pengecualian integer overflow.  

__*groupByColumn*__  
Satu atau beberapa kolom yang digunakan untuk melakukan operasi ringkasan atau agregat.  Klausa `GROUP BY` mengembalikan satu baris untuk setiap pengelompokan kolom.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan bulat yang merepresentasikan jumlah *columnExpression* jika semua input yang bukan `NULL` berupa bilangan bulat. Jika ada input yang bukan `NULL` dan bukan bilangan bulat, yang dikembalikan adalah nilai floating point yang mungkin merupakan perkiraan dari jumlah sebenarnya.

* * *

## Contoh {#examples}

### Mengembalikan jumlah ekspresi kolom pada seluruh sumber daya {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan jumlah ekspresi kolom yang dikelompokkan berdasarkan ekspresi kolom lain {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### Menggunakan `SUM` sebagai fungsi jendela untuk menghitung total berjalan {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
