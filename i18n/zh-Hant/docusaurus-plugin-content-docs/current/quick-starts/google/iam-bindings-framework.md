---
title: IAM 繫結架構
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
  - cspm
  - google
  - google cloud platform
  - gcp
description: "使用 StackQL 的 Google Cloud IAM GitOps 架構：以 Jsonnet 宣告組織、資料夾、專案與資源的自訂角色和角色繫結，先試執行再套用。"
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# 使用 StackQL 的 Google GitOps IAM 繫結架構

在本指南中，我們將示範如何使用 StackQL，以 GitOps 架構管理 GCP 中的 IAM 繫結（權利）。StackQL 是一款強大的開發工具，可使用 SQL 語法查詢及部署雲端基礎架構與資源。

測試環境：<span class="tutorial_tested_on">內嵌式 SQL 後端</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Google IAM 背景 {#google-iam-background}

Google Cloud Identity and Access Management（IAM）是用於管理 Google Cloud 資源存取權的架構，可讓管理員定義誰能對資源執行特定動作。它以身分（例如使用者與服務帳戶）、角色和權限為核心運作，指派給這些身分的角色決定了其存取權限。本指南將深入探討如何使用 StackQL（一款以 SQL 語法簡化雲端資源管理的工具）管理 IAM 繫結，IAM 繫結負責將這些角色與身分建立關聯。

IAM 繫結可以在下列任一層級建立：

- 組織層級
- 資料夾層級
- 專案層級
- 資源層級（儲存貯體、BigQuery 資料集等）
- 帳單帳戶層級

在組織或資料夾層級套用的 IAM 繫結會由所有子物件繼承。

## 宣告式繫結定義與 GitOps {#declarative-binding-definitions-and-gitops}

本指南將說明如何在原始碼存放庫中維護 IAM 繫結的主要版本，使存放庫成為狀態與歷程記錄的事實來源（此概念稱為 GitOps），以及如何使用 StackQL 透過 CI/CD 工作流程套用變更。

此範例說明如何從儲存在原始檔控制中的 `json` 資訊清單，將*多個*成員或主體對應至*多個*角色。

> GCP 主控台、`terraform` 和 `gcloud` 公用程式在每次作業中只允許您將*一個*成員對應至一或多個角色

## 運作方式 {#how-it-works}

我們將逐步介紹的架構會在您 GCP 階層中的所有節點部署 IAM 繫結，並建立自訂角色（具有精選的權限），而非使用預先定義的角色（例如 `roles/bigquery.dataEditor`）。

### 專案目錄結構 {#project-directory-structure}

此繫結部署架構使用下列目錄樹狀結構：

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

### `iql/iam.iql` 檔案 {#iqliamiql-file}

這是由 `stackql` 執行的主要檔案，它會使用 `data/iam.jsonnet` 呈現並套用由 `data/iam/bindings.json` 中的資料所產生、經過轉換的政策繫結

> 此檔案應該不需要修改

<details>

<summary>展開以查看 <b><code>iql/iam.iql</code></b> 檔案</summary>

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

此檔案提供前置處理 `iql/iam.iql` 時所用的資料，資料來源為 `data/iam/bindings.json`

> 此檔案應該不需要修改

<details>

<summary>展開以查看 <b><code>data/iam.jsonnet</code></b> 檔案</summary>

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

> 若要變更政策繫結，**需要修改**此檔案

這是政策與繫結資料的主要來源，其資料結構會在特定層級（父項）將一或多個主體（群組、服務帳戶等）對應至一或多個角色（預先定義或自訂），如下所示：

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

例如：

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

## 部署 {#deployment}

此流程刻意不透過 CI/CD 管線執行，因為變更並不頻繁，而且我們不希望授予服務帳戶部署此流程所需的權限等級。此流程應由 gcp-org-administrators Google 群組中具有特權的管理員成員執行。

使用者需要使用服務帳戶或互動式驗證向 GCP 進行驗證；如需讓 StackQL 向 Google 進行驗證的詳細資訊，請參閱 [**GCP 驗證**](https://google-provider.stackql.io/#authentication)。

執行下列命令以進行試執行：
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

此檔案中產生的資料可以在 `stackql shell` 中以個別陳述式執行，或執行下列命令以批次方式執行：

```shell
stackql exec -i iam-TEMPLATED.iql
```
或者，您也可以取得此指令碼產生的政策（JSON 承載），並使用 `gcloud iam` 命令執行。

