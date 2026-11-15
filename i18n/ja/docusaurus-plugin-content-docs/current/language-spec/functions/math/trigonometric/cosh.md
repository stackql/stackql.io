---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のハイパーボリックコサインを計算し、結果を返します。
image: "/img/stackql-featured-image.png"
---
指定した値のハイパーボリックコサインを計算します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
ハイパーボリックコサインを計算する数値式。

## 戻り値 {#return-values}
指定した値のハイパーボリックコサインを返します。

* * *

## 例 {#examples}

### 1 のハイパーボリックコサインを計算する {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

1 のハイパーボリックコサインは約 1.5431 であるため、このクエリは約 `1.5431` を返します。

### 0 のハイパーボリックコサインを計算する {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

このクエリは 0 のハイパーボリックコサインを計算します。0 のハイパーボリックコサインはちょうど 1 であるため、結果は `1` になります。
