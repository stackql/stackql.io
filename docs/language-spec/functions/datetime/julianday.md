---
title: JULIANDAY
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
Returns the Julian day - the number of days since noon in Greenwich on November 24, 4714 B.C. [(Proleptic Gregorian calendar)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[ Date Modifiers ]](/docs/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments

__*datetime_expression*__  
The datetime value.

__*modifier*__  
Zero or more modifiers to be applied to the *datetime_expression*.  For more information about modifiers, see [**Date Modifiers**](/docs/language-spec/functions/datetime/date_modifiers)

## Return Value(s)

Returns the [Julian Day](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar) representing the date component of the input *datetime_expression* with the *modifier* applied.

* * *

## Examples

### Calculate the days between two dates

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Use the [**ROUND**](/docs/language-spec/functions/math/round) to convert floating point numbers to the nearest integer.

:::

For more information, see [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)