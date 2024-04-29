---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Retrieve the mathematical constant π (pi) using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Returns the mathematical constant π (pi), which is the ratio of a circle's circumference to its diameter.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT PI() AS pi_value;
```

## Arguments

This function does not require any arguments.

## Return Value(s)
Returns the constant π, approximately equal to 3.14159.

* * *

## Examples

### Retrieve the value of π

```sql
SELECT PI() AS pi_value;
```

This query will output the value of π, which is approximately `3.14159`, useful in various mathematical calculations involving circles and other geometric functions.
