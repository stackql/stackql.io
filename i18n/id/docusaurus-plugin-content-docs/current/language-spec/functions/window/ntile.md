---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "Fungsi jendela NTILE di StackQL: membagi partisi menjadi N kelompok serata mungkin dan memberi nomor kelompok setiap baris dari 1 hingga N."
image: "/img/stackql-featured-image.png"
---
Membagi partisi menjadi N kelompok serata mungkin dan menetapkan bilangan bulat antara 1 dan N untuk setiap kelompok, sesuai urutan yang ditentukan oleh klausa `ORDER BY`, atau dalam urutan sembarang jika tidak ada. Jika perlu, kelompok yang lebih besar muncul lebih dulu.

Lihat juga:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Sintaksis {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumen {#arguments}

__*N*__
Bilangan bulat positif yang menentukan jumlah kelompok (tile) yang akan dibentuk dari partisi.

__*PARTITION BY column*__
Opsional. Membagi kumpulan hasil menjadi partisi. Fungsi `NTILE` diterapkan pada setiap partisi secara terpisah.

__*ORDER BY column*__
Menentukan urutan penempatan baris ke dalam tile.

## Nilai yang dikembalikan {#return-values}

Mengembalikan bilangan bulat antara 1 dan N yang menunjukkan tile (kelompok) tempat baris saat ini berada.

* * *

## Contoh {#examples}

### Mengelompokkan kontributor ke dalam kuartil berdasarkan tingkat kontribusi {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Gunakan fungsi `NTILE` untuk membuat segmen atau tingkatan dalam data Anda. Kasus penggunaan yang umum meliputi pembagian data ke dalam kuartil (N=4), desil (N=10), atau persentil (N=100).

:::

Untuk informasi selengkapnya, lihat [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
