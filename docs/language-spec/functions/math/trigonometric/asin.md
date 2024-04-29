---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the arc sine (inverse sine) of a number, returning an angle in radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the arc sine (inverse sine) of a number, which is the angle in radians whose sine is the specified number.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression within the range of -1 to 1, inclusive, for which the arc sine is to be calculated.

## Return Value(s)
Returns the arc sine of the specified value in radians.

* * *

## Examples

### Calculate the arc sine of 0.5

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

This query returns π/6 or approximately `0.5236`, as the arc sine of 0.5 corresponds to an angle of π/6 radians.

### Calculate the arc sine of -1

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

This query calculates the arc sine of -1, resulting in -π/2 or approximately `-1.5708`, as the sine of -π/2 is -1.
