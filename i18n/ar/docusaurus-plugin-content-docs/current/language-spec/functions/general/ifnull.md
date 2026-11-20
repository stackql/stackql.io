---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة IFNULL في StackQL: إرجاع أول وسيطة ليست NULL من وسيطتين بالضبط، وهي شكل من COALESCE بوسيطتين."
image: "/img/stackql-featured-image.png"
---
تقبل وسيطتين بالضبط وتعيد أول وسيطة غير `NULL`، ويمكن استخدامها لاستبدال قيمة `NULL` بقيمة افتراضية.  

> تكافئ الدالة `IFNULL` الدالة [`COALESCE`](/language-spec/functions/general/coalesce) بوسيطتين فقط، إذ يمكن أن تقبل الدالة [`COALESCE`](/language-spec/functions/general/coalesce) أكثر من وسيطتين.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expression*__  
تعبيران من أي نوع بيانات يُحلان إلى قيمة `NULL` أو قيمة غير `NULL`.

> إذا كانت كلتا الوسيطتين *expression* بقيمة `NULL`، تعيد الدالة `IFNULL` القيمة `NULL`.


## القيمة المرجعة {#return-values}

تعيد أول قيمة غير `NULL` من الوسيطتين *expression*. 

* * *

## أمثلة {#examples}

### استبدال قيمة `NULL` بقيمة افتراضية {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
