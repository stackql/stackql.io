---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الجذر التربيعي لعدد باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الجذر التربيعي لتعبير رقمي غير سالب.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير رقمي غير سالب يراد حساب جذره التربيعي.

## القيمة المرجعة {#return-values}
تعيد الجذر التربيعي للقيمة الرقمية غير السالبة المحددة.

* * *

## أمثلة {#examples}

### حساب الجذر التربيعي لعدد {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
