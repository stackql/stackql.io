---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the cosine of an angle, provided in radians, using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the cosine of a specified angle given in radians.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Arguments

__*radian_value*__  
A numeric expression that specifies the angle in radians for which the cosine is to be calculated.

## Return Value(s)
Returns the cosine of the specified angle.

* * *

## Examples

### Calculate the cosine of π/3 radians

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

This query returns `0.5`, as the cosine of π/3 radians is 0.5.

### Calculate the cosine of π radians

```sql
SELECT COS(PI()) AS cosine_value;
```

This query calculates the cosine of π radians, resulting in `-1`, since the cosine of π is -1.
