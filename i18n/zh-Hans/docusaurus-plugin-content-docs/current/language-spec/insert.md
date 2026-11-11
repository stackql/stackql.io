---
title: INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 INSERT：使用 SQL 语句创建云或 SaaS 资源的一个或多个实例，必填和可选字段以列和值的形式传入。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

创建资源的一个或多个新实例。 

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy)

* * * 

## 语法 {#syntax}

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

`RETURNING` 子句可用于从 `INSERT` 操作的响应中获取数据。这在以下情况下特别有用：
- API 返回重要信息，例如生成的 ID、状态码或其他元数据
- 需要捕获响应数据以便进一步处理
- 希望验证操作结果

`RETURNING` 子句中指定的字段将从 API 响应中反序列化，并显示在结果集中。

* * *

## 示例 {#examples}

### 基本 `INSERT` 语句 {#basic-insert-statement}
运行 INSERT 语句，创建 Compute Engine 磁盘资源。这是一次非阻塞（异步）调用。

```sql
-- Create a Compute Engine Disk resource
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### `INSERT` 语句（阻塞） {#insert-statement-blocking}
运行 INSERT 语句，创建 Compute Engine 磁盘资源。这是一次阻塞（同步）调用。

```sql
-- Create a Compute Engine Disk resource
INSERT /*+ AWAIT  */ INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### 带 `SELECT` 的 `INSERT` 语句 {#insert-statement-with-select}
运行带 SELECT 的 INSERT 语句。

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

### 带 `RETURNING` 子句的 `INSERT` 语句 {#insert-statement-with-returning-clause}
运行 `INSERT` 语句，并从 API 响应中获取指定字段。

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

结果：
```
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
|  code  | sqlState |            message             |                                    data                                     |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
| 090001 |    00000 | Statement executed             | [["1","John                                                                 |
|        |          | successfully.                  | Smith","john.smith@example.com","***MASKED***","199.99","20262"],["1","John |
|        |          |                                | Smith","john.smith@example.com","***MASKED***","199.99","20263"]]           |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
```
