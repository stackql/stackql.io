---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi agregat COUNT di StackQL: jumlah baris dalam sumber daya, atau jumlah nilai non-NULL suatu kolom dalam pengelompokan."
image: "/img/stackql-featured-image.png"
---
Mengembalikan jumlah total baris dalam sumber daya atau jumlah kemunculan kolom yang bukan `NULL` dalam pengelompokan.

Lihat juga:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumen {#arguments}

__*columnExpression*__  atau __*__  
Kolom atau ekspresi. Jika __*__ ditentukan, semua baris dalam sumber daya atau pengelompokan akan dihitung.

> Nilai `NULL` dari *columnExpression* tidak dihitung.  Jika semua nilai berupa `NULL`, fungsi `COUNT` mengembalikan `0`.

__*groupByColumn*__  
Satu atau beberapa kolom yang menjadi dasar operasi ringkasan atau agregat. Klausa `GROUP BY` mengembalikan satu baris untuk setiap pengelompokan kolom.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan bulat yang menyatakan hasil hitungan.

* * *

## Contoh {#examples}

### Mengembalikan jumlah baris pada seluruh sumber daya {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan jumlah baris yang dikelompokkan berdasarkan ekspresi kolom {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Menggunakan `COUNT` sebagai fungsi jendela untuk menghitung jumlah kumulatif {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
