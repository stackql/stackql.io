---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela DENSE_RANK di StackQL: peringkat baris saat ini dalam partisinya tanpa celah, sehingga baris yang seri berbagi peringkat dan peringkat berikutnya langsung menyusul."
image: "/img/stackql-featured-image.png"
---
Mengembalikan peringkat baris saat ini tanpa celah. Peringkat ini adalah nomor kelompok baris setara dari baris saat ini dalam partisinya. Baris dengan nilai yang sama pada kolom pengurutan menerima peringkat yang sama, dan nilai peringkat berikutnya selalu bertambah 1 (tanpa celah).

Baris diberi nomor mulai dari 1 sesuai urutan yang ditentukan oleh klausa `ORDER BY` dalam definisi jendela. Jika tidak ada klausa `ORDER BY`, semua baris dianggap sebagai baris setara dan fungsi ini selalu mengembalikan 1.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaksis {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `DENSE_RANK` diterapkan pada setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan pemberian peringkat. Jika tidak ada klausa `ORDER BY`, semua baris dianggap sebagai baris setara dan menerima peringkat 1.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan bulat yang menyatakan peringkat padat baris saat ini dalam partisi, tanpa celah untuk nilai yang seri.

* * *

## Contoh {#examples}

### Memberi peringkat kontributor berdasarkan kontribusi (tanpa celah) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### Membandingkan `DENSE_RANK` dengan `RANK` dan `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Memberi peringkat kontribusi gabungan dari beberapa repositori {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
