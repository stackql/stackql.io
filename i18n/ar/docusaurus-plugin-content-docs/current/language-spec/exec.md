---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "EXEC في StackQL: استدعاء أسلوب مورد في موفر مباشرة، مع تلميحات الاستعلام لإعادة النتائج أو انتظار الاكتمال، مثل بدء تشغيل مثيل أو إيقافه."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تنفذ أسلوب مورد في موفر (إجراء مدمج).  

راجع أيضًا:  
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy)

* * * 

## بناء الجملة {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## تلميحات الاستعلام {#query-hints}

يلخص الجدول التالي تلميحات الاستعلام المدعومة للأمر `EXEC`:  

| التلميح | الوصف |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | يعيد مجموعة النتائج، وهو مصمم للأساليب التي تعيد بيانات، ولكن استخدم `SELECT` إن أمكن. |
| `/*+ AWAIT  */` | يجعل العملية حاجبة (متزامنة)، ويوفر التحكم في البرامج النصية للعمليات طويلة الأمد (مثل عمليات تعديل البيانات).  ملاحظة: لا يدعم جميع الموفرين هذا التلميح |

* * *

## أمثلة {#examples}

### إيقاف مثيل Google Compute Engine {#stopping-a-google-compute-engine-instance}
شغّل جملة `EXEC` لإيقاف مثيل Compute Engine قيد التشغيل في جلسة مصادق عليها.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### بدء تشغيل مثيل Google Compute Engine (خيار متزامن) {#starting-a-google-compute-engine-instance-synchronous-option}
شغّل جملة `EXEC` لبدء تشغيل مثيل Compute Engine متوقف في جلسة مصادق عليها، مع استخدام تلميح الاستعلام `/*+ AWAIT  */` لجعل العملية حاجبة (متزامنة).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### بدء تشغيل مثيل AWS EC2 وإيقافه {#starting-and-stopping-an-aws-ec2-instance}
شغّل جمل `EXEC` لبدء تشغيل مثيل AWS EC2 وإيقافه في جلسة مصادق عليها.

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

### إرفاق قرص بمثيل Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
شغّل جملة `EXEC` لإرفاق قرص بمثيل Compute Engine في جلسة مصادق عليها.

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

### محاكاة إخلاء Azure Spot VM {#simulate-azure-spot-vm-eviction}
شغّل جملة `EXEC` لمحاكاة إخلاء مثيل Azure Spot VM. 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
