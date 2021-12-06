---
title: STRFTIME
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
Return a formatted datetime value based on a specified format.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[ Date Modifiers ]](/docs/language-spec/functions/datetime/date_modifiers) 

* * * 

## Syntax

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Arguments

__*format*__  
The desired format for the datetime value specified by the *datetime_expression* value.  See the table of formats below.

__*datetime_expression*__  
The date or time value to be formatted.

__*modifier*__  
Zero or more modifiers to be applied to the *datetime_expression*.  For more information about modifiers, see [**Date Modifiers**](/docs/language-spec/functions/datetime/date_modifiers)

### Formats

| Format | Description                    |
|--------|--------------------------------|
| %d     | day of the month: 01-31        |
| %f     | fractional seconds: SS.SSS     |
| %H     | hour: 00-24                    |
| %j     | day of the year: 001-366       |
| %J     | Julian day number              |
| %m     | month: 01-12                   |
| %M     | minute: 00-59                  |
| %s     | seconds since 1970-01-01       |
| %S     | seconds: 00-59                 |
| %w     | day of week 0-6 with Sunday==0 |
| %W     | week of the year: 00-53        |
| %Y     | year: 0000-9999                |
| %%     | escape a percent symbol: %     |

## Return Value(s)

Returns a formatted string representing the desired datetime format.

* * *

## Examples

### Return a date in US format

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Convert a datetime to an epoch timestamp

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Calculate the days between two dates

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)