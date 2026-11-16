---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のハイパーボリックアークサイン（逆双曲線正弦）を計算し、ラジアン単位の角度を返します。
image: "/img/stackql-featured-image.png"
---
数値のハイパーボリックアークサイン（逆双曲線正弦）を計算します。これは、ハイパーボリックサインが指定した数値になる値（ラジアン単位）です。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
ハイパーボリックアークサインを計算する数値式。

## 戻り値 {#return-values}
指定した値のハイパーボリックアークサインをラジアン単位で返します。

* * *

## 例 {#examples}

### 0.5 のハイパーボリックアークサインを計算する {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

0.5 のハイパーボリックアークサインは約 0.4812 ラジアンであるため、このクエリは約 `0.4812` を返します。

### -1 のハイパーボリックアークサインを計算する {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

このクエリは -1 のハイパーボリックアークサインを計算し、結果は約 `-0.8814` になります。これは、ハイパーボリックサインが -1 になるラジアン単位の値です。
