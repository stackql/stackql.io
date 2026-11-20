---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة التجميعية COUNT في StackQL: عدد الصفوف في مورد، أو عدد القيم غير NULL لعمود داخل مجموعة."
image: "/img/stackql-featured-image.png"
---
تعيد العدد الإجمالي للصفوف في مورد، أو عدد القيم غير `NULL` لعمود في مجموعة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## الوسيطات {#arguments}

__*columnExpression*__  أو __*__  
عمود أو تعبير.  إذا حددت __*__ فإن الدالة تحسب جميع الصفوف في المورد أو المجموعة.  

> لا تحسب الدالة قيم `NULL` في *columnExpression*.  إذا كانت جميع القيم `NULL`، تعيد الدالة `COUNT` القيمة `0`.

__*groupByColumn*__  
عمود أو أعمدة تجري عليها عمليات التلخيص أو التجميع.  تعيد عبارة `GROUP BY` صفًا واحدًا لكل مجموعة أعمدة.

## القيمة المرجعة {#return-values}

تعيد عددًا صحيحًا يمثل ناتج العد.

* * *

## أمثلة {#examples}

### إرجاع عدد الصفوف في مورد كامل {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع عدد الصفوف مجمعة حسب تعبير عمود {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### استخدام `COUNT` كدالة نافذة لحساب الأعداد التراكمية {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
