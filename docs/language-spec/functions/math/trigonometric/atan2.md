---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the arc tangent of two numbers (y/x), returning an angle in radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the arc tangent of two numbers, specifically the angle \( \theta \) in radians for which the tangent is the ratio of two specified numbers (y divided by x), correctly handling the signs of the inputs to determine the correct quadrant.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## Arguments

__*y*__  
The y-coordinate (numerator) of the point.

__*x*__  
The x-coordinate (denominator) of the point.

## Return Value(s)
Returns the arc tangent of y/x in radians, accounting for the correct quadrant of the angle.

* * *

## Examples

### Calculate the arc tangent of the point (1,1)

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

This query returns π/4 or approximately `0.785`, as the arc tangent of the point (1,1) corresponds to an angle of π/4 radians in the first quadrant.

### Calculate the arc tangent of the point (10, -10)

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

This query calculates the arc tangent of the point (10, -10), resulting in 3π/4 or approximately `2.356`, as the tangent of this angle is -1 in the second quadrant.
