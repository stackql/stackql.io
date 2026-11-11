---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 UPDATE：更改资源实例的指定字段，相当于 HTTP PATCH，目标由 WHERE 子句确定。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

更新资源中的指定字段（与 `patch` 操作同义）。

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## 语法 {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## 示例 {#examples}

### 基本 `UPDATE` 语句 {#basic-update-statement}
运行 `UPDATE` 语句，修改 Compute Engine Disk 资源的大小。

```sql
-- Update the size of a Compute Engine Disk resource
UPDATE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### `UPDATE` 语句（阻塞） {#update-statement-blocking}
运行 `UPDATE` 语句，修改 Compute Engine Disk 资源的大小。这是一次阻塞（同步）调用。

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo' 
```

### 修改 JSON 的 `UPDATE` 语句 {#update-statement-with-json-modification}
运行 `UPDATE` 语句，通过 JSON 操作修改资源。

```sql
-- Update an Azure Network Interface's properties
UPDATE azure.network.network_interfaces
SET data__properties = json_replace(
  json_remove(data__properties,
    '$.auxiliarySku',
    '$.provisioningState',
    '$.resourceGuid',
    '$.macAddress'
  ),
  '$.ipConfigurations[0].name',
  'newIpConfigurationName'
)
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND network_interface_name = 'vmss-nic-01';
```
