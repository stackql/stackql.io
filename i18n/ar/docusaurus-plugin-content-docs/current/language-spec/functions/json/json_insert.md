---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: إدراج قيم جديدة في مستند JSON دون الكتابة فوق البيانات الموجودة باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تدرج القيم الجديدة في مستند JSON في المسارات المحددة دون الكتابة فوق القيم الموجودة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*json_column*__  
عمود JSON أو التعبير المراد تعديله.

__*path*__  
تعبير مسار JSON الذي يجب إدراج القيمة الجديدة فيه. إذا كان المسار موجودًا بالفعل، فلا يتم استبدال القيمة الأصلية في ذلك المسار.

__*new_value*__  
القيمة المراد إدراجها في المسار المحدد في مستند JSON.

## القيمة المرجعة {#return-values}
تعيد مستند JSON بعد إدراج القيمة الجديدة إذا لم يكن المسار المحدد موجودًا بالفعل؛ وإلا فيعاد مستند JSON دون تغيير.

:::note السلوك إذا كان المفتاح موجودًا

| الدالة | الكتابة فوق القيمة إذا كانت موجودة؟ | الإنشاء إذا لم تكن موجودة؟ |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | لا | نعم |
| [`json_replace()`](/language-spec/functions/json/json_replace) | نعم | لا |
| [`json_set()`](/language-spec/functions/json/json_set) | نعم | نعم |

:::

* * *

## أمثلة {#examples}

### إدراج زوج مفتاح وقيمة جديد في كائن JSON {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

يدرج هذا الاستعلام مفتاحًا جديدًا اسمه 'region' بالقيمة 'Europe' في مستند JSON إذا لم يكن المفتاح 'region' موجودًا بالفعل. إذا كان 'region' مفتاحًا موجودًا بالفعل في المستند، فيعاد المستند الأصلي دون تغييرات.
