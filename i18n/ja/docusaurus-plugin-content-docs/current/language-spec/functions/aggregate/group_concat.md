---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の GROUP_CONCAT 集約関数：列またはグループの NULL 以外の値を、省略可能な区切り文字で 1 つの文字列に連結します。"
image: "/img/stackql-featured-image.png"
---
入力列またはグループの `NULL` 以外のすべての値を連結した文字列を返します。  

> 連結される要素の順序は不定です。

関連項目：  
[[` SELECT `]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引数 {#arguments}

__*columnExpression*__  
列または式。  

> *columnExpression* の値のうち少なくとも 1 つが `NULL` 以外である必要があります。そうでない場合は `NULL` が返されます。

__*separator*__  
連結結果に使用する区切り文字。*separator* を指定しない場合は、デフォルトの `,` が使用されます。

__*groupByColumn*__  
要約操作または集約操作の対象となる 1 つ以上の列。`GROUP BY` 句は、列のグループごとに 1 行を返します。

## 戻り値 {#return-values}

*separator* または `,` で区切られた値のリストとして文字列を返します。

* * *

## 例 {#examples}

### リソース全体で列の値のリストを返す {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 別の列式でグループ化した列式の値のリストを返す {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

詳細については、[https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat) を参照してください。
