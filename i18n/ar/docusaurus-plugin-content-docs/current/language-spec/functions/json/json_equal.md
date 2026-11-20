---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: مقارنة سلسلتين نصيتين بتنسيق JSON للتحقق من تكافئهما باستخدام SQL.
image: "/img/stackql-featured-image.png"
---
تقارن بين سلسلتين نصيتين بتنسيق JSON وتعيد 1 إذا كانتا متكافئتين، و0 بخلاف ذلك.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

:::tip 

الدالة `json_equal` هي دالة امتداد مخصصة في StackQL. وهي تدعم مقارنة كائنات JSON ومصفوفاته، وتعامل الكائنات التي تختلف فيها ترتيبات المفاتيح على أنها متكافئة وفق مواصفات JSON. الدالة `json_equal` متاحة في StackQL من الإصدار `0.5.707` فصاعدًا، وهي مدعومة فقط مع واجهة SQL الخلفية الافتراضية المضمنة التي تعمل في الذاكرة لـ StackQL.

:::

## بناء الجملة {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## الوسيطات {#arguments}

__*json1*__  
سلسلة نصية بتنسيق JSON لكائن أو مصفوفة.

__*json2*__  
سلسلة نصية أخرى بتنسيق JSON لمقارنتها بالأولى.

> يجب أن تكون الوسيطتان كلتاهما سلسلتين نصيتين بتنسيق JSON صالح.

## القيمة المرجعة {#return-values}
قيمة من نوع عدد صحيح: 1 إذا كانت السلسلتان النصيتان بتنسيق JSON متكافئتين، 0 إذا لم تكونا كذلك.

* * *

## أمثلة {#examples}

### مقارنة كائنات JSON {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### مقارنة مصفوفات JSON {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

لمزيد من المعلومات، راجع [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
