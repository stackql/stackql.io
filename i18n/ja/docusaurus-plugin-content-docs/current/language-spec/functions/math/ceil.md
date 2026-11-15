---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の CEIL 関数：数値引数を最も近い整数に切り上げます。"
image: "/img/stackql-featured-image.png"
---
数値引数を最も近い整数に切り上げます。  

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
真数値または概数値のデータ型カテゴリに属する式。

## 戻り値 {#return-values}
指定した数値式以上の最小の整数を返します。

* * *

## 例 {#examples}

### 浮動小数点数を最も近い（大きい方の）整数に切り上げる {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
