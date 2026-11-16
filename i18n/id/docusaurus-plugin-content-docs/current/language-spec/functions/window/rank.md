---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela RANK di StackQL: peringkat baris saat ini dalam partisinya dengan celah, sehingga baris yang seri berbagi peringkat dan peringkat sesudahnya dilewati."
image: "/img/stackql-featured-image.png"
---
Mengembalikan peringkat baris saat ini dengan celah. Peringkat ini adalah `ROW_NUMBER` dari baris setara pertama dalam setiap kelompok. Baris dengan nilai yang sama pada kolom pengurutan menerima peringkat yang sama, dan nilai peringkat berikutnya bertambah sebanyak jumlah baris yang seri (meninggalkan celah).

Jika tidak ada klausa `ORDER BY`, semua baris dianggap sebagai baris setara dan fungsi ini selalu mengembalikan 1.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Sintaksis {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `RANK` diterapkan pada setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan pemberian peringkat. Jika tidak ada klausa `ORDER BY`, semua baris dianggap sebagai baris setara dan menerima peringkat 1.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan bulat yang menyatakan peringkat baris saat ini dalam partisi, dengan celah untuk nilai yang seri.

* * *

## Contoh {#examples}

### Memberi peringkat kontributor berdasarkan kontribusi (dengan celah) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Membandingkan `RANK` dengan `DENSE_RANK` dan `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### Memberi peringkat kontributor di setiap repositori menggunakan `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
