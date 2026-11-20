---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة REGEXP_SUBSTR في StackQL: استخراج السلسلة الفرعية المطابقة لتعبير نمطي من قيمة إدخال."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

تعيد سلسلة فرعية من سلسلة الإدخال مطابقة لنمط محدد.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ مرجع التعبيرات النمطية ]](/language-spec/functions/re/reference) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*source*__  
قيمة حرفية نصية أو عمود من نوع السلسلة النصية تريد استخراج سلسلة فرعية منه وفق نمط يحدده تعبير نمطي (`pattern`).  

__*pattern*__  
نمط التعبير النمطي المراد البحث عنه.  

## القيمة المرجعة {#return-values}
تعيد سلسلة نصية.

* * *

## أمثلة {#examples}

### استخراج سلسلة فرعية وفق تعبير نمطي {#extract-a-substring-based-upon-a-regular-expression}

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
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\.[0-9][0-9]') as os_version 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------|------------|
| name |      os       | os_version |
|------|---------------|------------|
| vm0  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm1  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm2  | UbuntuServer  |      20.04 |
|------|---------------|------------|
```

</TabItem>
</Tabs>

لمزيد من المعلومات، راجع [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
