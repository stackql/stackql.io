---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って数学定数 π（円周率）を取得します。
image: "/img/stackql-featured-image.png"
---
数学定数 π（円周率）を返します。これは円周の長さと直径の比です。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT PI() AS pi_value;
```

## 引数 {#arguments}

この関数に引数は必要ありません。

## 戻り値 {#return-values}
約 3.14159 に等しい定数 π を返します。

* * *

## 例 {#examples}

### π の値を取得する {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

このクエリは π の値（約 `3.14159`）を出力します。この値は、円やその他の幾何関数を含むさまざまな数学計算に役立ちます。
