---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب لوغاريتم عدد لأي أساس باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب لوغاريتم عدد لأساس محدد، وهي شكل أعم للدالة اللوغاريتمية مقارنة بالدالة LN التي تستخدم الأساس e.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*base*__  
أساس اللوغاريتم، وهو تعبير رقمي موجب لا يساوي 1.

__*numeric_expression*__  
تعبير رقمي موجب يراد حساب لوغاريتمه.

## القيمة المرجعة {#return-values}
تعيد لوغاريتم القيمة الرقمية المحددة للأساس المحدد.

* * *

## أمثلة {#examples}

### حساب لوغاريتم العدد 1000 للأساس 10 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

يعيد هذا الاستعلام `3`، لأن 10^3 = 1000.

### حساب لوغاريتم العدد 16 للأساس 2 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
يحسب هذا الاستعلام لوغاريتم العدد 16 للأساس 2، والنتيجة `4`، لأن 2^4 = 16.
