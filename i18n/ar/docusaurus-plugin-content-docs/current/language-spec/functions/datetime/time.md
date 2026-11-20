---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة TIME في StackQL: إرجاع الوقت كسلسلة نصية بالتنسيق HH:MM:SS من قيمة زمنية ومعدّلات اختيارية."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية للوقت بالتنسيق HH:MM:SS.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ معدّلات التاريخ ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*datetime_expression*__  
قيمة datetime.

__*modifier*__  
صفر أو أكثر من المعدّلات التي تطبق على *datetime_expression*.  لمزيد من المعلومات حول المعدّلات، راجع [**معدّلات التاريخ**](/language-spec/functions/datetime/date_modifiers)

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية تمثل مكون الوقت من الإدخال *datetime_expression* بعد تطبيق *modifier*.

* * *

## أمثلة {#examples}

### إرجاع مكون الوقت من قيمة datetime {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### إرجاع مكون الوقت المحلي من قيمة datetime {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
