---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة JULIANDAY في StackQL: عدد الأيام مع الكسور منذ ظهر 24 نوفمبر 4714 قبل الميلاد لقيمة زمنية، وهو مفيد في العمليات الحسابية على التواريخ."
image: "/img/stackql-featured-image.png"
---
تعيد اليوم اليولياني - عدد الأيام منذ الظهر بتوقيت غرينتش في 24 نوفمبر 4714 قبل الميلاد [(التقويم الغريغوري الاستباقي)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ معدّلات التاريخ ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*datetime_expression*__  
قيمة datetime.

__*modifier*__  
صفر أو أكثر من المعدّلات التي تطبق على *datetime_expression*.  لمزيد من المعلومات حول المعدّلات، راجع [**معدّلات التاريخ**](/language-spec/functions/datetime/date_modifiers)

## القيمة المرجعة {#return-values}

تعيد [اليوم اليولياني](https://en.wikipedia.org/wiki/Julian_day) للإدخال *datetime_expression* بعد تطبيق *modifier*: عدد الأيام، بما في ذلك الكسر الذي يمثل وقت اليوم، منذ الظهر بتوقيت غرينتش في 24 نوفمبر 4714 قبل الميلاد وفق [التقويم الغريغوري الاستباقي](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).

* * *

## أمثلة {#examples}

### حساب عدد الأيام بين تاريخين {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

استخدم الدالة [**ROUND**](/language-spec/functions/math/round) لتحويل الأعداد بفاصلة عائمة إلى أقرب عدد صحيح.

:::

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
