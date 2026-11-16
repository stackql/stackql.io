---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: "EXEC en StackQL: llamar directamente a un método de recurso de un provider, con indicaciones de consulta para devolver resultados o esperar a que termine, como iniciar o detener una instancia."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Ejecuta un método de recurso de un provider (procedimiento integrado).  

Consulte también:  
[[ Jerarquía de recursos de StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaxis {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Indicaciones de consulta {#query-hints}

Las indicaciones de consulta admitidas para el comando `EXEC` se resumen a continuación:  

| Indicación           | Descripción                                                                                                                                                                |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Devuelve el conjunto de resultados, está pensada para métodos que devuelven datos; no obstante, use `SELECT` si es posible.                                                |
| `/*+ AWAIT  */`      | Hace que la operación sea bloqueante (síncrona), permite controlar el script en operaciones de larga duración (por ejemplo, operaciones de mutación). Nota: no se admite en todos los providers |

* * *

## Ejemplos {#examples}

### Detener una instancia de Google Compute Engine {#stopping-a-google-compute-engine-instance}
Ejecute una instrucción `EXEC` para detener una instancia de Compute Engine en ejecución en una sesión autenticada.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Iniciar una instancia de Google Compute Engine (opción síncrona) {#starting-a-google-compute-engine-instance-synchronous-option}
Ejecute una instrucción `EXEC` para iniciar una instancia de Compute Engine detenida en una sesión autenticada; se usa la indicación de consulta `/*+ AWAIT  */` para que la operación sea bloqueante (síncrona).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Iniciar y detener una instancia de AWS EC2 {#starting-and-stopping-an-aws-ec2-instance}
Ejecute instrucciones `EXEC` para iniciar y detener una instancia de AWS EC2 en una sesión autenticada.

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

### Conectar un disco a una instancia de Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
Ejecute una instrucción `EXEC` para conectar un disco a una instancia de Compute Engine en una sesión autenticada.

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC google.compute.instances.attach_disk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```

### Simular la expulsión de una VM de Azure Spot {#simulate-azure-spot-vm-eviction}
Ejecute una instrucción `EXEC` para simular la expulsión de una instancia de VM de Azure Spot.

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
