---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the hyperbolic arc cosine (inverse hyperbolic cosine) of a number, returning an angle in radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the hyperbolic arc cosine (inverse hyperbolic cosine) of a number, which is the non-negative value \( \theta \) in radians whose hyperbolic cosine is the specified number.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression greater than or equal to 1, for which the hyperbolic arc cosine is to be calculated.

## Return Value(s)
Returns the hyperbolic arc cosine of the specified value in radians.

* * *

## Examples

### Calculate the hyperbolic arc cosine of 1

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

This query returns `0`, as the hyperbolic arc cosine of 1 is 0 radians.

### Calculate the hyperbolic arc cosine of 10

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

This query calculates the hyperbolic arc cosine of 10, resulting in approximately `2.9932`, as this is the value in radians whose hyperbolic cosine is 10.
