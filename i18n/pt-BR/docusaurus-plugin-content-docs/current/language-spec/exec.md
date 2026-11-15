---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "EXEC no StackQL: chamar diretamente um método de recurso de um provider, com dicas de consulta para retornar resultados ou aguardar a conclusão, como iniciar ou parar uma instância."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Executa um método de recurso de um provider (procedimento integrado).

Veja também:  
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaxe {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Dicas de consulta {#query-hints}

As dicas de consulta compatíveis com o comando `EXEC` estão resumidas a seguir:

| Dica                 | Descrição                                                                                                                                                                  |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Retorna o conjunto de resultados, projetada para métodos que retornam dados; no entanto, use `SELECT` se possível.                                                        |
| `/*+ AWAIT  */`      | Torna a operação bloqueante (síncrona) e oferece controle de script para operações de longa duração (por exemplo, operações de mutação). Nota: nem todos os providers oferecem suporte a isso |

* * *

## Exemplos {#examples}

### Parar uma instância do Google Compute Engine {#stopping-a-google-compute-engine-instance}
Execute uma instrução `EXEC` para parar uma instância do Compute Engine em execução em uma sessão autenticada.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Iniciar uma instância do Google Compute Engine (opção síncrona) {#starting-a-google-compute-engine-instance-synchronous-option}
Execute uma instrução `EXEC` para iniciar uma instância do Compute Engine parada em uma sessão autenticada; a dica de consulta `/*+ AWAIT  */` torna a operação bloqueante (síncrona).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Iniciar e parar uma instância do AWS EC2 {#starting-and-stopping-an-aws-ec2-instance}
Execute uma instrução `EXEC` para iniciar uma instância do AWS EC2 em uma sessão autenticada.

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

### Anexar um disco a uma instância do Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
Execute uma instrução `EXEC` para anexar um disco a uma instância do Compute Engine em uma sessão autenticada.

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

### Simular a remoção de uma VM Spot do Azure {#simulate-azure-spot-vm-eviction}
Execute uma instrução `EXEC` para simular a remoção de uma instância de VM Spot do Azure.

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
