---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って角度を度からラジアンに変換します。
image: "/img/stackql-featured-image.png"
---
指定した角度を度からラジアンに変換します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*angle_in_degrees*__  
度単位の角度を表す数値式。

## 戻り値 {#return-values}
角度をラジアン単位に換算した値を返します。

* * *

## 例 {#examples}

### 180 度をラジアンに変換する {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

180 度をラジアンに変換すると π ラジアンになるため、このクエリは π（約 `3.14159`）を返します。

### 90 度をラジアンに変換する {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

このクエリは 90 度をラジアンに変換します。90 度は 180 度の半分であるため、結果は π/2、つまり約 `1.5708` になります。
