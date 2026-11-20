---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "UPDATE في StackQL: تغيير حقول محددة في مثيل مورد، بما يكافئ HTTP PATCH، مع تحديد الهدف بواسطة عبارة WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تحدّث الحقول المحددة في مورد (مرادف لعملية `patch`).

راجع أيضًا:  
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## بناء الجملة {#syntax}

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

## أمثلة {#examples}

### جملة `UPDATE` أساسية {#basic-update-statement}
شغّل جملة `UPDATE` لتعيين التصنيفات على حاوية Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### جملة `UPDATE` (حاجبة) {#update-statement-blocking}
شغّل جملة `UPDATE` لتعديل قاعدة جدار حماية في Compute Engine. هذا استدعاء حاجب (متزامن).

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### جملة `UPDATE` مع دوال JSON {#update-statement-with-json-functions}
شغّل جملة `UPDATE` تبنى قيمتها باستخدام دوال JSON. يتم تقييم الدالة التي لا تقرأ أي أعمدة قبل إرسال الطلب. لا يمكن للقيمة في عبارة `SET` قراءة الصف الذي يجري تحديثه، لذا لتغيير جزء من قيمة موجودة، اقرأها أولًا باستخدام جملة `SELECT` ثم عيّن القيمة المعدلة.

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
