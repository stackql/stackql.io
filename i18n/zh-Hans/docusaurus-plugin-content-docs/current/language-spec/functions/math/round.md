---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 ROUND 函数：将数值参数四舍五入到指定的小数位数，默认为 0。"
image: "/img/stackql-featured-image.png"
---
对数值参数进行四舍五入。  

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
属于精确数值或近似数值数据类型类别的表达式。

__*precision*__  
四舍五入 *numeric_expression* 时保留的小数点右侧位数。

> 如果省略 *precision* 参数，则假定其为 0。

## 返回值 {#return-values}
返回一个数值。

* * *

## 示例 {#examples}

### 将浮点数四舍五入到 2 位小数 {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
