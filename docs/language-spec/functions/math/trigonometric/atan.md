---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the arc tangent (inverse tangent) of a number, returning an angle in radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the arc tangent (inverse tangent) of a number, which is the angle in radians whose tangent is the specified number.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression for which the arc tangent is to be calculated.

## Return Value(s)
Returns the arc tangent of the specified value in radians.

* * *

## Examples

### Calculate the arc tangent of 1

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

This query returns π/4 or approximately `0.785`, as the arc tangent of 1 is π/4 radians.

### Calculate the arc tangent of 0

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

This query calculates the arc tangent of 0, resulting in `0`, since the tangent of 0 radians is 0.
