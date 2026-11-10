---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "REPLACE in StackQL: ersetzt alle Felder einer Ressourceninstanz, entsprechend einem HTTP PUT, im Gegensatz zur partiellen Aktualisierung durch UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Aktualisiert alle Felder einer Ressource (gleichbedeutend mit einer `put`-Operation).

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Syntax {#syntax}

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

## Beispiele {#examples}

### Einfache `REPLACE`-Anweisung {#basic-replace-statement}
Führen Sie eine `REPLACE`-Anweisung aus, um die Größe einer Compute Engine Disk-Ressource zu ändern.

```sql
-- Update the size of a Compute Engine Disk resource
REPLACE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### `REPLACE`-Anweisung (blockierend) {#replace-statement-blocking}
Führen Sie eine `REPLACE`-Anweisung aus, um die Größe einer Compute Engine Disk-Ressource zu ändern. Dies ist ein blockierender (synchroner) Aufruf.

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

### `REPLACE`-Anweisung mit JSON-Änderung {#replace-statement-with-json-modification}
Führen Sie eine `REPLACE`-Anweisung aus, um eine Ressource mittels JSON-Manipulation zu ändern.

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
