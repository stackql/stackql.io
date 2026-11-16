---
title: Fungsi jendela
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela di StackQL: peringkat, total berjalan, offset, dan distribusi pada baris yang saling terkait, dengan klausa PARTITION BY, ORDER BY, dan klausa bingkai."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Fungsi jendela melakukan perhitungan pada sekumpulan baris yang berkaitan dengan baris saat ini. Tidak seperti fungsi agregat yang mengembalikan satu hasil untuk sekelompok baris, fungsi jendela mengembalikan nilai untuk setiap baris dengan mempertimbangkan "jendela" berisi baris yang berkaitan.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` WITH (CTE) `]](/language-spec/with) [[ Referensi fungsi jendela ]](/language-spec/functions/window/row_number)

* * *

## Sintaksis {#syntax}

Fungsi jendela menggunakan klausa `OVER` untuk mendefinisikan spesifikasi jendela:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Fungsi jendela {#window-function}

Fungsi jendela adalah fungsi khusus yang melakukan perhitungan pada sekumpulan baris yang ditentukan oleh spesifikasi jendela. Fungsi ini dapat berupa fungsi jendela tersendiri (seperti `ROW_NUMBER` atau `LAG`) atau fungsi agregat (seperti `SUM` atau `COUNT`) yang digunakan dengan klausa `OVER`.

```sql
{ <aggregateFunction> | ROW_NUMBER() | RANK() | DENSE_RANK() | PERCENT_RANK() 
  | CUME_DIST() | NTILE(<int>) | LAG(<expr> [,<offset> [,<default>]]) 
  | LEAD(<expr> [,<offset> [,<default>]]) | FIRST_VALUE(<expr>) 
  | LAST_VALUE(<expr>) | NTH_VALUE(<expr>, <int>) }
```  

*windowFunction::=*

<RailroadDiagram 
type="windowFunction"
/>

### Spesifikasi jendela {#window-specification}

Spesifikasi jendela menentukan cara baris dipartisi, diurutkan, dan dibingkai untuk perhitungan fungsi jendela. `PARTITION BY` membagi baris ke dalam kelompok, `ORDER BY` menentukan urutan di dalam setiap partisi, dan klausa bingkai opsional menentukan baris mana, relatif terhadap baris saat ini, yang disertakan dalam perhitungan.

```sql
[ <windowName> ]
[ PARTITION BY <fieldList> ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ { ROWS | RANGE | GROUPS } 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW }
  | { ROWS | RANGE | GROUPS } BETWEEN 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW | <int> FOLLOWING }
    AND
    { <int> PRECEDING | CURRENT ROW | <int> FOLLOWING | UNBOUNDED FOLLOWING } ]
```

*windowSpec::=*

<RailroadDiagram 
type="windowSpec"
/>

## Fungsi jendela yang tersedia {#available-window-functions}

| Kategori | Fungsi |
|----------|-----------|
| **Pemeringkatan** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Offset** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Distribusi** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Agregat** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) dengan klausa __`OVER`__ |

* * *

## Contoh {#examples}

### Memberi peringkat dengan `ROW_NUMBER`, `RANK`, dan `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Gunakan fungsi pemeringkatan untuk memberikan peringkat pada baris berdasarkan nilai kolom.

```sql
-- Rank contributors by contribution count
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Total berjalan dan persentase {#running-totals-and-percentages}

Fungsi agregat seperti `SUM` dan `COUNT` dapat digunakan sebagai fungsi jendela untuk menghitung total berjalan dan persentase.

```sql
-- Running total and percentage of contributions
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

### Membandingkan baris dengan `LAG` dan `LEAD` {#comparing-rows-with-lag-and-lead}

Gunakan `LAG` dan `LEAD` untuk mengakses nilai dari baris sebelumnya atau berikutnya.

```sql
-- Compare each release to previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Membuat kelompok dengan `NTILE` {#creating-groups-with-ntile}

Gunakan `NTILE` untuk membagi baris ke dalam sejumlah kelompok yang ditentukan.

```sql
-- Group contributors into quartiles
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Menggunakan `PARTITION BY` untuk analisis per kelompok {#using-partition-by-for-grouped-analysis}

Gunakan `PARTITION BY` untuk melakukan perhitungan jendela di dalam kelompok.

```sql
-- Track cumulative issues by state
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Menggunakan bingkai jendela {#using-window-frames}

Tentukan klausa bingkai untuk mengontrol baris mana yang disertakan dalam perhitungan jendela.

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
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg,
    SUM(commits_this_week) OVER (ORDER BY week) as cumulative_commits
FROM weekly_totals
ORDER BY week;
```

Untuk dokumentasi terperinci tentang setiap fungsi jendela, lihat [Referensi fungsi jendela](/language-spec/functions/window/row_number).

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
