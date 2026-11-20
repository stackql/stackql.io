---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة JSON_TYPE في StackQL: نوع (object أو array أو text أو integer أو real أو true أو false أو null) العنصر الخارجي لقيمة JSON، أو نوع مسار داخلها."
image: "/img/stackql-featured-image.png"
---
تعيد نوع بيانات العنصر الخارجي لقيمة JSON، أو نوع بيانات العنصر الموجود في المسار الاختياري.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

:::tip 

استخدم الجملة [**DESCRIBE**](/language-spec/describe) لتحديد موقع أنواع البيانات `array` أو `object` التي يمكن استخدامها مع دوال JSON في StackQL.

:::

:::note 

إذا كنت تستخدم StackQL مع واجهة SQL الخلفية `postgres`، فاستخدم الدالة `json_typeof` بدلًا من ذلك.

:::

## بناء الجملة {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*json_array_or_object*__  
مصفوفة أو كائن JSON.

__*json_path*__  
مسار JSON إلى مصفوفة أو كائن موجود داخل *json_array_or_object*. راجع [jsonpath.com](https://jsonpath.com/).

> إذا لم يكن *json_path* موجودًا في *json_array_or_object*، فتعيد الدالة `NULL`.

> تطرح الدالة `json_type()` خطأ إذا كانت أي من وسيطاتها غير جيدة التكوين.

## القيمة المرجعة {#return-values}
سلسلة نصية تمثل نوع البيانات الذي تم حله.

> تشمل أنواع البيانات `null` و`true` و`false` و`integer` و`real` و`text` و`array` و`object`.

* * *

## أمثلة {#examples}

### إرجاع نوع بيانات حقل متداخل داخل كائن أو مصفوفة JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
