---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "EXEC у StackQL: прямий виклик методу ресурсу провайдера, як-от запуск чи зупинка екземпляра, з підказками для запиту, щоб повернути результати або дочекатися завершення."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Виконує метод ресурсу провайдера (вбудовану процедуру).  

Див. також:  
[[ Ієрархія ресурсів StackQL ]](/getting-started/resource-hierarchy)

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

## Підказки для запиту {#query-hints}

Підказки для запиту, які підтримує інструкція `EXEC`, наведено нижче:  

| Підказка | Опис |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Повертає набір результатів; призначена для методів, що повертають дані, однак за можливості використовуйте `SELECT`. |
| `/*+ AWAIT  */` | Робить операцію блокувальною (синхронною), забезпечує керування скриптом для тривалих операцій (наприклад, операцій змінення даних).  Примітка: не всі провайдери це підтримують |

* * *

## Приклади {#examples}

### Зупинка екземпляра Google Compute Engine {#stopping-a-google-compute-engine-instance}
Виконайте інструкцію `EXEC`, щоб зупинити запущений екземпляр Compute Engine в автентифікованому сеансі.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Запуск екземпляра Google Compute Engine (синхронний варіант) {#starting-a-google-compute-engine-instance-synchronous-option}
Виконайте інструкцію `EXEC`, щоб запустити зупинений екземпляр Compute Engine в автентифікованому сеансі; підказка для запиту `/*+ AWAIT  */` робить операцію блокувальною (синхронною).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Запуск і зупинка екземпляра AWS EC2 {#starting-and-stopping-an-aws-ec2-instance}
Виконайте інструкції `EXEC`, щоб запустити й зупинити екземпляр AWS EC2 в автентифікованому сеансі.

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

### Підключення диска до екземпляра Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
Виконайте інструкцію `EXEC`, щоб підключити диск до екземпляра Compute Engine в автентифікованому сеансі.

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

### Імітація витіснення Azure Spot VM {#simulate-azure-spot-vm-eviction}
Виконайте інструкцію `EXEC`, щоб імітувати витіснення екземпляра Azure Spot VM. 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
