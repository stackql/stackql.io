---
title: INSTR
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
Returns the character position of first occurrence of string within another string, returns 0 if the string being searched for is not found.

See also:  
[[` SELECT `]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Arguments

__*string*__  
The source string being searched.

__*searchstring*__  
The substring being searched for.

> If *string* and *searchstring* are both BLOBs, then `instr(<string>, <searchstring>)` returns one more than the number bytes prior to the first occurrence of *searchstring*, or 0 if *searchstring* does not occur anywhere within *string*. If both arguments are not `NULL` and are not BLOBs then both are interpreted as strings. 

## Return Value(s)
Returns an integer.

> If either the *string* or the *searchstring* values are `NULL`, then the result is `NULL`.

* * *

## Examples

### Search a field for a string

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurence of the term 'worker' in the 'name' column
*/
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)