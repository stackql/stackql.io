---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الجيب الزائدي لعدد وإرجاع النتيجة باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الجيب الزائدي لقيمة محددة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي يراد حساب الجيب الزائدي له.

## القيمة المرجعة {#return-values}
تعيد الجيب الزائدي للقيمة المحددة.

* * *

## أمثلة {#examples}

### حساب الجيب الزائدي للعدد 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

يعيد هذا الاستعلام `1.1752` تقريبًا، لأن الجيب الزائدي للعدد 1 يبلغ نحو 1.1752.

### حساب الجيب الزائدي للعدد 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

يحسب هذا الاستعلام الجيب الزائدي للعدد 0، والنتيجة `0`، لأن الجيب الزائدي للعدد 0 يساوي 0 تمامًا.
