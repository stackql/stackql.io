---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のアークサイン（逆正弦）を計算し、ラジアン単位の角度を返します。
image: "/img/stackql-featured-image.png"
---
数値のアークサイン（逆正弦）を計算します。これは、サインが指定した数値になる角度（ラジアン単位）です。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
アークサインを計算する、-1 以上 1 以下の範囲の数値式。

## 戻り値 {#return-values}
指定した値のアークサインをラジアン単位で返します。

* * *

## 例 {#examples}

### 0.5 のアークサインを計算する {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

0.5 のアークサインは π/6 ラジアンの角度に相当するため、このクエリは π/6、つまり約 `0.5236` を返します。

### -1 のアークサインを計算する {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

このクエリは -1 のアークサインを計算します。-π/2 のサインは -1 であるため、結果は -π/2、つまり約 `-1.5708` になります。
