---
title: INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 INSERT: 필수 필드와 선택 필드를 열과 값으로 전달하여 SQL 문으로 클라우드 또는 SaaS 리소스의 인스턴스를 하나 이상 생성합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

리소스의 새 인스턴스를 하나 이상 생성합니다. 

참고 항목:  
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy)

* * * 

## 구문 {#syntax}

*insertStatement::=*

<RailroadDiagram 
type="insert"
/>

&nbsp;  
&nbsp;

```sql
INSERT [ /*+ AWAIT  */ ] [ IGNORE ] [ INTO ] <multipartIdentifier>
( field_name [, field_name] ... )
{ VALUES ( value [, value] ... ) | <selectExpression> }
[ RETURNING field_name [, field_name] ... ];
```

* * *

## `RETURNING` 절 {#returning-clause}

`RETURNING` 절을 사용하면 `INSERT` 작업의 응답에서 데이터를 가져올 수 있습니다. 이 기능은 특히 다음과 같은 경우에 유용합니다.
- API가 생성된 ID, 상태 코드 또는 기타 메타데이터와 같은 중요한 정보를 반환하는 경우
- 추가 처리를 위해 응답 데이터를 캡처해야 하는 경우
- 작업 결과를 확인하려는 경우

`RETURNING` 절에 지정한 필드는 API 응답에서 역직렬화되어 결과 집합에 표시됩니다.

* * *

## 예제 {#examples}

### 기본 `INSERT` 문 {#basic-insert-statement}
INSERT 문을 실행하여 Compute Engine 디스크 리소스를 생성합니다.  이 호출은 논블로킹(비동기) 호출입니다.

```sql
-- Create a Compute Engine Disk resource
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### `INSERT` 문(블로킹) {#insert-statement-blocking}
INSERT 문을 실행하여 Compute Engine 디스크 리소스를 생성합니다.  이 호출은 블로킹(동기) 호출입니다.

```sql
-- Create a Compute Engine Disk resource
INSERT /*+ AWAIT  */ INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### `SELECT` 문을 사용하는 `INSERT` 문 {#insert-statement-with-select}
SELECT 문과 함께 INSERT 문을 실행합니다.

```sql
-- Create a Azure Network Interface
INSERT INTO azure.network.network_interfaces
  (
    resource_group_name,
    subscription_id,
    network_interface_name,
    data__location,
    data__properties
  )
  SELECT
    'vmss-flex',
    '0123456789',
    'vmss-flex-vnet-nic01-manual-' || '1',
    'eastus',
    json_replace(json_remove(properties,
                                  '$.allowPort25Out',
                                  '$.auxiliarySku',
                                  '$.provisioningState',
                                  '$.resourceGuid',
                                  '$.macAddress',
                                  '$.vnetEncryptionSupported',
                                  '$.enableIPForwarding',
                                  '$.defaultOutboundAccess',
                                  '$.primary',
                                  '$.virtualMachine',
                                  '$.hostedWorkloads',
                                  '$.tapConfigurations',
                                  '$.nicType',
                                  '$.auxiliaryMode',
                                  '$.ipConfigurations[0].id',
                                  '$.ipConfigurations[0].etag',
                                  '$.ipConfigurations[0].type',
                                  '$.ipConfigurations[0].properties.provisioningState',
                                  '$.ipConfigurations[0].properties.privateIPAddress',
                                  '$.ipConfigurations[0].properties.privateIPAllocationMethod',
                                  '$.ipConfigurations[0].properties.primary',
                                  '$.ipConfigurations[0].properties.privateIPAddressVersion'
                                ),
                      '$.ipConfigurations[0].name',
                      'vmss-flex-vnet-nic01-defaultIpConfiguration'
                      )
  FROM
    azure.network.network_interfaces
  WHERE subscription_id = '0123456789'
  AND resource_group_name = 'vmss-flex'
  AND network_interface_name = 'vmss-nic-01';
```

### `RETURNING` 절을 사용하는 `INSERT` 문 {#insert-statement-with-returning-clause}
`INSERT` 문을 실행하고 API 응답에서 특정 필드를 가져옵니다.

```sql
-- Execute a Snowflake SQL statement and return the response data
INSERT INTO snowflake.sqlapi.statements (
    data__statement,
    data__timeout,
    data__database,
    data__schema,
    "User-Agent",
    endpoint
)
SELECT
    'SELECT
        customer_id,
        customer_name,
        email,
        credit_card_number,
        purchase_amount,
        purchase_date
    FROM CUSTOMER_DATA',
    10,
    'MY_DATABASE',
    'MY_SCHEMA',
    'User-Agent',
    'ABCDEF-XY12345'
RETURNING code, sqlState, message, data;
```

결과:
```
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
|  code  | sqlState |            message             |                                    data                                     |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
| 090001 |    00000 | Statement executed             | [["1","John                                                                 |
|        |          | successfully.                  | Smith","john.smith@example.com","***MASKED***","199.99","20262"],["1","John |
|        |          |                                | Smith","john.smith@example.com","***MASKED***","199.99","20263"]]           |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
```
