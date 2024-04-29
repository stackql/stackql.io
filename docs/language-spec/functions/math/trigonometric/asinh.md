---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the hyperbolic arc sine (inverse hyperbolic sine) of a number, returning an angle in radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the hyperbolic arc sine (inverse hyperbolic sine) of a number, which is the value \( \theta \) in radians whose hyperbolic sine is the specified number.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression for which the hyperbolic arc sine is to be calculated.

## Return Value(s)
Returns the hyperbolic arc sine of the specified value in radians.

* * *

## Examples

### Calculate the hyperbolic arc sine of 0.5

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

This query returns approximately `0.4812`, as the hyperbolic arc sine of 0.5 is about 0.4812 radians.

### Calculate the hyperbolic arc sine of -1

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

This query calculates the hyperbolic arc sine of -1, resulting in approximately `-0.8814`, as this is the value in radians whose hyperbolic sine is -1.
