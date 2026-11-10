---
title: Data Types
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "StackQL data types: the types used for resource properties and parameters in queries and results, and how they map to provider API schemas."
image: "/img/stackql-featured-image.png"
---

StackQL data types are used to represent values for input or output fields (properties or parameters) used within cloud resources.  StackQL data types are listed in the table below:

| StackQL type | Description | Example |
|--|--|--|
| `string` | sequence of Unicode characters enclosed by single quotes | `'mybucket'` |
| `array` | ordered collection of values, surrounded by brackets `[ ]`, separated by a comma | `[0,1,2]` |
| `object` | unordered set of zero or more name/value pairs inserted between `{ }` and separated by a comma | `{"name": "mydb"}` |
| `boolean` | `true` or `false`, boolean values are not surrounded within quotes and will be treated as string values | `true` |
| `integer` | a whole number that can be positive, negative, or zero | `2` |
| `double` | double-precision floating-point number, can be positive or negative | `0.95` |
<br/>
<br/>
> StackQL data types map to the data types exposed in the APIs for all major cloud providers.

## Type Conversion {#type-conversion}

`CAST(expression AS type)` and `CONVERT(expression, type)` convert a value to another type.  In addition to the conversion types inherited from the MySQL grammar (`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`), StackQL `v1.0` and later accept `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN` and `BOOL`.  The conversion is performed by the SQL backend, so its semantics are those of SQLite or PostgreSQL.  Conversions are commonly used to compare a typed [session variable](/getting-started/session-variables) with a string column:

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

Boolean fields served by XML providers such as AWS are reported as `true` and `false`; StackQL releases before `v1.0` reported them as `0` and `1`.