---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi agregat AVG di StackQL: rata-rata nilai non-NULL dalam kolom atau pengelompokan."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai rata-rata dari semua nilai yang bukan `NULL` dalam kolom atau pengelompokan kolom.

Lihat juga:  
[[` SELECT `]](/language-spec/select)

* * * 

## Sintaksis {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumen {#arguments}

__*columnExpression*__  
Kolom atau ekspresi.

> Nilai string dan BLOB yang tidak dapat direpresentasikan sebagai nilai numerik diinterpretasikan sebagai `0`.

> Setidaknya satu nilai dari *columnExpression* harus bukan `NULL`; jika tidak, `NULL` dikembalikan.

__*groupByColumn*__  
Satu atau beberapa kolom yang menjadi dasar operasi ringkasan atau agregat. Klausa `GROUP BY` mengembalikan satu baris untuk setiap pengelompokan kolom.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai floating point yang merepresentasikan rata-rata *columnExpression*.

* * *

## Contoh {#examples}

### Mengembalikan rata-rata ekspresi kolom pada seluruh sumber daya {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Mengembalikan rata-rata ekspresi kolom yang dikelompokkan berdasarkan ekspresi kolom lain {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Menggunakan `AVG` sebagai fungsi jendela untuk menghitung rata-rata bergerak {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

Untuk informasi selengkapnya, lihat [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
