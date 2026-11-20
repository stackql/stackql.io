---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة JSON_ARRAY_LENGTH في StackQL: عدد العناصر في مصفوفة JSON، في المستوى الأعلى أو عند مسار داخل قيمة JSON."
image: "/img/stackql-featured-image.png"
---
تعيد عدد العناصر في مصفوفة JSON.  

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

:::tip 

استخدم الجملة [**DESCRIBE**](/language-spec/describe) لتحديد موقع أنواع البيانات `array` أو `object` التي يمكن استخدامها مع دوال JSON في StackQL. استخدم الدالة [**JSON_TYPE**](/language-spec/functions/json/json_type) لتحديد أنواع بيانات الكائنات المتداخلة أو عناصر المصفوفات.

:::

## بناء الجملة {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*json_array_or_object*__  
مصفوفة JSON أو كائن JSON.

> إذا كانت قيمة *json_array_or_object* كائن JSON (وليست مصفوفة) ولم تحدد *json_path*، تعاد القيمة `0`.

__*json_path*__  
مسار JSON الاختياري إلى مصفوفة موجودة داخل كائن.  راجع [jsonpath.com](https://jsonpath.com/).

> إذا تم توفير *json_path*، تحدد الدالة `json_array_length` موقع المسار داخل *json_array_or_object* وتعيد طول المصفوفة، أو `0` إذا كان *json_path* يشير إلى كائن (وليس مصفوفة).  إذا تعذر العثور على *json_path* داخل *json_array_or_object*، تعاد القيمة `NULL`.

> إذا لم تكن قيمة *json_array_or_object* بتنسيق JSON جيد التكوين، أو لم يكن *json_path* مسار JSON جيد التكوين، فإن الدالة تطرح خطأ.

## القيمة المرجعة {#return-values}
تعيد قيمة من نوع عدد صحيح.

* * *

## أمثلة {#examples}

### إرجاع عدد الأقراص المرفقة بمثيلات Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع عدد واجهات الشبكة المرفقة بمثيلات Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع عدد قوائم ACL المرتبطة بحاويات Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### إرجاع عدد النطاقات المرتبطة بحساب خدمة {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
