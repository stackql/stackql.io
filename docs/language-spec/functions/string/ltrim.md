---
title: LTRIM
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
Returns a string formed by removing white space or any characters that appear from the left side of an input string.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` RTRIM `]](/docs/language-spec/functions/string/rtrim) [[` TRIM `]](/docs/language-spec/functions/string/trim) 

* * * 

## Syntax

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Arguments

__*character_expression*__  
A literal string or string column.

__*character*__  
An optional character to remove if found at the beginning of the *character_expression*.

## Return Value(s)
Returns a string.

* * *

## Examples

### Remove leading whitespace from a string column

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)