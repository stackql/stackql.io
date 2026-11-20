---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة CEIL في StackQL: تقريب وسيطة رقمية لأعلى إلى أقرب عدد صحيح."
image: "/img/stackql-featured-image.png"
---
تقرّب وسيطة رقمية لأعلى إلى أقرب عدد صحيح.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير من فئة أنواع البيانات الرقمية الدقيقة أو الرقمية التقريبية.

## القيمة المرجعة {#return-values}
تعيد أصغر عدد صحيح أكبر من التعبير الرقمي المحدد أو يساويه.

* * *

## أمثلة {#examples}

### تقريب عدد بفاصلة عائمة لأعلى إلى أقرب عدد صحيح (أكبر) {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
