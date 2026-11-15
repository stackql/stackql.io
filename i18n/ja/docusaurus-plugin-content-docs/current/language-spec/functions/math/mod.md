---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、ある数値を別の数値で割った余りを計算します。
image: "/img/stackql-featured-image.png"
---
*x* を *y* で割った余りを計算し、剰余を返します。'%' 演算子の使用と似ていますが、整数以外の引数にも使用できます。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*x*__  
被除数。数値式です。

__*y*__  
除数。こちらも数値式です。

## 戻り値 {#return-values}
*x* を *y* で割った余りを返します。

* * *

## 例 {#examples}

### 10 を 3 で割った剰余を計算する {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
