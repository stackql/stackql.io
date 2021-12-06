---
title: GROUP_CONCAT
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
Returns a string which is the concatenation of all non `NULL` values of an input column or grouping.  

> The order of the concatenated elements is arbitrary.

See also:  
[[` SELECT `]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments

__*columnExpression*__  
A column or expression.  If __*__ is specified then all records in the resource or grouping are counted.  

> At least one value from the *columnExpression* must be non `NULL` otherwise `NULL` is returned.

__*separator*__  
Seperator used for the concatenated result, the default of `,` is used if *separator* is not specified.

__*groupByColumn*__  
A column or columns used to perform summary or aggregate operations against.  The `GROUP BY` clause returns one row for each column grouping.

## Return Value(s)

Returns a string as a list of values seperated by the *separator* or `,`.

* * *

## Examples

### Return a list of values for a column over an entire resource

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return a list of values for a column expression grouped by another column expression

```sql
SELECT tags, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

For more information, see [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).