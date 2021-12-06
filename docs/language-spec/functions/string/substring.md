---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
custom_edit_url: null
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---
Returns part of a character or binary expression.  `SUBSTR` is an alias for `SUBSTRING`.

See also:  
[[` SELECT `]](/docs/language-spec/select) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

:::info

To extract a field from a JSON object use the [**JSON_EXTRACT**](/docs/language-spec/functions/json/json_extract) function.

:::

## Syntax

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Arguments

__*expression*__  
An ASCII string or a BLOB.

> If *expression* is a string then characters indices refer to actual UTF-8 characters. If *expression* is a BLOB then the indices refer to bytes.

__*start*__  
An integer representing the first character or byte of the sub sequence of the input expression to be returned.  If *start* is negative then the first character of the substring is found by counting from the right rather than the left.

> The left-most character of *expression* is number 1.

__*length*__  
An optional integer value representing the length of the sub sequence to be returned after the *start* value.  If *length* is omitted then all characters through the end of the string or BLOB specified by the *expression* are returned.

> If *length* is negative then the absolute value of *length* is used.

## Return Value(s)

Returns a string or BLOB value depending upon the *expression* type.

* * *

## Examples

### Return the first *n* characters from a column

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Truncate the first *n* characters from a column

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```
### Return the last *n* characters from a column

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)