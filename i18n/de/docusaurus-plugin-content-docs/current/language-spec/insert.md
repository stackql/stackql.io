---
title: INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "INSERT in StackQL: erstellt eine oder mehrere Instanzen einer Cloud- oder SaaS-Ressource per SQL-Anweisung, Pflicht- und optionale Felder als Spalten und Werte."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Erstellt eine oder mehrere neue Instanzen einer Ressource. 

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy)

* * * 

## Syntax {#syntax}

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

## `RETURNING`-Klausel {#returning-clause}

Mit der `RETURNING`-Klausel können Sie Daten aus der Antwort der `INSERT`-Operation abrufen. Das ist besonders nützlich, wenn:
- die API wichtige Informationen wie generierte IDs, Statuscodes oder andere Metadaten zurückgibt
- Sie die Antwortdaten zur weiteren Verarbeitung erfassen müssen
- Sie das Ergebnis der Operation überprüfen möchten

Die in der `RETURNING`-Klausel angegebenen Felder werden aus der API-Antwort deserialisiert und in der Ergebnismenge angezeigt.

* * *

## Beispiele {#examples}

### Einfache `INSERT`-Anweisung {#basic-insert-statement}
Führen Sie eine INSERT-Anweisung aus, um eine Compute Engine Disk-Ressource zu erstellen.  Dies ist ein nicht blockierender (asynchroner) Aufruf.

```sql
-- Create a Compute Engine Disk resource
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### `INSERT`-Anweisung (blockierend) {#insert-statement-blocking}
Führen Sie eine INSERT-Anweisung aus, um eine Compute Engine Disk-Ressource zu erstellen.  Dies ist ein blockierender (synchroner) Aufruf.

```sql
-- Create a Compute Engine Disk resource
INSERT /*+ AWAIT  */ INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### `INSERT`-Anweisung mit `SELECT` {#insert-statement-with-select}
Führen Sie eine INSERT-Anweisung mit SELECT aus.

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

### `INSERT`-Anweisung mit `RETURNING`-Klausel {#insert-statement-with-returning-clause}
Führen Sie eine `INSERT`-Anweisung aus und rufen Sie bestimmte Felder aus der API-Antwort ab.

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

Ergebnis:
```
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
|  code  | sqlState |            message             |                                    data                                     |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
| 090001 |    00000 | Statement executed             | [["1","John                                                                 |
|        |          | successfully.                  | Smith","john.smith@example.com","***MASKED***","199.99","20262"],["1","John |
|        |          |                                | Smith","john.smith@example.com","***MASKED***","199.99","20263"]]           |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
```
