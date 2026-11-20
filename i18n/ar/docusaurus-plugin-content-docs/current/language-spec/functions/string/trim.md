---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة TRIM في StackQL: إزالة المسافات، أو أي حرف من مجموعة أحرف محددة، من طرفي سلسلة نصية."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية ناتجة عن إزالة المسافات، أو أي حرف من مجموعة أحرف محددة، من طرفي سلسلة نصية مدخلة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*character_expression*__  
قيمة حرفية نصية أو عمود من نوع سلسلة نصية.

__*character*__  
سلسلة أحرف اختيارية: تتم إزالة كل حرف فيها من بداية *character_expression* ونهايتها. إذا لم يتم تحديدها، تتم إزالة المسافات (وليس علامات الجدولة أو أحرف السطر الجديد).

## القيمة المرجعة {#return-values}
تعيد سلسلة نصية.

* * *

## أمثلة {#examples}

### إزالة المسافات البادئة واللاحقة من عمود من نوع سلسلة نصية {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
