---
title: Concatenation Operator
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
Returns the resultant string from the concatenation, or joining, of two or more string values.  

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Arguments

__*character_expression*__  
A literal string or string column.

## Return Value(s)

Returns a string.

* * *

## Examples

### Concatenate two fields together

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

For more information, see [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)