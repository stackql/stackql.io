---
title: عامل الدمج
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "عامل الدمج || في StackQL: ضم قيمتين أو أكثر من قيم السلاسل النصية في سلسلة نصية واحدة."
image: "/img/stackql-featured-image.png"
---
يعيد السلسلة النصية الناتجة عن دمج، أي ضم، قيمتين أو أكثر من قيم السلاسل النصية.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) 

* * * 

## بناء الجملة {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*character_expression1*__, __*character_expression2*__  
قيمة حرفية نصية أو عمود من نوع السلسلة النصية.

## القيمة المرجعة {#return-values}

يعيد سلسلة نصية.

:::note

منذ الإصدار `v1.0` من StackQL، يتعامل المحلل مع `||` على أنه عامل الدمج، بأولوية ربط أعلى من عوامل المقارنة، لذلك تقارن العبارة `WHERE id = prefix || suffix` قيمة `id` بالسلسلة النصية الناتجة عن الدمج.  كانت الإصدارات السابقة تعامل `||` كوحدة معجمية للعامل المنطقي `OR`.  استخدم `OR` للفصل المنطقي.  يمكن أيضًا دمج [متغيرات الجلسة](/getting-started/session-variables): `$prefix || '-' || $suffix`.

:::

* * *

## أمثلة {#examples}

### دمج حقلين معًا {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

لمزيد من المعلومات، راجع [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
