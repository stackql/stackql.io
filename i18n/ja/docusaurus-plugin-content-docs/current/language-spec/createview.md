---
title: CREATE VIEW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の CREATE VIEW：クエリ（クロスクラウドの UNION を含む）を名前付きビューとして、または結果を保存して再利用するマテリアライズドビューとして保存します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

セッション内に `view` または `materialized view` を作成します。長いクエリや複雑な `stackql` クエリを表すために使用できます。  

関連項目：  
[[ `SELECT` ]](/language-spec/select) [[ `REFRESH` ]](/language-spec/refreshview) [[ `DROP` ]](/language-spec/dropview)

* * * 

## 構文 {#syntax}

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

## 例 {#examples}

### `aws` と `google` の間のクロスクラウド `UNION` {#cross-cloud-union-between-aws-and-google}
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

### マテリアライズドビューを作成して使用する {#create-and-use-a-materialized-view}
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
