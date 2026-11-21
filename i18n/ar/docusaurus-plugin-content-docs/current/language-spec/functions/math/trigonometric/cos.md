---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب جيب التمام لزاوية معطاة بالراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب جيب التمام لزاوية محددة معطاة بالراديان.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*radian_value*__  
تعبير رقمي يحدد الزاوية بالراديان التي يراد حساب جيب التمام لها.

## القيمة المرجعة {#return-values}
تعيد جيب التمام للزاوية المحددة.

* * *

## أمثلة {#examples}

### حساب جيب التمام للزاوية π/3 راديان {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

يعيد هذا الاستعلام `0.5000000000000001`، وهو التقريب بالفاصلة العائمة لقيمة جيب التمام للزاوية π/3 راديان، وهي 0.5.

### حساب جيب التمام للزاوية π راديان {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

يحسب هذا الاستعلام جيب التمام للزاوية π راديان، والنتيجة `-1`، لأن جيب التمام للزاوية π هو -1.
