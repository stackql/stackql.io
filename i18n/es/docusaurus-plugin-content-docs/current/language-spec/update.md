---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "UPDATE en StackQL: cambiar los campos especificados de una instancia de recurso, equivalente a un PATCH de HTTP, con el destino identificado por la cláusula WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Actualiza los campos especificados de un recurso (sinónimo de una operación `patch`).

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Sintaxis {#syntax}

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

## Ejemplos {#examples}

### Instrucción `UPDATE` básica {#basic-update-statement}
Ejecute una instrucción `UPDATE` para modificar el tamaño de un recurso Disk de Compute Engine.

```sql
-- Update the size of a Compute Engine Disk resource
UPDATE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### Instrucción `UPDATE` (con bloqueo) {#update-statement-blocking}
Ejecute una instrucción `UPDATE` para modificar el tamaño de un recurso Disk de Compute Engine. Se trata de una invocación con bloqueo (síncrona).

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

### Instrucción `UPDATE` con modificación de JSON {#update-statement-with-json-modification}
Ejecute una instrucción `UPDATE` para modificar un recurso mediante la manipulación de JSON.

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
