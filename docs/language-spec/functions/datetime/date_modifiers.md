---
title: Date Modifiers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "Date and time modifiers in StackQL: shift DATE, TIME, DATETIME, JULIANDAY and STRFTIME results by days, months or years, to a period start, a weekday and more."
image: "/img/stackql-featured-image.png"
---

## Overview {#overview}

In StackQL datetime functions, a date or time value can be followed by zero or more modifiers that alter the date and/or time. Each modifier is a transformation that is applied to the time value to its left. Modifiers are applied in order from left to right. The available modifiers are as follows:

| Modifier             | Description                                                           |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s)           | Add ±NNN days to a date and time                                      |
| NNN hour(s)          | Add ±NNN hours to a date and time                                     |
| NNN minute(s)        | Add ±NNN minutes to a date and time                                   |
| NNN[.NNNN] second(s) | Add ±NNN seconds to/from a date and time                              |
| NNN month(s)         | Add ±NNN months to a date and time                                    |
| NNN year(s)          | Add ±NNN years to a date and time                                     |
| start of month       | Backward to the beginning of the month                                |
| start of year        | Backward to the beginning of the year                                 |
| start of day         | Backward to the beginning of the day                                  |
| weekday N            | Advance a date forward to the next date where the weekday number is N; a date already on weekday N is returned unchanged |
| localtime            | Return local time                                                     |
| utc                  | Return UTC time                                                       |
| unixepoch            | Unix time                                                             |

## Examples {#examples}

### Return the current date and time using the `now` time value {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convert UTC to localtime using the `localtime` modifier {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convert a local time to UTC {#convert-datetime-to-utc}

The `utc` modifier treats its input as a local time on the machine running StackQL and converts it to UTC.  A value that is already in UTC, such as `'now'` or a timestamp with a `Z` suffix, is returned unchanged.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

On a machine whose time zone is UTC+11, this query returns `2026-11-20 06:59:24`.

### Calculate a date relative to another date {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Get the specified weekday on or after a given date {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Get the start of the month for a given date {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Convert a Unix timestamp to a datetime {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

To convert a datetime to a Unix epoch timestamp see [**STRFTIME**](/language-spec/functions/datetime/strftime).  To calculate differences between two datetime objects see [**STRFTIME**](/language-spec/functions/datetime/strftime) or [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::
