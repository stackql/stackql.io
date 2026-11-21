---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة ABS في StackQL: القيمة المطلقة لوسيطة رقمية."
image: "/img/stackql-featured-image.png"
---
تعيد القيمة المطلقة للوسيطة الرقمية.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> تعيد الدالة `abs(X)` القيمة `NULL` إذا كانت `X` هي `NULL`. وتعيد الدالة `abs(X)` القيمة `0.0` إذا كانت `X` سلسلة نصية أو blob لا يمكن تحويلها إلى قيمة رقمية. وإذا كانت `X` هي العدد الصحيح -9223372036854775808، فإن الدالة `abs(X)` تطرح خطأ تجاوز سعة العدد الصحيح، لأنه لا توجد قيمة موجبة مكافئة لها بتمثيل المتمم الثنائي ذي 64 بت.

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير من فئة أنواع البيانات الرقمية الدقيقة أو الرقمية التقريبية.

## القيمة المرجعة {#return-values}

تعيد قيمة رقمية غير سالبة.

* * *

## أمثلة {#examples}

### إرجاع القيمة المطلقة لعدد {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs)
