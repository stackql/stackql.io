---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Insert new values into a JSON document without overwriting existing data using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Inserts new values into a JSON document at specified paths without overwriting existing values.

See also:  
[[`SELECT`]](/docs/language-spec/select) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[` JSON_SET `]](/docs/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/docs/language-spec/functions/json/json_replace)

* * * 

## Syntax

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Arguments

__*json_column*__  
The JSON column or expression to be modified.

__*path*__  
The JSON path expression where the new value should be inserted. If the path already exists, the original value at that path is not replaced.

__*new_value*__  
The value to insert at the specified path in the JSON document.

## Return Value(s)
Returns the JSON document with the new value inserted if the specified path does not already exist; otherwise, the JSON document is returned unchanged.

:::note Behavior If a Key Exists

| Function                                                     | Overwrite if already exists? | Create if does not exist? |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/docs/language-spec/functions/json/json_insert) | No                           | Yes                       |
| [`json_replace()`](/docs/language-spec/functions/json/json_replace) | Yes                          | No                        |
| [`json_set()`](/docs/language-spec/functions/json/json_set)       | Yes                          | Yes                       |

:::

* * *

## Examples

### Insert a new key-value pair into a JSON object

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT(json_data, '$.region', 'Europe') as updated_object;

/* expected output...
|-------------------------------------------------------------|
|                     updated_json                            |
|-------------------------------------------------------------|
| {"name":"ResourceOne", "type":"Storage", "region":"Europe"} |
|-------------------------------------------------------------|
*/
```

This query inserts a new key called 'region' with the value 'Europe' into the JSON document if the 'region' key does not already exist. If 'region' is already a key in the document, the original document is returned without changes.