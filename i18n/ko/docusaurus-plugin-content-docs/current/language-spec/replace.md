---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 REPLACE: UPDATE 문이 수행하는 부분 업데이트와 달리, HTTP PUT과 동일하게 리소스 인스턴스의 모든 필드를 대체합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

리소스의 모든 필드를 업데이트합니다(`put` 작업과 동일).

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## 구문 {#syntax}

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

## 예제 {#examples}

### 기본 `REPLACE` 문 {#basic-replace-statement}
`REPLACE` 문을 실행하여 Compute Engine 디스크 리소스의 크기를 수정합니다.

```sql
-- Update the size of a Compute Engine Disk resource
REPLACE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### `REPLACE` 문(블로킹) {#replace-statement-blocking}
`REPLACE` 문을 실행하여 Compute Engine 디스크 리소스의 크기를 수정합니다. 이 호출은 블로킹(동기) 호출입니다.

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

### JSON을 수정하는 `REPLACE` 문 {#replace-statement-with-json-modification}
`REPLACE` 문을 실행하여 JSON 조작으로 리소스를 수정합니다.

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
