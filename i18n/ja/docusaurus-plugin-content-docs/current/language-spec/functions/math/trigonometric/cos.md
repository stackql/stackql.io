---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、ラジアン単位で指定した角度のコサインを計算します。
image: "/img/stackql-featured-image.png"
---
ラジアン単位で指定した角度のコサインを計算します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*radian_value*__  
コサインを計算する角度をラジアン単位で指定する数値式。

## 戻り値 {#return-values}
指定した角度のコサインを返します。

* * *

## 例 {#examples}

### π/3 ラジアンのコサインを計算する {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

このクエリは `0.5000000000000001` を返します。これは、π/3 ラジアンのコサイン（0.5）の浮動小数点近似値です。

### π ラジアンのコサインを計算する {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

このクエリは π ラジアンのコサインを計算します。π のコサインは -1 であるため、結果は `-1` になります。
