---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الظل لزاوية معطاة بالراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الظل لزاوية محددة معطاة بالراديان.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*radian_value*__  
تعبير رقمي يحدد الزاوية بالراديان التي يراد حساب الظل لها.

## القيمة المرجعة {#return-values}
تعيد الظل للزاوية المحددة.

* * *

## أمثلة {#examples}

### حساب الظل للزاوية π/4 راديان {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

يعيد هذا الاستعلام `1`، لأن الظل للزاوية π/4 راديان هو 1.

### حساب الظل للزاوية π راديان {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

يحسب هذا الاستعلام الظل للزاوية π راديان، والنتيجة `0` تقريبًا، لأن الظل للزاوية π قريب للغاية من 0.
