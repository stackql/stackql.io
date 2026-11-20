---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: حساب اللوغاريتم الطبيعي لعدد باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحسب اللوغاريتم الطبيعي (اللوغاريتم للأساس e) لعدد معين.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير رقمي موجب يراد حساب لوغاريتمه الطبيعي.

## القيمة المرجعة {#return-values}
تعيد اللوغاريتم الطبيعي للقيمة الرقمية الموجبة المحددة.

* * *

## أمثلة {#examples}

### حساب اللوغاريتم الطبيعي للعدد 2.71828 (يساوي e تقريبًا) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

يعيد هذا الاستعلام قيمة قريبة من `1`، لأن اللوغاريتم الطبيعي للعدد e (الذي يساوي 2.71828 تقريبًا) هو 1.

### حساب اللوغاريتم الطبيعي لعدد أكبر {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

يحسب هذا الاستعلام اللوغاريتم الطبيعي للعدد 10، فيعطي القيمة اللوغاريتمية للعدد 10 للأساس e.
