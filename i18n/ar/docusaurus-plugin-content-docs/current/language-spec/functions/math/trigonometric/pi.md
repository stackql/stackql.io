---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: استرداد الثابت الرياضي π (pi) باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تعيد الثابت الرياضي π (pi)، وهو نسبة محيط الدائرة إلى قطرها.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT PI() AS pi_value;
```

## الوسيطات {#arguments}

لا تتطلب هذه الدالة أي وسيطات.

## القيمة المرجعة {#return-values}
تعيد الثابت π، الذي يساوي 3.14159 تقريبًا.

* * *

## أمثلة {#examples}

### استرداد قيمة π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

سيعرض هذا الاستعلام قيمة π، وهي `3.14159` تقريبًا، وهي مفيدة في العديد من العمليات الحسابية الرياضية المتعلقة بالدوائر والدوال الهندسية الأخرى.
