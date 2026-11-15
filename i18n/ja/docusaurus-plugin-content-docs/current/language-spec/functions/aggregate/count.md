---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の COUNT 集約関数：リソース内の行数、またはグループ内の列の NULL 以外の値の数。"
image: "/img/stackql-featured-image.png"
---
リソース内の行の総数、またはグループ内の列で `NULL` ではないインスタンスの数を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引数 {#arguments}

__*columnExpression*__ または __*__  
列または式。__*__ を指定すると、リソースまたはグループ内のすべての行がカウントされます。  

> *columnExpression* の値のうち少なくとも 1 つが `NULL` 以外である必要があります。そうでない場合は `NULL` が返されます。

__*groupByColumn*__  
要約操作または集約操作の対象となる 1 つ以上の列。`GROUP BY` 句は、列のグループごとに 1 行を返します。

## 戻り値 {#return-values}

カウントを表す整数を返します。

* * *

## 例 {#examples}

### リソース全体の行数を返す {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 列式でグループ化した行数を返す {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT tags, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### `COUNT` をウィンドウ関数として使用して累積カウントを計算する {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

詳細については、[https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count) を参照してください。
