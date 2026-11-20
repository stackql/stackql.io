---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "SELECT في StackQL: الاستعلام عن الموارد السحابية وموارد SaaS باستخدام SQL، مع مرشحات WHERE التي تقابل معلمات API، وعمليات الربط والتجميع والترتيب والوصول إلى حقول JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تعيد مثيلًا واحدًا أو أكثر لمورد.  

راجع أيضًا:
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTE) `]](/language-spec/with) [[` دوال النافذة `]](/language-spec/windowing_functions)

* * * 

## بناء الجملة {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

يتم التعامل مع `JOIN` بدون عبارة `ON` على أنه `CROSS JOIN`، لذا يمكن تحديد شرط الربط في عبارة `WHERE` بدلًا من ذلك. تفشل إصدارات StackQL قبل `v0.12.732` في تخطيط `JOIN` بدون شرط.

* * *

## أمثلة {#examples}

### جملة `SELECT` أساسية {#basic-select-statement}
شغّل جملة `SELECT` أساسية في جلسة تمت مصادقتها.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### إرجاع حقول محددة باستخدام جملة `SELECT` {#returning-specified-fields-using-a-select-statement}
شغّل جملة `SELECT` مع قائمة أعمدة لإرجاع حقول محددة من مثيل Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع مجموعة نتائج مرتبة حسب عمود بترتيب تصاعدي باستخدام عبارة `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
شغّل جملة `SELECT` لإرجاع حقول من مورد حاويات Cloud Storage، مع ترتيب النتائج تصاعديًا (الأقدم أولًا). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### إرجاع مجموعة نتائج مرتبة حسب عمود بترتيب تنازلي باستخدام عبارة `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
شغّل جملة `SELECT` لإرجاع حقول من مورد حاويات Cloud Storage، مع ترتيب النتائج تنازليًا (الأحدث أولًا).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### الحصول على النتيجة الأولى من عمود باستخدام العبارتين `ORDER BY` و`LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
شغّل جملة `SELECT` لإرجاع أحدث حاوية Cloud Storage تم إنشاؤها.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### استخدام الدالة `COUNT` مع العبارتين `GROUP BY` و`HAVING` لعد الموارد استنادًا إلى تجميع ومرشح {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
شغّل جملة `SELECT` مع الدالة `COUNT`، باستخدام العبارتين `GROUP BY` و`HAVING` لإرجاع عدد الحاويات في كل موقع باستثناء آسيا.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

لمزيد من المعلومات حول الدالة `COUNT` والدوال التجميعية الأخرى التي يدعمها StackQL، راجع [الدوال التجميعية](/language-spec/functions/aggregate/count).

### الحصول على قائمة الأجهزة الظاهرية حسب الطلب وواجهات الشبكة المقابلة لها من Azure VM Scale Set {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
شغّل جملة `SELECT` باستخدام الدالتين [`SPLIT_PART`](/language-spec/functions/string/split_part) و[`JSON_EXTRACT`](/language-spec/functions/json/json_extract) للحصول على قائمة بالأجهزة الظاهرية حسب الطلب (OD) وبطاقات واجهة الشبكة (NIC) الخاصة بها. لاحظ أنه بدون مؤهل، تُطبق `subscription_id` و`resource_group_name` على الموارد الثلاثة جميعها.

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

### استخدام تعبيرات الجدول الشائعة (CTE) مع `WITH` {#using-common-table-expressions-ctes-with-with}

تتيح لك تعبيرات CTE تعريف مجموعات نتائج مؤقتة مسماة يمكن الرجوع إليها في الاستعلام الرئيسي.

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

لمزيد من المعلومات حول تعبيرات CTE، راجع [WITH (CTE)](/language-spec/with).

لمزيد من المعلومات حول دوال النافذة، راجع [دوال النافذة](/language-spec/windowing_functions).
