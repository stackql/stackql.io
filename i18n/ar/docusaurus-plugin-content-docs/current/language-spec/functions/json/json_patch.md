---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: تطبيق JSON MergePatch على مستند JSON باستخدام SQL في StackQL لتعديل كائنات JSON أو دمجها.
image: "/img/stackql-featured-image.png"
---
تطبق JSON MergePatch (RFC 7396) على مستند JSON، فتعدله وفقًا للعمليات المحددة. تُستخدم هذه الدالة لإضافة العناصر أو تعديلها أو حذفها داخل كائنات JSON، لكنها تتعامل مع مصفوفات JSON كوحدات غير قابلة للتجزئة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*target_json*__  
مستند JSON المراد تصحيحه.

__*patch_json*__  
مستند JSON يصف التغييرات المراد تطبيقها وفقًا لخوارزمية MergePatch.

## القيمة المرجعة {#return-values}
تعيد مستند JSON المعدل بعد تطبيق التصحيح.

* * *

## أمثلة {#examples}

### تصحيح MergePatch بسيط {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

الإخراج: `{"a":1,"b":2,"c":3,"d":4}`

### استبدال مصفوفة بقيمة {#replace-an-array-with-a-value}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

الإخراج: `{"a":9,"b":2}`

### إزالة عنصر باستخدام null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

الإخراج: `{"a":9,"c":8}`

### تصحيح MergePatch متداخل {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

الإخراج: `{"a":{"x":1,"y":9},"b":3,"c":8}`

تتيح هذه الميزة إجراء تحديثات مرنة وديناميكية على بيانات JSON المخزنة في قاعدة بيانات، وهي مفيدة بشكل خاص للتطبيقات التي تتطلب تعديلات مشروطة على هياكل البيانات.
