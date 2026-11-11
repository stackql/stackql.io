---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 STRFTIME 函数：使用格式字符串格式化日期时间值，替换规则与 C 语言的 strftime 函数相同。"
image: "/img/stackql-featured-image.png"
---
根据指定的格式返回格式化后的日期时间值。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 日期修饰符 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 语法 {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*format*__  
由 *datetime_expression* 值指定的日期时间值所需的格式。请参阅下方的格式表。

__*datetime_expression*__  
要格式化的日期或时间值。

__*modifier*__  
应用于 *datetime_expression* 的零个或多个修饰符。有关修饰符的更多信息，请参阅[**日期修饰符**](/language-spec/functions/datetime/date_modifiers)。

### 格式 {#formats}

| 格式   | 说明                     |
|--------|--------------------------|
| %d     | 一个月中的第几天：01-31  |
| %f     | 带小数的秒：SS.SSS       |
| %H     | 小时：00-24              |
| %j     | 一年中的第几天：001-366  |
| %J     | 儒略日数                 |
| %m     | 月份：01-12              |
| %M     | 分钟：00-59              |
| %s     | 自 1970-01-01 以来的秒数 |
| %S     | 秒：00-59                |
| %w     | 星期几 0-6，星期日==0    |
| %W     | 一年中的第几周：00-53    |
| %Y     | 年份：0000-9999          |
| %%     | 转义百分号：%            |

## 返回值 {#return-values}

返回一个格式化的字符串，表示所需的日期时间格式。

* * *

## 示例 {#examples}

### 以美国格式返回日期 {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 将日期时间转换为纪元时间戳 {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 计算两个日期之间的天数 {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)。
