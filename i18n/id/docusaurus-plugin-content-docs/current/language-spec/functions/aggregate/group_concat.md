---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi agregat GROUP_CONCAT di StackQL: menggabungkan nilai non-NULL suatu kolom atau pengelompokan menjadi satu string dengan pemisah opsional."
image: "/img/stackql-featured-image.png"
---
Mengembalikan string yang merupakan penggabungan semua nilai yang bukan `NULL` dari kolom input atau pengelompokan.

> Urutan elemen yang digabungkan tidak tentu.

Lihat juga:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumen {#arguments}

__*columnExpression*__  
Kolom atau ekspresi.

> Setidaknya satu nilai dari *columnExpression* harus bukan `NULL`; jika tidak, `NULL` dikembalikan.

__*separator*__  
Pemisah yang digunakan untuk hasil penggabungan; nilai default `,` digunakan jika *separator* tidak ditentukan.

__*groupByColumn*__  
Satu atau beberapa kolom yang menjadi dasar operasi ringkasan atau agregat. Klausa `GROUP BY` mengembalikan satu baris untuk setiap pengelompokan kolom.

## Nilai yang dikembalikan {#return-values}

Mengembalikan string berupa daftar nilai yang dipisahkan oleh *separator* atau `,`.

* * *

## Contoh {#examples}

### Mengembalikan daftar nilai suatu kolom pada seluruh sumber daya {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan daftar nilai ekspresi kolom yang dikelompokkan berdasarkan ekspresi kolom lain {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
