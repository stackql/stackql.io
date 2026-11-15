---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "REPLACE en StackQL: reemplazar todos los campos de una instancia de recurso, equivalente a un PUT de HTTP, a diferencia de la actualización parcial que realiza UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Actualiza todos los campos de un recurso (sinónimo de una operación `put`).

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Sintaxis {#syntax}

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

## Ejemplos {#examples}

### Instrucción `REPLACE` básica {#basic-replace-statement}
Ejecute una instrucción `REPLACE` para modificar el tamaño de un recurso Disk de Compute Engine.

```sql
-- Update the size of a Compute Engine Disk resource
REPLACE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### Instrucción `REPLACE` (con bloqueo) {#replace-statement-blocking}
Ejecute una instrucción `REPLACE` para modificar el tamaño de un recurso Disk de Compute Engine. Se trata de una invocación con bloqueo (síncrona).

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

### Instrucción `REPLACE` con modificación de JSON {#replace-statement-with-json-modification}
Ejecute una instrucción `REPLACE` para modificar un recurso mediante la manipulación de JSON.

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
