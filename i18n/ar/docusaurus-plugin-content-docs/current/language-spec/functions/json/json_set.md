---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: تحديث القيم أو إدراجها في مستند JSON باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تحدّث القيم الموجودة أو تدرج قيمًا جديدة في مستند JSON في المسارات المحددة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*json_column*__  
عمود JSON أو التعبير المراد تعديله.

__*path*__  
تعبير مسار JSON الذي يحدد مكان تعيين القيمة. إذا كان المسار موجودًا، فيتم تحديث القيمة؛ وإذا لم يكن المسار موجودًا، فتتم إضافة زوج مفتاح وقيمة جديد.

__*value_to_set*__  
القيمة المراد تعيينها في المسار المحدد في مستند JSON.

## القيمة المرجعة {#return-values}
تعيد مستند JSON بعد تعيين القيمة في المسار المحدد. إذا كان المسار موجودًا، فيتم تحديث القيمة الموجودة؛ وإلا فتتم إضافة زوج مفتاح وقيمة جديد.

:::note السلوك إذا كان المفتاح موجودًا

| الدالة | الكتابة فوق القيمة إذا كانت موجودة؟ | الإنشاء إذا لم تكن موجودة؟ |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | لا | نعم |
| [`json_replace()`](/language-spec/functions/json/json_replace) | نعم | لا |
| [`json_set()`](/language-spec/functions/json/json_set) | نعم | نعم |

:::

* * *

## أمثلة {#examples}

### تحديث قيمة موجودة في كائن JSON {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending","region":"US"}', '$.status', 'active') as updated_json;

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne","status":"active","region":"US"}   |
-- |---------------------------------------------------------|
```

يحدّث هذا الاستعلام المفتاح 'status' إلى 'active' في مستند JSON إذا كان المفتاح 'status' موجودًا. وإذا لم يكن 'status' موجودًا في المستند، فتتم إضافته بالقيمة 'active'.

### إضافة زوج مفتاح وقيمة جديد إلى كائن JSON {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with a date value to a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending"}', '$.lastChecked', '2024-04-29') as updated_json;

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne","status":"pending","lastChecked":"2024-04-29"}   |
|-----------------------------------------------------------------------|
*/
```

يضيف هذا الاستعلام مفتاحًا جديدًا 'lastChecked' بالقيمة '2024-04-29' إلى مستند JSON إذا لم يكن 'lastChecked' موجودًا بالفعل. وإذا كان موجودًا، فيتم تحديث القيمة الموجودة إلى '2024-04-29'.
