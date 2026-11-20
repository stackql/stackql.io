---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة التجميعية AVG في StackQL: متوسط القيم غير NULL في عمود أو مجموعة."
image: "/img/stackql-featured-image.png"
---
تعيد متوسط جميع القيم غير `NULL` في عمود أو مجموعة من الأعمدة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## الوسيطات {#arguments}

__*columnExpression*__  
عمود أو تعبير.

> تفسر الدالة قيم السلاسل النصية وقيم BLOB التي لا يمكن تمثيلها كقيم رقمية على أنها `0`.  

> يجب أن تكون قيمة واحدة على الأقل من *columnExpression* غير `NULL`، وإلا تعيد الدالة `NULL`.

__*groupByColumn*__  
عمود أو أعمدة تجري عليها عمليات التلخيص أو التجميع.  تعيد عبارة `GROUP BY` صفًا واحدًا لكل مجموعة أعمدة.

## القيمة المرجعة {#return-values}

تعيد قيمة بفاصلة عائمة تمثل متوسط *columnExpression*.

* * *

## أمثلة {#examples}

### إرجاع متوسط تعبير عمود على مستوى مورد كامل {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع متوسط تعبير عمود مجمع حسب تعبير عمود آخر {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### استخدام `AVG` كدالة نافذة لحساب المتوسطات المتحركة {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
