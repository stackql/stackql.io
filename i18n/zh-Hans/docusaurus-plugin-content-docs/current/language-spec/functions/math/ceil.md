---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 CEIL 函数：将数值参数向上取整到最接近的整数。"
image: "/img/stackql-featured-image.png"
---
将数值参数向上取整到最接近的整数。  

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
属于精确数值或近似数值数据类型类别的表达式。

## 返回值 {#return-values}
返回大于或等于指定数值表达式的最小整数。

* * *

## 示例 {#examples}

### 将浮点数向上取整到最接近的（较大）整数 {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
