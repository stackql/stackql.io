---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Use regular expressions to match patterns in strings using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Checks if the source string matches the regular expression pattern provided.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Arguments

__*source_string*__  
The string in which to search for the pattern.

__*pattern_expression*__  
The regular expression pattern to match against the source string.

## Return Value(s)
Returns a boolean value: `TRUE` if the source string matches the regular expression pattern, otherwise `FALSE`.

* * *

## Examples

### Check if a string contains numbers

This example demonstrates checking whether the string 'the year is 2021' contains one or more numeric characters:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

This query will return `TRUE` because the string contains the substring '2021', which matches the pattern `[0-9]+`, indicating one or more digits.

### Validate email format

To check if a string is in a valid email format:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

This query returns `TRUE`, indicating that 'user@example.com' is formatted like a typical email address, matching the specified regular expression pattern.
