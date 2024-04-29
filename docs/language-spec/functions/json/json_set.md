---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Update or insert values in a JSON document using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Updates existing values or inserts new values in a JSON document at specified paths.

See also:  
[[`SELECT`]](/docs/language-spec/select) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[` JSON_SET `]](/docs/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/docs/language-spec/functions/json/json_replace)

* * * 

## Syntax

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Arguments

__*json_column*__  
The JSON column or expression to be modified.

__*path*__  
The JSON path expression indicating where to set the value. If the path exists, the value is updated; if the path does not exist, a new key-value pair is added.

__*value_to_set*__  
The value to be set at the specified path in the JSON document.

## Return Value(s)
Returns a JSON document with the value set at the specified path. If the path exists, the existing value is updated; otherwise, a new key-value pair is added.

:::note Behavior If a Key Exists

| Function                                                     | Overwrite if already exists? | Create if does not exist? |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/docs/language-spec/functions/json/json_insert) | No                           | Yes                       |
| [`json_replace()`](/docs/language-spec/functions/json/json_replace) | Yes                          | No                        |
| [`json_set()`](/docs/language-spec/functions/json/json_set)       | Yes                          | Yes                       |


* * *

## Examples

### Update an existing value in a JSON object

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET(json_data, '$.status', 'active') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne", "status":"active", "region":"US"} |
-- |---------------------------------------------------------|
```

This query updates the 'status' key to 'active' in the JSON document if the 'status' key exists. If 'status' does not exist in the document, it is added with the value 'active'.

### Add a new key-value pair to a JSON object

```sql
-- Example: Add a new key 'lastChecked' with the current date to a JSON object
SELECT JSON_SET(json_data, '$.lastChecked', '2024-04-29') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne", "status":"pending", "lastChecked":"2024-04-29"} |
|-----------------------------------------------------------------------|
*/
```

This query adds a new key 'lastChecked' with the value '2024-04-29' to the JSON document if 'lastChecked' does not already exist. If it does, the existing value is updated to '2024-04-29'.
