---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة DATETIME في StackQL: حساب قيمة datetime من إدخال وصفر أو أكثر من المعدّلات، وتعاد بالتنسيق YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة datetime محسوبة من قيمة datetime مدخلة وصفر أو أكثر من المعدّلات.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ معدّلات التاريخ ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*datetime_expression*__  
قيمة datetime.

__*modifier*__  
صفر أو أكثر من المعدّلات التي تطبق على *datetime_expression*.  لمزيد من المعلومات حول المعدّلات، راجع [**معدّلات التاريخ**](/language-spec/functions/datetime/date_modifiers)

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية تمثل قيمة datetime للإدخال *datetime_expression* بعد تطبيق *modifier*.

* * *

## أمثلة {#examples}

### تحويل UTC إلى التوقيت المحلي باستخدام المعدّل `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### تحويل توقيت محلي إلى UTC {#convert-datetime-to-utc}

يعامل المعدّل `utc` الإدخال الخاص به على أنه توقيت محلي على الجهاز الذي يعمل عليه StackQL ويحوله إلى UTC.  القيمة التي تكون بتوقيت UTC أصلًا، مثل `'now'` أو طابع زمني بلاحقة `Z`، تعاد دون تغيير.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

على جهاز منطقته الزمنية UTC+11، يعيد هذا الاستعلام `2026-11-20 06:59:24`.

### حساب تاريخ بالنسبة إلى تاريخ آخر {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### الحصول على يوم الأسبوع المحدد في تاريخ معين أو بعده {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### الحصول على بداية الشهر لتاريخ معين {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### تحويل طابع زمني لـ Unix إلى قيمة datetime {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

لتحويل قيمة datetime إلى طابع زمني لـ Unix epoch، راجع [**STRFTIME**](/language-spec/functions/datetime/strftime).  لحساب الفروق بين كائني datetime، راجع [**STRFTIME**](/language-spec/functions/datetime/strftime) أو [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
