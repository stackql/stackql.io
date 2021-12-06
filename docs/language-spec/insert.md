---
title: INSERT
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

Creates a new instance or instances of a resource. 

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * * 

## Syntax

*insertStatement::=*

<RailroadDiagram 
type="insert"
/>

&nbsp;  
&nbsp;

```sql
INSERT [ /*+ AWAIT  */ ] [ IGNORE ] [ INTO ] <multipartIdentifier>
[ (field_name [, field_name] ...) { VALUES (field_name [, field_name] ...) | <selectExpression>; } ;
```

* * *

## Examples

### Basic INSERT statement
Run an INSERT statement to create a Compute Engine Disk resource.  This is a non blocking (asynchronous) invocation.

```sql
-- Create a Compute Engine Disk resource
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### INSERT statement (blocking)
Run an INSERT statement to create a Compute Engine Disk resource.  This is a blocking (synchronous) invocation.

```sql
-- Create a Compute Engine Disk resource
INSERT /*+ AWAIT  */ INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```
