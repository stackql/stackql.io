---
title: Framework liên kết IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
  - cspm
  - google
  - google cloud platform
  - gcp
description: "Framework GitOps cho Google Cloud IAM bằng StackQL: khai báo vai trò tùy chỉnh và liên kết vai trò cho tổ chức, thư mục, dự án và tài nguyên trong Jsonnet, chạy thử rồi áp dụng."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# Framework GitOps liên kết IAM cho Google bằng StackQL

Trong hướng dẫn này, chúng tôi sẽ minh họa một framework GitOps để quản lý liên kết IAM (quyền sử dụng) trong GCP bằng StackQL, một công cụ phát triển mạnh mẽ cho phép truy vấn và triển khai hạ tầng và tài nguyên đám mây bằng cú pháp SQL.

Đã kiểm thử với <span class="tutorial_tested_on">backend SQL nhúng</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Kiến thức nền về Google IAM {#google-iam-background}

Google Cloud Identity and Access Management (IAM) là một framework để quản lý quyền truy cập vào tài nguyên Google Cloud, cho phép quản trị viên xác định ai có thể thực hiện những hành động cụ thể trên tài nguyên. IAM xoay quanh danh tính (như người dùng và tài khoản dịch vụ), vai trò và quyền, trong đó các vai trò được gán cho những danh tính này quyết định quyền truy cập của chúng. Hướng dẫn này đi sâu vào việc quản lý liên kết IAM, tức là liên kết các vai trò này với danh tính, bằng StackQL, một công cụ đơn giản hóa việc quản lý tài nguyên đám mây bằng cú pháp SQL.

Có thể tạo liên kết IAM ở bất kỳ cấp nào sau đây:

- cấp tổ chức
- cấp thư mục
- cấp dự án
- cấp tài nguyên (bucket, tập dữ liệu BigQuery, v.v.)
- cấp tài khoản thanh toán

Mọi đối tượng con đều kế thừa liên kết IAM được áp dụng ở cấp tổ chức hoặc cấp thư mục.

## Định nghĩa liên kết dạng khai báo và GitOps {#declarative-binding-definitions-and-gitops}

Hướng dẫn này sẽ chỉ cho bạn cách lưu bản gốc của liên kết IAM trong kho lưu trữ mã nguồn, biến kho lưu trữ thành nguồn dữ liệu gốc cho trạng thái và lịch sử, một khái niệm được gọi là GitOps, và cách dùng StackQL để áp dụng thay đổi bằng các workflow CI/CD.

Ví dụ này cho bạn thấy cách ánh xạ *nhiều* thành viên hoặc principal tới *nhiều* vai trò từ một manifest `json` được lưu trong hệ thống quản lý mã nguồn.

> Bảng điều khiển GCP, các tiện ích `terraform` và `gcloud` chỉ cho phép bạn ánh xạ *một* thành viên tới một hoặc nhiều vai trò trong mỗi thao tác

## Cách hoạt động {#how-it-works}

Framework mà chúng tôi sẽ hướng dẫn bạn sẽ triển khai liên kết IAM trên mọi nút trong phân cấp GCP của bạn, đồng thời tạo các vai trò tùy chỉnh (với các quyền được chọn lọc) - thay vì các vai trò được định nghĩa sẵn, chẳng hạn như `roles/bigquery.dataEditor`.

### Cấu trúc thư mục dự án {#project-directory-structure}

Cây thư mục sau được dùng cho framework triển khai liên kết này:

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

### Tệp `iql/iam.iql` {#iqliamiql-file}

Đây là tệp chính mà `stackql` sẽ chạy; với `data/iam.jsonnet`, tệp này sẽ tạo và áp dụng các liên kết chính sách đã được chuyển đổi, sinh ra từ dữ liệu trong `data/iam/bindings.json`

> tệp này không cần phải sửa đổi

<details>

<summary>Mở rộng để xem tệp <b><code>iql/iam.iql</code></b></summary>

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

Tệp này cung cấp dữ liệu dùng để tiền xử lý `iql/iam.iql`, lấy dữ liệu đó từ `data/iam/bindings.json`

> tệp này không cần phải sửa đổi

<details>

<summary>Mở rộng để xem tệp <b><code>data/iam.jsonnet</code></b></summary>

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

> tệp này __sẽ cần được sửa đổi__ để thay đổi liên kết chính sách

Đây là nguồn chính của dữ liệu chính sách và liên kết; cấu trúc dữ liệu ánh xạ một hoặc nhiều principal (nhóm, tài khoản dịch vụ, v.v.) tới một hoặc nhiều vai trò (được định nghĩa sẵn hoặc tùy chỉnh) ở một cấp cụ thể (cấp cha) như minh họa dưới đây:

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

ví dụ:

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

## Triển khai {#deployment}

Quy trình này được cố ý không chạy qua pipeline CI/CD, vì các thay đổi không thường xuyên và chúng tôi không muốn cấp cho một tài khoản dịch vụ các mức đặc quyền cần thiết để triển khai nó.  Quy trình này cần được chạy bởi một quản trị viên có đặc quyền, là thành viên của nhóm Google gcp-org-administrators.

Người dùng cần được xác thực với GCP, bằng tài khoản dịch vụ hoặc xác thực tương tác; để biết thêm thông tin về cách xác thực StackQL với Google, hãy xem [__Xác thực GCP__](https://google-provider.stackql.io/#authentication).

Chạy lệnh sau để chạy thử:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Dữ liệu thu được trong tệp này có thể được thực thi dưới dạng từng câu lệnh riêng lẻ trong `stackql shell` hoặc dưới dạng một lô bằng cách chạy:

```shell
stackql exec -i iam-TEMPLATED.iql
```
Ngoài ra, bạn có thể lấy các chính sách (payload JSON) do tập lệnh này tạo ra và chạy chúng bằng lệnh `gcloud iam`.
