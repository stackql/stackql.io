---
title: INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 INSERT：以 SQL 陳述式建立雲端或 SaaS 資源的一或多個執行個體，並以資料行和值傳入必要與選用欄位。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

建立資源的一個或多個新執行個體。

另請參閱：  
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy)

* * * 

## 語法 {#syntax}

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

## `RETURNING` 子句 {#returning-clause}

`RETURNING` 子句可讓您從 `INSERT` 作業的回應中擷取資料。這在下列情況特別有用：
- API 傳回重要資訊，例如產生的 ID、狀態碼或其他中繼資料
- 您需要擷取回應資料以供後續處理
- 您想要確認作業結果

`RETURNING` 子句中指定的欄位會從 API 回應還原序列化，並顯示在結果集中。

* * *

## 範例 {#examples}

### 基本 `INSERT` 陳述式 {#basic-insert-statement}
執行 `INSERT` 陳述式以建立 Compute Engine Disk 資源。這是非封鎖式（非同步）叫用。

```sql
-- Create a Compute Engine Disk resource
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### `INSERT` 陳述式（封鎖式） {#insert-statement-blocking}
執行 `INSERT` 陳述式以建立 Compute Engine Disk 資源。這是封鎖式（同步）叫用。

```sql
-- Create a Compute Engine Disk resource
INSERT /*+ AWAIT  */ INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### 搭配 `SELECT` 的 `INSERT` 陳述式 {#insert-statement-with-select}
執行搭配 `SELECT` 的 `INSERT` 陳述式。

```sql
-- Create an Azure Network Interface
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

### 搭配 `RETURNING` 子句的 `INSERT` 陳述式 {#insert-statement-with-returning-clause}
執行 `INSERT` 陳述式，並從 API 回應中擷取特定欄位。

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

結果：
```
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
|  code  | sqlState |            message             |                                    data                                     |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
| 090001 |    00000 | Statement executed             | [["1","John                                                                 |
|        |          | successfully.                  | Smith","john.smith@example.com","***MASKED***","199.99","20262"],["1","John |
|        |          |                                | Smith","john.smith@example.com","***MASKED***","199.99","20263"]]           |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
```
