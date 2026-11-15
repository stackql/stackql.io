---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って 2 つの数値（y/x）のアークタンジェントを計算し、ラジアン単位の角度を返します。
image: "/img/stackql-featured-image.png"
---
2 つの数値のアークタンジェントを計算します。具体的には、タンジェントが指定した 2 つの数値の比（y を x で割った値）になる角度 \( \theta \)（ラジアン単位）です。入力の符号を正しく扱い、正しい象限を判定します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*y*__  
点の y 座標（分子）。

__*x*__  
点の x 座標（分母）。

## 戻り値 {#return-values}
角度の正しい象限を考慮して、y/x のアークタンジェントをラジアン単位で返します。

* * *

## 例 {#examples}

### 点 (1,1) のアークタンジェントを計算する {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

点 (1,1) のアークタンジェントは第 1 象限の π/4 ラジアンの角度に相当するため、このクエリは π/4、つまり約 `0.785` を返します。

### 点 (10, -10) のアークタンジェントを計算する {#calculate-the-arc-tangent-of-the-point-10--10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

このクエリは点 (10, -10) のアークタンジェントを計算します。この角度のタンジェントは第 2 象限で -1 であるため、結果は 3π/4、つまり約 `2.356` になります。
