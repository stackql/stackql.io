---
title: TRUNC
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

Returns the integer part of a number by rounding towards zero, stripping away any fractional component.

See also:  
[[`SELECT`]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments

__*numeric_expression*__  
An expression of the exact numeric or approximate numeric data type category.

## Return Value(s)
Returns a numeric value truncated to its integer component.

* * *

## Examples

### Truncate the number of days since creation of Google Cloud Storage buckets to two decimal places

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
