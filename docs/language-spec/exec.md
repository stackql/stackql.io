---
title: EXEC
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
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Executes a provider resource method (built in procedure).  

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * * 

## Syntax

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Query Hints

Query hints supported for the `EXEC` command are summarised below:  

| Hint                 | Description                                                                                                                 |
|----------------------|-----------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Returns result set, designed for methods that return data, use `SELECT` however if possible.                                |
| `/*+ AWAIT  */`      | Makes the operation blocking (synchronous), provides script control for long running operations (e.g. mutation operations). |

* * *

## Examples

### Stopping a Google Compute Engine instance
Run an EXEC statement to stop a running Compute Engine instance in an authenticated session.

```sql
-- Stop a running Compute Engine resource instance
EXEC compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Starting a Google Compute Engine instance (synchronous option)
Run an EXEC statement to start a stopped Compute Engine instance in an authenticated session, uses the `/*+ AWAIT  */` query hint to make the operation blocking (synchronous).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Attach a Disk to a Google Compute Engine instance
Run an EXEC statement to attach a Disk to a Compute Engine instance in an authenticated session.

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC compute.instances.attachDisk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```
