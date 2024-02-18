---
title: JSON_EACH
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
Table-valued function which returns a table consisting of one row for each array element or object member.

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` DESCRIBE `]](/docs/language-spec/describe) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/docs/language-spec/functions/json/json_array_length) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

:::tip 

Use the [**DESCRIBE**](/docs/language-spec/describe) function to locate `array` datatypes which can be used with the `json_each` function.

:::

:::note 

The `json_each` function is implemented as the `json_array_elements_text` function if you are using stackql with a `postgres` sql backend

:::

## Syntax

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## Arguments

__*json_array_or_object*__  
A JSON array or object.

__*json_path*__  
The JSON path to an array or object contained within an *json_array_or_object*.  See [jsonpath.com](https://jsonpath.com/).

> If *json_path* does not exist in *json_array_or_object*, then the function returns `NULL`.

> The `json_type()` function throws an error if any of its arguments are not well-formed.

## Return Value(s)
A table consisting of one row for each array element or object member.

* * *

## Examples

### Unnest an array of strings into a table joined with associated fields

for context:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

returns...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

The following `stackql` query uses the `json_each` function to unnest the `members` array into a table joined with the `role` and optionally `condition` fields.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

would return...

```
|--------------------------------------|-------------------------------------------------------|                                                                                                     
|                 role                 |                     member                            |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com           |                                                                                                     
|--------------------------------------|---------------------------------------------- --------|                                                                                                     
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com    |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com        |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| ...                                  | ...                                                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|    
```

For more information, see [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)