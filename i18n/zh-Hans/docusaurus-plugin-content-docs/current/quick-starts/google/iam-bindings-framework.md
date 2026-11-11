---
title: IAM 绑定框架
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
  - cspm
  - google
  - google cloud platform
  - gcp
description: 使用 SQL 查询和部署 Google Cloud 基础设施与资源
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# 使用 StackQL 的 Google GitOps IAM 绑定框架

本指南演示一个使用 StackQL 在 GCP 中管理 IAM 绑定（权限分配）的 GitOps 框架。StackQL 是一款功能强大的开发工具，可使用 SQL 语法查询和部署云基础设施与资源。  

测试环境：<span class="tutorial_tested_on">嵌入式 SQL 后端</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Google IAM 背景 {#google-iam-background}

Google Cloud Identity and Access Management（IAM）是用于管理 Google Cloud 资源访问权限的框架，管理员可以通过它定义谁能对资源执行特定操作。它围绕身份（例如用户和服务账号）、角色和权限运作，分配给这些身份的角色决定其访问权限。本指南深入介绍如何使用 StackQL 管理 IAM 绑定，IAM 绑定将这些角色关联到身份，而 StackQL 是一款用 SQL 语法简化云资源管理的工具。  

IAM 绑定可以在以下任一层级创建：

- 组织层级
- 文件夹层级
- 项目层级
- 资源层级（存储桶、BigQuery 数据集等）
- 结算账号层级

在组织或文件夹层级应用的 IAM 绑定会被所有子对象继承。  

## 声明式绑定定义与 GitOps {#declarative-binding-definitions-and-gitops}

本指南将介绍如何在源代码仓库中维护 IAM 绑定的主副本，使仓库成为状态和历史的事实来源（这一理念称为 GitOps），以及如何使用 StackQL 通过 CI/CD 工作流应用更改。   

此示例展示如何根据存储在源代码管理中的 `json` 清单，将*多个*成员或主体映射到*多个*角色。  

> GCP 控制台、`terraform` 和 `gcloud` 工具在每次操作中只允许将*一个*成员映射到一个或多个角色  

## 工作原理 {#how-it-works}

下面介绍的框架会在 GCP 层次结构的所有节点上部署 IAM 绑定，并创建自定义角色（包含精心挑选的权限），而不是使用 `roles/bigquery.dataEditor` 这类预定义角色。

### 项目目录结构 {#project-directory-structure}

此绑定部署框架使用以下目录树：

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

### `iql/iam.iql` 文件 {#iqliamiql-file}

这是由 `stackql` 运行的主文件，它使用 `data/iam.jsonnet` 渲染并应用转译后的策略绑定，这些绑定由 `data/iam/bindings.json` 中的数据生成

> 此文件应无需修改

<details>

<summary>展开查看 <b><code>iql/iam.iql</code></b> 文件</summary>

```sql
/*
     ROOT LEVEL (ORG AND FOLDER) IAM FOR SERVICE ACCOUNTS AND GROUPS
*/
{{ $root := . }}

/*** create custom roles ***/ {{ range .custom_roles }}

INSERT INTO google.iam.`organizations.roles`(
 parent,
 data__role,
 data__roleId )
SELECT   
 '{{ $root.organization_id }}',
 '{"title": "{{ .title }}", "description": "{{ .description }}", "stage": "{{ .stage }}", "includedPermissions": {{ .includedPermissions }}}', 
 '{{ .id }}' 
;{{ end }}

/*** create org role bindings */  

EXEC google.cloudresourcemanager.organizations.setIamPolicy 
@resource = '{{ $root.organization_id }}' 
@@json = '{
  "policy": {
    "bindings": {{ $root.bindings.org }}
   }
}';

/*** create billing role bindings */

EXEC google.cloudbilling.billingAccounts.setIamPolicy 
@resource = '{{ $root.billing_account_id }}' 
@@json = '{
  "policy": {
    "bindings": {{ $root.bindings.billingacct }}
   }
}';

/*** create nonprod folder role bindings */  

EXEC google.cloudresourcemanager.folders.setIamPolicy 
@resource = '{{ $root.nonprod_folder_id }}' 
@@json = '{
  "policy": {
    "bindings": {{ $root.bindings.folders.nonprod }}
   }
}';

/*** create prod folder role bindings */  

EXEC google.cloudresourcemanager.folders.setIamPolicy 
@resource = '{{ $root.prod_folder_id }}' 
@@json = '{
  "policy": {
    "bindings": {{ $root.bindings.folders.prod }}
   }
}';

/*** create datalabs folder role bindings */  

EXEC google.cloudresourcemanager.folders.setIamPolicy 
@resource = '{{ $root.datalabs_folder_id }}' 
@@json = '{
  "policy": {
    "bindings": {{ $root.bindings.folders.datalabs }}
   }
}';

/*** create stackql-audit project level role bindings */  

EXEC google.cloudresourcemanager.folders.setIamPolicy 
@resource = 'projects/stackql-audit' 
@@json = '{
  "policy": {
    "bindings": {{ $root.bindings.projects.stackql_audit }}
   }
}';

/*** create role bindings for buckets in stackql-terraform */ {{ range $root.bindings.buckets }}

-- creating policy bindings for {{ .name }}
EXEC google.storage.buckets.setIamPolicy 
@bucket = '{{ .name }}'
@@json = '{
	"bindings": {{ .data }}
}';
{{ end }}

/*** create role bindings for topics in stackql-audit */ {{ range $root.bindings.topics }}

-- creating policy bindings for {{ .name }}
EXEC google.pubsub.`projects.topics`.setIamPolicy
@resource = '{{ .resource }}'
@@json = '{
	"bindings": {{ .data }}
}';
{{ end }}
```

</details>


## `data/iam.jsonnet` {#dataiamjsonnet}

此文件由 `iql/iam.iql` 预处理，数据来自 `data/iam/bindings.json`

> 此文件应无需修改

<details>

<summary>展开查看 <b><code>data/iam.jsonnet</code></b> 文件</summary>

```javascript
// update the following files only:
//   data/iam/custom_roles.json
//   data/iam/bindings.json

// variables
local custom_roles_data = import './data/iam/custom_roles.json';
local bindings_data = import './data/iam/bindings.json';
local organization_id = 'organizations/123466304837';
local billing_account_id = 'billingAccounts/123456-9DFD97-EE2B33';
local nonprod_folder_id = 'folders/1234016945998';
local prod_folder_id = 'folders/123431606453';
local datalabs_folder_id = 'folders/123464988355';
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
			resource: "projects/stackql-audit/topics/stackql-np-log-topic",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.topics.stackql_np_log_topic)),
		},
		{
			name: "stackql-prod-log-topic",
			resource: "projects/stackql-audit/topics/stackql-prod-log-topic",
			data: std.flattenArrays(std.map(generate_binding, bindings_data.topics.stackql_prod_log_topic)),
		},		
	],
 },
} 
```

</details>

## `data/iam/bindings.json` {#dataiambindingsjson}

> 要更改策略绑定，__需要修改此文件__

这是策略和绑定数据的主数据源，其数据结构将一个或多个主体（群组、服务账号等）映射到某一特定层级（父级）上的一个或多个角色（预定义或自定义），如下所示：

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

此流程有意不通过 CI/CD 流水线运行，因为更改并不频繁，而且我们不希望向服务账号授予部署所需的特权级别。它应由具有特权的管理员运行，该管理员须是 Google 群组 gcp-org-administrators 的成员。  

用户需要通过服务账号或交互式身份验证完成 GCP 身份验证，有关 StackQL 向 Google 进行身份验证的更多信息，请参阅 [__GCP 身份验证__](https://google.stackql.io/providers/google/#authentication)。  

运行以下命令执行试运行：
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

此文件中生成的数据可以在 `stackql shell` 中作为单独的语句逐条执行，也可以运行以下命令批量执行：

```shell
stackql exec -i iam-TEMPLATED.iql \
```
或者，也可以取出此脚本生成的策略（JSON 负载），使用 `gcloud iam` 命令运行。
