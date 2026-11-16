---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って 10 を底とする数値の対数を計算します。
image: "/img/stackql-featured-image.png"
---
指定した数値の 10 を底とする対数を計算します。これは、その値を得るために 10 を何乗する必要があるかを示します。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
10 を底とする対数を計算する正の数値式。

## 戻り値 {#return-values}
指定した数値の 10 を底とする対数を返します。

* * *

## 例 {#examples}

### 10 を底とする 100 の対数を計算する {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

10^2 = 100 であるため、このクエリは `2` を返します。

### 10 を底とする 1000 の対数を計算する {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

このクエリは 10 を底とする 1000 の対数を計算します。10^3 = 1000 であるため、結果は `3` になります。
