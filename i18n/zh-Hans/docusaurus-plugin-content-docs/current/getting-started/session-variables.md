---
title: 会话变量
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 会话变量
  - set
  - unset
  - identifier
description: "StackQL 中的会话变量：使用 SET 设置带类型的标量值，在谓词和表达式中通过 $name 解引用，并借助 IDENTIFIER 将其用作关系名和列名。"
image: "/img/stackql-featured-image.png"
---

另请参阅：  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Provider 会话 ]](/getting-started/provider-sessions) [[ 使用变量 ]](/getting-started/variables)

会话变量是由当前会话保存的标量值，可在之后的语句中引用。会话变量在 StackQL `v1.0` 及更高版本中可用，适用于交互式 shell、`exec` 脚本、PostgreSQL 线协议以及 MCP 服务器。

会话变量不同于[使用变量](/getting-started/variables)中描述的模板化变量。模板化变量通过 `--iqldata` 和 `--var` 提供给 `stackql exec`，并在对脚本文本进行语法分析之前渲染到其中。会话变量在语法分析后的语句内部解析，保留其类型，并且可以在语句之间更改。

## 设置和移除变量 {#setting-and-removing-variables}

[`SET`](/language-spec/set) 为变量赋字符串、数字、布尔值或 `null`。[`UNSET`](/language-spec/unset) 移除变量；`SET name = DEFAULT` 与之等效。显式的 `null` 是一个存储的值，而不是重置。

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

数组和对象不是标量值。要保存 JSON 文档，请将其存储为字符串：

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## 解引用变量 {#dereferencing-a-variable}

在任何表达式位置使用不带引号的 `$name`。字符串会按 SQL 字面量进行转义；数字、布尔值和 `null` 保留其类型。带单引号的 `'$name'` 是字面文本 `$name`，而不是解引用。引用不存在的变量会在发出任何 Provider 请求之前失败。

解析后的值可用于普通 SQL 表达式，包括 `CAST`、`CONVERT`、使用 `||` 的拼接以及函数调用：

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

赋值只接受标量字面量或直接引用；转换和其他表达式应在使用变量的位置应用，而不是在 `SET` 语句中。

## 谓词中的变量 {#variables-in-predicates}

`WHERE` 子句中的变量在语句规划之前解析，因此它被路由到 Provider API 的方式与字面量完全相同。这样，必需参数就可以由变量提供：

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

带类型的值按其类型进行比较。`WHERE active = $enabled` 配合 `SET enabled = false` 会按布尔值过滤；当 Provider 列是字符串而变量是数字时，应使用 `WHERE id = CAST($some_id AS TEXT)` 这种形式。

## 用作对象名称的变量 {#variables-as-object-names}

变量除了表示值，还可以用来命名关系和列。名称在语法分析后的语句中被转换为标识符，绝不会被拼接到 SQL 文本中，因此包含语句、为空或不是字符串的值会在访问任何 Provider 之前被拒绝。

| 位置 | 形式 |
|--|--|
| `FROM` 子句 | `FROM $relation_name`，或按组成部分写为 `FROM $provider.$service.$resource` |
| 表达式中的列 | `IDENTIFIER($column_name)`，参阅 [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| 列别名或表别名 | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| DDL 列名、`INSERT` 列列表、`UPDATE` 目标 | 带双引号的 `"$name"` 形式 |
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

连接、CTE 和子查询以相同的方式解析其关系变量。以点分隔的关系值最多有 4 个组成部分。变量不是任意的 SQL 片段：关键字、以逗号分隔的列列表或整个 `WHERE` 子句都不能被替换进来。

## 作用域与隔离 {#scope-and-isolation}

变量属于设置它们的会话。到 `stackql srv` 服务器的不同连接拥有各自独立的变量，不同的 `exec` 调用也是如此。每个 MCP 请求都是一个独立的会话，因此 `SET` 和使用它的查询必须在同一个语句批次中发送。新会话开始时没有任何变量。

会话是唯一的作用域。`SET SESSION` 和 `UNSET SESSION` 均被接受，含义与 `SET` 和 `UNSET` 完全相同；保留该关键字是为了让脚本的写法与区分会话设置和全局设置的数据库保持一致。`GLOBAL` 会被拒绝，因为不存在跨会话共享的状态。

预处理语句在执行时解析变量，而不是在预处理时；包含已绑定会话值的计划不会在多次执行之间复用。

启用[访问管理](/command-line-usage/access-management)后，读取变量需要 `session/variables:read`，设置或重置变量需要 `session/config:write`。将变量解析为对象名称并不会授予对该对象的任何权限；解析出的关系照常在执行前进行授权。
