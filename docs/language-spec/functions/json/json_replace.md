---
title: JSON_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Replace existing values in a JSON document using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Updates existing values in a JSON object based on their path. `JSON_REPLACE` does not add new keys to the document; it only replaces the values of existing keys.

See also:  
[[`SELECT`]](/docs/language-spec/select) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[` JSON_SET `]](/docs/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/docs/language-spec/functions/json/json_replace)

* * * 

## Syntax

```sql
SELECT JSON_REPLACE(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Arguments

__*json_column*__  
The JSON column or expression from which to replace values.

__*path*__  
The JSON path expression pointing to the location within the JSON document where the replacement should occur. The path must point to an existing key.

__*new_value*__  
The new value to replace at the specified path in the JSON document.

## Return Value(s)
Returns a modified JSON document with the specified value replaced. If the specified path does not exist, the original JSON document is returned unchanged.

:::note Behavior If a Key Exists

| Function                                                     | Overwrite if already exists? | Create if does not exist? |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/docs/language-spec/functions/json/json_insert) | No                           | Yes                       |
| [`json_replace()`](/docs/language-spec/functions/json/json_replace) | Yes                          | No                        |
| [`json_set()`](/docs/language-spec/functions/json/json_set)       | Yes                          | Yes                       |


* * *

## Examples

### Replace a Value in a JSON Object

```sql
SELECT sku as original,
JSON_REPLACE(sku, '$.name', 'Standard_EU') as updated
FROM azure.container_registry.registries 
WHERE registryName = 'exampleRegistryProd' 
AND subscriptionId = '12345678-abcd-1234-abcd-1234abcd5678' 
AND resourceGroupName = 'exampleResourceGroupProd';
/* expected output...
|---------------------------------------|------------------------------------------|
|               original                |                 updated                  |
|---------------------------------------|------------------------------------------|
| {"name":"Standard","tier":"Standard"} | {"name":"Standard_EU","tier":"Standard"} |
|---------------------------------------|------------------------------------------|
*/
```