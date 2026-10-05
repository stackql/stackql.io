---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "JSON_OBJECT function in StackQL: build a JSON object from key and value arguments, for example to construct request bodies for INSERT and UPDATE."
image: "/img/stackql-featured-image.png"
---
Returns an object constructred from arguments provided.  

See also:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Data Types ]](/language-spec/data-types)

* * * 

:::tip 

Use the [**DESCRIBE**](/language-spec/describe) function to locate `array` or `object` datatypes which can be used with StackQL JSON functions.

:::

:::note 

The `json_object` function as `jsonb_build_object` if you are using stackql with a `postgres` SQL backend.

:::

## Syntax

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Arguments

__*key1, key2, ...*__  
The labels for the JSON object.

__*value1, value2, ...*__  
The values corresponding to the labels in the JSON object.

## Return Value(s)
A string representing the well-formed JSON object.

* * *

## Examples

### Return the datatype of a field nested within a JSON object or array

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

For more information, see [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)