---
title: CREATE VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "CREATE VIEW in StackQL: speichert eine Abfrage, auch Cloud-übergreifende UNIONs, als benannte View oder als materialisierte View mit gespeicherten Ergebnissen."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Erstellt eine `view` oder eine `materialized view` innerhalb einer Sitzung, die eine lange oder komplexe `stackql`-Abfrage repräsentieren kann.  

Siehe auch:  
[[ `SELECT` ]](/language-spec/select) [[ `REFRESH` ]](/language-spec/refreshview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## Syntax {#syntax}

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

## Beispiele {#examples}

### Cloud-übergreifende `UNION` zwischen `aws` und `google` {#cross-cloud-union-between-aws-and-google}
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

### Eine materialisierte View erstellen und verwenden {#create-and-use-a-materialized-view}
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
