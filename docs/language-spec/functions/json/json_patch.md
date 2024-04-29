---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Apply a JSON MergePatch to a JSON document using SQL in StackQL, modifying or merging JSON objects.
image: "/img/stackql-featured-image.png"
---
Applies a JSON MergePatch (RFC-7396) to a JSON document, modifying it according to specified operations. This function is used to add, modify, or delete elements within JSON objects but treats JSON arrays as atomic units.

See also:  
[[`SELECT`]](/docs/language-spec/select) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract)

* * * 

## Syntax

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Arguments

__*target_json*__  
The JSON document to be patched.

__*patch_json*__  
A JSON document describing the changes to be applied according to the MergePatch algorithm.

## Return Value(s)
Returns the modified JSON document after the patch has been applied.

* * *

## Examples

### Simple MergePatch

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Output: `{"a":1,"b":2,"c":3,"d":4}`

### Replace Element in Array

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Output: `{"a":9,"b":2}`

### Remove Element Using Null

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Output: `{"a":9,"c":8}`

### Nested MergePatch

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Output: `{"a":{"x":1,"y":9},"b":3,"c":8}`

This functionality allows for flexible and dynamic updates to JSON stored in a database, particularly useful for applications requiring conditional modifications of data structures.
