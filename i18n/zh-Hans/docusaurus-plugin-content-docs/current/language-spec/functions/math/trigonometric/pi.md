---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 获取数学常数 π（pi）。
image: "/img/stackql-featured-image.png"
---
返回数学常数 π（pi），即圆的周长与直径之比。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT PI() AS pi_value;
```

## 参数 {#arguments}

此函数不需要任何参数。

## 返回值 {#return-values}
返回常数 π，约等于 3.14159。

* * *

## 示例 {#examples}

### 获取 π 的值 {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

此查询输出 π 的值，约为 `3.14159`，可用于涉及圆和其他几何函数的各种数学计算。
