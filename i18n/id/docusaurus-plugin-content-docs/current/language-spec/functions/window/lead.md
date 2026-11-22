---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela LEAD di StackQL: mengevaluasi ekspresi terhadap baris berikutnya dalam partisi, dengan offset dan nilai default opsional."
image: "/img/stackql-featured-image.png"
---
Mengembalikan hasil evaluasi ekspresi terhadap baris selanjutnya dalam partisi.

Bentuk pertama fungsi `LEAD()` mengembalikan hasil evaluasi ekspresi terhadap baris berikutnya dalam partisi. Atau, jika tidak ada baris berikutnya (karena baris saat ini adalah baris terakhir), `NULL` dikembalikan.

Jika argumen offset diberikan, argumen tersebut harus berupa bilangan bulat nonnegatif. Nilai yang dikembalikan adalah hasil evaluasi ekspresi terhadap baris yang berada *offset* baris setelah baris saat ini di dalam partisi. Jika offset bernilai 0, ekspresi dievaluasi terhadap baris saat ini. Jika tidak ada baris yang berada *offset* baris setelah baris saat ini, `NULL` dikembalikan.

Jika nilai default juga diberikan, nilai tersebut dikembalikan sebagai pengganti `NULL` jika baris yang ditunjuk oleh offset tidak ada.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Sintaksis {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expr*__
Ekspresi yang dievaluasi terhadap baris selanjutnya.

__*offset*__
Opsional. Bilangan bulat nonnegatif yang menentukan berapa baris ke depan yang dilihat. Nilai default: 1.

__*default*__
Opsional. Nilai yang dikembalikan jika baris pada offset tersebut tidak ada. Nilai default: `NULL`.

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `LEAD` diterapkan di dalam setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan pemrosesan baris.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai ekspresi yang dievaluasi terhadap baris selanjutnya yang ditentukan, atau `NULL` (atau nilai default) jika baris tersebut tidak ada.

* * *

## Contoh {#examples}

### Melihat ke rilis berikutnya {#look-ahead-to-the-next-release}

```sql
-- Compare each release to the previous and next release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
