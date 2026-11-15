---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って任意の底で数値の対数を計算します。
image: "/img/stackql-featured-image.png"
---
指定した底で数値の対数を計算します。底 \(e\) を使用する LN と比べて、より一般的な形式の対数関数です。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*base*__  
対数の底。1 以外の正の数値式です。

__*numeric_expression*__  
対数を計算する正の数値式。

## 戻り値 {#return-values}
指定した底での、指定した数値の対数を返します。

* * *

## 例 {#examples}

### 10 を底とする 1000 の対数を計算する {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

\(10^3 = 1000\) であるため、このクエリは `3` を返します。

### 2 を底とする 16 の対数を計算する {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
このクエリは 2 を底とする 16 の対数を計算します。\(2^4 = 16\) であるため、結果は `4` になります。
