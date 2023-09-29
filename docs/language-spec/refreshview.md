---
title: REFRESH VIEW
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
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Refreshes the data in a `materialized view` within a session.  

See also:  
[[ `CREATE` ]](/docs/language-spec/createview) [[ `DROP` ]](/docs/language-spec/dropview)

* * * 

## Syntax

*refreshViewStatement::=*

<RailroadDiagram 
type="refreshview"
/>

&nbsp;  
&nbsp;  

```sql
REFRESH MATERIALIZED VIEW <viewName>;
```

* * *

## Examples

### Refresh a Materialized View
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```
