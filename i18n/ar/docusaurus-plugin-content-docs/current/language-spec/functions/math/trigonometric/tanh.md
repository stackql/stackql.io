---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الظل الزائدي لعدد وإرجاع النتيجة باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الظل الزائدي لقيمة محددة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي يراد حساب الظل الزائدي له.

## القيمة المرجعة {#return-values}
تعيد الظل الزائدي للقيمة المحددة.

* * *

## أمثلة {#examples}

### حساب الظل الزائدي للعدد 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

يعيد هذا الاستعلام `0.7616` تقريبًا، لأن الظل الزائدي للعدد 1 يبلغ نحو 0.7616.

### حساب الظل الزائدي للعدد 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

يحسب هذا الاستعلام الظل الزائدي للعدد 0، والنتيجة `0`، لأن الظل الزائدي للعدد 0 يساوي 0 تمامًا.
