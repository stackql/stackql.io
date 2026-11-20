---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الجيب العكسي (الدالة العكسية للجيب) لعدد، مع إرجاع زاوية بالراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الجيب العكسي (الدالة العكسية للجيب) لعدد، وهو الزاوية بالراديان التي يساوي جيبها العدد المحدد.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي ضمن النطاق من -1 إلى 1، بما في ذلك -1 و1، يراد حساب الجيب العكسي له.

## القيمة المرجعة {#return-values}
تعيد الجيب العكسي للقيمة المحددة بالراديان.

* * *

## أمثلة {#examples}

### حساب الجيب العكسي للعدد 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

يعيد هذا الاستعلام π/6 أو `0.5236` تقريبًا، لأن الجيب العكسي للعدد 0.5 يقابل زاوية مقدارها π/6 راديان.

### حساب الجيب العكسي للعدد -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

يحسب هذا الاستعلام الجيب العكسي للعدد -1، والنتيجة -π/2 أو `-1.5708` تقريبًا، لأن الجيب للزاوية -π/2 هو -1.
