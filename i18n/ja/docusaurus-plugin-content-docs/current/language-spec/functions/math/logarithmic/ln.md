---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値の自然対数を計算します。
image: "/img/stackql-featured-image.png"
---
指定した数値の自然対数（e を底とする対数）を計算します。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
自然対数を計算する正の数値式。

## 戻り値 {#return-values}
指定した正の数値の自然対数を返します。

* * *

## 例 {#examples}

### 2.71828（約 e）の自然対数を計算する {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

e（約 2.71828）の自然対数は 1 であるため、このクエリは `1` に近い値を返します。

### より大きな数値の自然対数を計算する {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

このクエリは 10 の自然対数を計算し、e を底とする 10 の対数値を求めます。
