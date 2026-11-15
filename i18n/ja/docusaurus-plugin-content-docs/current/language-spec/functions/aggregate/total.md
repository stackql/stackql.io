---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の TOTAL 集約関数：列またはグループ内の NULL 以外の値の合計（常に浮動小数点数）。"
image: "/img/stackql-featured-image.png"
---
列または列のグループ内にある、`NULL` 以外のすべての値の合計を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## 構文 {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引数 {#arguments}

__*columnExpression*__  
列または式。

> *columnExpression* のすべての値が `NULL` の場合、`TOTAL` は `0.0` を返します。  

__*groupByColumn*__  
要約操作または集約操作の対象となる 1 つ以上の列。`GROUP BY` 句は、列のグループごとに 1 行を返します。

## 戻り値 {#return-values}

*columnExpression* の合計を表す浮動小数点値を返します。

* * *

## 例 {#examples}

### リソース全体で列式の合計を返す {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 別の列式でグループ化した列式の合計を返す {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

詳細については、[https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc) を参照してください。
