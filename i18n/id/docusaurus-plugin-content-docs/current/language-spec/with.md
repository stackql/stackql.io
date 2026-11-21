---
title: WITH (CTE)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: "WITH di StackQL: mendefinisikan ekspresi tabel umum (CTE), kumpulan hasil sementara bernama yang cakupannya terbatas pada pernyataan SELECT yang mengikutinya."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Mendefinisikan ekspresi tabel umum (CTE) untuk digunakan dalam pernyataan `SELECT` yang mengikutinya. CTE berfungsi sebagai kumpulan hasil sementara bernama yang hanya ada selama kueri berlangsung.

Lihat juga:
[[` SELECT `]](/language-spec/select)

* * *

## Sintaksis {#syntax}

*withClause::=*

<RailroadDiagram
type="withClause"
/>

*cteDefinition::=*

<RailroadDiagram
type="cteDefinition"
/>

&nbsp;
&nbsp;

```sql
WITH [RECURSIVE] cte_name [(column_name, ...)] AS (
    select_statement
)
[, cte_name [(column_name, ...)] AS (
    select_statement
)]
SELECT ... FROM cte_name ...;
```

## Argumen {#arguments}

__*RECURSIVE*__  
Kata kunci opsional yang memungkinkan CTE mereferensikan dirinya sendiri, sehingga memungkinkan kueri rekursif seperti menelusuri data hierarkis.

__*cte_name*__  
Nama yang diberikan untuk CTE. Nama ini dapat direferensikan dalam kueri utama seolah-olah merupakan tabel.

__*column_name*__  
Opsional. Nama kolom untuk kumpulan hasil CTE. Jika tidak ditentukan, nama kolom diturunkan dari pernyataan SELECT.

__*select_statement*__  
Kueri yang mendefinisikan kumpulan hasil CTE.

## Nilai yang dikembalikan {#return-values}

CTE tidak mengembalikan nilai secara langsung. CTE mendefinisikan kumpulan hasil sementara yang dapat direferensikan dalam kueri utama.

* * *

## Contoh {#examples}

### CTE dasar untuk mengorganisasi kueri yang kompleks {#basic-cte-for-organizing-complex-queries}

CTE dapat menyederhanakan kueri yang kompleks dengan memecahnya menjadi komponen logis yang bernama.

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg,
    SUM(commits_this_week) OVER (ORDER BY week) as cumulative_commits
FROM weekly_totals
ORDER BY week;
```

### Menggabungkan data dari beberapa sumber dengan `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

CTE berguna untuk menggabungkan data dari beberapa sumber sebelum melakukan analisis.

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

### Menggunakan CTE dengan fungsi jendela untuk pemeringkatan {#using-ctes-with-window-functions-for-ranking}

```sql
-- Rank contributors within each repo using CTE and window functions
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo,
        SUM(contributions) OVER (PARTITION BY repo) as repo_total_contributions
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

### Beberapa CTE dalam satu kueri {#multiple-ctes-in-a-single-query}

Anda dapat mendefinisikan beberapa CTE yang dipisahkan dengan koma.

```sql
-- Analyze issues and releases together
WITH open_issues AS (
    SELECT number, title, created_at
    FROM github.issues.issues
    WHERE owner = 'stackql'
      AND repo = 'stackql'
      AND state = 'open'
),
recent_releases AS (
    SELECT tag_name, published_at
    FROM github.repos.releases
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    ORDER BY published_at DESC
    LIMIT 5
)
SELECT
    'Open Issues' as category,
    COUNT(*) as count
FROM open_issues
UNION ALL
SELECT
    'Recent Releases' as category,
    COUNT(*) as count
FROM recent_releases;
```

## CTE rekursif {#recursive-ctes}

CTE rekursif memungkinkan CTE mereferensikan dirinya sendiri, sehingga memungkinkan kueri yang menelusuri struktur data hierarkis atau berbentuk graf.

```sql
WITH RECURSIVE cte_name (columns) AS (
    -- Base case (anchor member)
    SELECT ...
    UNION ALL
    -- Recursive case (references cte_name)
    SELECT ... FROM cte_name WHERE ...
)
SELECT * FROM cte_name;
```

:::tip

CTE rekursif sangat berguna untuk data hierarkis seperti bagan organisasi, sistem file, atau pohon dependensi. Selalu sertakan kondisi penghentian dalam kasus rekursif untuk mencegah perulangan tanpa akhir.

:::

## Praktik terbaik {#best-practices}

1. **Gunakan CTE untuk keterbacaan**: pecah kueri yang kompleks menjadi komponen logis yang bernama.
2. **Gabungkan dengan fungsi jendela**: CTE bekerja dengan baik bersama fungsi jendela untuk analitik tingkat lanjut.
3. **Rangkaikan beberapa CTE**: referensikan CTE sebelumnya di CTE berikutnya untuk membangun logika yang kompleks langkah demi langkah.
4. **Pertimbangkan performa**: meskipun CTE meningkatkan keterbacaan, CTE yang sangat besar dapat memengaruhi performa. Uji dengan volume data Anda yang sebenarnya.

Untuk informasi selengkapnya, lihat [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
