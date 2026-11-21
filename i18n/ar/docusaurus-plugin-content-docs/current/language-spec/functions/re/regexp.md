---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: مطابقة الأنماط في السلاسل النصية بالتعبيرات النمطية باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
يتحقق مما إذا كانت السلسلة النصية المصدر تطابق نمط التعبير النمطي المقدم.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*source_string*__  
السلسلة النصية التي يتم البحث فيها عن النمط.

__*pattern_expression*__  
نمط التعبير النمطي المراد مطابقته مع السلسلة النصية المصدر.

## القيمة المرجعة {#return-values}
يعيد `1` إذا كانت السلسلة النصية المصدر تطابق نمط التعبير النمطي، وإلا `0`.

* * *

## أمثلة {#examples}

### التحقق مما إذا كانت سلسلة نصية تحتوي على أرقام {#check-if-a-string-contains-numbers}

يوضح هذا المثال التحقق مما إذا كانت السلسلة النصية 'the year is 2021' تحتوي على حرف رقمي واحد أو أكثر:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

سيعيد هذا الاستعلام `1` لأن السلسلة النصية تحتوي على السلسلة الفرعية '2021'، التي تطابق النمط `[0-9]+`، الذي يشير إلى رقم واحد أو أكثر.

### التحقق من صحة تنسيق البريد الإلكتروني {#validate-email-format}

للتحقق مما إذا كانت سلسلة نصية بتنسيق بريد إلكتروني صالح:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' AS match_result;
```

يعيد هذا الاستعلام `1`، مما يشير إلى أن 'user@example.com' منسق مثل عنوان بريد إلكتروني نموذجي، إذ يطابق نمط التعبير النمطي المحدد.  تكتب النقطة التي تسبق لاحقة المجال بالشكل `\\.` لأن القيمة الحرفية النصية في StackQL تستهلك شرطة مائلة عكسية واحدة، لذا تكتب الشرطة المائلة العكسية التي تنتمي إلى التعبير النمطي مرتين (راجع [مرجع التعبيرات النمطية](/language-spec/functions/re/reference)).
