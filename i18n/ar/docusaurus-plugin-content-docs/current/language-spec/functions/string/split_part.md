---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة SPLIT_PART في StackQL: تقسيم سلسلة نصية حسب فاصل وإرجاع الجزء الموجود في موضع يبدأ ترقيمه من 1، وهي مفيدة لتفكيك معرفات الموارد وARN."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

تقسم السلسلة النصية المصدر حسب فاصل وتعيد الجزء المحدد (مع العد من واحد).  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*source*__  
قيمة حرفية نصية أو عمود من نوع سلسلة نصية تريد إجراء التقسيم عليه.

__*sep*__  
حرف أو أكثر تريد استخدامه كفاصل.  

> إذا كانت الوسيطة `sep` تتكون من عدة أحرف، فإن السلسلة النصية بأكملها هي الفاصل: يعيد الاستدعاء `SPLIT_PART('a||b||c', '||', 3)` القيمة `c`.  

> يمكن استخدام أي أحرف في الفاصل، بما في ذلك الأحرف غير ASCII مثل `€`.  

__*part*__  
العنصر المراد إرجاعه من السلسلة النصية المقسمة (بترقيم يبدأ من 1). تستخدم القيم السالبة للعد إلى الوراء من العنصر الأخير.  

## القيمة المرجعة {#return-values}
تعيد سلسلة نصية.

* * *

## أمثلة {#examples}

### استخراج العناصر من سلسلة نصية مفصولة بشرطة مائلة للأمام {#extract-elements-from-a-string-separated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'النتائج', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resource_group_name = 'stackql-ops-cicd-dev-01' 
 AND subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

### استخراج العنصر الأخير من عنوان URL {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'النتائج', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT
name,
SPLIT_PART(network, '/', -1) as network,
SPLIT_PART(region, '/', -1) as region
FROM google.compute.vpn_gateways WHERE project = 'stackql-demo';
```

</TabItem>
<TabItem value="results">

```
+------------------------+-----------------------+----------------------+
| name                   | network               | region               |
+------------------------+-----------------------+----------------------+
| gcp-aws-vpn-gateway    | stackql-shrd-vpc-nw   | australia-southeast1 |
+------------------------+-----------------------+----------------------+
```

</TabItem>
</Tabs>

لمزيد من المعلومات، راجع [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
