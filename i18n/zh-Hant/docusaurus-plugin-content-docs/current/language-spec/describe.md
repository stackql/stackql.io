---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 DESCRIBE：在查詢或變更資源之前，列出資源的欄位，或特定方法的參數與傳回型別。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

描述資源或資源上的特定方法。

另請參閱：
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy)

* * *

## 語法 {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

描述資源時，`DESC` 可作為 `DESCRIBE` 的別名；`DESCRIBE METHOD` 則必須使用完整關鍵字。

## 回應 {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| 資料行 | 描述 |
| --- | --- |
| `name` | 欄位名稱 |
| `type` | 資料型別（`string`、`integer`、`boolean`、`number`、`object`、`array`） |
| `description` | 欄位描述（若提供 `EXTENDED`） |

</TabItem>
<TabItem value="describe-method">

| 資料行 | 描述 |
| --- | --- |
| `name` | 欄位名稱 |
| `type` | 資料型別（`string`、`integer`、`boolean`、`number`、`object`、`array`） |
| `param_type` | `input_required`、`input_optional` 或 `output` |
| `shape` | 物件和陣列欄位的 JSON Schema 子集；純量欄位則為空。包含巢狀的 `properties`、`items`、`required`、`enum`、`default`、`description`，若 Provider 有提供，也會選擇性地包含布林值（`readOnly`、`writeOnly`、`deprecated`） |
| `description` | 欄位描述（若提供 `EXTENDED`） |

</TabItem>
</Tabs>

* * *

## 範例 {#examples}

### 基本 `DESCRIBE` 陳述式 {#basic-describe-statement}
從已驗證的工作階段執行基本 `DESCRIBE` 陳述式，以列出資源中的欄位。

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### 延伸 `DESCRIBE` 陳述式 {#extended-describe-statement}
從已驗證的工作階段執行延伸 `DESCRIBE` 陳述式，以列出資源中的欄位及其描述。

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### `DESC` 別名 {#desc-alias}
`DESC` 是對資源使用 `DESCRIBE` 時的簡寫，可搭配或不搭配 `EXTENDED`。凡是 `DESCRIBE` 被視為唯讀陳述式之處，它也同樣被視為唯讀陳述式，包括 `read_only` 和 `safe` 模式下 MCP 伺服器的 `run_select_query` 工具。

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### 基本 `DESCRIBE METHOD` 陳述式 {#basic-describe-method-statement}
檢查資源上的單一方法。結果包含輸入參數和回應欄位，巢狀結構則呈現在 `shape` 資料行中。

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### 延伸 `DESCRIBE METHOD` 陳述式 {#extended-describe-method-statement}
執行延伸 `DESCRIBE METHOD` 陳述式，除了 shape 之外，也包含每個欄位的描述。

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### 探索 `INSERT` 承載 {#discovering-an-insert-payload}
`DESCRIBE METHOD` 是探索 `INSERT`（或任何變更方法）接受哪些欄位的標準方式。`shape` 資料行帶有巢狀結構，代理程式和人員都能用它在一次查詢中建構承載。

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
