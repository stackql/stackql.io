---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة UPPER في StackQL: تحويل سلسلة نصية إلى أحرف كبيرة."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية مدخلة بأحرف كبيرة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> تعمل الدالة `upper()` مع أحرف ASCII فقط.

## الوسيطات {#arguments}

__*character_expression*__  
سلسلة نصية بترميز ASCII.

## القيمة المرجعة {#return-values}
تعيد قيمة من نوع سلسلة نصية.

* * *

## أمثلة {#examples}

### إرجاع سلسلة نصية بأحرف كبيرة {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
