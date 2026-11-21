---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: "SHOW في StackQL: سرد الموفرين والخدمات والموارد والأساليب، وإنشاء قوالب INSERT، وفحص إصدار الملف الثنائي قيد التشغيل والمساهمين فيه وتبعياته."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

تسرد هذه الجملة العناصر في مجموعة، على سبيل المثال عرض جميع الخدمات المتاحة في موفر، أو عرض جميع الموارد المتاحة في خدمة معينة. كما تكشف عن إصدار الملف الثنائي لـ StackQL قيد التشغيل والمساهمين فيه وتبعياته البرمجية.

راجع أيضًا:  
[[ التسلسل الهرمي لموارد StackQL ]](/getting-started/resource-hierarchy)

* * * 

## بناء الجملة {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE '<pattern>' | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

لا تتطلب الكلمات الأساسية `VERSION` و`CONTRIBUTORS` و`DEPENDENCIES` أي تكوين للموفر أو للمصادقة. لا تدعم `DEPENDENCIES` إلا عبارة `LIKE` اختيارية؛ أما `IN` و`FROM` و`WHERE` و`LIMIT` فغير مدعومة.

* * *

## أمثلة {#examples}

### سرد جميع الموفرين {#list-all-providers}
اسرد الموفرين المتاحين في بيئة StackQL المحلية: الموفرين الذين تم سحبهم من سجل الموفرين، بالإضافة إلى الموفرين المدمجين في StackQL.
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>القيمة المرجعة:</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### سرد جميع الخدمات في موفر {#list-all-services-in-a-cloud-provider}
اسرد جميع الخدمات المتاحة في موفر معين، واستخدم الكلمة الأساسية الاختيارية EXTENDED لإرجاع معلومات إضافية.
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>القيمة المرجعة:</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### سرد جميع الموارد في خدمة موفر {#list-all-resources-within-a-cloud-provider-service}
اسرد جميع الموارد المتاحة في خدمة معينة من خدمات موفر. تعيد الكلمة الأساسية الاختيارية `EXTENDED` وصف المورد بالإضافة إلى اسمه.
```sql
-- Returns all resources available in a provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>القيمة المرجعة:</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### عرض الأساليب المتاحة لمورد في خدمة موفر {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
اسرد جميع الأساليب المتاحة لمورد معين.
```sql
-- Returns all methods available for a provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>القيمة المرجعة:</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### إنشاء قالب `INSERT` باستخدام الأمر `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

يمكن استخدام الأمر `SHOW` لإنشاء قوالب لجمل `INSERT` كما هو موضح هنا:

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>القيمة المرجعة:</strong></em></summary>

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

</details>

> يمكن قصر الحقول المرجعة على الحقول التي يتطلبها الموفر فقط؛ راجع [__إنشاء قوالب البنية التحتية__](/getting-started/templating) للاطلاع على أمثلة على ذلك.

> يمكن بعد ذلك استخدام هذا القالب مع ملف بيانات `json` أو `jsonnet` لتوفير القيم في وقت التشغيل؛ لمزيد من المعلومات، راجع [__استخدام المتغيرات__](/getting-started/variables).

### عرض إصدار StackQL {#show-the-stackql-version}

تعيد هذه الجملة إصدار عملية StackQL قيد التشغيل. هذا مفيد في أوضاع واجهة الأوامر وexec وخادم بروتوكول الاتصال الخاص بـ Postgres عندما تحتاج إلى التأكد من البناء الذي يجيب عن الاستعلامات. تتوفر المعلومات نفسها أيضًا من خلال أداة MCP `server_info`.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>القيمة المرجعة:</strong></em></summary>

```
|---------|
| version |
|---------|
| 1.0.888 |
|---------|
```

</details>

استخدم الكلمة الأساسية الاختيارية `EXTENDED` لإرجاع إيداع git والطابع الزمني للبناء ومنصة البناء أيضًا:

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>القيمة المرجعة:</strong></em></summary>

```
|---------|---------|----------------------|----------|
| version | commit  |      build_date      | platform |
|---------|---------|----------------------|----------|
| 1.0.888 | f7b4116 | 2026-11-05T00:26:03Z | Windows  |
|---------|---------|----------------------|----------|
```

</details>

> لا تتطلب الجملة `SHOW VERSION` أي تكوين للموفر أو السجل أو المصادقة، ويمكن تنفيذها بأمان في أي جلسة.

### سرد المساهمين في StackQL {#list-stackql-contributors}

تعيد هذه الجملة لوحة صدارة المساهمين المضمنة في الملف الثنائي لـ StackQL قيد التشغيل. تنسب اللقطة الفضل إلى المساهمين من البشر على GitHub الذين أضافوا نجمة إلى مستودع StackQL، مرتبين حسب عدد المساهمات من الأعلى إلى الأدنى. اللقطة ثابتة لكل بناء ولا تستعلم عن GitHub في وقت التشغيل.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| العمود | الوصف |
| --- | --- |
| `contributor` | اسم المستخدم على GitHub |
| `contributions` | عدد المساهمات المسجل في اللقطة؛ يظهر مع `EXTENDED` |

### سرد التبعيات البرمجية للملف الثنائي {#list-the-binarys-software-dependencies}

تعيد هذه الجملة، دون اتصال بالشبكة، جرد وحدات Go المتضمنة في الملف التنفيذي لـ StackQL قيد التشغيل. يوفر ذلك قائمة مواد برمجية (SBOM) خفيفة للملف الثنائي، يمكن للمستخدمين والوكلاء الاستعلام عنها في وقت التشغيل. يشمل الجرد تبعيات Go المباشرة والمتعدية؛ أما تعريفات الموفرين المثبتة والأغلفة المنفصلة وأدوات CI والوحدات المخصصة للاختبار فقط وصور الحاويات وحزم نظام التشغيل فهي خارج نطاقه.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

الأعمدة الافتراضية هي `name` و`version` و`license` و`description`. تضيف الكلمة الأساسية `EXTENDED` الأعمدة المتبقية الواردة أدناه دون تغيير الصفوف المرجعة.

| العمود | الوصف |
| --- | --- |
| `name` | مسار وحدة Go الفعلي، مع استخدام مسار الاستبدال عند تطبيق استبدال لوحدة Go |
| `version` | إصدار الوحدة الدقيق، بما في ذلك الإصدارات التمهيدية والإصدارات الزائفة |
| `license` | معرف الترخيص المكتشف؛ `UNKNOWN` عند تعذر تحديده أو التباسه |
| `description` | وصف المكون، عند توفره |
| `source_url` | عنوان URL لمستودع المصدر؛ يظهر مع `EXTENDED` |
| `license_url` | عنوان URL لملف الترخيص، مقفل على وسم الوحدة أو مراجعة المصدر؛ يظهر مع `EXTENDED` |
| `purl` | عنوان Package URL الذي يحدد الوحدة والإصدار؛ يظهر مع `EXTENDED` |
| `checksum` | المجموع الاختباري لوحدة Go مع البادئة `h1:`، وليس ملخص SHA-256 لملف تنفيذي أو ملف مضغوط؛ يظهر مع `EXTENDED` |
| `original_name` | مسار الوحدة المطلوب في الأصل عند تطبيق استبدال؛ يظهر مع `EXTENDED` |
| `original_version` | الإصدار المطلوب في الأصل عند تطبيق استبدال؛ يظهر مع `EXTENDED` |

تكون الصفوف مرتبة حسب اسم الوحدة الفعلي، ثم حسب الإصدار. تستخدم الاستبدالات إصدار البديل ومجموعه الاختباري وبيانات النسب الخاصة به. قيمة `original_name` و`original_version` هي `NULL` للوحدات التي ليس لها استبدال. بيانات التعريف الأخرى غير المتوفرة قيمتها `NULL` أيضًا، باستثناء التراخيص التي يتعذر تحديدها فقيمتها `UNKNOWN`. ليس للاستبدالات التي تشير إلى نظام الملفات المحلي إصدار أو عناوين URL أو مجموع اختباري مختلق.

استخدم عبارة `LIKE` للبحث في اسم الوحدة الفعلي أو اسم الوحدة الأصلي أو الوصف:

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

المطابقة غير حساسة لحالة الأحرف. يطابق `%` أي عدد من الأحرف (صفر أو أكثر)، ويطابق `_` أي حرف واحد بالضبط. لا يشمل البحث عمودي الترخيص والإصدار. كرّر علامة الاقتباس المفردة داخل القيمة الحرفية النصية في SQL؛ وبعد فك ترميز السلسلة النصية في SQL، تكون الشرطات المائلة العكسية حرفية ولا تلغي أحرف البدل في `LIKE`.

عندما لا تتطابق أي وحدة، لا تحتوي النتيجة على أي صف. يحتفظ تنسيق CSV بعناوين الأعمدة، ويحتفظ عملاء بروتوكول الاتصال الخاص بـ PostgreSQL ببيانات تعريف الأعمدة، ويكون إخراج JSON هو `null`.

يجمع الجرد بين بيانات التعريف المضمنة وقت البناء ومعلومات بناء Go الخاصة بالملف التنفيذي. لا تحتاج الاستعلامات إلى الوصول إلى الشبكة أو تثبيت Go أو نسخة محلية من التعليمات البرمجية المصدر أو ذاكرة تخزين مؤقت للوحدات. حتى عمليات البناء التي تفتقر إلى بيانات التعريف المنشأة تبلغ عن وحداتها المرتبطة، وتظهر بيانات النسب المفقودة بالقيمة `UNKNOWN` أو `NULL`.

يغطي الإخراج تبعيات Go الخاصة بالملف الثنائي فقط. ولا يتضمن علاقات التبعية ولا يتيح تصدير مستند SPDX أو CycloneDX، وهو لا يمثل قائمة SBOM كاملة للنشر ولا تحديد الامتثال للترخيص.

* * *
