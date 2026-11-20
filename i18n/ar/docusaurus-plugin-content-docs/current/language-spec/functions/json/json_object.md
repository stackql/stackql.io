---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "الدالة JSON_OBJECT في StackQL: إنشاء كائن JSON من وسيطات المفاتيح والقيم، على سبيل المثال لإنشاء نصوص الطلبات لجمل INSERT وUPDATE."
image: "/img/stackql-featured-image.png"
---
تعيد كائنًا يتم إنشاؤه من الوسيطات المقدمة.

راجع أيضًا:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ أنواع البيانات ]](/language-spec/data-types)

* * * 

:::tip 

استخدم الجملة [**DESCRIBE**](/language-spec/describe) لتحديد موقع أنواع البيانات `array` أو `object` التي يمكن استخدامها مع دوال JSON في StackQL.

:::

:::note 

إذا كنت تستخدم stackql مع واجهة SQL الخلفية `postgres`، فاستخدم الدالة `jsonb_build_object` بدلًا من الدالة `json_object`: يمرر StackQL الدالة `json_object` إلى PostgreSQL دون تغيير، ودالة PostgreSQL التي تحمل هذا الاسم تأخذ مفاتيحها وقيمها في صورة مصفوفات نصية.

:::

## بناء الجملة {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## الوسيطات {#arguments}

__*key1, key2, ...*__  
تسميات كائن JSON.

__*value1, value2, ...*__  
القيم المقابلة للتسميات في كائن JSON.

## القيمة المرجعة {#return-values}
سلسلة نصية تمثل كائن JSON جيد التكوين.

* * *

## أمثلة {#examples}

### إنشاء مصفوفة من كائنات JSON من عمود {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

لمزيد من المعلومات، راجع [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
