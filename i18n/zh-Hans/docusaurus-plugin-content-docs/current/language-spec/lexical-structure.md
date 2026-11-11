---
title: 词法结构
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 的词法结构：解析器如何对语句、标识符、引号、字面量、注释和空白字符进行词法分析。"
image: "/img/stackql-featured-image.png"
---

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy) [[ 关键字 ]](/language-spec/keywords) [[ 数据类型 ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


StackQL 的词法结构和语法以 MySQL 的语言实现（对 ANSI SQL-92 标准的一种改编）为蓝本，并带有一些扩展。StackQL 语句用于查询云资源（使用 `SELECT` 语法），或使用 `INSERT`、`UPDATE`、`REPLACE` 和 `DELETE` 等标准数据操作语言（DML）语句创建、修改或删除基础设施。此外，非 DML 操作（例如启动实例，或者在 AWS S3 的存储桶中获取或放入对象）被实现为内置 Provider 过程（类似于 DBMS 中的存储过程）。内置 Provider 过程使用 `EXEC` 语句调用。

StackQL 的 SQL 领域特定语言既支持声明式的基础设施编程，也支持过程式的基础设施编程。

* * * 

## 语法 {#syntax}
StackQL 操作由一条或多条以分号（`;`）结尾的语句组成。语句包括关键字、标识符、参数和值，例如，用于在 GCP 中创建 10GB 磁盘的 StackQL 语句如下：

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

在上面的示例中，_`INSERT`_、_`INTO`_ 和 _`SELECT`_ 是关键字，_google.compute.disks_ 是标识符，_project_、_zone_、_name_、_sizeGb_ 是参数，_'myProject'_、_'australia-southeast1-a'_、_'my10GbDisk'_、_10_ 是值。

## 关键字 {#keywords}
关键字不区分大小写，即 `INSERT` 和 `insert` 被同等对待，不过按照惯例通常使用大写。完整的关键字列表见“关键字”一节。

## 标识符 {#identifiers}
标识符是表示云服务商资源层次结构中对象的符号。标识符由多个部分组成，有关标识符的更多信息，请参阅“StackQL 资源层次结构”主题。

## 参数和值 {#parameters-and-values}
参数由资源规定；对给定的云服务商资源使用 `DESCRIBE` 语句即可查到这些参数。值是与参数对应的带类型的标量或复杂对象。有关参数和值的更多信息，请参阅 `DESCRIBE` 和“数据类型”主题。

## 变量 {#variables}
以美元符号为前缀的标识符（例如 `$region`）会解引用通过 [`SET`](/language-spec/set) 赋值的会话变量；在对语句进行规划之前，它会被替换为该变量的带类型的值。带单引号的 `'$region'` 是字符串字面量。在 `FROM` 子句中，`$variable` 解析为关系名；在表达式位置，`IDENTIFIER($variable)` 解析为列名。带双引号并以美元符号为前缀的路径（例如 `"$.auth.aws.password"`）是 `SET` 和 `UNSET` 接受的运行时配置目标。会话变量在 StackQL `v1.0` 及更高版本中可用，请参阅[会话变量](/getting-started/session-variables)。

## 运算符 {#operators}
`||` 是字符串拼接运算符，其结合优先级高于比较运算符。`OR` 是布尔析取。`v1.0` 之前的 StackQL 发布版本将 `||` 词法分析为 `OR`。

## 注释 {#comments}
StackQL 支持在 StackQL 语句中使用多行注释和行内注释，注释示例如下：

```shell
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
