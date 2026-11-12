---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "REPLACE dans StackQL : remplacer tous les champs d'une instance de ressource, équivalent à un PUT HTTP, par opposition à la mise à jour partielle effectuée par UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Met à jour tous les champs d'une ressource (synonyme d'une opération `put`).

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Syntaxe {#syntax}

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

## Exemples {#examples}

### Instruction `REPLACE` de base {#basic-replace-statement}
Exécutez une instruction `REPLACE` pour modifier la taille d'une ressource Compute Engine Disk.

```sql
-- Update the size of a Compute Engine Disk resource
REPLACE google.compute.disks 
SET sizeGb = 20
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name = 'test10gbdisk';
```

### Instruction `REPLACE` (bloquante) {#replace-statement-blocking}
Exécutez une instruction `REPLACE` pour modifier la taille d'une ressource Compute Engine Disk. Il s'agit d'un appel bloquant (synchrone).

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

### Instruction `REPLACE` avec modification de JSON {#replace-statement-with-json-modification}
Exécutez une instruction `REPLACE` pour modifier une ressource par manipulation de JSON.

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
