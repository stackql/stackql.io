---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REPLACE：替换资源实例的所有字段，相当于 HTTP PUT，与 UPDATE 执行的部分更新相对。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

更新资源中的所有字段（等同于 `put` 操作）。

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## 语法 {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## 示例 {#examples}

### 基本 `REPLACE` 语句 {#basic-replace-statement}
运行 `REPLACE` 语句，修改 Compute Engine 磁盘资源的大小。

```sql
-- Update the size of a Compute Engine Disk resource
REPLACE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### `REPLACE` 语句（阻塞） {#replace-statement-blocking}
运行 `REPLACE` 语句，修改 Compute Engine 磁盘资源的大小。这是一次阻塞（同步）调用。

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo' 
```

### 带 JSON 修改的 `REPLACE` 语句 {#replace-statement-with-json-modification}
运行 `REPLACE` 语句，通过处理 JSON 来修改资源。

```sql
-- Update an Azure Network Interface's properties
REPLACE azure.network.network_interfaces
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
