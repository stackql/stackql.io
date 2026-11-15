---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のハイパーボリックサインを計算し、結果を返します。
image: "/img/stackql-featured-image.png"
---
指定した値のハイパーボリックサインを計算します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
ハイパーボリックサインを計算する数値式。

## 戻り値 {#return-values}
指定した値のハイパーボリックサインを返します。

* * *

## 例 {#examples}

### 1 のハイパーボリックサインを計算する {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

1 のハイパーボリックサインは約 1.1752 であるため、このクエリは約 `1.1752` を返します。

### 0 のハイパーボリックサインを計算する {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

このクエリは 0 のハイパーボリックサインを計算します。0 のハイパーボリックサインはちょうど 0 であるため、結果は `0` になります。
