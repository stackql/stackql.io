---
title: 資料型別
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 資料型別：查詢與結果中資源屬性和參數使用的型別，以及其如何對應到 Provider API schema。"
image: "/img/stackql-featured-image.png"
---

StackQL 資料型別用來表示雲端資源中所使用之輸入或輸出欄位（屬性或參數）的值。下表列出 StackQL 資料型別：

| StackQL 型別 | 描述 | 範例 |
|--|--|--|
| `string` | 以單引號括住的 Unicode 字元序列 | `'mybucket'` |
| `array` | 以方括號 `[ ]` 括住、以逗號分隔的有序值集合 | `[0,1,2]` |
| `object` | 置於 `{ }` 之間、以逗號分隔，由零或多個名稱/值對組成的無序集合 | `{"name": "mydb"}` |
| `boolean` | `true` 或 `false`，不以引號括住（以引號括住的值，例如 `'true'`，會被視為字串） | `true` |
| `integer` | 可為正數、負數或零的整數 | `2` |
| `double` | 雙精確度浮點數，可為正數或負數 | `0.95` |
<br/>
<br/>
> StackQL 資料型別會對應到所有主要雲端服務供應商的 API 所公開的資料型別。

## 型別轉換 {#type-conversion}

`CAST(expression AS type)` 和 `CONVERT(expression, type)` 會將值轉換為另一種型別。除了繼承自 MySQL 文法的轉換型別（`CHAR`、`DATE`、`DATETIME`、`DECIMAL`、`SIGNED`、`UNSIGNED`、`TIME`、`JSON`）之外，StackQL `v1.0` 及更新版本也接受 `TEXT`、`INTEGER`、`INT`、`BIGINT`、`BOOLEAN` 和 `BOOL`。轉換由 SQL 後端執行，因此其語意與 SQLite 或 PostgreSQL 相同。轉換常用於將具型別的[工作階段變數](/getting-started/session-variables)與字串資料行進行比較：

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

AWS 等 XML Provider 所提供的布林欄位會回報為 `true` 和 `false`；`v1.0` 之前的 StackQL 發行版本則將其回報為 `0` 和 `1`。
