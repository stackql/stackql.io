---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って 2 を底とする数値の対数を計算します。
image: "/img/stackql-featured-image.png"
---
指定した数値の 2 を底とする対数を計算し、その値を得るために 2 を何乗する必要があるかを返します。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
2 を底とする対数を計算する正の数値式。

## 戻り値 {#return-values}
指定した数値の 2 を底とする対数を返します。

* * *

## 例 {#examples}

### 2 を底とする 16 の対数を計算する {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

2^4 = 16 であるため、このクエリは `4` を返します。

### 2 を底とする 32 の対数を計算する {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

このクエリは 2 を底とする 32 の対数を計算します。2^5 = 32 であるため、結果は `5` になります。
