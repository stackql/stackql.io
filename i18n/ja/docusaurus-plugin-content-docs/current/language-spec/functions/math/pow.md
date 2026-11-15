---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、ある数値を別の数値でべき乗した値を計算します。
image: "/img/stackql-featured-image.png"
---
*x* の *y* 乗を計算します。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*x*__  
底。数値式です。

__*y*__  
底 *x* を累乗する指数。こちらも数値式です。

## 戻り値 {#return-values}
*x* の *y* 乗の結果を返します。

* * *

## 例 {#examples}

### x の y 乗を計算する {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
