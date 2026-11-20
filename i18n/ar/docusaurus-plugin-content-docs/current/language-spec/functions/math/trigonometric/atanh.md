---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الظل الزائدي العكسي (الدالة العكسية للظل الزائدي) لعدد باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الظل الزائدي العكسي (الدالة العكسية للظل الزائدي) لعدد، وهو القيمة التي يساوي ظلها الزائدي العدد المحدد.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي ضمن النطاق من -1 إلى 1 (دون تضمين -1 و1)، يراد حساب الظل الزائدي العكسي له.

## القيمة المرجعة {#return-values}
تعيد الظل الزائدي العكسي للقيمة المحددة.

* * *

## أمثلة {#examples}

### حساب الظل الزائدي العكسي للعدد 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

يعيد هذا الاستعلام `0.5493` تقريبًا، لأن الظل الزائدي العكسي للعدد 0.5 يبلغ نحو 0.5493.

### حساب الظل الزائدي العكسي للعدد -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

يحسب هذا الاستعلام الظل الزائدي العكسي للعدد -0.5، والنتيجة `-0.5493` تقريبًا، لأن هذه هي القيمة التي يساوي ظلها الزائدي -0.5.
