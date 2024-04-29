---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Convert an angle from radians to degrees using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Converts a specified angle from radians to degrees.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Arguments

__*angle_in_radians*__  
A numeric expression representing an angle in radians.

## Return Value(s)
Returns the equivalent of the angle in degrees.

* * *

## Examples

### Convert π radians to degrees

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

This query will return approximately `180`, as converting π radians (approximately `3.14159`) results in about 180 degrees.

### Convert π/2 radians to degrees

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

This query calculates the conversion of π/2 radians (approximately `1.5708`) to degrees, resulting in about `90`, since π/2 radians is the equivalent of a 90-degree angle.
