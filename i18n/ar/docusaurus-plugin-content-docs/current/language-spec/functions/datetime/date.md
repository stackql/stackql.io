---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة DATE في StackQL: إرجاع تاريخ كسلسلة نصية بالتنسيق YYYY-MM-DD من قيمة زمنية ومعدّلات اختيارية."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية لتاريخ بالتنسيق YYYY-MM-DD.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ معدّلات التاريخ ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*datetime_expression*__  
قيمة datetime.

__*modifier*__  
صفر أو أكثر من المعدّلات التي تطبق على *datetime_expression*.  لمزيد من المعلومات حول المعدّلات، راجع [**معدّلات التاريخ**](/language-spec/functions/datetime/date_modifiers)

## القيمة المرجعة {#return-values}

تعيد سلسلة نصية تمثل مكون التاريخ من قيمة الإدخال *datetime_expression* بعد تطبيق *modifier*.

* * *

## أمثلة {#examples}

### إرجاع مكون التاريخ من قيمة datetime {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### إرجاع مكون التاريخ المحلي من قيمة datetime {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
