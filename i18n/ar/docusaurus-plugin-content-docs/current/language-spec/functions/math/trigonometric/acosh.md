---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب جيب التمام الزائدي العكسي (الدالة العكسية لجيب التمام الزائدي) لعدد باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب جيب التمام الزائدي العكسي (الدالة العكسية لجيب التمام الزائدي) لعدد، وهو القيمة غير السالبة التي يساوي جيب تمامها الزائدي العدد المحدد.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي أكبر من 1 أو يساويه، يراد حساب جيب التمام الزائدي العكسي له.

## القيمة المرجعة {#return-values}
تعيد جيب التمام الزائدي العكسي للقيمة المحددة.

* * *

## أمثلة {#examples}

### حساب جيب التمام الزائدي العكسي للعدد 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

يعيد هذا الاستعلام `0`، لأن جيب التمام الزائدي العكسي للعدد 1 هو 0.

### حساب جيب التمام الزائدي العكسي للعدد 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

يحسب هذا الاستعلام جيب التمام الزائدي العكسي للعدد 10، والنتيجة `2.9932` تقريبًا، لأن هذه هي القيمة التي يساوي جيب تمامها الزائدي 10.
