---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the remainder of one number divided by another using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the remainder of *x* divided by *y*, returning the modulus. This is similar to using the '%' operator but is compatible with non-integer arguments.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Arguments

__*x*__  
The dividend, which is a numeric expression.

__*y*__  
The divisor, also a numeric expression.

## Return Value(s)
Returns the remainder of *x* divided by *y*.

* * *

## Examples

### Calculate the modulus of 10 divided by 3

```sql
SELECT MOD(10, 3) AS remainder;
```
