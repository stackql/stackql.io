---
title: CREATE VIEW
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

Creates a `view` within a session which can be used to represent a long or complex `stackql` query.  

See also:  
[[ SELECT ]](/docs/language-spec/select)

* * * 

## Syntax

*useStatement::=*

<RailroadDiagram 
type="createview"
/>

&nbsp;  
&nbsp;  

```sql
CREATE VIEW AS 
<selectStatement> [ UNION | JOIN <selectStatement> ];
```

* * *

:::info

Available in version __`0.3.x`__ and above.

:::


## Examples

### Cross cloud `UNION` between `aws` and `google`
```sql
CREATE VIEW dual_cloud_block_storage AS
SELECT 
 'google' AS vendor, 
 name, 
 split_part(split_part(type, '/', 11), '-', 2) AS type, 
 status, 
 sizeGb AS size 
FROM google.compute.disks 
 WHERE project = '<YOUR_GCP_PROJECT>' 
 AND zone = 'australia-southeast1-a'
UNION
SELECT 
 'aws' AS vendor, 
 volumeId AS name, 
 volumeType AS type, 
 status, 
 size 
FROM aws.ec2.volumes 
 WHERE region = 'ap-southeast-2';
```
