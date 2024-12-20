---
title: CHAR
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

The `CHAR()` function returns the character corresponding to the specified ASCII code.

See also:  
[[`SELECT`]](/docs/language-spec/select)

* * *

:::note
When using StackQL with a PostgreSQL backend, the equivalent function is `CHR()`.
:::

## Syntax

```sql
SELECT CHAR(ascii_code) FROM <multipartIdentifier>;
```

> The `CHAR()` function returns the character associated with the given ASCII code.

## Arguments

**_ascii_code_**  
An integer representing the ASCII code of the desired character.

## Return Value(s)

Returns a single-character string corresponding to the specified ASCII code.

* * *

## Common ASCII Codes

| Character     | Description           | ASCII Code | Usage Example                   |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | Newline               | 10         | `CHAR(10)`                      |
| `'`           | Single Quote          | 39         | `CHAR(39)`                      |
| `"`           | Double Quote          | 34         | `CHAR(34)`                      |
| (space)       | Space                 | 32         | `CHAR(32)`                      |
| `:`           | Colon                 | 58         | `CHAR(58)`                      |
| `;`           | Semicolon             | 59         | `CHAR(59)`                      |
| `\t`          | Horizontal Tab        | 9          | `CHAR(9)`                       |
| `\r`          | Carriage Return       | 13         | `CHAR(13)`                      |
| `\b`          | Backspace             | 8          | `CHAR(8)`                       |
| `\f`          | Form Feed             | 12         | `CHAR(12)`                      |

* * *

## Examples

### Concatenate Strings with Special Characters

To concatenate strings with a newline character between them:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Result:**

```
He said "Hello" and left.
```

* * *
