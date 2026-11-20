---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のハイパーボリックアークコサイン（逆双曲線余弦）を計算します。
image: "/img/stackql-featured-image.png"
---
数値のハイパーボリックアークコサイン（逆双曲線余弦）を計算します。これは、ハイパーボリックコサインが指定した数値になる負でない値です。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
ハイパーボリックアークコサインを計算する、1 以上の数値式。

## 戻り値 {#return-values}
指定した値のハイパーボリックアークコサインを返します。

* * *

## 例 {#examples}

### 1 のハイパーボリックアークコサインを計算する {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

1 のハイパーボリックアークコサインは 0 であるため、このクエリは `0` を返します。

### 10 のハイパーボリックアークコサインを計算する {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

このクエリは 10 のハイパーボリックアークコサインを計算し、結果は約 `2.9932` になります。これは、ハイパーボリックコサインが 10 になる値です。
