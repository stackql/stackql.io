---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "TRUNC function in StackQL: the integer part of a number, rounding towards zero and discarding any fraction."
image: "/img/stackql-featured-image.png"
---

Returns the integer part of a number by rounding towards zero, stripping away any fractional component.

See also:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*numeric_expression*__  
An expression of the exact numeric or approximate numeric data type category.

## Return Value(s) {#return-values}
Returns a numeric value truncated to its integer component.

* * *

## Examples {#examples}

### Truncate the number of days since creation of Google Cloud Storage buckets to two decimal places {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-two-decimal-places}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
