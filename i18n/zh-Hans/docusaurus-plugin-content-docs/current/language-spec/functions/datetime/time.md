---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 TIME 函数：根据时间值和可选的修饰符，以 HH:MM:SS 字符串形式返回时间。"
image: "/img/stackql-featured-image.png"
---
返回格式为 HH:MM:SS 的时间字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 日期修饰符 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 语法 {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*datetime_expression*__  
日期时间值。

__*modifier*__  
应用于 *datetime_expression* 的零个或多个修饰符。有关修饰符的更多信息，请参阅[**日期修饰符**](/language-spec/functions/datetime/date_modifiers)。

## 返回值 {#return-values}

返回一个字符串，表示应用 *modifier* 后输入 *datetime_expression* 的时间部分。

* * *

## 示例 {#examples}

### 返回日期时间值的时间部分 {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### 返回日期时间值的本地时间部分 {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)。
