---
title: LENGTH
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
Returns the number of characters in a string input expression prior to the first `NULL` character, or the number of bytes in a BLOB expression.  

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

:::info

To get the length of a JSON array use the [**JSON_ARRAY_LENGTH**](/docs/language-spec/functions/json/json_array_length) function.

:::

## Syntax

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Arguments

__*expression*__  
An ASCII string or a BLOB.

> Returns `NULL` if *expression* is `NULL`.

> If *expression* is numeric then the length of a string representation of *expression* is returned.

## Return Value(s)
Returns an integer.

* * *

## Examples

### Return the length of a string column

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)