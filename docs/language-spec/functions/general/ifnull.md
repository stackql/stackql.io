---
title: IFNULL
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
Accepts exactly two arguments and returns the first non `NULL` argument, can be used to substitute a default value for a `NULL` value.  

> The `IFNULL` function is equivalent to the [`COALESCE`](/docs/language-spec/functions/general/coalesce) function with two arguments only, the [`COALESCE`](/docs/language-spec/functions/general/coalesce) function can have more than two arguments.

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` COALESCE `]](/docs/language-spec/functions/general/coalesce) [[` NULLIF `]](/docs/language-spec/functions/general/nullif)

* * * 

## Syntax

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Arguments

__*expression*__  
Two expressions of any data type which resolve to a `NULL` or non `NULL` value.

> If both *expression* arguments are `NULL`, the `IFNULL` function returns `NULL`.


## Return Value(s)

Returns the first non `NULL` value from the two *expression* arguments. 

* * *

## Examples

### Replace a `NULL` value with a default value

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)