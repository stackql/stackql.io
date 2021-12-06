---
title: NULLIF
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
Accepts two arguments and returns a `NULL` value if they are equal, otherwise the first argument is returned.  

> The `NULLIF` function is equvalent to `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> `NULLIF` is useful with aggregate functions such as [`AVG`](/docs/language-spec/functions/aggregate/avg), [`MAX`](/docs/language-spec/functions/aggregate/max), [`MIN`](/docs/language-spec/functions/aggregate/min), [`SUM`](/docs/language-spec/functions/aggregate/sum), and [`COUNT`](/docs/language-spec/functions/aggregate/count), where the result set contains special values that are not `NULL`, but that you want to treat as `NULL` for the purposes of aggregation.

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` COALESCE `]](/docs/language-spec/functions/general/coalesce) [[` IFNULL `]](/docs/language-spec/functions/general/ifnull)

* * * 

## Syntax

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Arguments

__*expression*__  
Two expressions of any data type which resolve to a `NULL` or non `NULL` value.

> The `NULLIF` function searches its arguments from left to right for an argument that defines a collating function and uses that collating function for all string comparisons.  If neither argument defines a collating function then `BINARY` collation is used.

## Return Value(s)

Returns `NULL` if the two *expression* arguments resolve to the same value, otherwise the first value is returned. 

* * *

## Examples

### Count records where a field is not equal to a given value

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE name != 'controller-0';
*/
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)