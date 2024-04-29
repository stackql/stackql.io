---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Remove elements from a JSON document by specifying paths using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Removes specified elements from a JSON document using path expressions. If a specified path does not exist in the document, it is ignored. Sequential processing of paths can affect the structure for subsequent removals.

See also:  
[[`SELECT`]](/docs/language-spec/select) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract)

* * * 

## Syntax

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Arguments

__*json_document*__  
The JSON document from which elements are to be removed.

__*path1, path2, ...*__  
One or more path expressions pointing to elements in the JSON document to be removed.

## Return Value(s)
Returns a copy of the JSON document with the specified elements removed. If no paths are provided, returns the input JSON reformatted with excess whitespace removed.

* * *

## Examples

### Remove an Element from a JSON Array

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Output: `'[0,1,3,4]'`

### Remove Multiple Elements from a JSON Array

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Output: `'[1,3,4]'`

### Remove Last Element Using Negative Index

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Output: `'[1,2,3]'`

### Remove Non-existent Path

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Output: `'{"x":25,"y":42}'`

### Remove Existing Key from JSON Object

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Output: `'{"x":25}'`

### Edge Case: Removal with Root Path

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Output: `NULL`

These examples illustrate how `json_remove()` can be used to manipulate JSON data dynamically, addressing various scenarios from simple deletions to handling edge cases in data structure modifications.
