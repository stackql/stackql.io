---
title: 数据类型
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 数据类型：查询和结果中资源属性与参数所使用的类型，以及它们与 Provider API schema 的对应关系。"
image: "/img/stackql-featured-image.png"
---

StackQL 数据类型用于表示云资源中输入或输出字段（属性或参数）的值。下表列出了 StackQL 数据类型：

| StackQL 类型 | 说明 | 示例 |
|--|--|--|
| `string` | 由单引号括起的 Unicode 字符序列 | `'mybucket'` |
| `array` | 有序的值集合，用方括号 `[ ]` 括起，以逗号分隔 | `[0,1,2]` |
| `object` | 由零个或多个名称/值对组成的无序集合，置于 `{ }` 之间，以逗号分隔 | `{"name": "mydb"}` |
| `boolean` | `true` 或 `false`，布尔值不用引号括起，并将被视为字符串值 | `true` |
| `integer` | 整数，可以为正数、负数或零 | `2` |
| `double` | 双精度浮点数，可以为正数或负数 | `0.95` |
<br/>
<br/>
> StackQL 数据类型与所有主流云服务商的 API 中公开的数据类型相对应。

## 类型转换 {#type-conversion}

`CAST(expression AS type)` 和 `CONVERT(expression, type)` 将值转换为另一种类型。除了从 MySQL 语法继承的转换类型（`CHAR`、`DATE`、`DATETIME`、`DECIMAL`、`SIGNED`、`UNSIGNED`、`TIME`、`JSON`）之外，StackQL `v1.0` 及更高版本还接受 `TEXT`、`INTEGER`、`INT`、`BIGINT`、`BOOLEAN` 和 `BOOL`。转换由 SQL 后端执行，因此其语义与 SQLite 或 PostgreSQL 的语义一致。转换通常用于将带类型的[会话变量](/getting-started/session-variables)与字符串列进行比较：

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

由 AWS 等 XML Provider 提供的布尔字段以 `true` 和 `false` 的形式报告；`v1.0` 之前的 StackQL 发布版本将其报告为 `0` 和 `1`。
