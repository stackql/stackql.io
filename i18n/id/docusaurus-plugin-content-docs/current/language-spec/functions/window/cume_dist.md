---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela CUME_DIST di StackQL: distribusi kumulatif baris saat ini dalam partisinya, yaitu nomor baris dibagi jumlah baris partisi."
image: "/img/stackql-featured-image.png"
---
Mengembalikan distribusi kumulatif suatu nilai dalam partisi. Nilai ini dihitung sebagai `row-number / partition-rows`, dengan `row-number` adalah nilai yang dikembalikan oleh `ROW_NUMBER()` untuk baris setara terakhir dalam kelompok dan `partition-rows` adalah jumlah baris dalam partisi.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaksis {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `CUME_DIST` diterapkan pada setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan perhitungan distribusi kumulatif.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan floating point antara 0.0 dan 1.0 yang menyatakan nilai distribusi kumulatif.

* * *

## Contoh {#examples}

### Menghitung distribusi kumulatif kontributor {#calculate-cumulative-distribution-of-contributors}

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

Fungsi `CUME_DIST` mengembalikan proporsi baris yang nilainya kurang dari atau sama dengan nilai baris saat ini. Nilai 0.75 berarti 75% baris memiliki nilai yang kurang dari atau sama dengan nilai baris saat ini.

:::

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
