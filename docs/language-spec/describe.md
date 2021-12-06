---
title: DESCRIBE
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

Describes a resource, including all of the output fields for a given resource.  

See also:  
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy)

* * * 

## Syntax

*describeStatement::=*

<RailroadDiagram 
type="describe"
/>

&nbsp;  
&nbsp;  

```sql
DESCRIBE [ EXTENDED ] <multipartIdentifier> ;
```

* * *

## Examples

### Basic DESCRIBE Statement
Run a basic DESCRIBE statement to list the fields in a resource from an authenticated session.

```sql
-- Show the available fields in a Compute Engine resource
USE google;
DESCRIBE compute.instances;
```

### Extended DESCRIBE Statement
Run an extended DESCRIBE statement to list the fields in a resource and their descriptions from an authenticated session.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```