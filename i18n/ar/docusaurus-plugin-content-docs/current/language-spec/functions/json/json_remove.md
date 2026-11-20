---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: إزالة عناصر من مستند JSON بتحديد مساراتها باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تزيل العناصر المحددة من مستند JSON باستخدام تعبيرات المسار. إذا لم يكن أحد المسارات المحددة موجودًا في المستند، فيتم تجاهله. يمكن أن تؤثر معالجة المسارات بالتتابع على البنية في عمليات الإزالة اللاحقة.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*json_document*__  
مستند JSON المراد إزالة العناصر منه.

__*path1, path2, ...*__  
تعبير مسار واحد أو أكثر يشير إلى العناصر المراد إزالتها في مستند JSON.

## القيمة المرجعة {#return-values}
تعيد نسخة من مستند JSON بعد إزالة العناصر المحددة. إذا لم يتم تقديم أي مسارات، فتعيد JSON المقدم كإدخال بعد إعادة تنسيقه وإزالة المسافات البيضاء الزائدة منه.

* * *

## أمثلة {#examples}

### إزالة عنصر من مصفوفة JSON {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

الإخراج: `'[0,1,3,4]'`

### إزالة عدة عناصر من مصفوفة JSON {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

الإخراج: `'[1,3,4]'`

### إزالة العنصرين الأخير والأول باستخدام فهرس سالب {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

الإخراج: `'[1,2,3]'`

### إزالة مسار غير موجود {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

الإخراج: `'{"x":25,"y":42}'`

### إزالة مفتاح موجود من كائن JSON {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

الإخراج: `'{"x":25}'`

### حالة حدية: الإزالة باستخدام المسار الجذر {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

الإخراج: `NULL`

توضح هذه الأمثلة كيفية استخدام الدالة `json_remove()` لمعالجة بيانات JSON ديناميكيًا، بما يغطي سيناريوهات متنوعة تمتد من عمليات الحذف البسيطة إلى التعامل مع الحالات الحدية في تعديلات هياكل البيانات.
