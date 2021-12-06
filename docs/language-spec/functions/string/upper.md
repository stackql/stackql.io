---
title: UPPER
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
Returns an input string in uppercase.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` LOWER `]](/docs/language-spec/functions/string/lower) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

## Syntax

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> The `upper()` function works for ASCII characters only.

## Arguments

__*character_expression*__  
An ASCII string.

## Return Value(s)
Returns a string value.

* * *

## Examples

### Return a string in uppercase

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)