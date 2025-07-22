---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the hyperbolic tangent of a number, returning the result using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the hyperbolic tangent of a specified value.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression for which the hyperbolic tangent is to be calculated.

## Return Value(s)
Returns the hyperbolic tangent of the specified value.

* * *

## Examples

### Calculate the hyperbolic tangent of 1

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

This query returns approximately `0.7616`, as the hyperbolic tangent of 1 is about 0.7616.

### Calculate the hyperbolic tangent of 0

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

This query calculates the hyperbolic tangent of 0, resulting in `0`, since the hyperbolic tangent of 0 is exactly 0.
