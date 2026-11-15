---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数値のアークタンジェント（逆正接）を計算し、ラジアン単位の角度を返します。
image: "/img/stackql-featured-image.png"
---
数値のアークタンジェント（逆正接）を計算します。これは、タンジェントが指定した数値になる角度（ラジアン単位）です。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*value*__  
アークタンジェントを計算する数値式。

## 戻り値 {#return-values}
指定した値のアークタンジェントをラジアン単位で返します。

* * *

## 例 {#examples}

### 1 のアークタンジェントを計算する {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

1 のアークタンジェントは π/4 ラジアンであるため、このクエリは π/4、つまり約 `0.785` を返します。

### 0 のアークタンジェントを計算する {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

このクエリは 0 のアークタンジェントを計算します。0 ラジアンのタンジェントは 0 であるため、結果は `0` になります。
