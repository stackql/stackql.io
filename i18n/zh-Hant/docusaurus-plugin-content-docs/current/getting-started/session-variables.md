---
title: 工作階段變數
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 工作階段變數
  - set
  - unset
  - identifier
description: "StackQL 中的工作階段變數：以 SET 設定具型別的純量值，在述詞與運算式中以 $name 解除參考，並透過 IDENTIFIER 作為關聯與資料行名稱。"
image: "/img/stackql-featured-image.png"
---

另請參閱：  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Provider 工作階段 ]](/getting-started/provider-sessions) [[ 使用變數 ]](/getting-started/variables)

工作階段變數是由目前工作階段保存、並可在之後的陳述式中參考的純量值。工作階段變數適用於 StackQL `v1.0` 及更新版本，可在互動式 shell、`exec` 指令碼、PostgreSQL 線路協定以及 MCP 伺服器中使用。

工作階段變數不同於[使用變數](/getting-started/variables)中說明的範本化變數。範本化變數是透過 `--iqldata` 與 `--var` 提供給 `stackql exec`，並在剖析之前轉譯到指令碼文字中。工作階段變數則是在剖析後的陳述式內解析，會保留其型別，而且可以在陳述式之間變更。

## 設定與移除變數 {#setting-and-removing-variables}

[`SET`](/language-spec/set) 可指派字串、數字、布林值或 `null`。[`UNSET`](/language-spec/unset) 會移除變數；`SET name = DEFAULT` 的作用相同。明確指定的 `null` 是已儲存的值，而不是重設。

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

陣列與物件不是純量值。若要保存 JSON 文件，請將其儲存為字串：

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## 解除參考變數 {#dereferencing-a-variable}

在任何運算式位置使用不加引號的 `$name`。字串會逸出為 SQL 常值；數字、布林值與 `null` 會保留其型別。以單引號括住的 `'$name'` 是常值文字 `$name`，而不是解除參考。參考不存在的變數時，會在發出任何 Provider 要求之前失敗。

解析後的值可用於一般的 SQL 運算式，包括 `CAST`、`CONVERT`、以 `||` 串連以及函式呼叫：

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

指派只接受純量常值或直接參考；轉換與其他運算式請在使用變數的地方套用，而不是在 `SET` 陳述式中。

## 述詞中的變數 {#variables-in-predicates}

`WHERE` 子句中的變數會在規劃陳述式之前解析，因此會像常值一樣被路由到 Provider API。所以必要參數可以由變數提供：

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

具型別的值會依其型別進行比較。搭配 `SET enabled = false` 時，`WHERE active = $enabled` 會依布林值篩選；當 Provider 的資料行是字串而變數是數字時，應使用 `WHERE id = CAST($some_id AS TEXT)` 這種形式。

## 以變數作為物件名稱 {#variables-as-object-names}

變數除了可以表示值，也可以命名關聯與資料行。名稱會在剖析後的陳述式中轉換為識別碼，絕不會直接插入 SQL 文字，因此包含陳述式、內容為空或不是字串的值，都會在存取任何 Provider 之前遭到拒絕。

| 位置 | 形式 |
|--|--|
| `FROM` 子句 | `FROM $relation_name`，或依元件分別指定的 `FROM $provider.$service.$resource` |
| 運算式中的資料行 | `IDENTIFIER($column_name)`，請參閱 [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| 資料行或資料表別名 | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| DDL 資料行名稱、`INSERT` 資料行清單、`UPDATE` 目標 | 以雙引號括住的 `"$name"` 形式 |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

聯結、CTE 與子查詢也以相同方式解析其關聯變數。以點分隔的關聯值最多有四個元件。變數不是任意的 SQL 片段：無法以變數取代關鍵字、以逗號分隔的資料行清單或整個 `WHERE` 子句。

## 範圍與隔離 {#scope-and-isolation}

變數屬於設定它們的工作階段。連到 `stackql srv` 伺服器的不同連線各有各的變數，不同的 `exec` 叫用也是如此。每個 MCP 要求都是獨立的工作階段，因此 `SET` 與使用它的查詢必須在同一個陳述式批次中送出。新的工作階段開始時沒有任何變數。

工作階段是唯一的範圍。`SET SESSION` 與 `UNSET SESSION` 可被接受，意義與 `SET` 和 `UNSET` 完全相同；此關鍵字是為了讓指令碼的寫法與區分工作階段設定和全域設定的資料庫保持一致。`GLOBAL` 會遭到拒絕，因為不存在跨工作階段共用的狀態。

預備陳述式會在執行時解析變數，而不是在準備時解析，而且包含已繫結工作階段值的計畫不會在多次執行之間重複使用。

啟用[存取管理](/command-line-usage/access-management)時，讀取變數需要 `session/variables:read`，設定或重設變數則需要 `session/config:write`。將變數解析為物件名稱並不會授與該物件的任何權限；解析後的關聯仍會照常在執行前進行授權。
