---
title: 日期修饰符
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的日期和时间修饰符：按天、月或年偏移 DATE、TIME、DATETIME、JULIANDAY 和 STRFTIME 的结果，或移至周期起点、指定星期几等。"
image: "/img/stackql-featured-image.png"
---

## 概述 {#overview}

StackQL 日期时间函数中的日期或时间值之后可以跟零个或多个修饰符，用于更改日期和/或时间。每个修饰符都是一种转换，应用于其左侧的时间值。修饰符按从左到右的顺序应用。可用的修饰符如下：

| 修饰符               | 说明                                  |
|----------------------|---------------------------------------|
| NNN day(s)           | 为日期和时间加上 ±NNN 天              |
| NNN hour(s)          | 为日期和时间加上 ±NNN 小时            |
| NNN minute(s)        | 为日期和时间加上 ±NNN 分钟            |
| NNN[.NNNN] second(s) | 为日期和时间加上或减去 ±NNN 秒        |
| NNN month(s)         | 为日期和时间加上 ±NNN 个月            |
| NNN year(s)          | 为日期和时间加上 ±NNN 年              |
| start of month       | 回退到当月的开始                      |
| start of year        | 回退到当年的开始                      |
| start of day         | 回退到当天的开始                      |
| weekday N            | 将日期推进到下一个星期编号为 N 的日期 |
| localtime            | 返回本地时间                          |
| utc                  | 返回 UTC 时间                         |
| unixepoch            | Unix 时间                             |

## 示例 {#examples}

### 使用 `now` 修饰符返回当前日期和时间 {#return-the-current-date-and-time-using-the-now-modifier}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

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
