---
title: Concatenation Operator
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "The || concatenation operator in StackQL: join two or more string values into one string."
image: "/img/stackql-featured-image.png"
---
Returns the resultant string from the concatenation, or joining, of two or more string values.  

See also:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Arguments

__*character_expression*__  
A literal string or string column.

## Return Value(s)

Returns a string.

:::note

From StackQL `v1.0` the parser treats `||` as the concatenation operator, binding more tightly than comparison operators, so `WHERE id = prefix || suffix` compares `id` with the joined string.  Earlier releases tokenised `||` as boolean `OR`.  Use `OR` for boolean disjunction.  [Session variables](/getting-started/session-variables) can be concatenated too: `$prefix || '-' || $suffix`.

:::

* * *

## Examples

### Concatenate two fields together

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

For more information, see [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)