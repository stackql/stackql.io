---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "EXEC в StackQL: прямой вызов метода ресурса провайдера, например запуск или остановка экземпляра, с подсказками запроса для возврата результатов или ожидания завершения."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Выполняет метод ресурса провайдера (встроенную процедуру).  

См. также:  
[[ Иерархия ресурсов StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Синтаксис {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Подсказки запроса {#query-hints}

Подсказки запроса, которые поддерживает инструкция `EXEC`, приведены ниже:  

| Подсказка | Описание |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Возвращает результирующий набор; предназначена для методов, которые возвращают данные, однако по возможности используйте `SELECT`. |
| `/*+ AWAIT  */` | Делает операцию блокирующей (синхронной), обеспечивает управление скриптом при длительных операциях (например, операциях изменения данных).  Примечание: поддерживается не всеми провайдерами |

* * *

## Примеры {#examples}

### Остановка экземпляра Google Compute Engine {#stopping-a-google-compute-engine-instance}
Выполните инструкцию `EXEC`, чтобы остановить работающий экземпляр Compute Engine в аутентифицированном сеансе.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Запуск экземпляра Google Compute Engine (синхронный вариант) {#starting-a-google-compute-engine-instance-synchronous-option}
Выполните инструкцию `EXEC`, чтобы запустить остановленный экземпляр Compute Engine в аутентифицированном сеансе; подсказка запроса `/*+ AWAIT  */` делает операцию блокирующей (синхронной).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Запуск и остановка экземпляра AWS EC2 {#starting-and-stopping-an-aws-ec2-instance}
Выполните инструкции `EXEC`, чтобы запустить и остановить экземпляр AWS EC2 в аутентифицированном сеансе.

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

### Подключение диска к экземпляру Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
Выполните инструкцию `EXEC`, чтобы подключить диск к экземпляру Compute Engine в аутентифицированном сеансе.

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

### Имитация вытеснения точечной виртуальной машины Azure {#simulate-azure-spot-vm-eviction}
Выполните инструкцию `EXEC`, чтобы имитировать вытеснение экземпляра точечной виртуальной машины Azure. 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
