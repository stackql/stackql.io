---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "DATE function in StackQL: return a date as a YYYY-MM-DD string from a time value and optional modifiers."
image: "/img/stackql-featured-image.png"
---
Returns a date string in the format YYYY-MM-DD.  

See also:  
[[` SELECT `]](/language-spec/select) [[ Date Modifiers ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*datetime_expression*__  
The datetime value.

__*modifier*__  
Zero or more modifiers to be applied to the *datetime_expression*.  For more information about modifiers, see [**Date Modifiers**](/language-spec/functions/datetime/date_modifiers)

## Return Value(s) {#return-values}

Returns a string representing the date component of the input *datetime_expression* with the *modifier* applied.

* * *

## Examples {#examples}

### Return the date component of a datetime value {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Return the local date component of a datetime value {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

For more information, see [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)