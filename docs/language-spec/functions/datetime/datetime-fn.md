---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "DATETIME function in StackQL: compute a datetime value from an input and one or more modifiers, returned as YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Returns a calculated datetime value from an input datetime and one or more modifiers.  

See also:  
[[` SELECT `]](/language-spec/select) [[ Date Modifiers ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*datetime_expression*__  
The datetime value.

__*modifier*__  
Zero or more modifiers to be applied to the *datetime_expression*.  For more information about modifiers, see [**Date Modifiers**](/language-spec/functions/datetime/date_modifiers)

## Return Value(s) {#return-values}

Returns a string representing the datetime of the input *datetime_expression* with the *modifier* applied.

* * *

## Examples {#examples}

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

### Get the next specified weekday after a given date {#get-the-next-specified-weekday-after-a-given-date}

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

For more information, see [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)