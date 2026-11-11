---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 DATETIME 函数：根据输入值和一个或多个修饰符计算日期时间值，以 YYYY-MM-DD HH:MM:SS 格式返回。"
image: "/img/stackql-featured-image.png"
---
根据输入的日期时间和一个或多个修饰符，返回计算得到的日期时间值。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 日期修饰符 ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## 语法 {#syntax}

```sql
SELECT DATETIME(datetime_expression , modifier, ...) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*datetime_expression*__  
日期时间值。

__*modifier*__  
应用于 *datetime_expression* 的零个或多个修饰符。有关修饰符的更多信息，请参阅[**日期修饰符**](/language-spec/functions/datetime/date_modifiers)。

## 返回值 {#return-values}

返回一个字符串，表示应用 *modifier* 后输入 *datetime_expression* 的日期时间。

* * *

## 示例 {#examples}

### 使用 `localtime` 修饰符将 UTC 转换为本地时间 {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### 将日期时间转换为 UTC {#covert-datetime-to-utc}

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

### 计算相对于另一个日期的日期 {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### 获取给定日期之后的下一个指定星期几 {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### 获取给定日期的月初 {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### 将 Unix 时间戳转换为日期时间 {#covert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

要将日期时间转换为 Unix 纪元时间戳，请参阅 [**STRFTIME**](/language-spec/functions/datetime/strftime)。要计算两个日期时间对象之间的差值，请参阅 [**STRFTIME**](/language-spec/functions/datetime/strftime) 或 [**JULIANDAY**](/language-spec/functions/datetime/julianday)。

:::

有关更多信息，请参阅 [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)。
