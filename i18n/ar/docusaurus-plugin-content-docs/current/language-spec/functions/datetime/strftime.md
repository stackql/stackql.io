---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة STRFTIME في StackQL: تنسيق قيمة datetime باستخدام سلسلة تنسيق، بالاستبدالات نفسها التي تستخدمها الدالة strftime في لغة C."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة datetime منسقة وفق تنسيق محدد.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ معدّلات التاريخ ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*format*__  
التنسيق المطلوب لقيمة datetime التي تحددها القيمة *datetime_expression*.  راجع جدول التنسيقات أدناه.

__*datetime_expression*__  
قيمة التاريخ أو الوقت المراد تنسيقها.

__*modifier*__  
صفر أو أكثر من المعدّلات التي تطبق على *datetime_expression*.  لمزيد من المعلومات حول المعدّلات، راجع [**معدّلات التاريخ**](/language-spec/functions/datetime/date_modifiers)

### التنسيقات {#formats}

| التنسيق | الوصف |
|--------|--------------------------------|
| %d     | يوم الشهر: 01-31 |
| %f     | الثواني مع الكسور: SS.SSS |
| %H     | الساعة: 00-24 |
| %j     | يوم السنة: 001-366 |
| %J     | رقم اليوم اليولياني |
| %m     | الشهر: 01-12 |
| %M     | الدقيقة: 00-59 |
| %s     | الثواني منذ 1970-01-01 |
| %S     | الثواني: 00-59 |
| %w     | يوم الأسبوع 0-6 حيث الأحد==0 |
| %W     | أسبوع السنة: 00-53 |
| %Y     | السنة: 0000-9999 |
| %%     | إلغاء رمز النسبة المئوية: % |

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية منسقة تمثل تنسيق datetime المطلوب.

* * *

## أمثلة {#examples}

### إرجاع تاريخ بالتنسيق الأمريكي {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### تحويل قيمة datetime إلى طابع زمني epoch {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### حساب عدد الأيام بين تاريخين {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
