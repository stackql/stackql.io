---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のアークコサイン（逆余弦）を計算し、ラジアン単位の角度を返します。
image: "/img/stackql-featured-image.png"
---
数値のアークコサイン（逆余弦）を計算します。これは、コサインが指定した数値になる角度（ラジアン単位）です。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
アークコサインを計算する、-1 以上 1 以下の範囲の数値式。

## 戻り値 {#return-values}
指定した値のアークコサインをラジアン単位で返します。

* * *

## 例 {#examples}

### 0.5 のアークコサインを計算する {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

0.5 のアークコサインは π/3 ラジアン（60 度）、つまり約 1.047 ラジアンであるため、このクエリは約 `1.047` を返します。

### 1 のアークコサインを計算する {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

このクエリは 1 のアークコサインを計算します。0 ラジアンのコサインは 1 であるため、結果は `0` になります。
