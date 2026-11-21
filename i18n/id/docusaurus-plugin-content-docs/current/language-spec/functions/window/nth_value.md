---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela NTH_VALUE di StackQL: nilai ekspresi untuk baris ke-N dari bingkai jendela."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nilai ekspresi yang dievaluasi terhadap baris ke-N dalam bingkai jendela.

Fungsi jendela bawaan ini menghitung bingkai jendela untuk setiap baris dengan cara yang sama seperti fungsi jendela agregat. Fungsi ini mengembalikan nilai ekspresi yang dievaluasi terhadap baris N dari bingkai jendela. Baris diberi nomor di dalam bingkai jendela mulai dari 1 sesuai urutan yang ditentukan oleh klausa `ORDER BY` jika ada, atau dalam urutan sembarang jika tidak ada. Jika tidak ada baris ke-N dalam bingkai jendela, `NULL` dikembalikan.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## Sintaksis {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expr*__
Ekspresi yang dievaluasi terhadap baris ke-N dalam bingkai jendela.

__*N*__
Bilangan bulat positif yang menentukan nomor baris yang akan diambil (dimulai dari 1).

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `NTH_VALUE` diterapkan di dalam setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan pemrosesan baris.

__*ROWS BETWEEN frame_start AND frame_end*__
Opsional. Menentukan bingkai jendela.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai ekspresi yang dievaluasi terhadap baris ke-N dalam bingkai jendela, atau `NULL` jika baris ke-N tidak ada.

* * *

## Contoh {#examples}

### Mendapatkan tiga kontributor teratas {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
