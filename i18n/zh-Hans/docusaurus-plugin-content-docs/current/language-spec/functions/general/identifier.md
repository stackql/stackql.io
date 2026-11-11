---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 会话变量
  - identifier
description: "StackQL 中的 IDENTIFIER 函数：将保存列名的会话变量解析为列引用，使变量可在投影和 ORDER BY 中指定列。"
image: "/img/stackql-featured-image.png"
---
将字符串值（通常是会话变量）解析为列名。如果直接写 `$variable` 会被读取为标量值，就在该位置使用此函数。在 StackQL `v1.0` 及更高版本中可用。

另请参阅：  
[[` SET `]](/language-spec/set) [[ 会话变量 ]](/getting-started/session-variables)

* * * 

## 语法 {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## 参数 {#arguments}

__*variable*__  
保存列名的会话变量或字符串字面量。接受 `t.name` 这样的限定名称。恰好接受一个参数，且该参数必须是单个字符串值；`'t.' || $column_name` 这样的拼接、第二个参数或数值会被拒绝，并返回 `IDENTIFIER requires one string value`。为空的值或包含语句的值会被拒绝，并返回 `invalid identifier value`，未定义的变量则因未定义而失败。所有这些检查都在向 Provider 发出任何请求之前完成。

## 返回值 {#return-values}

在解析后的语句中，该函数被替换为对所指定列的引用，因此它返回该列的值。名称会被转换为标识符，绝不会拼接到 SQL 文本中。得到的列引用可用于任何可以使用列的位置：选择列表、`WHERE`、`GROUP BY` 和 `ORDER BY` 子句、函数参数，以及 CTE 和子查询内部。

* * *

## 示例 {#examples}

### 按变量中指定的列进行投影和排序 {#project-and-order-by-a-column-named-in-a-variable}

要返回的列和用于排序的列都取自 `column_name`，输出列则按 `output_name` 重命名。选择列表和 `ORDER BY` 中需要使用 `IDENTIFIER`，因为在这些位置直接写 `$column_name` 得到的是字符串 `'name'`，而不是该列。别名则使用双引号形式。变量解析后，该语句等同于 `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`。

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### 按变量中指定的列进行过滤 {#filter-on-a-column-named-in-a-variable}

解析后的引用既可用于投影，也可用于谓词。这里用于过滤的字段和要匹配的值都来自变量：`IDENTIFIER($filter_column)` 成为列 `status`，而 `$filter_value` 作为字符串字面量 `'RUNNING'` 传入。这两种形式有意不同，因为前者指定列，后者提供值。

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### 用表别名限定列 {#qualify-the-column-with-a-table-alias}

带点号的值解析为限定列引用。表的别名为 `w`，该别名本身取自变量，`IDENTIFIER($column_name)` 则成为 `w.name`。当连接两侧存在同名列、引用会产生歧义时，应使用这种形式。

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### 被拒绝的值 {#rejected-values}

由于该值成为名称而不是查询文本，任何不能用作名称的内容都会在语句规划之前被拒绝，不会有请求到达 Provider。下面的前两条语句因 `invalid identifier value` 而失败，第三条因 `IDENTIFIER requires one string value` 而失败。

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> `FROM` 子句中的 `$variable` 无需 `IDENTIFIER` 即可解析为关系，而带双引号的 `"$variable"` 用于别名、DDL 列名、`INSERT` 列列表和 `UPDATE` 目标。
