---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、数値のハイパーボリックタンジェントを計算して結果を返します。
image: "/img/stackql-featured-image.png"
---
指定した値のハイパーボリックタンジェントを計算します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
ハイパーボリックタンジェントを計算する数値式です。

## 戻り値 {#return-values}
指定した値のハイパーボリックタンジェントを返します。

* * *

## 例 {#examples}

### 1 のハイパーボリックタンジェントを計算する {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

1 のハイパーボリックタンジェントは約 0.7616 であるため、このクエリはおよそ `0.7616` を返します。

### 0 のハイパーボリックタンジェントを計算する {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

このクエリは 0 のハイパーボリックタンジェントを計算します。0 のハイパーボリックタンジェントはちょうど 0 であるため、結果は `0` になります。
