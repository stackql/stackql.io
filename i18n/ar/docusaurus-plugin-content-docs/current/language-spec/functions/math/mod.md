---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب باقي قسمة عدد على عدد آخر باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب باقي قسمة *x* على *y*، وتعيد الباقي (modulus). وهذا مشابه لاستخدام العامل '%' لكنه متوافق مع الوسيطات التي ليست أعدادًا صحيحة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*x*__  
المقسوم، وهو تعبير رقمي.

__*y*__  
المقسوم عليه، وهو تعبير رقمي أيضًا.

## القيمة المرجعة {#return-values}
تعيد باقي قسمة *x* على *y*.

* * *

## أمثلة {#examples}

### حساب باقي قسمة 10 على 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
