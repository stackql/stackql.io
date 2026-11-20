---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب جيب التمام الزائدي لعدد وإرجاع النتيجة باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب جيب التمام الزائدي لقيمة محددة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*value*__  
تعبير رقمي يراد حساب جيب التمام الزائدي له.

## القيمة المرجعة {#return-values}
تعيد جيب التمام الزائدي للقيمة المحددة.

* * *

## أمثلة {#examples}

### حساب جيب التمام الزائدي للعدد 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

يعيد هذا الاستعلام `1.5431` تقريبًا، لأن جيب التمام الزائدي للعدد 1 يبلغ نحو 1.5431.

### حساب جيب التمام الزائدي للعدد 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

يحسب هذا الاستعلام جيب التمام الزائدي للعدد 0، والنتيجة `1`، لأن جيب التمام الزائدي للعدد 0 يساوي 1 تمامًا.
