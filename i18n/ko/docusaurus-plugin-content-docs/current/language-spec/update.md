---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 UPDATE: 리소스 인스턴스의 지정된 필드를 변경하며 HTTP PATCH에 해당하고, 대상은 WHERE 절로 식별합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

리소스의 지정된 필드를 업데이트합니다(`patch` 작업과 같은 의미).

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## 구문 {#syntax}

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

## 예제 {#examples}

### 기본 `UPDATE` 문 {#basic-update-statement}
`UPDATE` 문을 실행하여 Compute Engine Disk 리소스의 크기를 수정합니다.

```sql
-- Update the size of a Compute Engine Disk resource
UPDATE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### `UPDATE` 문(블로킹) {#update-statement-blocking}
`UPDATE` 문을 실행하여 Compute Engine Disk 리소스의 크기를 수정합니다. 이는 블로킹(동기) 호출입니다.

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

### JSON 수정을 사용하는 `UPDATE` 문 {#update-statement-with-json-modification}
`UPDATE` 문을 실행하여 JSON 조작으로 리소스를 수정합니다.

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
