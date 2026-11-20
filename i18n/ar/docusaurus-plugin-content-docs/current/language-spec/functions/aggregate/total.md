---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة التجميعية TOTAL في StackQL: مجموع القيم غير NULL في عمود أو مجموعة، ويعاد دائمًا كعدد بفاصلة عائمة."
image: "/img/stackql-featured-image.png"
---
تعيد مجموع جميع القيم غير `NULL` في عمود أو مجموعة من الأعمدة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## الوسيطات {#arguments}

__*columnExpression*__  
عمود أو تعبير.

> إذا كانت جميع القيم في *columnExpression* هي `NULL`، تعيد الدالة `TOTAL` القيمة `0.0`.  

__*groupByColumn*__  
عمود أو أعمدة تجري عليها عمليات التلخيص أو التجميع.  تعيد عبارة `GROUP BY` صفًا واحدًا لكل مجموعة أعمدة.

## القيمة المرجعة {#return-values}

تعيد قيمة بفاصلة عائمة تمثل مجموع *columnExpression*.

* * *

## أمثلة {#examples}

### إرجاع مجموع تعبير عمود على مستوى مورد كامل {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع مجموع تعبير عمود مجمع حسب تعبير عمود آخر {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
