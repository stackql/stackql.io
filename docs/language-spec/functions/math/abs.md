---
title: ABS
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
Returns the absolute value of the numeric argument.  

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `abs(X)` returns `NULL` if `X` is `NULL`. `abs(X)` returns `0.0` if `X` is a string or blob that cannot be converted to a numeric value. If `X` is the integer -9223372036854775808 then `abs(X)` throws an integer overflow error since there is no equivalent positive 64-bit two complement value.

## Arguments

__*numeric_expression*__  
An expression of the exact numeric or approximate numeric data type category.

## Return Value(s)

Returns a positive numeric value.

* * *

## Examples

### Return the absolute value of a number

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_mathfunc.html](https://www.sqlite.org/lang_mathfunc.html)