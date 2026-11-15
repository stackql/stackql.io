---
title: IAM 바인딩 프레임워크
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
  - cspm
  - google
  - google cloud platform
  - gcp
description: SQL을 사용한 Google Cloud 인프라 및 리소스의 쿼리와 배포
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# StackQL을 사용한 Google GitOps IAM 바인딩 프레임워크

이 가이드에서는 StackQL을 사용하여 GCP에서 IAM 바인딩(사용 권한)을 관리하는 GitOps 프레임워크를 보여 줍니다. StackQL은 SQL 구문으로 클라우드 인프라와 리소스를 쿼리하고 배포할 수 있는 강력한 개발 도구입니다.  

테스트 환경 <span class="tutorial_tested_on">임베디드 SQL 백엔드</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Google IAM 배경 {#google-iam-background}

Google Cloud Identity and Access Management(IAM)은 Google Cloud 리소스에 대한 액세스를 관리하는 프레임워크로, 관리자가 리소스에 대해 누가 특정 작업을 수행할 수 있는지 정의할 수 있게 해 줍니다. IAM은 ID(사용자, 서비스 계정 등), 역할, 권한을 중심으로 작동하며, 이러한 ID에 할당된 역할이 해당 ID의 액세스 권한을 결정합니다. 이 가이드에서는 SQL 구문으로 클라우드 리소스 관리를 간소화하는 도구인 StackQL을 사용하여, 역할을 ID에 연결하는 IAM 바인딩을 관리하는 방법을 자세히 살펴봅니다.  

IAM 바인딩은 다음 수준 중 어디에서나 생성할 수 있습니다:

- 조직 수준
- 폴더 수준
- 프로젝트 수준
- 리소스 수준(버킷, BigQuery 데이터 세트 등)
- 결제 계정 수준

조직 또는 폴더 수준에서 적용된 IAM 바인딩은 모든 하위 객체에 상속됩니다.  

## 선언적 바인딩 정의와 GitOps {#declarative-binding-definitions-and-gitops}

이 가이드에서는 IAM 바인딩을 소스 코드 리포지토리에서 마스터로 관리하여 리포지토리를 상태와 이력의 단일 정보 출처로 만드는 방법(GitOps라고 하는 개념)과, StackQL을 사용하여 CI/CD 워크플로로 변경 사항을 적용하는 방법을 보여 줍니다.   

이 예제에서는 소스 제어에 저장된 `json` 매니페스트를 사용하여 *여러* 구성원 또는 보안 주체를 *여러* 역할에 매핑하는 방법을 보여 줍니다.  

> GCP 콘솔, `terraform`, `gcloud` 유틸리티에서는 각 작업에서 *하나의* 구성원만 하나 이상의 역할에 매핑할 수 있습니다  

## 작동 방식 {#how-it-works}

이 가이드에서 살펴볼 프레임워크는 GCP 계층 구조의 모든 노드에 IAM 바인딩을 배포하고, (엄선된 권한으로) 커스텀 역할도 생성합니다. 이는 예를 들어 `roles/bigquery.dataEditor` 같은 사전 정의된 역할과는 다릅니다.

### 프로젝트 디렉터리 구조 {#project-directory-structure}

이 바인딩 배포 프레임워크에서는 다음 디렉터리 트리를 사용합니다:

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

### `iql/iam.iql` 파일 {#iqliamiql-file}

`stackql` 명령으로 실행하는 기본 파일입니다. 이 파일은 `data/iam.jsonnet` 파일을 사용하여 `data/iam/bindings.json` 파일의 데이터로 생성된 트랜스파일된 정책 바인딩을 렌더링하고 적용합니다.

> 이 파일은 수정할 필요가 없습니다

<details>

<summary>펼쳐서 <b><code>iql/iam.iql</code></b> 파일 보기</summary>

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

이 파일은 `data/iam/bindings.json` 파일에서 데이터를 가져와 `iql/iam.iql` 파일에서 전처리됩니다.

> 이 파일은 수정할 필요가 없습니다

<details>

<summary>펼쳐서 <b><code>data/iam.jsonnet</code></b> 파일 보기</summary>

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

> 정책 바인딩을 변경하려면 이 파일을 __수정해야 합니다__

이 파일은 정책 및 바인딩 데이터의 마스터 소스입니다. 데이터 구조는 아래와 같이 특정 수준(상위)에서 하나 이상의 보안 주체(그룹, 서비스 계정 등)를 하나 이상의 역할(사전 정의된 역할 또는 커스텀 역할)에 매핑합니다:

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

예를 들어 다음과 같습니다:

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

## 배포 {#deployment}

변경이 자주 발생하지 않고, 이를 배포하는 데 필요한 수준의 권한을 서비스 계정에 부여하지 않기 위해 이 프로세스는 의도적으로 CI/CD 파이프라인을 통해 실행하지 않습니다. 이 프로세스는 gcp-org-administrators Google 그룹의 구성원인 권한 있는 관리자가 실행해야 합니다.  

사용자는 서비스 계정 또는 대화형 인증을 사용하여 GCP에 인증되어 있어야 합니다. StackQL을 Google에 인증하는 방법에 대한 자세한 내용은 [__GCP 인증__](https://google.stackql.io/providers/google/#authentication)을 참조하세요.  

다음 명령을 실행하여 드라이 런을 수행합니다:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

이 파일의 결과 데이터는 `stackql shell` 명령에서 개별 문으로 실행하거나, 다음 명령을 실행하여 배치로 실행할 수 있습니다:

```shell
stackql exec -i iam-TEMPLATED.iql \
```
또는 이 스크립트로 생성된 정책(JSON 페이로드)을 가져와 `gcloud iam` 명령으로 실행할 수도 있습니다.
