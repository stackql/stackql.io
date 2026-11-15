---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、ラジアン単位で指定した角度のサインを計算します。
image: "/img/stackql-featured-image.png"
---
ラジアン単位で指定した角度のサインを計算します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*radian_value*__  
サインを計算する角度をラジアン単位で指定する数値式。

## 戻り値 {#return-values}
指定した角度のサインを返します。

* * *

## 例 {#examples}

### π/2 ラジアンのサインを計算する {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

π/2 ラジアンのサインは 1 であるため、このクエリは `1` を返します。

### π ラジアンのサインを計算する {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

このクエリは π ラジアンのサインを計算します。π のサインは 0 であるため、結果は約 `0` になります。
