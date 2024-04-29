---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the tangent of an angle, provided in radians, using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the tangent of a specified angle given in radians.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Arguments

__*radian_value*__  
A numeric expression that specifies the angle in radians for which the tangent is to be calculated.

## Return Value(s)
Returns the tangent of the specified angle.

* * *

## Examples

### Calculate the tangent of π/4 radians

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

This query returns `1`, as the tangent of π/4 radians is 1.

### Calculate the tangent of π radians

```sql
SELECT TAN(PI()) AS tangent_value;
```

This query calculates the tangent of π radians, resulting in approximately `0`, since the tangent of π is very close to 0.
