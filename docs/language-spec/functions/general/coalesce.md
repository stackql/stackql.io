---
title: COALESCE
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
Returns the first argument which is not `NULL` from a series of two or more arguments, can be used to substitute a default value for a `NULL` value.  

> The `COALESCE` function is equivalent to the `NVL` function in Oracle or the `IFNULL` function in MySQL.

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` IFNULL `]](/docs/language-spec/functions/general/ifnull) [[` NULLIF `]](/docs/language-spec/functions/general/nullif)

* * * 

## Syntax

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Arguments

__*expression*__  
Two or more expressions of any data type which resolve to a `NULL` or non `NULL` value.

> If all of the *expression* arguments are `NULL`, the `COALESCE` function returns `NULL`.

## Return Value(s)

Returns the first non `NULL` value from the series of *expression* arguments.

* * *

## Examples

### Replace a `NULL` value with a default value

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)