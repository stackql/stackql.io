---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الجيب لزاوية معطاة بالراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الجيب لزاوية محددة معطاة بالراديان.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*radian_value*__  
تعبير رقمي يحدد الزاوية بالراديان التي يراد حساب الجيب لها.

## القيمة المرجعة {#return-values}
تعيد الجيب للزاوية المحددة.

* * *

## أمثلة {#examples}

### حساب الجيب للزاوية π/2 راديان {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

يعيد هذا الاستعلام `1`، لأن الجيب للزاوية π/2 راديان هو 1.

### حساب الجيب للزاوية π راديان {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

يحسب هذا الاستعلام الجيب للزاوية π راديان، والنتيجة `0` تقريبًا، لأن الجيب للزاوية π هو 0.
