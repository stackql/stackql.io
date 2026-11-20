---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة التجميعية SUM في StackQL: مجموع القيم غير NULL في عمود أو مجموعة، ويعاد كعدد صحيح عندما تكون جميع قيم الإدخال أعدادًا صحيحة."
image: "/img/stackql-featured-image.png"
---
تعيد مجموع جميع القيم غير `NULL` في عمود أو مجموعة من الأعمدة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

استخدم الدالة [**TOTAL**](/language-spec/functions/aggregate/total) لجمع الأعداد ذات الفاصلة العائمة أو القيم الطويلة.

:::

## بناء الجملة {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## الوسيطات {#arguments}

__*columnExpression*__  
عمود أو تعبير.

> إذا كانت جميع القيم في *columnExpression* هي `NULL`، تعيد الدالة `SUM` القيمة `NULL`.  

> تطرح الدالة `SUM` الاستثناء "integer overflow" إذا حدث تجاوز لسعة الأعداد الصحيحة في أي مرحلة أثناء الحساب.  أما الدالة [`TOTAL`](/language-spec/functions/aggregate/total) فلا تطرح أبدًا استثناء تجاوز سعة الأعداد الصحيحة.  

__*groupByColumn*__  
عمود أو أعمدة تجري عليها عمليات التلخيص أو التجميع.  تعيد عبارة `GROUP BY` صفًا واحدًا لكل مجموعة أعمدة.

## القيمة المرجعة {#return-values}

تعيد عددًا صحيحًا يمثل مجموع *columnExpression* إذا كانت جميع قيم الإدخال غير `NULL` أعدادًا صحيحة. إذا لم تكن بعض قيم الإدخال أعدادًا صحيحة أو كانت `NULL`، تعيد الدالة قيمة بفاصلة عائمة قد تكون تقريبًا للمجموع الحقيقي.

* * *

## أمثلة {#examples}

### إرجاع مجموع تعبير عمود على مستوى مورد كامل {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع مجموع تعبير عمود مجمع حسب تعبير عمود آخر {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### استخدام `SUM` كدالة نافذة لحساب المجاميع التراكمية {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
