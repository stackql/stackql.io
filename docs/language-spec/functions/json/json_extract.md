---
title: JSON_EXTRACT
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
Returns one or more values from a well-formed JSON object or array.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` DESCRIBE `]](/docs/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/docs/language-spec/functions/json/json_array_length) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

:::tip 

Use the [**DESCRIBE**](/docs/language-spec/describe) function to locate `array` or `object` datatypes which can be used with StackQL JSON functions. Use the [**JSON_TYPE**](/docs/language-spec/functions/json/json_type) function to resolve the datatypes of nested objects or array elements.

:::

## Syntax

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Arguments

__*json_array_or_object*__  
A JSON array or object.

__*json_path*__  
The JSON path to an array or object contained within an *json_array_or_object*.  See [jsonpath.com](https://jsonpath.com/).

> If a single *json_path* is provided then a scalar result is returned mapping to the datatype of the resolved path.

> If multiple *json_path* arguments are provided, then the result will be an array composed of objects resolved by the repsective paths.

> Specifying `'$'` for the *json_path* argument will return the entire JSON object corresponding to the column represented by the *json_array_or_object* argument.


## Return Value(s)
A string, integer, object, array, boolean or null depending upon the *json_array_or_object* and *json_path* provided as input.

* * *

## Examples

### Return the disk objects associated with each Google Compute Engine instance

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return the device name and size of the first disk attached to each Google Compute Engine instance

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return the network tier of first NIC attached to each Google Compute Engine instances

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return a list of scopes associated with the first service account associated with each Google Compute Engine instance

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

For more information, see [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)