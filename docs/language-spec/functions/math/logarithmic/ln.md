---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Calculate the natural logarithm of a number using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Calculates the natural logarithm (logarithm to the base \(e\)) of a given number.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
A positive numeric expression for which the natural logarithm is to be calculated.

## Return Value(s)
Returns the natural logarithm of the specified positive numeric value.

* * *

## Examples

### Calculate the natural logarithm of 2.71828 (approximately \(e\))

```sql
SELECT LN(2.71828) AS natural_log;
```

This query returns a value close to `1`, since the natural logarithm of \(e\) (approximately 2.71828) is 1.

### Calculate the natural logarithm of a larger number

```sql
SELECT LN(10) AS natural_log;
```

This query calculates the natural logarithm of 10, providing the logarithmic value of 10 to the base \(e\).