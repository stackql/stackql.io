---
title: DROP VIEW
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
//import Gist from 'react-gist';

Drops a `view` or `materialized view` within a session.  

See also:  
 [[ `CREATE` ]](/docs/language-spec/createview) [[ `REFRESH` ]](/docs/language-spec/refreshview)

* * * 

## Syntax

*dropViewStatement::=*

<RailroadDiagram 
type="dropview"
/>

&nbsp;  
&nbsp;  

```sql
DROP [ MATERIALIZED ] VIEW <viewName>;
```

* * *

## Examples

### Drop a Materialized View
```sql
DROP MATERIALIZED VIEW vw_ec2_instance_types;
```
