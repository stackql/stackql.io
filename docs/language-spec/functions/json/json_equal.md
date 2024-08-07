---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Compare two JSON strings for equivalence using SQL.
image: "/img/stackql-featured-image.png"
---
Compares two JSON strings and returns true if they are equivalent, false otherwise.

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` DESCRIBE `]](/docs/language-spec/describe) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/docs/language-spec/functions/json/json_array_length) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

:::tip 

The `json_equal` function is a custom StackQL extension function. It supports comparing JSON objects and arrays, treating objects with keys in different orders as equivalent according to the JSON specification. The `json_equal` function is available in StackQL versions `0.5.707` and above and is only supported with the default embedded, in-memory SQL backend for StackQL.

:::

## Syntax

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Arguments

__*json1*__  
A JSON string representing an object or array.

__*json2*__  
Another JSON string to compare against the first.

> Both arguments must be valid JSON strings.

## Return Value(s)
An integer value: 1 if the JSON strings are equivalent, 0 if they are not.

* * *

## Examples

### Compare JSON Objects

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM aws.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND data__Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### Compare JSON Arrays

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

For more information, see [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).

