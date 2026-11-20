---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: تحويل زاوية من الدرجات إلى الراديان باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحول زاوية محددة من الدرجات إلى الراديان.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*angle_in_degrees*__  
تعبير رقمي يمثل زاوية بالدرجات.

## القيمة المرجعة {#return-values}
تعيد ما يعادل الزاوية بالراديان.

* * *

## أمثلة {#examples}

### تحويل 180 درجة إلى راديان {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

سيعيد هذا الاستعلام π (`3.14159` تقريبًا)، لأن تحويل 180 درجة إلى راديان ينتج π راديان.

### تحويل 90 درجة إلى راديان {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

يحسب هذا الاستعلام تحويل 90 درجة إلى راديان، والنتيجة π/2 أو `1.5708` تقريبًا، لأن 90 درجة هي نصف 180 درجة.
