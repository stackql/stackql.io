---
title: Regex Reference
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "Regular expression reference for StackQL: syntax, character classes, anchors, quantifiers and groups for REGEXP_LIKE, REGEXP_REPLACE and REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Reference for regular expressions used in StackQL.  

See also:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Supported syntax {#supported-syntax}

The following regular expression syntax is supported in StackQL:    

| Expression | Description |
|------------|-------------|
| `X*` | zero or more occurrences of `X` |
| `X+` | one or more occurrences of `X` | 
| `X?` | zero or one occurrence of `X` |
| `X{n}` | exactly `n` occurrences of `X` |
| `X{n,}` | `n` or more occurrences of `X` |
| `X{n,m}` | between `n` and `m` occurrences of `X` |
| `(X)` | match `X` |
| `X`&vert;`Y` | `X` or `Y` |
| `^X` | `X` occurring at the beginning of the string |
| `X$` | `X` occurring at the end of the string |
| `.` | Match any single character, including a newline |
| `\c` | Character `c` where `c` is one of `\{}()[]`&vert;`*+?.` |
| `\c` | C-language escapes for `c` in `afnrtv`. ex: `\t` or `\n` |
| `[abc]` | Any single character from the set `abc` |
| `[^abc]` | Any single character not in the set `abc` |
| `[a-z]` | Any single character in the range `a-z` |
| `[^a-z]` | Any single character not in the range `a-z` |
| `\d`, `\w`, `\s` | A digit, a word character (`[0-9A-Za-z_]`), a whitespace character |
| `\D`, `\W`, `\S` | Any character that is not a digit, not a word character, not a whitespace character |
| `\b` | A word boundary |  

&nbsp;  
&nbsp;  
Patterns use RE2 syntax, as implemented by Go's `regexp` package.  Backreferences (`\1`) and lookaround assertions are not supported: a pattern that uses them is rejected with an error.

A backslash in a StackQL string literal starts an escape sequence (`'\n'` is a newline), so a backslash that belongs to the regular expression is written twice: `'\\d+'` matches one or more digits and `'[0-9]+\\.[0-9]+'` matches a literal dot between digits.

For the full syntax, see [RE2 syntax](https://github.com/google/re2/wiki/Syntax).