---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the base-2 logarithm of a number using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the base-2 logarithm of a given number, returning the power to which the number 2 must be raised to obtain the value.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
A positive numeric expression for which the base-2 logarithm is to be calculated.

## Return Value(s)
Returns the base-2 logarithm of the specified numeric value.

* * *

## Examples

### Calculate the base-2 logarithm of 16

```sql
SELECT LOG2(16) AS log_result;
```

This query returns `4`, as \(2^4 = 16\).

### Calculate the base-2 logarithm of 32

```sql
SELECT LOG2(32) AS log_result;
```

This query calculates the base-2 logarithm of 32, resulting in `5`, since \(2^5 = 32\).
