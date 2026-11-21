---
title: إطار عمل روابط IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
  - cspm
  - google
  - google cloud platform
  - gcp
description: "إطار عمل GitOps لـ Google Cloud IAM باستخدام StackQL: عرّف الأدوار المخصصة وروابط الأدوار للمؤسسة والمجلدات والمشاريع والموارد في Jsonnet، وشغّلها تجريبيًا ثم طبّقها."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# إطار عمل GitOps لروابط IAM في Google باستخدام StackQL

في هذا الدليل، سنوضح إطار عمل GitOps لإدارة روابط IAM (الاستحقاقات) في GCP باستخدام StackQL، وهي أداة تطوير قوية تتيح الاستعلام عن البنية التحتية والموارد السحابية ونشرها باستخدام بناء جملة SQL.

تم الاختبار باستخدام <span class="tutorial_tested_on">واجهة SQL الخلفية المضمنة</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## خلفية عن IAM في Google {#google-iam-background}

يمثل Google Cloud Identity and Access Management (IAM) إطار عمل لإدارة الوصول إلى موارد Google Cloud، إذ يتيح للمسؤولين تحديد من يمكنه تنفيذ إجراءات معينة على الموارد. يقوم على الهويات (مثل المستخدمين وحسابات الخدمة) والأدوار والأذونات، حيث تحكم الأدوار المعينة لهذه الهويات حقوق الوصول الخاصة بها. يتناول هذا الدليل بالتفصيل إدارة روابط IAM، التي تربط هذه الأدوار بالهويات، باستخدام StackQL، وهي أداة تبسط إدارة الموارد السحابية باستخدام بناء جملة SQL.

يمكن إنشاء روابط IAM على أي من المستويات التالية:

- مستوى المؤسسة
- مستوى المجلد
- مستوى المشروع
- مستوى المورد (الحاويات ومجموعات بيانات BigQuery وغيرها)
- مستوى حساب الفوترة

ترث جميع الكائنات الفرعية روابط IAM المطبقة على مستوى المؤسسة أو المجلد.

## تعريفات الروابط التصريحية وGitOps {#declarative-binding-definitions-and-gitops}

سيوضح لك الدليل كيفية إدارة روابط IAM في مستودع التعليمات البرمجية المصدر بحيث يصبح المصدر الموثوق للحالة وتاريخ التغييرات، وهو مفهوم معروف باسم GitOps، وكيفية استخدام StackQL لتطبيق التغييرات باستخدام سير عمل CI/CD.

يوضح لك هذا المثال كيفية ربط *العديد* من الأعضاء أو كيانات الأمان مع *العديد* من الأدوار من ملف بيان `json` مخزن في نظام التحكم بالمصادر.

> تسمح لك وحدة تحكم GCP والأداتان `terraform` و`gcloud` بربط عضو *واحد* فقط بدور واحد أو أكثر في كل عملية

## آلية العمل {#how-it-works}

سينشر إطار العمل الذي سنرشدك خلاله روابط IAM عبر جميع العقد في التسلسل الهرمي لـ GCP، بالإضافة إلى إنشاء أدوار مخصصة (بأذونات منتقاة) - بخلاف الأدوار المحددة مسبقًا مثل `roles/bigquery.dataEditor` على سبيل المثال.

### بنية دليل المشروع {#project-directory-structure}

يستخدم إطار عمل نشر الروابط هذا شجرة الأدلة التالية:

```
├── iql
│   ├── iam.iql
│   ├── ...
├── data
│   ├── iam.jsonnet
│   ├── iam
│   │   ├── bindings.json
│   │   ├── custom_roles.json
```

### الملف `iql/iam.iql` {#iqliamiql-file}

هذا هو الملف الأساسي الذي سيشغله `stackql`، وباستخدام `data/iam.jsonnet` سينشئ من القالب روابط السياسات المحولة برمجيًا الناتجة عن البيانات الموجودة في `data/iam/bindings.json` ويطبقها

> لا يفترض أن تحتاج إلى تعديل هذا الملف

<details>

<summary>وسّع لعرض الملف <b><code>iql/iam.iql</code></b></summary>

```sql
/*
     ROOT LEVEL (ORG AND FOLDER) IAM FOR SERVICE ACCOUNTS AND GROUPS
*/
{{ $root := . }}

/*** create custom roles ***/ {{ range .custom_roles }}

INSERT INTO google.iam.roles (
 organizationsId,
 data__role,
 data__roleId
)
SELECT
 '{{ $root.organization_id }}',
 '{"title": "{{ .title }}", "description": "{{ .description }}", "stage": "{{ .stage }}", "includedPermissions": {{ .includedPermissions }}}',
 '{{ .id }}'
;{{ end }}

/*** create org role bindings ***/

REPLACE google.cloudresourcemanager.organizations_iam_policies
SET data__policy = '{"bindings": {{ $root.bindings.org }}}'
WHERE organizationsId = '{{ $root.organization_id }}';

/*** create billing role bindings ***/

REPLACE google.cloudbilling.billing_accounts_iam_policies
SET data__policy = '{"bindings": {{ $root.bindings.billingacct }}}'
WHERE billingAccountsId = '{{ $root.billing_account_id }}';

/*** create nonprod folder role bindings ***/

REPLACE google.cloudresourcemanager.folders_iam_policies
SET data__policy = '{"bindings": {{ $root.bindings.folders.nonprod }}}'
WHERE foldersId = '{{ $root.nonprod_folder_id }}';

/*** create prod folder role bindings ***/

REPLACE google.cloudresourcemanager.folders_iam_policies
SET data__policy = '{"bindings": {{ $root.bindings.folders.prod }}}'
WHERE foldersId = '{{ $root.prod_folder_id }}';

/*** create datalabs folder role bindings ***/

REPLACE google.cloudresourcemanager.folders_iam_policies
SET data__policy = '{"bindings": {{ $root.bindings.folders.datalabs }}}'
WHERE foldersId = '{{ $root.datalabs_folder_id }}';

/*** create stackql-audit project level role bindings (conditional bindings need policy version 3) ***/

REPLACE google.cloudresourcemanager.projects_iam_policies
SET data__policy = '{"version": 3, "bindings": {{ $root.bindings.projects.stackql_audit }}}'
WHERE projectsId = 'stackql-audit';

/*** create role bindings for buckets in stackql-terraform ***/ {{ range $root.bindings.buckets }}

-- creating policy bindings for {{ .name }}
REPLACE google.storage.buckets_iam_policies
SET data__bindings = '{{ .data }}'
WHERE bucket = '{{ .name }}';
{{ end }}

/*** create role bindings for topics in stackql-audit ***/ {{ range $root.bindings.topics }}

-- creating policy bindings for {{ .name }}
REPLACE google.pubsub.topics_iam_policies
SET data__policy = '{"bindings": {{ .data }}}'
WHERE projectsId = '{{ .project }}'
AND topicsId = '{{ .name }}';
{{ end }}
```

</details>


## `data/iam.jsonnet` {#dataiamjsonnet}

يوفر هذا الملف البيانات المستخدمة في المعالجة المسبقة للملف `iql/iam.iql`، مع الحصول عليها من `data/iam/bindings.json`

> لا يفترض أن تحتاج إلى تعديل هذا الملف

<details>

<summary>وسّع لعرض الملف <b><code>data/iam.jsonnet</code></b></summary>

```javascript
// update the following files only:
//   data/iam/custom_roles.json
//   data/iam/bindings.json

// variables
local custom_roles_data = import './data/iam/custom_roles.json';
local bindings_data = import './data/iam/bindings.json';
local organization_id = '123466304837';
local billing_account_id = '123456-9DFD97-EE2B33';
local nonprod_folder_id = '1234016945998';
local prod_folder_id = '123431606453';
local datalabs_folder_id = '123464988355';
local environments = [{name: 'prod'}, {name: 'nonprod'}, {name: 'datalabs'}];

// DO NOT MODIFY BEYOND THIS POINT

local generate_binding(x) =
	local members = [x for x in x.members];
	std.map(function(r) {"role": r, "members": members} , [x for x in x.roles]);
	
local generate_conditional_binding(x) =
	local members = [x for x in x.members];
	local condition = x.condition;
	std.map(function(r) {"role": r, "members": members, "condition": condition} , [x for x in x.roles]);
	
{
 organization_id: organization_id,
 billing_account_id: billing_account_id,
 nonprod_folder_id: nonprod_folder_id,
 prod_folder_id: prod_folder_id,
 datalabs_folder_id: datalabs_folder_id,
 custom_roles: custom_roles_data,
 environments: environments,
 bindings: {
	org: std.flattenArrays(std.map(generate_binding, bindings_data.org)),
	billingacct: std.flattenArrays(std.map(generate_binding, bindings_data.billingacct)),
	folders: {
		nonprod: std.flattenArrays(std.map(generate_binding, bindings_data.folders.nonprod)),
		prod: std.flattenArrays(std.map(generate_binding, bindings_data.folders.prod)),
		datalabs: std.flattenArrays(std.map(generate_binding, bindings_data.folders.datalabs)),
	},
	projects: {
		stackql_audit: std.flattenArrays(std.map(generate_conditional_binding, bindings_data.projects.stackql_audit)),
	},
	buckets: [
		{
			name: "stackql-tf-prod",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.buckets.stackql_tf_prod)),
		},
		{
			name: "stackql-tf-modules-prod",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.buckets.stackql_tf_modules_prod)),
		},
		{
			name: "stackql-tf-nonprod",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.buckets.stackql_tf_nonprod)),
		},
		{
			name: "stackql-tf-modules-nonprod",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.buckets.stackql_tf_modules_nonprod)),
		},
		{
			name: "stackql-tf-datalabs",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.buckets.stackql_tf_datalabs)),
		},
		{
			name: "stackql-tf-modules-datalabs",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.buckets.stackql_tf_modules_datalabs)),
		},
	],
	topics: [
		{
			name: "stackql-np-log-topic",
			project: "stackql-audit",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.topics.stackql_np_log_topic)),
		},
		{
			name: "stackql-prod-log-topic",
			project: "stackql-audit",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.topics.stackql_prod_log_topic)),
		},		
	],
 },
} 
```

</details>

## `data/iam/bindings.json` {#dataiambindingsjson}

> هذا الملف __سيحتاج إلى تعديل__ لإجراء تغييرات على روابط السياسات

هذا هو المصدر الأساسي لبيانات السياسات والروابط، إذ تربط بنية البيانات كيان أمان أو أكثر (المجموعات وحسابات الخدمة وغيرها) بدور أو أكثر (محدد مسبقًا أو مخصص) على مستوى معين (الأصل) كما هو موضح أدناه:

```json
  "<< level >>": [
    {
      "members": [
        "<< principal >>",
        ...
      ],
      "roles": [
        "<< role >>",
        ...
      ]
    },
```

على سبيل المثال:

```json
  "folders": {
    "nonprod": [
      {
        "members": [
          "serviceAccount:terraform-nonprod@stackql-terraform.iam.gserviceaccount.com"
        ],
        "roles": [
          "roles/compute.networkAdmin",
          "roles/resourcemanager.folderViewer",
          "roles/resourcemanager.projectCreator",
          "roles/storage.admin",
          "roles/artifactregistry.admin",
          "roles/container.serviceAgent",
          "roles/iam.securityAdmin",
          "roles/bigquery.admin",
          "roles/serviceusage.serviceUsageAdmin",
          "roles/cloudsql.admin",
          "roles/iam.serviceAccountAdmin",
          "roles/pubsub.admin",
          "roles/cloudfunctions.developer"
        ]
      }
    ],
```

## النشر {#deployment}

لا يتم تشغيل هذه العملية عبر خط أنابيب CI/CD عن قصد، لأن التغييرات نادرة، ولأننا لا نرغب في منح حساب خدمة مستويات الامتيازات المطلوبة لنشر ذلك. يجب أن يشغل هذه العملية مسؤول يتمتع بامتيازات وعضو في مجموعة Google المسماة gcp-org-administrators.

يجب أن يكون المستخدم قد أجرى المصادقة على GCP باستخدام حساب خدمة أو المصادقة التفاعلية، ولمزيد من المعلومات حول مصادقة StackQL لدى Google، راجع [__مصادقة GCP__](https://google-provider.stackql.io/#authentication).

شغّل الأمر التالي لإجراء تشغيل تجريبي:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

يمكن تنفيذ البيانات الناتجة في هذا الملف كجمل منفردة في `stackql shell` أو كدفعة واحدة بتشغيل:

```shell
stackql exec -i iam-TEMPLATED.iql
```
بدلًا من ذلك، يمكنك أخذ السياسات (حمولات JSON) التي ينشئها هذا البرنامج النصي وتشغيلها باستخدام الأمر `gcloud iam`.
