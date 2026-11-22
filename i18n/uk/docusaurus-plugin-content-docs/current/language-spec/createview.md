---
title: CREATE VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "CREATE VIEW у StackQL: збереження запиту, зокрема міжхмарних UNION, як іменованого подання або матеріалізованого, результати якого зберігаються для повторного використання."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Створює подання (`view`) або матеріалізоване подання (`materialized view`) у межах сеансу, яке можна використовувати замість довгого або складного запиту `stackql`.  

Див. також:  
[[ `SELECT` ]](/language-spec/select) [[ `REFRESH` ]](/language-spec/refreshview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## Синтаксис {#syntax}

*createViewStatement::=*

<RailroadDiagram 
type="createview"
/>

&nbsp;  
&nbsp;  

```sql
CREATE [ OR REPLACE ] [ MATERIALIZED ] VIEW <viewName> AS 
<selectStatement> [ UNION | JOIN <selectStatement> ];
```

* * *

## Приклади {#examples}

### Міжхмарне об'єднання `UNION` між `aws` і `google` {#cross-cloud-union-between-aws-and-google}
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

### Створення й використання матеріалізованого подання {#create-and-use-a-materialized-view}
```sql
/* -------------------------- */
/* create a materialized view */
/* -------------------------- */
CREATE MATERIALIZED VIEW vw_ec2_instance_types AS 
SELECT 
 memoryInfo, 
 hypervisor, 
 autoRecoverySupported, 
 instanceType, 
 SPLIT_PART(processorInfo, '\n', 3) as processorArch, 
 currentGeneration, 
 freeTierEligible, 
 hibernationSupported,
 SPLIT_PART(vCpuInfo, '\n', 2) as vCPUs, 
 bareMetal, 
 burstablePerformanceSupported, 
 dedicatedHostsSupported 
FROM aws.ec2.instance_types 
WHERE region = 'us-east-1';
/* ------------------------- */
/* use the materialized view */
/* ------------------------- */
SELECT 
 i.instanceId, 
 i.instanceType, 
 it.vCPUs, 
 it.memoryInfo 
FROM aws.ec2.instances i 
 INNER JOIN vw_ec2_instance_types it 
 ON i.instanceType = it.instanceType 
WHERE i.region = 'us-east-1';
```
