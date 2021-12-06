---
title: SUM
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
[[` SELECT `]](/docs/language-spec/select) [[` TOTAL `]](/docs/language-spec/functions/aggregate/total)

* * * 

:::tip

Use the [**TOTAL**](/docs/language-spec/functions/aggregate/total) function to sum floating point numbers or long values.

:::

## Syntax

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Arguments

__*columnExpression*__  
A column or expression.

> If all the values in the *columnExpression* are `NULL` then `SUM` returns `NULL`.  

> `SUM` will throw an "integer overflow" exception if an integer overflow occurs at any point during the computation.  The [`TOTAL`]](/docs/language-spec/functions/aggregate/total) function never throws an integer overflow.  

__*groupByColumn*__  
A column or columns used to perform summary or aggregate operations against.  The `GROUP BY` clause returns one row for each column grouping.

## Return Value(s)

Returns an integer representing the sum of the *columnExpression* if all non `NULL` inputs are integers. If any inputs are not integers or are `NULL` then a floating point value which might be an approximation to the true sum is returned.

* * *

## Examples

### Return the sum of a column expression over an entire resource

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return the sum of a column expression grouped by another column expression

```sql
SELECT tags, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

For more information, see [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).