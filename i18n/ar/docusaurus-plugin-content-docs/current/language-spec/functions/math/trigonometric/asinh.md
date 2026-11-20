---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب الجيب الزائدي العكسي (الدالة العكسية للجيب الزائدي) لعدد باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب الجيب الزائدي العكسي (الدالة العكسية للجيب الزائدي) لعدد، وهو القيمة التي يساوي جيبها الزائدي العدد المحدد.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي يراد حساب الجيب الزائدي العكسي له.

## القيمة المرجعة {#return-values}
تعيد الجيب الزائدي العكسي للقيمة المحددة.

* * *

## أمثلة {#examples}

### حساب الجيب الزائدي العكسي للعدد 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

يعيد هذا الاستعلام `0.4812` تقريبًا، لأن الجيب الزائدي العكسي للعدد 0.5 يبلغ نحو 0.4812.

### حساب الجيب الزائدي العكسي للعدد -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

يحسب هذا الاستعلام الجيب الزائدي العكسي للعدد -1، والنتيجة `-0.8814` تقريبًا، لأن هذه هي القيمة التي يساوي جيبها الزائدي -1.
