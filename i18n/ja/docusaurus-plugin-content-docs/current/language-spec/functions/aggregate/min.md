---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の MIN 集約関数：列または列のグループ内の最小値。"
image: "/img/stackql-featured-image.png"
---
列の入力または列のグループに基づいて最小値を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

### 集約関数 {#aggregate-function}

__*columnExpression*__  
演算子（`+`、`-`、...）を含む列または式。

> `MIN` 関数は、列のデータ型に基づいて、指定された列の最小値を返します。たとえば、整数の列に対する `MIN` 操作は、その列または列のグループの最小の整数を返します。一方、文字列の列または列のグループに対する `MIN` 操作は、並べ替えたときに最初になる値を返します。文字列の比較には `BINARY` 照合関数が使用されます（バイト単位で比較されるため、大文字は小文字より前に並びます）。

__*groupByColumn*__  
要約操作または集約操作の対象となる 1 つ以上の列。`GROUP BY` 句は、列のグループごとに 1 行を返します。

> `MIN` 関数は `NULL` 値を無視します。

> グループ内のすべての値が `NULL` の場合、`MIN` 関数は `NULL` を返します。

### スカラー関数 {#scalar-function}

__*scalarExpression*__  
最小値を決定して返す対象となる列または式のリスト（2 つ以上）。 

> いずれかの引数が `NULL` の場合、スカラー `MIN` 関数は `NULL` を返します。 

> スカラー（複数引数）の `MIN` 関数は、照合関数を定義する引数を左から右へ検索し、その照合関数をすべての文字列比較に使用します。 

> スカラー `MIN` 関数のどの引数も照合関数を定義していない場合は、`BINARY` 照合関数が使用されます。 

> 引数が 1 つだけ指定された場合は、`MIN` 集約関数が呼び出されます。

## 戻り値 {#return-values}

入力のデータ型（またはグループ）に基づいて最小値を返します。

* * *

## 例 {#examples}

### 列の最小値を返す {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### 列の値ごとに最小値を返す {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### スカラー `min` 関数を使用して値のリストから最小値を返す {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### `MIN` をウィンドウ関数として使用してパーティション内の最小値を求める {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

詳細については、[https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) または [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar) を参照してください。
