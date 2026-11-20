---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة LTRIM في StackQL: إزالة المسافات، أو أي حرف من مجموعة أحرف محددة، من يسار سلسلة نصية."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية ناتجة عن إزالة المسافات، أو أي حرف من مجموعة أحرف محددة، من الجانب الأيسر لسلسلة نصية مدخلة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*character_expression*__  
قيمة حرفية نصية أو عمود من نوع سلسلة نصية.

__*character*__  
سلسلة أحرف اختيارية: تتم إزالة كل حرف فيها من بداية *character_expression*. إذا لم يتم تحديدها، تتم إزالة المسافات (وليس علامات الجدولة أو أحرف السطر الجديد).

## القيمة المرجعة {#return-values}
تعيد سلسلة نصية.

* * *

## أمثلة {#examples}

### إزالة المسافات البادئة من عمود من نوع سلسلة نصية {#remove-leading-spaces-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
