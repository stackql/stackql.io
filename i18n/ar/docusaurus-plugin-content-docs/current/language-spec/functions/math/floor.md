---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة FLOOR في StackQL: تقريب وسيطة رقمية لأسفل إلى أقرب عدد صحيح."
image: "/img/stackql-featured-image.png"
---
تقرّب وسيطة رقمية لأسفل إلى أقرب عدد صحيح.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير من فئة أنواع البيانات الرقمية الدقيقة أو الرقمية التقريبية.

## القيمة المرجعة {#return-values}
تعيد أكبر عدد صحيح أصغر من التعبير الرقمي المحدد أو يساويه.

* * *

## أمثلة {#examples}

### تقريب عدد بفاصلة عائمة لأسفل إلى أقرب عدد صحيح (أصغر) {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_floor
FROM google.storage.buckets WHERE project = 'stackql';
```
