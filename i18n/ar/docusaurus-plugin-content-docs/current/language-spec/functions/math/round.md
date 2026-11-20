---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة ROUND في StackQL: تقريب وسيطة رقمية إلى عدد معين من المنازل العشرية، والقيمة الافتراضية صفر."
image: "/img/stackql-featured-image.png"
---
تقوم بتقريب وسيطة رقمية.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير من فئة أنواع البيانات الرقمية الدقيقة أو الرقمية التقريبية.

__*precision*__  
عدد الأرقام على يمين الفاصلة العشرية الذي يتم تقريب *numeric_expression* إليه.

> إذا لم يتم تحديد الوسيطة *precision*، فسيتم اعتبار قيمتها 0.

## القيمة المرجعة {#return-values}
تعيد قيمة رقمية.

* * *

## أمثلة {#examples}

### تقريب عدد بفاصلة عائمة إلى منزلتين عشريتين {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
