---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "EXEC in StackQL: ruft eine Provider-Ressourcenmethode direkt auf, mit Abfragehinweisen für Ergebnisse oder das Warten auf Abschluss, etwa zum Starten einer Instanz."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Führt eine Ressourcenmethode eines Providers aus (integrierte Prozedur).  

Siehe auch:  
[[ StackQL-Ressourcenhierarchie ]](/getting-started/resource-hierarchy)

* * * 

## Syntax {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Abfragehinweise {#query-hints}

Die für den Befehl `EXEC` unterstützten Abfragehinweise sind nachfolgend zusammengefasst:  

| Hinweis              | Beschreibung                                                                                                                                                                                       |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Gibt die Ergebnismenge zurück, vorgesehen für Methoden, die Daten zurückgeben; verwenden Sie jedoch nach Möglichkeit `SELECT`.                                                                      |
| `/*+ AWAIT  */`      | Macht die Operation blockierend (synchron) und ermöglicht die Skriptsteuerung bei lang laufenden Operationen (z. B. Mutationsoperationen).  Hinweis: Dies wird nicht von allen Providern unterstützt |

* * *

## Beispiele {#examples}

### Eine Google Compute Engine-Instanz stoppen {#stopping-a-google-compute-engine-instance}
Führen Sie eine `EXEC`-Anweisung aus, um eine laufende Compute Engine-Instanz in einer authentifizierten Sitzung zu stoppen.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Eine Google Compute Engine-Instanz starten (synchrone Option) {#starting-a-google-compute-engine-instance-synchronous-option}
Führen Sie eine `EXEC`-Anweisung aus, um eine gestoppte Compute Engine-Instanz in einer authentifizierten Sitzung zu starten; der Abfragehinweis `/*+ AWAIT  */` macht die Operation blockierend (synchron).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Eine AWS EC2-Instanz starten und stoppen {#starting-and-stopping-an-aws-ec2-instance}
Führen Sie eine `EXEC`-Anweisung aus, um eine AWS EC2-Instanz in einer authentifizierten Sitzung zu starten.

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

### Eine Disk an eine Google Compute Engine-Instanz anhängen {#attach-a-disk-to-a-google-compute-engine-instance}
Führen Sie eine `EXEC`-Anweisung aus, um eine Disk in einer authentifizierten Sitzung an eine Compute Engine-Instanz anzuhängen.

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

### Die Entfernung einer Azure Spot-VM simulieren {#simulate-azure-spot-vm-eviction}
Führen Sie eine `EXEC`-Anweisung aus, um die Entfernung einer Azure Spot-VM-Instanz zu simulieren. 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
