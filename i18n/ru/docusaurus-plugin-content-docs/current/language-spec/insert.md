---
title: INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "INSERT в StackQL: создание одного или нескольких экземпляров ресурса облака или SaaS SQL-инструкцией с обязательными и необязательными полями в виде столбцов и значений."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Создает новый экземпляр или экземпляры ресурса.

См. также:  
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Синтаксис {#syntax}

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

## Предложение `RETURNING` {#returning-clause}

Предложение `RETURNING` позволяет получать данные из ответа операции `INSERT`. Это особенно полезно, когда:
- API возвращает важную информацию, например сгенерированные идентификаторы, коды состояния или другие метаданные
- вам нужно сохранить данные ответа для дальнейшей обработки
- вы хотите проверить результат операции

Поля, указанные в предложении `RETURNING`, десериализуются из ответа API и отображаются в результирующем наборе.

* * *

## Примеры {#examples}

### Базовая инструкция `INSERT` {#basic-insert-statement}
Выполните инструкцию `INSERT`, чтобы создать ресурс диска Compute Engine. Это неблокирующий (асинхронный) вызов.

```sql
-- Create a Compute Engine Disk resource
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### Инструкция `INSERT` (блокирующая) {#insert-statement-blocking}
Выполните инструкцию `INSERT`, чтобы создать ресурс диска Compute Engine. Это блокирующий (синхронный) вызов.

```sql
-- Create a Compute Engine Disk resource
INSERT /*+ AWAIT  */ INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### Инструкция `INSERT` с инструкцией `SELECT` {#insert-statement-with-select}
Выполните инструкцию `INSERT` с инструкцией `SELECT`.

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

### Инструкция `INSERT` с предложением `RETURNING` {#insert-statement-with-returning-clause}
Выполните инструкцию `INSERT` и получите определенные поля из ответа API.

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

Результат:
```
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
|  code  | sqlState |            message             |                                    data                                     |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
| 090001 |    00000 | Statement executed             | [["1","John                                                                 |
|        |          | successfully.                  | Smith","john.smith@example.com","***MASKED***","199.99","20262"],["1","John |
|        |          |                                | Smith","john.smith@example.com","***MASKED***","199.99","20263"]]           |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
```
