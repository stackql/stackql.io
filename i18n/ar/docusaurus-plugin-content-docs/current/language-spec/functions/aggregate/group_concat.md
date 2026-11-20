---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة التجميعية GROUP_CONCAT في StackQL: دمج القيم غير NULL لعمود أو مجموعة في سلسلة نصية واحدة مع فاصل اختياري."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية ناتجة عن دمج جميع القيم غير `NULL` لعمود إدخال أو مجموعة.  

> ترتيب العناصر المدمجة اعتباطي.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## الوسيطات {#arguments}

__*columnExpression*__  
عمود أو تعبير.  

> يجب أن تكون قيمة واحدة على الأقل من *columnExpression* غير `NULL`، وإلا تعيد الدالة `NULL`.

__*separator*__  
الفاصل المستخدم في النتيجة المدمجة، وتستخدم الدالة الفاصل الافتراضي `,` إذا لم يتم تحديد *separator*.

__*groupByColumn*__  
عمود أو أعمدة تجري عليها عمليات التلخيص أو التجميع.  تعيد عبارة `GROUP BY` صفًا واحدًا لكل مجموعة أعمدة.

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية على شكل قائمة من القيم مفصولة بالفاصل *separator* أو `,`.

* * *

## أمثلة {#examples}

### إرجاع قائمة بقيم عمود على مستوى مورد كامل {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع قائمة بقيم تعبير عمود مجمعة حسب تعبير عمود آخر {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
