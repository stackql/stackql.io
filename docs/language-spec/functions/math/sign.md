---
title: SIGN
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
Returns -1 if the input value is negative, 0 if the input value is 0, and 1 is the input value is positive.  

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
An expression of the exact numeric or approximate numeric data type category.

> If *numeric_expression* is `NULL` or is a `string` or BLOB that cannot be losslessly converted into a number, then the function will return `NULL`.

## Return Value(s)

Returns `-1`, `0` or `1`.

* * *

## Examples

### Determine the sign of a number

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)