---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の AVG 集約関数：列またはグループ内の NULL 以外の値の平均。"
image: "/img/stackql-featured-image.png"
---
列または列のグループ内にある、`NULL` 以外のすべての値の平均値を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引数 {#arguments}

__*columnExpression*__  
列または式。

> 数値として表現できない文字列値と BLOB 値は `0` として解釈されます。  

> *columnExpression* の値のうち少なくとも 1 つが `NULL` 以外である必要があります。そうでない場合は `NULL` が返されます。

__*groupByColumn*__  
要約操作または集約操作の対象となる 1 つ以上の列。`GROUP BY` 句は、列のグループごとに 1 行を返します。

## 戻り値 {#return-values}

*columnExpression* の平均を表す浮動小数点値を返します。

* * *

## 例 {#examples}

### リソース全体で列式の平均を返す {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 別の列式でグループ化した列式の平均を返す {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### `AVG` をウィンドウ関数として使用して移動平均を計算する {#use-avg-as-a-window-function-to-calculate-moving-averages}

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
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

詳細については、[https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg) を参照してください。
