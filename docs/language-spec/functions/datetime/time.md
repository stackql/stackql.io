---
title: TIME
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
Returns a time string in the format HH:MM:SS.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[ Date Modifiers ]](/docs/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments

__*datetime_expression*__  
The datetime value.

__*modifier*__  
Zero or more modifiers to be applied to the *datetime_expression*.  For more information about modifiers, see [**Date Modifiers**](/docs/language-spec/functions/datetime/date_modifiers)

## Return Value(s)

Returns a string representing the time component of the input *datetime_expression* with the *modifier* applied.

* * *

## Examples

### Return the time component of a datetime value

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Return the local time component of a datetime value

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

For more information, see [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)