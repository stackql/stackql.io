---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Splits source string based upon a seperator and returns the given part (counting from one).  

See also:  
[[` SELECT `]](/docs/language-spec/select) 

* * * 

## Syntax

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## Arguments

__*source*__  
A literal string or string column that you want to perform the split on.

__*sep*__  
A character or characters you want to use as the separator.  

> If `sep` is composed of multiple characters, each character is treated as separator.  

> Only ASCII (1-byte) symbols are supported as separators.  

__*part*__  
The element (one based) of the split string to return.  

## Return Value(s)
Returns a string.

* * *

## Examples

### Extract elements from a string seperated by a forward slash


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resourceGroupName = 'stackql-ops-cicd-dev-01' 
 AND subscriptionId = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

For more information, see [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)