---
title: ROUND
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
Rounds a numeric argument.  

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
An expression of the exact numeric or approximate numeric data type category.

__*precision*__  
The number of digits to the right of the decimal point to round the *numeric_expression* to.

> If the *precision* argument is omitted, it is assumed to be 0.

## Return Value(s)
Returns a numeric value.

* * *

## Examples

### Round a floating point number to two decimal places

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
For more information, see [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)