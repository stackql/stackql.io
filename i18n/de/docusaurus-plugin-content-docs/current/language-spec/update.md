---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "UPDATE in StackQL: bestimmte Felder einer Ressourceninstanz ändern, entsprechend einem HTTP PATCH, wobei das Ziel über die WHERE-Klausel bestimmt wird."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Aktualisiert bestimmte Felder einer Ressource (gleichbedeutend mit einer `patch`-Operation).

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Syntax {#syntax}

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

## Beispiele {#examples}

### Einfache `UPDATE`-Anweisung {#basic-update-statement}
Führen Sie eine `UPDATE`-Anweisung aus, um die Größe einer Compute-Engine-Disk-Ressource zu ändern.

```sql
-- Update the size of a Compute Engine Disk resource
UPDATE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### `UPDATE`-Anweisung (blockierend) {#update-statement-blocking}
Führen Sie eine `UPDATE`-Anweisung aus, um die Größe einer Compute-Engine-Disk-Ressource zu ändern. Dies ist ein blockierender (synchroner) Aufruf.

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

### `UPDATE`-Anweisung mit JSON-Änderung {#update-statement-with-json-modification}
Führen Sie eine `UPDATE`-Anweisung aus, um eine Ressource mittels JSON-Manipulation zu ändern.

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
