---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة NULLIF في StackQL: إرجاع NULL عندما تتساوى وسيطتان، وإلا إرجاع الوسيطة الأولى."
image: "/img/stackql-featured-image.png"
---
تقبل وسيطتين وتعيد قيمة `NULL` إذا كانتا متساويتين، وإلا تعيد الوسيطة الأولى.  

> تكافئ الدالة `NULLIF` التعبير `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> الدالة `NULLIF` مفيدة مع الدوال التجميعية مثل [`AVG`](/language-spec/functions/aggregate/avg) و[`MAX`](/language-spec/functions/aggregate/max) و[`MIN`](/language-spec/functions/aggregate/min) و[`SUM`](/language-spec/functions/aggregate/sum) و[`COUNT`](/language-spec/functions/aggregate/count)، حين تحتوي مجموعة النتائج على قيم خاصة ليست `NULL` لكنك تريد معاملتها كقيمة `NULL` لأغراض التجميع.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expression*__  
تعبيران من أي نوع بيانات يُحلان إلى قيمة `NULL` أو قيمة غير `NULL`.

> تبحث الدالة `NULLIF` في وسيطاتها من اليسار إلى اليمين عن وسيطة تحدد دالة ترتيب، وتستخدم دالة الترتيب هذه في جميع مقارنات السلاسل النصية.  إذا لم تحدد أي من الوسيطتين دالة ترتيب، فإن الدالة تستخدم الترتيب `BINARY`.

## القيمة المرجعة {#return-values}

تعيد `NULL` إذا كانت الوسيطتان *expression* تُحلان إلى القيمة نفسها، وإلا تعيد القيمة الأولى. 

* * *

## أمثلة {#examples}

### حساب عدد الصفوف التي لا يساوي فيها حقل قيمة معينة {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name != 'controller-0';
*/
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
