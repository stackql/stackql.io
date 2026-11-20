---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة SIGN في StackQL: إرجاع -1 أو 0 أو 1 بحسب ما إذا كانت وسيطة رقمية سالبة أو 0 أو موجبة."
image: "/img/stackql-featured-image.png"
---
تعيد -1 إذا كانت قيمة الإدخال سالبة، و0 إذا كانت قيمة الإدخال 0، و1 إذا كانت قيمة الإدخال موجبة.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*numeric_expression*__  
تعبير من فئة أنواع البيانات الرقمية الدقيقة أو الرقمية التقريبية.

> إذا كان *numeric_expression* يساوي `NULL` أو كان من النوع `string` أو BLOB ولا يمكن تحويله إلى رقم دون فقدان، فستعيد الدالة `NULL`.

## القيمة المرجعة {#return-values}

تعيد `-1` أو `0` أو `1`.

* * *

## أمثلة {#examples}

### تحديد إشارة عدد {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
