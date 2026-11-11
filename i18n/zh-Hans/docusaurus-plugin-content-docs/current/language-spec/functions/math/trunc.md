---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 TRUNC 函数：一个数的整数部分，向零取整并舍弃小数部分。"
image: "/img/stackql-featured-image.png"
---

通过向零取整返回一个数的整数部分，去除所有小数部分。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
属于精确数值或近似数值数据类型类别的表达式。

## 返回值 {#return-values}
返回截断为整数部分的数值。

* * *

## 示例 {#examples}

### 将 Google Cloud Storage 存储桶自创建以来的天数截断到 2 位小数 {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-two-decimal-places}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
