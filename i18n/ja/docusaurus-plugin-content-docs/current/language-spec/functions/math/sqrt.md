---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値の平方根を計算します。
image: "/img/stackql-featured-image.png"
---
負でない数値式の平方根を計算します。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
平方根を計算する、負でない数値式。

## 戻り値 {#return-values}
指定した負でない数値の平方根を返します。

* * *

## 例 {#examples}

### 数値の平方根を計算する {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
