---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the square root of a number using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the square root of a non-negative numeric expression.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
A non-negative numeric expression for which the square root is to be calculated.

## Return Value(s)
Returns the square root of the specified non-negative numeric value.

* * *

## Examples

### Calculate the square root of a number

```sql
SELECT SQRT(9) AS square_root;
```