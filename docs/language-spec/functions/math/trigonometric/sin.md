---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the sine of an angle, provided in radians, using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the sine of a specified angle given in radians.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Arguments

__*radian_value*__  
A numeric expression that specifies the angle in radians for which the sine is to be calculated.

## Return Value(s)
Returns the sine of the specified angle.

* * *

## Examples

### Calculate the sine of π/2 radians

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

This query returns `1`, as the sine of π/2 radians is 1.

### Calculate the sine of π radians

```sql
SELECT SIN(PI()) AS sine_value;
```

This query calculates the sine of π radians, resulting in approximately `0`, as the sine of π is 0.
