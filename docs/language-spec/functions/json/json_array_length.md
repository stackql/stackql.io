---
title: JSON_ARRAY_LENGTH
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
Returns the number of elements in a JSON array.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` DESCRIBE `]](/docs/language-spec/describe) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

:::tip 

Use the [**DESCRIBE**](/docs/language-spec/describe) function to locate `array` or `object` datatypes which can be used with StackQL JSON functions. Use the [**JSON_TYPE**](/docs/language-spec/functions/json/json_type) function to resolve the datatypes of nested objects or array elements.

:::

## Syntax

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Arguments

__*json_array_or_object*__  
A JSON array or object.

> If *json_array_or_object* is a JSON object (not an array) and *json_path* is not specified then `0` is returned.

__*json_path*__  
The optional JSON path to an array contained within an object.  See [jsonpath.com](https://jsonpath.com/).

> If *json_path* is supplied then the `json_array_length` function locates the path within *json_array_or_object* and returns the length of the array or `0` if *json_path* is an object (not an array).  If *json_path* cannot be located within *json_array_or_object*, then `NULL` is returned.

> If *json_array_or_object* or *json_path* are not JSON objects or are not well-formed, then an error is thrown.

## Return Value(s)
Returns an integer.

* * *

## Examples

### Return the number of disks attached to Google Compute Engine instances

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return the number of network interfaces attached to Google Compute Engine instances

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return the number of ACLs associated with Google Storage Buckets

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Return the number of scopes associated with a service account

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

For more information, see [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)