---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 IIF 函数：根据条件是否为真返回两个值之一，是内联的 if-then-else 表达式。"
image: "/img/stackql-featured-image.png"
---
根据输入表达式的求值结果返回一个值。  

另请参阅：  
[[` SELECT `]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> `iif()` 函数在逻辑上等价于 `CASE` 表达式 `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## 参数 {#arguments}

__*expression*__  
要求值的布尔表达式。

:::tip 

*expression* 参数可以是嵌套函数，例如 `JSON_EXTRACT`。

:::

__*result_if_true*__  
*expression* 求值为 `true` 时返回的结果。

__*result_if_false*__  
*expression* 求值为 `false` 时返回的结果。

## 返回值 {#return-values}
*result_if_true* 或 *result_if_false* 参数的数据类型。

* * *

## 示例 {#examples}

### 对 Compute Engine 实例的磁盘大小进行分类 {#catgorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(json_extract(disks, '$[0].diskSizeGb') > 9, "> 10GB", "<= 10GB") as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
