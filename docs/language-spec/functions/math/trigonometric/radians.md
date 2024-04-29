---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Convert an angle from degrees to radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Converts a specified angle from degrees to radians.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Arguments

__*angle_in_degrees*__  
A numeric expression representing an angle in degrees.

## Return Value(s)
Returns the equivalent of the angle in radians.

* * *

## Examples

### Convert 180 degrees to radians

```sql
SELECT RADIANS(180) AS radians_value;
```

This query will return π (approximately `3.14159`), as converting 180 degrees to radians results in π radians.

### Convert 90 degrees to radians

```sql
SELECT RADIANS(90) AS radians_value;
```

This query calculates the conversion of 90 degrees to radians, resulting in π/2 or approximately `1.5708`, since 90 degrees is half of 180 degrees.
