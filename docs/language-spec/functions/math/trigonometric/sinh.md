---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the hyperbolic sine of a number, returning the result using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the hyperbolic sine of a specified value.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression for which the hyperbolic sine is to be calculated.

## Return Value(s)
Returns the hyperbolic sine of the specified value.

* * *

## Examples

### Calculate the hyperbolic sine of 1

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

This query returns approximately `1.1752`, as the hyperbolic sine of 1 is about 1.1752.

### Calculate the hyperbolic sine of 0

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

This query calculates the hyperbolic sine of 0, resulting in `0`, since the hyperbolic sine of 0 is exactly 0.
