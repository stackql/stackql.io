---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: تحويل زاوية من الراديان إلى الدرجات باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحول زاوية محددة من الراديان إلى الدرجات.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*angle_in_radians*__  
تعبير رقمي يمثل زاوية بالراديان.

## القيمة المرجعة {#return-values}
تعيد ما يعادل الزاوية بالدرجات.

* * *

## أمثلة {#examples}

### تحويل π راديان إلى درجات {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

سيعيد هذا الاستعلام `180` تقريبًا، لأن تحويل π راديان (`3.14159` تقريبًا) ينتج نحو 180 درجة.

### تحويل π/2 راديان إلى درجات {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

يحسب هذا الاستعلام تحويل π/2 راديان (`1.5708` تقريبًا) إلى درجات، والنتيجة نحو `90`، لأن π/2 راديان تعادل زاوية مقدارها 90 درجة.
