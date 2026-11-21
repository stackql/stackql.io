---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة JSON_EACH ذات القيمة الجدولية في StackQL: توسيع مصفوفة JSON أو كائن JSON إلى صف واحد لكل عنصر أو عضو، لربط استجابات API المتداخلة وتصفيتها."
image: "/img/stackql-featured-image.png"
---
دالة ذات قيمة جدولية يتكون الجدول الذي تعيده من صف واحد لكل عنصر في المصفوفة أو عضو في الكائن.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

:::tip 

استخدم الجملة [**DESCRIBE**](/language-spec/describe) لتحديد موقع أنواع البيانات `array` التي يمكن استخدامها مع الدالة `json_each`.

:::

:::note 

تُنفذ الدالة `json_each` باستخدام الدالة `json_array_elements_text` إذا كنت تستخدم stackql مع واجهة SQL الخلفية `postgres`

:::

## بناء الجملة {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object [, json_path]);
```
## الوسيطات {#arguments}

__*json_array_or_object*__  
مصفوفة JSON أو كائن JSON.

__*json_path*__  
اختياري. مسار JSON إلى مصفوفة أو كائن موجود داخل *json_array_or_object*.  راجع [jsonpath.com](https://jsonpath.com/).

> إذا لم يوجد *json_path* في *json_array_or_object*، لا تعيد الدالة أي صفوف.

> تطرح الدالة `json_each()` خطأ إذا لم تكن قيمة *json_array_or_object* بتنسيق JSON جيد التكوين.

## القيمة المرجعة {#return-values}
جدول يتكون من صف واحد لكل عنصر في المصفوفة أو عضو في الكائن.

* * *

## أمثلة {#examples}

### فك تداخل مصفوفة من السلاسل النصية إلى جدول مربوط بالحقول المرتبطة بها {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

للتوضيح:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

تعيد...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

يستخدم استعلام `stackql` التالي الدالة `json_each` لفك تداخل المصفوفة `members` إلى جدول مربوط بالحقل `role`، وبالحقل `condition` اختياريًا.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

سيعيد...

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
