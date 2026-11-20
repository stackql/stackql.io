---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة RTRIM في StackQL: إزالة المسافات، أو أي حرف من مجموعة أحرف محددة، من يمين سلسلة نصية."
image: "/img/stackql-featured-image.png"
---
تعيد سلسلة نصية ناتجة عن إزالة المسافات، أو أي حرف من مجموعة أحرف محددة، من الجانب الأيمن لسلسلة نصية مدخلة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*character_expression*__  
قيمة حرفية نصية أو عمود من نوع سلسلة نصية.

__*character*__  
سلسلة أحرف اختيارية: تتم إزالة كل حرف فيها من نهاية *character_expression*. إذا لم يتم تحديدها، تتم إزالة المسافات (وليس علامات الجدولة أو أحرف السطر الجديد).

## القيمة المرجعة {#return-values}
تعيد سلسلة نصية.

* * *

## أمثلة {#examples}

### إزالة المسافات اللاحقة من عمود من نوع سلسلة نصية {#remove-trailing-spaces-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim)
