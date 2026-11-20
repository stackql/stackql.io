---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة INSTR في StackQL: موضع أول تكرار لسلسلة نصية داخل سلسلة نصية أخرى بترقيم يبدأ من 1، أو 0 إذا لم يتم العثور عليها."
image: "/img/stackql-featured-image.png"
---
تعيد موضع الحرف لأول تكرار لسلسلة نصية داخل سلسلة نصية أخرى، أو 0 إذا لم يتم العثور على السلسلة النصية التي يجري البحث عنها.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*string*__  
السلسلة النصية المصدر التي يجري البحث فيها.

__*searchstring*__  
السلسلة الفرعية التي يجري البحث عنها.

> إذا كانت الوسيطتان *string* و*searchstring* كلتاهما من نوع BLOB، فإن الاستدعاء `instr(<string>, <searchstring>)` يعيد قيمة تزيد بمقدار واحد على عدد البايتات التي تسبق أول تكرار للوسيطة *searchstring*، أو 0 إذا لم تظهر *searchstring* في أي موضع داخل *string*. إذا لم تكن أي من الوسيطتين `NULL` ولم تكن أي منهما من نوع BLOB، فيتم تفسير كلتيهما على أنهما سلسلتان نصيتان. 

## القيمة المرجعة {#return-values}
تعيد عددًا صحيحًا.

> إذا كانت قيمة الوسيطة *string* أو الوسيطة *searchstring* هي `NULL`، فإن النتيجة هي `NULL`.

* * *

## أمثلة {#examples}

### البحث عن سلسلة نصية في حقل {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
