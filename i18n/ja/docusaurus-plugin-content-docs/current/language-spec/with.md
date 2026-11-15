---
title: WITH (CTEs)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の WITH：単一の SELECT、INSERT、UPDATE、DELETE 文をスコープとする、名前付きの一時的な結果セットである共通テーブル式（CTE）を定義します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

後続の `SELECT`、`INSERT`、`UPDATE`、`DELETE` 文で使用する共通テーブル式（CTE）を定義します。CTE は、クエリの実行中にのみ存在する、名前付きの一時的な結果セットとして機能します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## 構文 {#syntax}

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

## 引数 {#arguments}

__*RECURSIVE*__
CTE が自身を参照できるようにする省略可能なキーワードです。階層データの走査などの再帰クエリが可能になります。

__*cte_name*__
CTE に割り当てる名前です。この名前は、メインクエリでテーブルと同じように参照できます。

__*column_name*__
省略可能。CTE の結果セットの列名です。指定しない場合、列名は SELECT 文から導出されます。

__*select_statement*__
CTE の結果セットを定義するクエリです。

## 戻り値 {#return-values}

CTE は値を直接返しません。メインクエリで参照できる一時的な結果セットを定義します。

* * *

## 例 {#examples}

### 複雑なクエリを整理するための基本的な CTE {#basic-cte-for-organizing-complex-queries}

CTE を使用すると、複雑なクエリを名前付きの論理的な構成要素に分割して簡素化できます。

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

### `UNION ALL` で複数のソースのデータを組み合わせる {#combining-data-from-multiple-sources-with-union-all}

CTE は、分析を行う前に複数のソースのデータを組み合わせる場合に便利です。

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

### ウィンドウ関数と CTE を使用した順位付け {#using-ctes-with-window-functions-for-ranking}

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

### 1 つのクエリ内の複数の CTE {#multiple-ctes-in-a-single-query}

複数の CTE をカンマで区切って定義できます。

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

## 再帰 CTE {#recursive-ctes}

再帰 CTE を使用すると、CTE が自身を参照できるため、階層構造やグラフ構造のデータをたどるクエリを記述できます。

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

再帰 CTE は、組織図、ファイルシステム、依存関係ツリーなどの階層データに対して強力です。無限ループを防ぐため、再帰部には必ず終了条件を含めてください。

:::

## ベストプラクティス {#best-practices}

1. **読みやすさのために CTE を使用する**：複雑なクエリを名前付きの論理的な構成要素に分割します。
2. **ウィンドウ関数と組み合わせる**：CTE はウィンドウ関数と相性がよく、高度な分析に役立ちます。
3. **複数の CTE を連鎖させる**：後の CTE で前の CTE を参照し、複雑なロジックを段階的に組み立てます。
4. **パフォーマンスを考慮する**：CTE は読みやすさを向上させますが、非常に大きな CTE はパフォーマンスに影響する場合があります。実際のデータ量でテストしてください。

詳細については、[https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html) を参照してください。
