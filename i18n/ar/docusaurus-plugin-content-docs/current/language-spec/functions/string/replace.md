---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة REPLACE في StackQL: استبدال كل تكرار لسلسلة فرعية داخل سلسلة نصية بسلسلة نصية أخرى."
image: "/img/stackql-featured-image.png"
---
تعيد نسخة من سلسلة نصية تم فيها استبدال كل تكرار لسلسلة فرعية بسلسلة نصية أخرى.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*character_expression*__  
قيمة حرفية نصية أو عمود من نوع سلسلة نصية تريد إجراء الاستبدال عليه.

__*pattern*__  
السلسلة الفرعية المراد العثور عليها في السلسلة النصية الأصلية.

> إذا كانت *pattern* سلسلة نصية فارغة أو لم يتم العثور عليها في *character_expression*، فإن الدالة تعيد *character_expression* دون تغيير. 

__*replacement*__  
السلسلة النصية البديلة.

> إذا لم تكن *replacement* سلسلة نصية في الأصل، فيتم تحويل نوعها إلى سلسلة نصية بترميز UTF-8 قبل المعالجة.

> تتم المقارنات باستخدام تسلسل الترتيب `BINARY`.

## القيمة المرجعة {#return-values}
تعيد سلسلة نصية.

* * *

## أمثلة {#examples}

### استبدال نمط سلسلة نصية في عمود {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
