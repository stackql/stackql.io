---
title: التسلسل الهرمي للموارد
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "التسلسل الهرمي للموارد في StackQL: كيف تتحول واجهات API السحابية وواجهات API لخدمات SaaS إلى جداول SQL قابلة للاستعلام عبر الموفرين والخدمات والموارد والأساليب."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

راجع أيضًا:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

يتم تمثيل الكائنات في StackQL وفق التسلسل الهرمي التالي، مع عرض مثال يستخدم موفر Google Cloud:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="التسلسل الهرمي لموارد StackQL"/>

### استخدام تدوين الكائنات في StackQL {#using-the-stackql-object-notation}

يتم التعبير عن هذا التسلسل الهرمي بتدوين الكائنات `<provider>.<service>.<resource>` ضمن سياق موفر معين، ويظهر هنا مثال على استخدام تدوين الكائنات هذا في جملة [`SELECT`](/language-spec/select):  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

إذا كان اسم المورد أو الخدمة يحتوي على نقطة (`.`)، فاستخدم علامة الاقتباس العكسية (__`__) لإحاطة اسم المورد، على سبيل المثال:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### عرض الخدمات في موفر {#showing-services-in-a-provider}

يمكن سرد الخدمات داخل موفر باستخدام الأمر [`SHOW SERVICES`](/language-spec/show) كما هو موضح هنا:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### عرض الموارد داخل خدمة {#showing-resources-within-a-service}

وبالمثل، يمكن سرد الموارد داخل خدمة باستخدام [`SHOW RESOURCES`](/language-spec/show) كما هو موضح هنا:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### عرض الحقول داخل مورد {#showing-fields-within-a-resource}

يمكن عرض الحقول داخل مورد باستخدام جملة [`DESCRIBE`](/language-spec/describe) كما هو موضح هنا:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### عرض الأساليب القابلة للتنفيذ لمورد {#showing-executable-methods-for-a-resource}

يمكن عرض الأساليب المتاحة لمورد باستخدام الأمر [`SHOW METHODS`](/language-spec/show) كما هو موضح هنا:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
