---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب اللوغاريتم للأساس 10 لعدد باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب اللوغاريتم للأساس 10 لعدد معين، أي تحدد الأس الذي يجب رفع العدد 10 إليه للحصول على القيمة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير رقمي موجب يراد حساب لوغاريتمه للأساس 10.

## القيمة المرجعة {#return-values}
تعيد اللوغاريتم للأساس 10 للقيمة الرقمية المحددة.

* * *

## أمثلة {#examples}

### حساب اللوغاريتم للأساس 10 للعدد 100 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

يعيد هذا الاستعلام `2`، لأن 10^2 = 100.

### حساب اللوغاريتم للأساس 10 للعدد 1000 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

يحسب هذا الاستعلام اللوغاريتم للأساس 10 للعدد 1000، والنتيجة `3`، لأن 10^3 = 1000.
