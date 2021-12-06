---
title: DATETIME
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
Returns a calculated datetime value from an input datetime and one or more modifiers.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[ Date Modifiers ]](/docs/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax

```sql
SELECT DATETIME(datetime_expression , modifier, ...) FROM <multipartIdentifier>;
```

## Arguments

__*datetime_expression*__  
The datetime value.

__*modifier*__  
Zero or more modifiers to be applied to the *datetime_expression*.  For more information about modifiers, see [**Date Modifiers**](/docs/language-spec/functions/datetime/date_modifiers)

## Return Value(s)

Returns a string representing the datetime of the input *datetime_expression* with the *modifier* applied.

* * *

## Examples

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

For more information, see [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)