---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the arc cosine (inverse cosine) of a number, returning an angle in radians using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the arc cosine (inverse cosine) of a number, which is the angle in radians whose cosine is the specified number.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression within the range of -1 to 1, inclusive, for which the arc cosine is to be calculated.

## Return Value(s)
Returns the arc cosine of the specified value in radians.

* * *

## Examples

### Calculate the arc cosine of 0.5

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

This query returns approximately `1.047`, as the arc cosine of 0.5 radians is π/3, which is about 1.047 radians.

### Calculate the arc cosine of 1

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

This query calculates the arc cosine of 1, resulting in `0`, since the cosine of 0 radians is 1.
