---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة REGEXP_REPLACE في StackQL: استبدال الأجزاء المطابقة لتعبير نمطي في سلسلة نصية بسلسلة نصية بديلة."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

تستبدل سلسلة فرعية مطابقة بسلسلة نصية بديلة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ مرجع التعبيرات النمطية ]](/language-spec/functions/re/reference) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## الوسيطات {#arguments}

__*source*__  
قيمة حرفية نصية أو عمود من نوع السلسلة النصية يتم فيه استبدال مطابقات التعبير النمطي `pattern` بالقيمة `replacement`.  

__*pattern*__  
نمط التعبير النمطي المراد البحث عنه.  

__*replacement*__  
السلسلة النصية البديلة.

> تدعم المراجع الخلفية لمجموعات الالتقاط من `\1` إلى `\9` في السلسلة النصية البديلة.  

## القيمة المرجعة {#return-values}
تعيد سلسلة نصية.

* * *

## أمثلة {#examples}

### استبدال نمط تعبير نمطي في عمود {#replace-a-regular-expression-pattern-in-a-column}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'النتائج', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name,
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

لمزيد من المعلومات، راجع [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
