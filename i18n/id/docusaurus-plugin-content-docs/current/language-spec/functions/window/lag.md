---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela LAG di StackQL: mengevaluasi ekspresi terhadap baris sebelumnya dalam partisi, dengan offset dan nilai default opsional."
image: "/img/stackql-featured-image.png"
---
Mengembalikan hasil evaluasi ekspresi terhadap baris sebelumnya dalam partisi.

Bentuk pertama fungsi `LAG()` mengembalikan hasil evaluasi ekspresi terhadap baris sebelumnya dalam partisi. Atau, jika tidak ada baris sebelumnya (karena baris saat ini adalah baris pertama), `NULL` dikembalikan.

Jika argumen offset diberikan, argumen tersebut harus berupa bilangan bulat nonnegatif. Nilai yang dikembalikan adalah hasil evaluasi ekspresi terhadap baris yang berada *offset* baris sebelum baris saat ini di dalam partisi. Jika offset bernilai 0, ekspresi dievaluasi terhadap baris saat ini. Jika tidak ada baris yang berada *offset* baris sebelum baris saat ini, `NULL` dikembalikan.

Jika nilai default juga diberikan, nilai tersebut dikembalikan sebagai pengganti `NULL` jika baris yang ditunjuk oleh offset tidak ada.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Sintaksis {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*expr*__
Ekspresi yang dievaluasi terhadap baris sebelumnya.

__*offset*__
Opsional. Bilangan bulat nonnegatif yang menentukan berapa baris ke belakang yang dilihat. Nilai default: 1.

__*default*__
Opsional. Nilai yang dikembalikan jika baris pada offset tersebut tidak ada. Nilai default: `NULL`.

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `LAG` diterapkan di dalam setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan pemrosesan baris.

## Nilai yang dikembalikan {#return-values}

Mengembalikan nilai ekspresi yang dievaluasi terhadap baris sebelumnya yang ditentukan, atau `NULL` (atau nilai default) jika baris tersebut tidak ada.

* * *

## Contoh {#examples}

### Membandingkan setiap rilis dengan rilis sebelumnya {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to the previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Menganalisis selang waktu antara pembuatan issue {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
