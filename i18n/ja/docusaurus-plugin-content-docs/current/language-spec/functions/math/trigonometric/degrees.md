---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って角度をラジアンから度に変換します。
image: "/img/stackql-featured-image.png"
---
指定した角度をラジアンから度に変換します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*angle_in_radians*__  
ラジアン単位の角度を表す数値式。

## 戻り値 {#return-values}
角度を度単位に換算した値を返します。

* * *

## 例 {#examples}

### π ラジアンを度に変換する {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

π ラジアン（約 `3.14159`）を変換すると約 180 度になるため、このクエリは約 `180` を返します。

### π/2 ラジアンを度に変換する {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

このクエリは π/2 ラジアン（約 `1.5708`）を度に変換します。π/2 ラジアンは 90 度の角度に相当するため、結果は約 `90` になります。
