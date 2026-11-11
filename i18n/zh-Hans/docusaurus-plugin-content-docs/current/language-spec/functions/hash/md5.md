---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - 哈希
  - 摘要
description: "StackQL 中的 MD5 函数：返回字符串的小写十六进制 MD5 摘要，用于生成指纹和变化检测，而非安全用途。"
image: "/img/stackql-featured-image.png"
---
以 32 个小写十六进制字符的形式返回字符串的 MD5 摘要。在 StackQL `v1.0` 及更高版本中可用。

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 拼接 ]](/language-spec/functions/string/concatenation)

* * * 

## 语法 {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expression*__  
字符串，或被强制转换为文本的值。参数为 `NULL` 时返回 `NULL`。

## 返回值 {#return-values}

返回 32 个字符的小写十六进制字符串。

> `MD5` 用于生成指纹和变化检测。它不是密码或安全原语。在 PostgreSQL 后端上，使用数据库自带的 `md5` 函数。

* * *

## 示例 {#examples}

### 为派生值生成指纹 {#fingerprint-a-derived-value}

变量作为带类型的值代入，整数被转换为文本并与后缀拼接，然后对转为小写的结果计算摘要。相同的输入始终产生相同的 32 个字符的十六进制字符串，因此该值可用作稳定的键。

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### 检测配置变化 {#detect-configuration-changes}

对序列化的 JSON 列进行哈希，可得到一个便于存储并在多次运行之间比较的短值。摘要与上次运行不同的存储桶，其 IAM 配置已发生变化，无需存储或比对整个文档。

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
