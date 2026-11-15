---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のハイパーボリックアークタンジェント（逆双曲線正接）を計算し、ラジアン単位の角度を返します。
image: "/img/stackql-featured-image.png"
---
数値のハイパーボリックアークタンジェント（逆双曲線正接）を計算します。これは、ハイパーボリックタンジェントが指定した数値になる値 \( \theta \)（ラジアン単位）です。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
ハイパーボリックアークタンジェントを計算する、-1 より大きく 1 より小さい範囲の数値式。

## 戻り値 {#return-values}
指定した値のハイパーボリックアークタンジェントをラジアン単位で返します。

* * *

## 例 {#examples}

### 0.5 のハイパーボリックアークタンジェントを計算する {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

0.5 のハイパーボリックアークタンジェントは約 0.5493 ラジアンであるため、このクエリは約 `0.5493` を返します。

### -0.5 のハイパーボリックアークタンジェントを計算する {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

このクエリは -0.5 のハイパーボリックアークタンジェントを計算し、結果は約 `-0.5493` になります。これは、ハイパーボリックタンジェントが -0.5 になるラジアン単位の値です。
