---
title: Date Modifiers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

## Overview

The date or time values datetime functions in StackQL can be followed by zero or more modifiers that alter the date and/or time. Each modifier is a transformation that is applied to the time value to its left. Modifiers are applied in order from left to right. The available modifiers are as follows:

| Modifier             | Description                                                           |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s)           | Add ±NNN days to a date and time                                      |
| NNN hour(s)          | Add ±NNN hours to a date and time                                     |
| NNN minute(s)        | Add ±NNN minutes to a date and time                                   |
| NNN[.NNNN] second(s) | Add ±NNN seconds to/from a date and time                              |
| NNN month(s)         | Add ±NNN months to a date and time                                    |
| NNN year(s)          | Add ±NNN year to a date and time                                      |
| start of month       | Backward to the beginning of the month                                |
| start of year        | Backward to the beginning of the year                                 |
| start of day         | Backward to the beginning of the day                                  |
| weekday N            | Advance a date forward to the next date where the weekday number is N |
| localtime            | Return local time                                                     |
| utc                  | Return UTC time                                                       |
| unixepoch            | Unix time                                                             |

## Examples

### Return the current date and time using the `now` modifier

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convert UTC to localtime using the `localtime` modifier

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Covert datetime to UTC

```sql
SELECT name, timeCreated,
datetime('now', 'utc') as now_utc
FROM google.storage.buckets WHERE project = 'stackql';
```

### Calculate a date relative to another date

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Get the next specified weekday after a given date

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Get the start of the month for a given date

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Covert a Unix timestamp to a datetime

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

To covert a datetime to a Unix epoch timestamp see [**STRFTIME**](/docs/language-spec/functions/datetime/strftime).  To calculate differences between two datetime objects see [**STRFTIME**](/docs/language-spec/functions/datetime/strftime) or [**JULIANDAY**](/docs/language-spec/functions/datetime/julianday).

:::
