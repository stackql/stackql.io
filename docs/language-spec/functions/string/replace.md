---
title: REPLACE
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
Returns a string formed by substituting a portion of a string for every occurrence of another string.

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Arguments

__*character_expression*__  
A literal string or string column that you want to perform the replacement on.

__*pattern*__  
The substring to be found in the original string.

> If *pattern* is an empty string or is not found in *character_expression*, then *character_expression* will be returned unchanged. 

__*replacement*__  
The replacement string.

> If *replacement* is not initially a string, it is cast to a UTF-8 string prior to processing.

> The `BINARY` collating sequence is used for comparisons.

## Return Value(s)
Returns a string.

* * *

## Examples

### Replace a string pattern in a column

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)