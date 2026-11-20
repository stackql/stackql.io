---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة LOWER في StackQL: تحويل سلسلة نصية إلى أحرف صغيرة."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية مدخلة بأحرف صغيرة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> تعمل الدالة `lower()` مع أحرف ASCII فقط.

## الوسيطات {#arguments}

__*character_expression*__  
سلسلة نصية بترميز ASCII.

## القيمة المرجعة {#return-values}
تعيد قيمة من نوع سلسلة نصية.

* * *

## أمثلة {#examples}

### إرجاع سلسلة نصية بأحرف صغيرة {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
