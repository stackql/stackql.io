---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة REGEXP_LIKE في StackQL: اختبار مطابقة سلسلة نصية لتعبير نمطي، مع إرجاع 1 للقيمة true و0 للقيمة false، لمرشحات الأنماط في عبارات WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

تتحقق مما إذا كانت السلسلة النصية المصدر تطابق النمط، وتعيد `1` للقيمة true أو `0` للقيمة false.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ مرجع التعبيرات النمطية ]](/language-spec/functions/re/reference) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*source*__  
قيمة حرفية نصية أو عمود من نوع السلسلة النصية تريد اختبار وجود نمط فيه يحدده تعبير نمطي (`pattern`).  

__*pattern*__  
نمط التعبير النمطي المراد البحث عنه.  

## القيمة المرجعة {#return-values}
تعيد `1` للقيمة true (تطابق السلسلة النصية المصدر النمط) أو `0` للقيمة false.  

* * *

## أمثلة {#examples}

### تحديد ما إذا كانت سلسلة نصية تحتوي على نمط {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, '^vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

لمزيد من المعلومات، راجع [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
