---
title: معدّلات التاريخ
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "معدّلات التاريخ والوقت في StackQL: إزاحة نتائج DATE وTIME وDATETIME وJULIANDAY وSTRFTIME بالأيام أو الأشهر أو السنوات، أو إلى بداية فترة أو يوم من الأسبوع، وغير ذلك."
image: "/img/stackql-featured-image.png"
---

## نظرة عامة {#overview}

في دوال التاريخ والوقت في StackQL، يمكن أن يتبع قيمة التاريخ أو الوقت صفر أو أكثر من المعدّلات التي تغير التاريخ و/أو الوقت. كل معدّل هو تحويل يطبق على القيمة الزمنية الموجودة على يساره. تطبق المعدّلات بالترتيب من اليسار إلى اليمين. المعدّلات المتاحة هي كما يلي:

| المعدّل | الوصف |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s)           | يضيف ±NNN من الأيام إلى تاريخ ووقت |
| NNN hour(s)          | يضيف ±NNN من الساعات إلى تاريخ ووقت |
| NNN minute(s)        | يضيف ±NNN من الدقائق إلى تاريخ ووقت |
| NNN[.NNNN] second(s) | يضيف ±NNN من الثواني إلى تاريخ ووقت أو يطرحها منه |
| NNN month(s)         | يضيف ±NNN من الأشهر إلى تاريخ ووقت |
| NNN year(s)          | يضيف ±NNN من السنوات إلى تاريخ ووقت |
| start of month       | يعود إلى بداية الشهر |
| start of year        | يعود إلى بداية السنة |
| start of day         | يعود إلى بداية اليوم |
| weekday N            | يقدم التاريخ إلى التاريخ التالي الذي يكون فيه رقم يوم الأسبوع N |
| localtime            | يعيد الوقت المحلي |
| utc                  | يعيد الوقت بتوقيت UTC |
| unixepoch            | وقت Unix |

## أمثلة {#examples}

### إرجاع التاريخ والوقت الحاليين باستخدام القيمة الزمنية `now` {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

### تحويل UTC إلى التوقيت المحلي باستخدام المعدّل `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### تحويل توقيت محلي إلى UTC {#convert-datetime-to-utc}

يتعامل المعدّل `utc` مع قيمة الإدخال كوقت محلي على الجهاز الذي يشغل StackQL ويحولها إلى UTC.  أما القيمة التي تكون بتوقيت UTC أصلًا، مثل `'now'` أو طابع زمني بلاحقة `Z`، فتعاد دون تغيير.

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

### الحصول على يوم الأسبوع المحدد التالي بعد تاريخ معين {#get-the-next-specified-weekday-after-a-given-date}

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
