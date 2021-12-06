---
title: COUNT
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
Returns the total number of records in a resource or a count of the non `NULL` instances of a column in a grouping.  

See also:  
[[` SELECT `]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments

__*columnExpression*__  or __*__  
A column or expression.  If __*__ is specified then all records in the resource or grouping are counted.  

> At least one value from the *columnExpression* must be non `NULL` otherwise `NULL` is returned.

__*groupByColumn*__  
A column or columns used to perform summary or aggregate operations against.  The `GROUP BY` clause returns one row for each column grouping.

## Return Value(s)

Returns an integer representing the count.

* * *

## Examples

### Return the average of a column expression over an entire resource

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return the average of a column expression grouped by another column expression

```sql
SELECT tags, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

For more information, see [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).