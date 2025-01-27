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
image: "/img/stackql-featured-image.png"
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

| Hint                 | Description                                                                                                                                                                |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Returns result set, designed for methods that return data, use `SELECT` however if possible.                                                                               |
| `/*+ AWAIT  */`      | Makes the operation blocking (synchronous), provides script control for long running operations (e.g. mutation operations).  Note: this is not supported for all providers |

* * *

## Examples

### Stopping a Google Compute Engine instance
Run an `EXEC` statement to stop a running Compute Engine instance in an authenticated session.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Starting a Google Compute Engine instance (synchronous option)
Run an `EXEC` statement to start a stopped Compute Engine instance in an authenticated session, uses the `/*+ AWAIT  */` query hint to make the operation blocking (synchronous).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Starting and stopping an AWS EC2 instance
Run an `EXEC` statement to start an AWS EC2 instance in an authenticated session.

```sql
-- Start an EC2 instance
EXEC aws.ec2_native.instances.start 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
-- Stop an EC2 instance
EXEC aws.ec2_native.instances.stop 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
```

### Attach a Disk to a Google Compute Engine instance
Run an `EXEC` statement to attach a Disk to a Compute Engine instance in an authenticated session.

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC google.compute.instances.attachDisk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```

### Simulate Azure Spot VM eviction
Run an `EXEC` statement to simulate eviction of Azure Spot VM Instance. 

```sql
exec azure.compute.virtual_machine.simulate_eviction
@subscriptionId = '0123456789',
@resourceGroupName = 'vmss-flex',
@vmName = 'vmss-flex-manual-1';
```
