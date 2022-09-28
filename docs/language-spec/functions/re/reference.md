---
title: Regex Reference
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
Reference for regular expressions used in StackQL.  

See also:  
[[` REGEXP_LIKE `]](/docs/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/docs/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/docs/language-spec/functions/re/regexp_replace)

* * * 

## Supported syntax

The following regular expression syntax is supported in StackQL:    

| Expression | Description |
|------------|-------------|
| `X*` | zero or more occurrences of `X` |
| `X+` | one or more occurrences of `X` | 
| `X?` | zero or one occurrences of `X` |
| `(X)` | match `X` |
| `X`&vert;`Y` | `X` or `Y` |
| `^X` | `X` occurring at the beginning of the string |
| `X$` | `X` occurring at the end of the string |
| `.` | Match any single character |
| `\c` | Character `c` where `c` is one of `\{}()[]`&vert;`*+?.`
| `\c` | C-language escapes for `c` in `afnrtv`. ex: `\t` or `\n` |
| `[abc]` | Any single character from the set `abc` |
| `[^abc]` | Any single character not in the set `abc` |
| `[a-z]` | Any single character in the range `a-z` |
| `[^a-z]` | Any single character not in the range `a-z` |  

&nbsp;  
&nbsp;  
For more information, see [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)