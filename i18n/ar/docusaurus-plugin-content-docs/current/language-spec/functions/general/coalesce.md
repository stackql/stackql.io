---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة COALESCE في StackQL: إرجاع أول وسيطة غير NULL من وسيطتين أو أكثر، في العادة لاستبدال قيمة مفقودة بقيمة افتراضية."
image: "/img/stackql-featured-image.png"
---
تعيد أول وسيطة ليست `NULL` من سلسلة من وسيطتين أو أكثر، ويمكن استخدامها لاستبدال قيمة `NULL` بقيمة افتراضية.  

> تكافئ الدالة `COALESCE` الدالة `NVL` في Oracle أو الدالة `IFNULL` في MySQL.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*expression*__  
تعبيران أو أكثر من أي نوع بيانات تُحل إلى قيمة `NULL` أو قيمة غير `NULL`.

> إذا كانت جميع الوسيطات *expression* بقيمة `NULL`، تعيد الدالة `COALESCE` القيمة `NULL`.

## القيمة المرجعة {#return-values}

تعيد أول قيمة غير `NULL` من سلسلة الوسيطات *expression*.

* * *

## أمثلة {#examples}

### استبدال قيمة `NULL` بقيمة افتراضية {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
