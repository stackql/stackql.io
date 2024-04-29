---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the hyperbolic arc tangent (inverse hyperbolic tangent) of a number, returning an angle in radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the hyperbolic arc tangent (inverse hyperbolic tangent) of a number, which is the value \( \theta \) in radians whose hyperbolic tangent is the specified number.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression within the range of -1 to 1 (exclusive), for which the hyperbolic arc tangent is to be calculated.

## Return Value(s)
Returns the hyperbolic arc tangent of the specified value in radians.

* * *

## Examples

### Calculate the hyperbolic arc tangent of 0.5

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

This query returns approximately `0.5493`, as the hyperbolic arc tangent of 0.5 is about 0.5493 radians.

### Calculate the hyperbolic arc tangent of -0.5

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

This query calculates the hyperbolic arc tangent of -0.5, resulting in approximately `-0.5493`, as this is the value in radians whose hyperbolic tangent is -0.5.
