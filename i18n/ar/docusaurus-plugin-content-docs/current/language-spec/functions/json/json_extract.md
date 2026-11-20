---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة JSON_EXTRACT في StackQL: استخراج قيمة واحدة أو أكثر من كائن أو مصفوفة JSON حسب المسار، وهي الأداة الرئيسية لتسطيح الحقول المتداخلة في استجابات API."
image: "/img/stackql-featured-image.png"
---
تعيد قيمة واحدة أو أكثر من كائن أو مصفوفة JSON جيدة التكوين.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

:::tip 

استخدم الجملة [**DESCRIBE**](/language-spec/describe) لتحديد موقع أنواع البيانات `array` أو `object` التي يمكن استخدامها مع دوال JSON في StackQL. استخدم الدالة [**JSON_TYPE**](/language-spec/functions/json/json_type) لتحديد أنواع بيانات الكائنات المتداخلة أو عناصر المصفوفات.

:::

:::note 

تُنفذ الدالة `json_extract` باستخدام الدالة `json_extract_path_text` إذا كنت تستخدم stackql مع واجهة SQL الخلفية `postgres`

:::


## بناء الجملة {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*json_array_or_object*__  
مصفوفة أو كائن JSON.

__*json_path*__  
مسار JSON إلى مصفوفة أو كائن موجود داخل *json_array_or_object*. راجع [jsonpath.com](https://jsonpath.com/).

> إذا تم تقديم *json_path* واحد، فتعاد نتيجة عددية يطابق نوعها نوع بيانات المسار الذي تم حله.

> إذا تم تقديم عدة وسيطات *json_path*، فستكون النتيجة مصفوفة مكونة من الكائنات التي تم حلها بواسطة المسارات المعنية.

> يؤدي تحديد `'$'` للوسيطة *json_path* إلى إرجاع كائن JSON بالكامل المقابل للعمود الذي تمثله الوسيطة *json_array_or_object*.

> ملاحظة: يمكنك استخدام أدوات مثل [`json-viewer`](https://aarrange.com/json-viewer) لتطوير تعبيرات مسار JSON واختبارها

## القيمة المرجعة {#return-values}
سلسلة نصية أو عدد صحيح أو كائن أو مصفوفة أو قيمة منطقية أو null، حسب *json_array_or_object* و*json_path* المقدمة كإدخال.

* * *

## أمثلة {#examples}

### إرجاع كائنات الأقراص المرتبطة بكل مثيل Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع اسم الجهاز وحجم القرص الأول المرفق بكل مثيل Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع مستوى الشبكة لأول NIC مرفقة بكل مثيل Google Compute Engine {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### إرجاع قائمة النطاقات المرتبطة بأول حساب خدمة مرتبط بكل مثيل Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

لمزيد من المعلومات، راجع [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
