---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "REPLACE في StackQL: استبدال جميع حقول مثيل المورد، بما يعادل طلب HTTP PUT، على عكس التحديث الجزئي الذي تجريه UPDATE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تعمل على تحديث جميع الحقول في مورد (وهي مرادفة لعملية `put`).

راجع أيضًا:  
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## بناء الجملة {#syntax}

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

## أمثلة {#examples}

### جملة `REPLACE` أساسية {#basic-replace-statement}
شغّل جملة `REPLACE` لاستبدال جميع الموضوعات في مستودع GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### جملة `REPLACE` (حاجبة) {#replace-statement-blocking}
شغّل جملة `REPLACE` لاستبدال قاعدة جدار حماية في Compute Engine. هذا استدعاء حاجب (متزامن).

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### جملة `REPLACE` مع دوال JSON {#replace-statement-with-json-functions}
شغّل جملة `REPLACE` تُبنى قيمتها باستخدام دوال JSON. يتم تقييم الدالة التي لا تقرأ أي أعمدة قبل إرسال الطلب. لا يمكن لقيمة `SET` قراءة الصف الذي يجري استبداله، لذا لتغيير جزء من قيمة موجودة، اقرأها أولًا باستخدام `SELECT` ثم عيّن القيمة المعدلة.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
