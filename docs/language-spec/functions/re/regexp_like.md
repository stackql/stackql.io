---
title: REGEXP_LIKE
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

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Checks if source string matches pattern and returns `true` or `false`.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[ Regular Expression Reference ]](/docs/language-spec/functions/re/reference) 

* * * 

## Syntax

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Arguments

__*source*__  
A literal string or string column that you want to test for the existence of a pattern defined by a regular expression (`pattern`).  

__*pattern*__  
The regular expression pattern to search for.  

## Return Value(s)
Returns a `0` if `false` and `1` if `true`.  

* * *

## Examples

### Determine if a string contains a pattern

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name, 
regexp_like(name, 'vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscriptionId = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

For more information, see [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)