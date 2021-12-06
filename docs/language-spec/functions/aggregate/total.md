---
title: TOTAL
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
Returns the sum of all non `NULL` values in a column or grouping of columns.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` SUM `]](/docs/language-spec/functions/aggregate/sum)

* * * 

## Syntax

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments

__*columnExpression*__  
A column or expression.

> If all the values in the *columnExpression* are `NULL` then `TOTAL` returns `0.0`.  

__*groupByColumn*__  
A column or columns used to perform summary or aggregate operations against.  The `GROUP BY` clause returns one row for each column grouping.

## Return Value(s)

Returns a floating point value representing the sum of the *columnExpression*.

* * *

## Examples

### Return the sum of a column expression over an entire resource

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return the sum of a column expression grouped by another column expression

```sql
SELECT tags, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

For more information, see [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).