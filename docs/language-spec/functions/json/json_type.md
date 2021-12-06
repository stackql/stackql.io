---
title: JSON_TYPE
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
Returns the datatype of the outermost element of an input JSON object or array and path.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` DESCRIBE `]](/docs/language-spec/describe) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

:::tip 

Use the [**DESCRIBE**](/docs/language-spec/describe) function to locate `array` or `object` datatypes which can be used with StackQL JSON functions.

:::

## Syntax

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## Arguments

__*json_array_or_object*__  
A JSON array or object.

__*json_path*__  
The JSON path to an array or object contained within an *json_array_or_object*.  See [jsonpath.com](https://jsonpath.com/).

> If *json_path* does not exist in *json_array_or_object*, then the function returns `NULL`.

> The `json_type()` function throws an error if any of its arguments are not well-formed.

## Return Value(s)
A string representing the resolved datatype.

> Datatypes include `null`, `true`, `false`, `integer`, `real`, `text`, `array`, or `object`.

* * *

## Examples

### Return the datatype of a field nested within a JSON object or array

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

For more information, see [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)