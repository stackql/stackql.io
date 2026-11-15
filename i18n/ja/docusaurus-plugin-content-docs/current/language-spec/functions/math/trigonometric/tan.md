---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、ラジアン単位で指定した角度のタンジェントを計算します。
image: "/img/stackql-featured-image.png"
---
ラジアン単位で指定した角度のタンジェントを計算します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*radian_value*__  
タンジェントを計算する角度をラジアン単位で指定する数値式です。

## 戻り値 {#return-values}
指定した角度のタンジェントを返します。

* * *

## 例 {#examples}

### π/4 ラジアンのタンジェントを計算する {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

π/4 ラジアンのタンジェントは 1 であるため、このクエリは `1` を返します。

### π ラジアンのタンジェントを計算する {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

このクエリは π ラジアンのタンジェントを計算します。π のタンジェントは 0 にきわめて近いため、結果はおよそ `0` になります。
