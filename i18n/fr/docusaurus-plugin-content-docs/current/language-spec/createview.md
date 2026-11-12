---
title: CREATE VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "CREATE VIEW dans StackQL : enregistrer une requête, y compris des UNION multicloud, comme vue nommée ou vue matérialisée dont les résultats sont stockés pour réutilisation."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Crée, au sein d'une session, une `view` ou une `materialized view` qui peut servir à représenter une requête `stackql` longue ou complexe.  

Voir aussi :  
[[ `SELECT` ]](/language-spec/select) [[ `REFRESH` ]](/language-spec/refreshview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## Syntaxe {#syntax}

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

## Exemples {#examples}

### `UNION` multicloud entre `aws` et `google` {#cross-cloud-union-between-aws-and-google}
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

### Créer et utiliser une vue matérialisée {#create-and-use-a-materialized-view}
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
