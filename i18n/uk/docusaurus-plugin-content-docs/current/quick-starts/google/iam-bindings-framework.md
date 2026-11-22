---
title: Фреймворк прив'язок IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
  - cspm
  - google
  - google cloud platform
  - gcp
description: "Фреймворк GitOps для Google Cloud IAM на основі StackQL: опишіть у Jsonnet власні ролі та прив'язки ролей для організації, папок, проєктів і ресурсів, виконайте пробний запуск і застосуйте їх."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# Фреймворк GitOps для прив'язок IAM у Google на основі StackQL

У цьому посібнику ми покажемо фреймворк GitOps для керування прив'язками IAM (правами доступу) у GCP за допомогою StackQL - потужного інструмента розробника, що дає змогу виконувати запити до хмарної інфраструктури та ресурсів і розгортати їх за допомогою синтаксису SQL.  

Перевірено з <span class="tutorial_tested_on">вбудований бекенд SQL</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Загальні відомості про Google IAM {#google-iam-background}

Google Cloud Identity and Access Management (IAM) - це фреймворк для керування доступом до ресурсів Google Cloud, що дає змогу адміністраторам визначати, хто може виконувати певні дії з ресурсами. Він побудований навколо ідентичностей (як-от користувачі та облікові записи служб), ролей і дозволів, причому ролі, призначені цим ідентичностям, визначають їхні права доступу. У цьому посібнику детально розглянуто керування прив'язками IAM, які пов'язують ці ролі з ідентичностями, за допомогою StackQL - інструмента, що спрощує керування хмарними ресурсами за допомогою синтаксису SQL.  

Прив'язки IAM можна створювати на будь-якому з таких рівнів:

- рівень організації
- рівень папки
- рівень проєкту
- рівень ресурсу (бакети, набори даних BigQuery тощо)
- рівень платіжного облікового запису

Усі дочірні об'єкти успадковують прив'язки IAM, застосовані на рівні організації або папки.  

## Декларативні визначення прив'язок і GitOps {#declarative-binding-definitions-and-gitops}

Цей посібник покаже, як зберігати еталонні визначення прив'язок IAM у репозиторії вихідного коду, зробивши його джерелом істини для стану й історії (цю концепцію називають GitOps), і як за допомогою StackQL застосовувати зміни в робочих процесах CI/CD.   

Цей приклад показує, як зіставити *багатьох* учасників або principal з *багатьма* ролями за допомогою маніфесту `json`, що зберігається в системі керування версіями.  

> Консоль GCP, утиліти `terraform` і `gcloud` дають змогу зіставляти лише *одного* учасника з однією або кількома ролями в кожній операції  

## Як це працює {#how-it-works}

Фреймворк, який ми розглянемо, розгортає прив'язки IAM на всіх вузлах вашої ієрархії GCP, а також створює власні ролі (з ретельно дібраними дозволами) - на відміну від попередньо визначених ролей, як-от, наприклад, `roles/bigquery.dataEditor`.

### Структура каталогів проєкту {#project-directory-structure}

У цьому фреймворку розгортання прив'язок використано таке дерево каталогів:

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

### Файл `iql/iam.iql` {#iqliamiql-file}

Це основний файл, який запускатиме `stackql`; за допомогою `data/iam.jsonnet` він сформує та застосує транспільовані прив'язки політик, згенеровані з даних у `data/iam/custom_roles.json` і `data/iam/bindings.json`

> цей файл не потрібно змінювати

<details>

<summary>Розгорніть, щоб переглянути файл <b><code>iql/iam.iql</code></b></summary>

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

Цей файл надає дані для попередньої обробки `iql/iam.iql`, беручи їх із `data/iam/custom_roles.json` і `data/iam/bindings.json`

> цей файл не потрібно змінювати

<details>

<summary>Розгорніть, щоб переглянути файл <b><code>data/iam.jsonnet</code></b></summary>

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

> цей файл __потрібно буде змінити__, щоб внести зміни до прив'язок політик

Це головне джерело даних про політики й прив'язки; структура даних зіставляє одного або кількох principal (групи, облікові записи служб тощо) з однією або кількома ролями (попередньо визначеними або власними) на певному рівні (батьківському), як показано нижче:

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

наприклад:

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

## Розгортання {#deployment}

Цей процес навмисно не запускають через конвеєр CI/CD, оскільки зміни нечасті, і ми не хочемо надавати обліковому запису служби рівень привілеїв, потрібний для такого розгортання.  Його має запускати привілейований адміністратор, який є членом групи Google gcp-org-administrators.  

Користувач має пройти автентифікацію в GCP за допомогою облікового запису служби або інтерактивної автентифікації; докладніше про автентифікацію StackQL у Google див. [__Автентифікація в GCP__](https://google-provider.stackql.io/#authentication).  

Виконайте таку команду, щоб здійснити пробний запуск:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Отримані в цьому файлі дані можна виконати як окремі інструкції в `stackql shell` або як пакет, виконавши команду:

```shell
stackql exec -i iam-TEMPLATED.iql
```
Або ж ви можете взяти політики (корисні навантаження JSON), згенеровані цим скриптом, і виконати їх за допомогою команди `gcloud iam`.
