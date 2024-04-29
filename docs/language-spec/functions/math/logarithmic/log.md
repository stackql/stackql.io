---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the logarithm of a number for any base using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the logarithm of a number to a specified base, which is a more general form of logarithmic function compared to LN, which uses base \(e\).

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*base*__  
The base of the logarithm, a positive numeric expression other than 1.

__*numeric_expression*__  
A positive numeric expression for which the logarithm is to be calculated.

## Return Value(s)
Returns the logarithm of the specified numeric value to the specified base.

* * *

## Examples

### Calculate the logarithm of 1000 with base 10

```sql
SELECT LOG(10, 1000) AS log_result;
```

This query returns `3`, as \(10^3 = 1000\).

### Calculate the logarithm of 16 with base 2

```sql
SELECT LOG(2, 16) AS log_result;
```
This query calculates the logarithm of 16 with base 2, resulting in `4`, since \(2^4 = 16\).
