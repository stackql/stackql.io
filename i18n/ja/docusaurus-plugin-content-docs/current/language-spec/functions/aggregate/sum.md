---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の SUM 集約関数：列またはグループ内の NULL 以外の値の合計（すべての入力が整数の場合は整数）。"
image: "/img/stackql-featured-image.png"
---
列または列のグループ内にある、`NULL` 以外のすべての値の合計を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

浮動小数点数や長整数値を合計するには、[**TOTAL**](/language-spec/functions/aggregate/total) 関数を使用します。

:::

## 構文 {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引数 {#arguments}

__*columnExpression*__  
列または式。

> *columnExpression* のすべての値が `NULL` の場合、`SUM` は `NULL` を返します。  

> 計算中のいずれかの時点で整数オーバーフローが発生すると、`SUM` は「integer overflow」例外を発生させます。[`TOTAL`](/language-spec/functions/aggregate/total) 関数が整数オーバーフローを発生させることはありません。  

__*groupByColumn*__  
要約操作または集約操作の対象となる 1 つ以上の列。`GROUP BY` 句は、列のグループごとに 1 行を返します。

## 戻り値 {#return-values}

`NULL` 以外の入力がすべて整数の場合は、*columnExpression* の合計を表す整数を返します。`NULL` 以外の入力のいずれかが整数でない場合は、真の合計の近似値である可能性がある浮動小数点値を返します。

* * *

## 例 {#examples}

### リソース全体で列式の合計を返す {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 別の列式でグループ化した列式の合計を返す {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### `SUM` をウィンドウ関数として使用して累計を計算する {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

詳細については、[https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc) を参照してください。
