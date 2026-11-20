---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب ناتج رفع عدد إلى قوة عدد آخر باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب *x* مرفوعًا إلى القوة *y*.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*x*__  
عدد الأساس، وهو تعبير رقمي.

__*y*__  
الأس الذي يُرفع إليه عدد الأساس *x*، وهو تعبير رقمي أيضًا.

## القيمة المرجعة {#return-values}
تعيد ناتج رفع *x* إلى القوة *y*.

* * *

## أمثلة {#examples}

### حساب x مرفوعًا إلى القوة y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
