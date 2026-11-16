---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela LAST_VALUE di StackQL: nilai ekspresi untuk baris terakhir dari bingkai jendela."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai ekspresi yang dievaluasi terhadap baris terakhir dalam bingkai jendela.

Fungsi jendela bawaan ini menghitung bingkai jendela untuk setiap baris dengan cara yang sama seperti fungsi jendela agregat. Fungsi ini mengembalikan nilai ekspresi yang dievaluasi terhadap baris terakhir dalam bingkai jendela untuk setiap baris.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Sintaksis {#syntax}

```sql
SELECT LAST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expr*__
Ekspresi yang dievaluasi terhadap baris terakhir dalam bingkai jendela.

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `LAST_VALUE` diterapkan di dalam setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan pemrosesan baris.

__*ROWS BETWEEN frame_start AND frame_end*__
Opsional. Menentukan bingkai jendela. Opsi yang umum meliputi:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Tanpa klausa bingkai, bingkai yang digunakan adalah `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: bingkai ini berakhir pada baris terakhir yang seri dengan baris saat ini pada kolom `ORDER BY`, bukan pada baris saat ini.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai ekspresi yang dievaluasi terhadap baris terakhir dalam bingkai jendela.

* * *

## Contoh {#examples}

### Menemukan rilis terbaru sebagai konteks {#find-the-latest-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Secara default, bingkai jendela membentang dari awal partisi hingga baris terakhir yang seri dengan baris saat ini pada kolom `ORDER BY`, sehingga fungsi `LAST_VALUE` mengembalikan nilai dari baris seri terakhir tersebut (baris saat ini itu sendiri jika nilai `ORDER BY` baris itu unik), bukan nilai terakhir dalam partisi. Untuk mendapatkan nilai terakhir dalam partisi, gunakan `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
