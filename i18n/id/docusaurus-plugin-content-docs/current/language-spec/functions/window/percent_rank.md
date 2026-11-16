---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela PERCENT_RANK di StackQL: peringkat relatif baris saat ini dalam partisinya sebagai nilai antara 0.0 dan 1.0."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai antara 0.0 dan 1.0 yang menyatakan peringkat relatif baris saat ini dalam partisinya.

Terlepas dari namanya, fungsi ini mengembalikan nilai yang sama dengan `(rank - 1) / (partition-rows - 1)`, dengan `rank` adalah nilai yang dikembalikan oleh fungsi jendela bawaan `RANK()` dan `partition-rows` adalah jumlah total baris dalam partisi. Jika partisi hanya berisi satu baris, fungsi ini mengembalikan 0.0.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Sintaksis {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `PERCENT_RANK` diterapkan pada setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan perhitungan peringkat persentil.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan floating point antara 0.0 dan 1.0 yang menyatakan peringkat persentil.

* * *

## Contoh {#examples}

### Menghitung peringkat persentil kontributor {#calculate-percentile-ranking-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

Nilai `PERCENT_RANK` sebesar 0.0 menunjukkan baris dengan peringkat terendah, sedangkan 1.0 menunjukkan baris dengan peringkat tertinggi. Gunakan fungsi ini untuk mengetahui posisi suatu nilai dalam sebuah distribusi.

:::

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
