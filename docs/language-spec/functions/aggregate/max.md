---
title: MAX
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
Returns the maximum value based upon a column input or grouping of columns.  

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Arguments

### Aggregate function

__*columnExpression*__  
A column or expression including operands (`+`, `-`, ...).

> The `MAX` function will return the largest value for the given column based upon the column data type, for example a `MAX` operation on an integer column will return the largest integer for that column or column grouping, whereas a `MAX` operation on a string column or column grouping will return the largest ASCII value (generally the first value when sorted in lexographic order with some differences).

__*groupByColumn*__  
A column or columns used to perform summary or aggregate operations against.  The `GROUP BY` clause returns one row for each column grouping.

> The `MAX` function ignores `NULL` values.

> The `MAX` function returns `NULL` if all the values in the group are `NULL`.

### Scalar function

__*scalarExpression*__  
A list columns or expressions (2 or more) from which the largest value is determined and returned. 

> The scalar `MAX` function returns `NULL` if any argument is `NULL`. 

> The scalar or multi-argument `MAX` function searches its arguments from left to right for an argument that defines a collating function and uses that collating function for all string comparisons. 

> If none of the arguments the scalar `MAX` function define a collating function, then the `BINARY` collating function is used. 

> If only one argument is provided, then the `MAX` aggregate function is invoked.

## Return Value(s)

Returns the maximum value based upon the input data type (or grouping).

* * *

## Examples

### Return the maximum value for a column

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Return the maximum value for each value in a column

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Return the maximum value from a list of values using the scalar `max` function

```sql
SELECT max(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) or [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)