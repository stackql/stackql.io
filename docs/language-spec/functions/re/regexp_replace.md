---
title: REGEXP_REPLACE
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

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Replaces a matching substring with replacement string.  

See also:  
[[` SELECT `]](/docs/language-spec/select) [[ Regular Expression Reference ]](/docs/language-spec/functions/re/reference) 

* * * 

## Syntax

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Arguments

__*source*__  
A literal string or string column from which you want to extract a substring based upon a pattern defined by a regular expression (`pattern`).  

__*pattern*__  
The regular expression pattern to search for.  

__*replacement*__  
The replacement string.

> Supports backreferences to captured groups `\1` through `\9` in replacement string.  

## Return Value(s)
Returns a string.

* * *

## Examples

### Replace a regular expression pattern in a column

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
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscriptionId = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

For more information, see [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)