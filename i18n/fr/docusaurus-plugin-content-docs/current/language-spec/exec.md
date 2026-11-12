---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
description: "EXEC dans StackQL : appeler directement une méthode de ressource, comme démarrer ou arrêter une instance, avec des indications de requête pour renvoyer les résultats ou attendre la fin."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Exécute une méthode de ressource d'un provider (procédure intégrée).  

Voir aussi :  
[[ Hiérarchie des ressources StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Syntaxe {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Indications de requête {#query-hints}

Les indications de requête prises en charge par la commande `EXEC` sont résumées ci-dessous :  

| Indication           | Description                                                                                                                                                                                                                            |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Renvoie le jeu de résultats, conçue pour les méthodes qui renvoient des données ; utilisez toutefois `SELECT` si possible.                                                                                                             |
| `/*+ AWAIT  */`      | Rend l'opération bloquante (synchrone), permet de contrôler le déroulement des scripts pour les opérations de longue durée (par exemple les opérations de mutation).  Remarque : elle n'est pas prise en charge par tous les providers |

* * *

## Exemples {#examples}

### Arrêter une instance Google Compute Engine {#stopping-a-google-compute-engine-instance}
Dans une session authentifiée, exécutez une instruction `EXEC` pour arrêter une instance Compute Engine en cours d'exécution.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Démarrer une instance Google Compute Engine (option synchrone) {#starting-a-google-compute-engine-instance-synchronous-option}
Dans une session authentifiée, exécutez une instruction `EXEC` pour démarrer une instance Compute Engine arrêtée ; l'indication de requête `/*+ AWAIT  */` rend l'opération bloquante (synchrone).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Démarrer et arrêter une instance AWS EC2 {#starting-and-stopping-an-aws-ec2-instance}
Dans une session authentifiée, exécutez une instruction `EXEC` pour démarrer une instance AWS EC2.

```sql
-- Start an EC2 instance
EXEC aws.ec2.instances.start_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
-- Stop an EC2 instance
EXEC aws.ec2.instances.stop_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
```

### Attacher un disque à une instance Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
Dans une session authentifiée, exécutez une instruction `EXEC` pour attacher un disque à une instance Compute Engine.

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC google.compute.instances.attachDisk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```

### Simuler l'éviction d'une VM Azure Spot {#simulate-azure-spot-vm-eviction}
Exécutez une instruction `EXEC` pour simuler l'éviction d'une instance de VM Azure Spot. 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
