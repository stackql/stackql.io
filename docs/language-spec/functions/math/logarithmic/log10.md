---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the base-10 logarithm of a number using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the base-10 logarithm of a given number, determining the power to which the number 10 must be raised to achieve the value.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
A positive numeric expression for which the base-10 logarithm is to be calculated.

## Return Value(s)
Returns the base-10 logarithm of the specified numeric value.

* * *

## Examples

### Calculate the base-10 logarithm of 100

```sql
SELECT LOG10(100) AS log_result;
```

This query returns `2`, as \(10^2 = 100\).

### Calculate the base-10 logarithm of 1000

```sql
SELECT LOG10(1000) AS log_result;
```

This query calculates the base-10 logarithm of 1000, resulting in `3`, since \(10^3 = 1000\).
