---
title: Фреймворк привязок IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
  - cspm
  - google
  - google cloud platform
  - gcp
description: "Фреймворк GitOps для Google Cloud IAM на базе StackQL: пользовательские роли и привязки ролей для организации, папок, проектов и ресурсов в Jsonnet, пробный запуск и применение."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# Фреймворк GitOps для привязок IAM в Google с использованием StackQL

В этом руководстве мы покажем фреймворк GitOps для управления привязками IAM (правами доступа) в GCP с помощью StackQL - мощного инструмента разработчика, который позволяет выполнять запросы к облачной инфраструктуре и ресурсам и развертывать их с помощью синтаксиса SQL.  

Проверено с <span class="tutorial_tested_on">встроенный SQL-бэкенд</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Общие сведения о Google IAM {#google-iam-background}

Google Cloud Identity and Access Management (IAM) - это фреймворк для управления доступом к ресурсам Google Cloud, который позволяет администраторам определять, кто может выполнять определенные действия с ресурсами. Он строится вокруг удостоверений (например, пользователей и сервисных аккаунтов), ролей и разрешений: роли, назначенные этим удостоверениям, определяют их права доступа. В этом руководстве подробно рассматривается управление привязками IAM, которые связывают эти роли с удостоверениями, с помощью StackQL - инструмента, который упрощает управление облачными ресурсами за счет синтаксиса SQL.  

Привязки IAM можно создавать на любом из следующих уровней:

- уровень организации
- уровень папки
- уровень проекта
- уровень ресурса (бакеты, наборы данных BigQuery и т. д.)
- уровень платежного аккаунта

Привязки IAM, примененные на уровне организации или папки, наследуются всеми дочерними объектами.  

## Декларативные определения привязок и GitOps {#declarative-binding-definitions-and-gitops}

Это руководство покажет, как хранить основную копию привязок IAM в репозитории исходного кода, сделав его источником истины для состояния и истории (этот подход называется GitOps), и как применять изменения с помощью StackQL в рабочих процессах CI/CD.   

Этот пример показывает, как сопоставить *множество* участников или субъектов с *множеством* ролей с помощью манифеста `json`, хранящегося в системе управления версиями.  

> Консоль GCP и утилиты `terraform` и `gcloud` позволяют сопоставить только *одного* участника с одной или несколькими ролями за одну операцию  

## Как это работает {#how-it-works}

Фреймворк, который мы рассмотрим, развертывает привязки IAM во всех узлах вашей иерархии GCP, а также создает пользовательские роли (с тщательно подобранными разрешениями) - в отличие от предопределенных ролей, таких как, например, `roles/bigquery.dataEditor`.

### Структура каталогов проекта {#project-directory-structure}

В этом фреймворке развертывания привязок используется следующее дерево каталогов:

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

Это основной файл, который запускает `stackql`; с помощью `data/iam.jsonnet` он формирует и применяет транспилированные привязки политик, созданные из данных в `data/iam/bindings.json`

> этот файл не требуется изменять

<details>

<summary>Разверните, чтобы увидеть файл <b><code>iql/iam.iql</code></b></summary>

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

Этот файл предоставляет данные для предварительной обработки файла `iql/iam.iql`, загружая их из `data/iam/bindings.json`

> этот файл не требуется изменять

<details>

<summary>Разверните, чтобы увидеть файл <b><code>data/iam.jsonnet</code></b></summary>

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

> этот файл __потребуется изменить__, чтобы внести изменения в привязки политик

Это основной источник данных о политиках и привязках; структура данных сопоставляет одного или нескольких субъектов (группы, сервисные аккаунты и т. д.) с одной или несколькими ролями (предопределенными или пользовательскими) на определенном уровне (родительском), как показано ниже:

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

например:

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

## Развертывание {#deployment}

Этот процесс намеренно не запускается через конвейер CI/CD, поскольку изменения вносятся нечасто, а мы не хотим предоставлять сервисному аккаунту уровень привилегий, необходимый для такого развертывания.  Его должен запускать привилегированный администратор, входящий в группу Google gcp-org-administrators.  

Пользователь должен пройти аутентификацию в GCP с помощью сервисного аккаунта или интерактивной аутентификации; подробнее об аутентификации StackQL в Google см. [__Аутентификация в GCP__](https://google-provider.stackql.io/#authentication).  

Выполните следующую команду для пробного запуска:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Полученное содержимое этого файла можно выполнить отдельными инструкциями в `stackql shell` или пакетом с помощью команды:

```shell
stackql exec -i iam-TEMPLATED.iql
```
Также можно взять политики (полезные нагрузки JSON), сформированные этим скриптом, и выполнить их с помощью команды `gcloud iam`.
