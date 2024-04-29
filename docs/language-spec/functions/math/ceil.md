---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---
Rounds a numeric argument up to the nearest integer.  

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
An expression of the exact numeric or approximate numeric data type category.

## Return Value(s)
Returns the smallest integer greater than or equal to the specified numeric expression.

* * *

## Examples

### Round up a floating point number to the closest (larger) integer

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
