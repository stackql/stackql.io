---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the power of one number raised to another using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates *x* raised to the power *y*.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Arguments

__*x*__  
The base number, which is a numeric expression.

__*y*__  
The exponent to which the base number *x* is raised, also a numeric expression.

## Return Value(s)
Returns the result of *x* raised to the power *y*.

* * *

## Examples

### Calculate x raised to the power y


```sql
SELECT POW(2, 3) AS result;
```
