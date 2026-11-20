---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الظل العكسي لعددين (y/x)، مع إرجاع زاوية بالراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الظل العكسي لعددين، أي الزاوية بالراديان التي يساوي ظلها النسبة بين عددين محددين (ناتج قسمة y على x)، مع معالجة إشارتي قيمتي الإدخال بشكل صحيح لتحديد الربع الصحيح.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*y*__  
الإحداثي y (البسط) للنقطة.

__*x*__  
الإحداثي x (المقام) للنقطة.

## القيمة المرجعة {#return-values}
تعيد الظل العكسي لـ y/x بالراديان، مع مراعاة الربع الصحيح للزاوية.

* * *

## أمثلة {#examples}

### حساب الظل العكسي للنقطة (1,1) {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

يعيد هذا الاستعلام π/4 أو `0.785` تقريبًا، لأن الظل العكسي للنقطة (1,1) يقابل زاوية مقدارها π/4 راديان في الربع الأول.

### حساب الظل العكسي للنقطة (-10, 10) {#calculate-the-arc-tangent-of-the-point--10-10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

يحسب هذا الاستعلام زاوية النقطة (-10, 10)، حيث y = 10 وx = -10، والنتيجة 3π/4 أو `2.356` تقريبًا: ظل هذه الزاوية هو -1 والنقطة في الربع الثاني.
