---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة LENGTH في StackQL: عدد الأحرف في سلسلة نصية، أو عدد البايتات في قيمة من نوع BLOB."
image: "/img/stackql-featured-image.png"
---
تعيد عدد الأحرف في تعبير إدخال من نوع سلسلة نصية قبل أول حرف NUL (`char(0)`)، أو عدد البايتات في تعبير من نوع BLOB.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

للحصول على طول مصفوفة JSON، استخدم الدالة [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## بناء الجملة {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expression*__  
سلسلة نصية أو قيمة من نوع BLOB.

> تعيد `NULL` إذا كانت قيمة *expression* هي `NULL`.

> إذا كانت قيمة *expression* رقمية، فإن الدالة تعيد طول تمثيل *expression* كسلسلة نصية.

## القيمة المرجعة {#return-values}
تعيد عددًا صحيحًا.

* * *

## أمثلة {#examples}

### إرجاع طول عمود من نوع سلسلة نصية {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
