---
title: データ型
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL のデータ型：クエリと結果でリソースのプロパティやパラメーターに使われる型と、プロバイダーの API スキーマとの対応。"
image: "/img/stackql-featured-image.png"
---

StackQL のデータ型は、クラウドリソースで使用される入力フィールドまたは出力フィールド（プロパティまたはパラメーター）の値を表すために使用されます。StackQL のデータ型を次の表に示します。

| StackQL の型 | 説明 | 例 |
|--|--|--|
| `string` | シングルクォートで囲まれた Unicode 文字のシーケンス | `'mybucket'` |
| `array` | 角かっこ `[ ]` で囲まれ、カンマで区切られた値の順序付きコレクション | `[0,1,2]` |
| `object` | `{ }` の間に置かれ、カンマで区切られた 0 個以上の名前と値のペアの順序なしセット | `{"name": "mydb"}` |
| `boolean` | `true` または `false`。引用符で囲みません（`'true'` のように引用符で囲んだ値は文字列として扱われます） | `true` |
| `integer` | 正、負、またはゼロの整数 | `2` |
| `double` | 正または負の倍精度浮動小数点数 | `0.95` |
<br/>
<br/>
> StackQL のデータ型は、主要なすべてのクラウドベンダーの API で公開されているデータ型に対応します。

## 型変換 {#type-conversion}

`CAST(expression AS type)` と `CONVERT(expression, type)` は、値を別の型に変換します。MySQL の文法から継承した変換型（`CHAR`、`DATE`、`DATETIME`、`DECIMAL`、`SIGNED`、`UNSIGNED`、`TIME`、`JSON`）に加えて、StackQL `v1.0` 以降は `TEXT`、`INTEGER`、`INT`、`BIGINT`、`BOOLEAN`、`BOOL` も受け付けます。変換は SQL バックエンドによって実行されるため、そのセマンティクスは SQLite または PostgreSQL のものになります。変換は、型付きの[セッション変数](/getting-started/session-variables)を文字列の列と比較する際によく使用されます。

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

AWS などの XML プロバイダーが提供するブール値のフィールドは、`true` と `false` として報告されます。`v1.0` より前の StackQL リリースでは、`0` と `1` として報告されていました。
