---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب اللوغاريتم للأساس 2 لعدد باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب اللوغاريتم للأساس 2 لعدد معين، وتعيد الأس الذي يجب رفع العدد 2 إليه للحصول على القيمة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير رقمي موجب يراد حساب لوغاريتمه للأساس 2.

## القيمة المرجعة {#return-values}
تعيد اللوغاريتم للأساس 2 للقيمة الرقمية المحددة.

* * *

## أمثلة {#examples}

### حساب اللوغاريتم للأساس 2 للعدد 16 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

يعيد هذا الاستعلام `4`، لأن 2^4 = 16.

### حساب اللوغاريتم للأساس 2 للعدد 32 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

يحسب هذا الاستعلام اللوغاريتم للأساس 2 للعدد 32، والنتيجة `5`، لأن 2^5 = 32.
