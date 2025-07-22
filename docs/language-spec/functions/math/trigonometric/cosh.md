---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the hyperbolic cosine of a number, returning the result using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the hyperbolic cosine of a specified value.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * * 

## Syntax

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Arguments

__*value*__  
A numeric expression for which the hyperbolic cosine is to be calculated.

## Return Value(s)
Returns the hyperbolic cosine of the specified value.

* * *

## Examples

### Calculate the hyperbolic cosine of 1

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

This query returns approximately `1.5431`, as the hyperbolic cosine of 1 is about 1.5431.

### Calculate the hyperbolic cosine of 0

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

This query calculates the hyperbolic cosine of 0, resulting in `1`, since the hyperbolic cosine of 0 is exactly 1.
