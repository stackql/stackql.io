---
title: IAM バインディングフレームワーク
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
  - cspm
  - google
  - google cloud platform
  - gcp
description: "StackQL を使用した Google Cloud IAM の GitOps フレームワーク：組織、フォルダー、プロジェクト、リソースのカスタムロールとロールバインディングを Jsonnet で宣言し、ドライランを実行してから適用します。"
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# StackQL を使用した Google の GitOps IAM バインディングフレームワーク

このガイドでは、StackQL を使用して GCP の IAM バインディング（権限付与）を管理する GitOps フレームワークを紹介します。StackQL は、SQL 構文でクラウドインフラストラクチャとリソースのクエリとデプロイを行える強力な開発ツールです。  

動作確認済み <span class="tutorial_tested_on">組み込み SQL バックエンド</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Google IAM の背景 {#google-iam-background}

Google Cloud Identity and Access Management（IAM）は、Google Cloud リソースへのアクセスを管理するためのフレームワークです。管理者は、誰がリソースに対して特定の操作を実行できるかを定義できます。IAM は ID（ユーザーやサービスアカウントなど）、ロール、権限を中心に機能し、ID に割り当てられたロールがそのアクセス権を決定します。このガイドでは、ロールと ID を結び付ける IAM バインディングを StackQL で管理する方法を詳しく説明します。StackQL は、SQL 構文でクラウドリソースの管理を簡素化するツールです。  

IAM バインディングは、次のいずれのレベルでも作成できます：

- 組織レベル
- フォルダーレベル
- プロジェクトレベル
- リソースレベル（バケット、BigQuery データセットなど）
- 請求先アカウントレベル

組織レベルまたはフォルダーレベルで適用された IAM バインディングは、すべての子オブジェクトに継承されます。  

## 宣言的なバインディング定義と GitOps {#declarative-binding-definitions-and-gitops}

このガイドでは、IAM バインディングをソースコードリポジトリで一元管理し、リポジトリを状態と履歴の信頼できる唯一の情報源にする方法（GitOps と呼ばれる考え方）と、StackQL を使用して CI/CD ワークフローで変更を適用する方法を説明します。   

この例では、ソース管理に保存された `json` マニフェストから、*複数*のメンバーまたはプリンシパルを*複数*のロールにマッピングする方法を示します。  

> GCP コンソール、`terraform`、`gcloud` ユーティリティでは、1 回の操作で *1 つ*のメンバーを 1 つ以上のロールにマッピングすることしかできません  

## 仕組み {#how-it-works}

このガイドで説明するフレームワークは、GCP の階層内のすべてのノードに IAM バインディングをデプロイするとともに、カスタムロール（厳選した権限を持つもの）を作成します。これは、たとえば `roles/bigquery.dataEditor` のような事前定義ロールとは異なります。

### プロジェクトのディレクトリ構造 {#project-directory-structure}

このバインディングデプロイフレームワークでは、次のディレクトリツリーを使用します：

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

### `iql/iam.iql` ファイル {#iqliamiql-file}

これは `stackql` によって実行されるメインファイルです。`data/iam.jsonnet` を使用して、`data/iam/bindings.json` のデータから生成されたトランスパイル済みのポリシーバインディングをレンダリングし、適用します。

> このファイルを変更する必要はありません

<details>

<summary>展開して <b><code>iql/iam.iql</code></b> ファイルを表示</summary>

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

このファイルは、`iql/iam.iql` の前処理に使用するデータを、`data/iam/bindings.json` から取得して提供します。

> このファイルを変更する必要はありません

<details>

<summary>展開して <b><code>data/iam.jsonnet</code></b> ファイルを表示</summary>

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

> ポリシーバインディングを変更するには、このファイルを __変更する必要があります__

これはポリシーとバインディングのデータのマスターソースです。次に示すように、このデータ構造は、特定のレベル（親）で 1 つ以上のプリンシパル（グループ、サービスアカウントなど）を 1 つ以上のロール（事前定義またはカスタム）にマッピングします：

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

たとえば：

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

## デプロイ {#deployment}

変更の頻度が低く、またデプロイに必要なレベルの権限をサービスアカウントに付与したくないため、このプロセスは意図的に CI/CD パイプラインでは実行しません。このプロセスは、gcp-org-administrators Google グループのメンバーである特権管理者が実行します。  

ユーザーは、サービスアカウントまたは対話型の認証のいずれかを使用して、GCP に認証されている必要があります。StackQL から Google への認証の詳細については、[__GCP の認証__](https://google-provider.stackql.io/#authentication) を参照してください。  

ドライランを実行するには、次のコマンドを実行します：
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

このファイルに出力されたデータは、`stackql shell` で個々の文として実行するか、次のコマンドでバッチとして実行できます：

```shell
stackql exec -i iam-TEMPLATED.iql
```
または、このスクリプトで生成されたポリシー（JSON ペイロード）を取り出し、`gcloud iam` コマンドで実行することもできます。
