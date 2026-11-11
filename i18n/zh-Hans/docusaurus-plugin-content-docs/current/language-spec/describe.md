---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 DESCRIBE：在查询或变更之前，列出资源的字段，或某个方法的参数和返回类型。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

描述资源或资源上的某个方法。

另请参阅：
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy)

* * *

## 语法 {#syntax}

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

描述资源时，可以使用 `DESC` 作为 `DESCRIBE` 的别名；`DESCRIBE METHOD` 必须使用完整的关键字。

## 响应 {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| 列 | 说明 |
| --- | --- |
| `name` | 字段名称 |
| `type` | 数据类型（`string`、`integer`、`boolean`、`number`、`object`、`array`） |
| `description` | 字段说明（如果提供了 `EXTENDED`） |

</TabItem>
<TabItem value="describe-method">

| 列 | 说明 |
| --- | --- |
| `name` | 字段名称 |
| `type` | 数据类型（`string`、`integer`、`boolean`、`number`、`object`、`array`） |
| `param_type` | `input_required`、`input_optional` 或 `output` |
| `shape` | 对象和数组字段的 JSON Schema 子集；标量字段为空。包括嵌套的 `properties`、`items`、`required`、`enum`、`default`、`description`，如果 Provider 提供，还可包括布尔值（`readOnly`、`writeOnly`、`deprecated`） |
| `description` | 字段说明（如果提供了 `EXTENDED`） |

</TabItem>
</Tabs>

* * *

## 示例 {#examples}

### 基本 `DESCRIBE` 语句 {#basic-describe-statement}
在已通过身份验证的会话中运行基本 `DESCRIBE` 语句，列出资源中的字段。

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### 扩展 `DESCRIBE` 语句 {#extended-describe-statement}
在已通过身份验证的会话中运行扩展 `DESCRIBE` 语句，列出资源中的字段及其说明。

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### `DESC` 别名 {#desc-alias}
`DESC` 是对资源使用 `DESCRIBE` 的简写，可带或不带 `EXTENDED`。它在所有将 `DESCRIBE` 视为只读语句的地方同样被视为只读语句，包括 `read_only` 和 `safe` 模式下 MCP 服务器的 `run_select_query` 工具。

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### 基本 `DESCRIBE METHOD` 语句 {#basic-describe-method-statement}
对资源上的单个方法进行内省。结果包括输入参数和响应字段，嵌套结构呈现在 `shape` 列中。

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### 扩展 `DESCRIBE METHOD` 语句 {#extended-describe-method-statement}
运行扩展 `DESCRIBE METHOD` 语句，在 shape 之外同时包含每个字段的说明。

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### 发现 `INSERT` 负载 {#discovering-an-insert-payload}
`DESCRIBE METHOD` 是发现 `INSERT`（或任何变更方法）接受哪些字段的标准方式。`shape` 列包含嵌套结构，智能体和人都可以借助它在一次查询中构造出负载。

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
