---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الظل العكسي (الدالة العكسية للظل) لعدد، مع إرجاع زاوية بالراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الظل العكسي (الدالة العكسية للظل) لعدد، وهو الزاوية بالراديان التي يساوي ظلها العدد المحدد.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي يراد حساب الظل العكسي له.

## القيمة المرجعة {#return-values}
تعيد الظل العكسي للقيمة المحددة بالراديان.

* * *

## أمثلة {#examples}

### حساب الظل العكسي للعدد 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

يعيد هذا الاستعلام π/4 أو `0.785` تقريبًا، لأن الظل العكسي للعدد 1 هو π/4 راديان.

### حساب الظل العكسي للعدد 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

يحسب هذا الاستعلام الظل العكسي للعدد 0، والنتيجة `0`، لأن الظل للزاوية 0 راديان هو 0.
