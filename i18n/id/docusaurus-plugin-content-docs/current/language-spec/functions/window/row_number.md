---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela ROW_NUMBER di StackQL: memberi nomor pada baris dalam partisi mulai dari 1 sesuai urutan klausa ORDER BY milik jendela."
image: "/img/stackql-featured-image.png"
---
Mengembalikan nomor baris dalam partisi saat ini. Baris diberi nomor mulai dari 1 sesuai urutan yang ditentukan oleh klausa `ORDER BY` dalam definisi jendela, atau dalam urutan sembarang jika tidak ada.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Sintaksis {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `ROW_NUMBER` diterapkan pada setiap partisi secara terpisah dan mengatur ulang nomor baris untuk setiap partisi.

__*ORDER BY column*__
Menentukan urutan pemberian nomor baris. Diperlukan agar hasilnya bermakna.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan bulat yang menyatakan nomor urut baris dalam partisi, dimulai dari 1.

* * *

## Contoh {#examples}

### Memberi peringkat kontributor dalam satu repositori {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Melacak urutan issue dari waktu ke waktu {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Membandingkan `ROW_NUMBER` dengan `RANK` dan `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
