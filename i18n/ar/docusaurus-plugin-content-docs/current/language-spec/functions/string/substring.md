---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة SUBSTRING في StackQL (الاسم المستعار SUBSTR): إرجاع جزء من سلسلة نصية أو قيمة ثنائية حسب موضع البداية والطول."
image: "/img/stackql-featured-image.png"
---
تعيد جزءًا من تعبير حرفي أو ثنائي. الدالة `SUBSTR` اسم مستعار للدالة `SUBSTRING`.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

:::info

لاستخراج حقل من كائن JSON، استخدم الدالة [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## بناء الجملة {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expression*__  
سلسلة نصية أو قيمة من نوع BLOB.

> إذا كانت *expression* سلسلة نصية، فإن فهارس الأحرف تشير إلى أحرف UTF-8 الفعلية. إذا كانت *expression* من نوع BLOB، فإن الفهارس تشير إلى البايتات.

__*start*__  
عدد صحيح يمثل الحرف أو البايت الأول من التسلسل الفرعي المراد إرجاعه من تعبير الإدخال. إذا كانت *start* سالبة، فيتم تحديد الحرف الأول من السلسلة الفرعية بالعد من اليمين وليس من اليسار.

> الحرف الموجود في أقصى يسار *expression* هو الحرف رقم 1.

__*length*__  
قيمة اختيارية من نوع عدد صحيح تمثل طول التسلسل الفرعي المراد إرجاعه بعد قيمة *start*. إذا لم يتم تحديد *length*، فإن الدالة تعيد جميع الأحرف حتى نهاية السلسلة النصية أو قيمة BLOB التي تحددها *expression*.

> إذا كانت *length* سالبة، فتعاد الأحرف التي تسبق *start*، بعدد يساوي القيمة المطلقة للوسيطة *length*: يعيد الاستدعاء `SUBSTR('abcdef', 4, -2)` القيمة `bc`.

## القيمة المرجعة {#return-values}

تعيد قيمة من نوع سلسلة نصية أو BLOB حسب نوع *expression*.

* * *

## أمثلة {#examples}

### إرجاع أول *n* أحرف من عمود {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### اقتطاع أول *n* أحرف من عمود {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع آخر *n* أحرف من عمود {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
