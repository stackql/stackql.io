---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب جيب التمام العكسي (الدالة العكسية لجيب التمام) لعدد، مع إرجاع زاوية بالراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب جيب التمام العكسي (الدالة العكسية لجيب التمام) لعدد، وهو الزاوية بالراديان التي يساوي جيب تمامها العدد المحدد.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي ضمن النطاق من -1 إلى 1، بما في ذلك -1 و1، يراد حساب جيب التمام العكسي له.

## القيمة المرجعة {#return-values}
تعيد جيب التمام العكسي للقيمة المحددة بالراديان.

* * *

## أمثلة {#examples}

### حساب جيب التمام العكسي للعدد 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

يعيد هذا الاستعلام `1.047` تقريبًا، لأن جيب التمام العكسي للعدد 0.5 هو π/3 راديان (60 درجة)، أي نحو 1.047 راديان.

### حساب جيب التمام العكسي للعدد 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

يحسب هذا الاستعلام جيب التمام العكسي للعدد 1، والنتيجة `0`، لأن جيب التمام للزاوية 0 راديان هو 1.
