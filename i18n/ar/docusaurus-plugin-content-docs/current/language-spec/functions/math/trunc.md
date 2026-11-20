---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة TRUNC في StackQL: الجزء الصحيح من عدد، مع التقريب نحو الصفر وتجاهل أي كسر."
image: "/img/stackql-featured-image.png"
---

تعيد الجزء الصحيح من عدد بالتقريب نحو الصفر، مع إزالة أي جزء كسري.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير من فئة أنواع البيانات الرقمية الدقيقة أو الرقمية التقريبية.

## القيمة المرجعة {#return-values}
تعيد قيمة رقمية مقتطعة إلى جزئها الصحيح.

* * *

## أمثلة {#examples}

### اقتطاع عدد الأيام منذ إنشاء حاويات Google Cloud Storage إلى أيام كاملة {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
