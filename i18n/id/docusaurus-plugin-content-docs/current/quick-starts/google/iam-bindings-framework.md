---
title: Framework binding IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
  - cspm
  - google
  - google cloud platform
  - gcp
description: "Framework GitOps untuk IAM Google Cloud dengan StackQL: deklarasikan peran kustom dan binding peran untuk organisasi, folder, proyek, dan sumber daya di Jsonnet, lakukan uji coba, lalu terapkan."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# Framework GitOps untuk binding IAM Google menggunakan StackQL

Dalam panduan ini, kami akan menunjukkan framework GitOps untuk manajemen binding IAM (hak akses) di GCP menggunakan StackQL, alat pengembang yang andal yang memungkinkan Anda mengkueri dan men-deploy infrastruktur serta sumber daya cloud menggunakan sintaksis SQL.  

Diuji dengan <span class="tutorial_tested_on">backend SQL tersemat</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Latar belakang IAM Google {#google-iam-background}

Google Cloud Identity and Access Management (IAM) adalah framework untuk mengelola akses ke sumber daya Google Cloud, yang memungkinkan administrator menentukan siapa yang dapat melakukan tindakan tertentu pada sumber daya. IAM berpusat pada identitas (seperti pengguna dan akun layanan), peran, dan izin; peran yang ditetapkan ke identitas tersebut mengatur hak aksesnya. Panduan ini membahas secara mendalam pengelolaan binding IAM, yang menghubungkan peran tersebut ke identitas, menggunakan StackQL, alat yang menyederhanakan pengelolaan sumber daya cloud dengan sintaksis SQL.  

Binding IAM dapat dibuat di salah satu tingkat berikut:

- tingkat organisasi
- tingkat folder
- tingkat proyek
- tingkat sumber daya (bucket, dataset BigQuery, dll.)
- tingkat akun penagihan

Binding IAM yang diterapkan di tingkat organisasi atau folder diwarisi oleh semua objek turunan.  

## Definisi binding deklaratif dan GitOps {#declarative-binding-definitions-and-gitops}

Panduan ini akan menunjukkan cara mengelola binding IAM di repositori kode sumber Anda sehingga repositori tersebut menjadi sumber kebenaran untuk status dan riwayat, sebuah konsep yang dikenal sebagai GitOps, serta cara menggunakan StackQL untuk menerapkan perubahan melalui workflow CI/CD.   

Contoh ini menunjukkan cara memetakan *banyak* anggota atau prinsipal ke *banyak* peran dari manifes `json` yang disimpan di kontrol sumber.  

> Konsol GCP serta utilitas `terraform` dan `gcloud` hanya memungkinkan Anda memetakan *satu* anggota ke satu peran atau lebih dalam setiap operasi  

## Cara kerja {#how-it-works}

Framework yang akan kami jelaskan langkah demi langkah akan men-deploy binding IAM di semua node dalam hierarki GCP Anda, serta membuat peran kustom (dengan izin yang dikurasi) - berbeda dengan peran yang telah ditetapkan, misalnya `roles/bigquery.dataEditor`.

### Struktur direktori proyek {#project-directory-structure}

Pohon direktori berikut digunakan untuk framework deployment binding ini:

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

### File `iql/iam.iql` {#iqliamiql-file}

Ini adalah file utama yang akan dijalankan oleh `stackql`; dengan menggunakan `data/iam.jsonnet`, file ini akan merender dan menerapkan binding kebijakan hasil transpilasi yang dibuat dari data di `data/iam/bindings.json`

> file ini seharusnya tidak perlu diubah

<details>

<summary>Perluas untuk melihat file <b><code>iql/iam.iql</code></b></summary>

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

File ini menyediakan data yang digunakan untuk memproses terlebih dahulu `iql/iam.iql`, dengan sumber data dari `data/iam/bindings.json`

> file ini seharusnya tidak perlu diubah

<details>

<summary>Perluas untuk melihat file <b><code>data/iam.jsonnet</code></b></summary>

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

> file ini __perlu diubah__ untuk melakukan perubahan binding kebijakan

Ini adalah sumber utama data kebijakan dan binding; struktur datanya memetakan satu atau banyak prinsipal (grup, akun layanan, dll.) ke satu atau banyak peran (yang telah ditetapkan atau kustom) di tingkat tertentu (induk) seperti yang ditunjukkan di bawah ini:

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

misalnya:

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

## Deployment {#deployment}

Proses ini sengaja tidak dijalankan melalui pipeline CI/CD, karena perubahannya jarang terjadi, dan kami tidak ingin memberikan tingkat hak istimewa yang diperlukan untuk men-deploy ini kepada akun layanan.  Proses ini harus dijalankan oleh administrator dengan hak istimewa yang merupakan anggota grup Google gcp-org-administrators.  

Pengguna harus terautentikasi ke GCP menggunakan akun layanan atau autentikasi interaktif; untuk informasi selengkapnya tentang mengautentikasi StackQL ke Google, lihat [__Autentikasi GCP__](https://google-provider.stackql.io/#authentication).  

Jalankan perintah berikut untuk melakukan uji coba:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Data yang dihasilkan dalam file ini dapat dijalankan sebagai pernyataan satu per satu di `stackql shell` atau sebagai batch dengan menjalankan:

```shell
stackql exec -i iam-TEMPLATED.iql
```
Sebagai alternatif, Anda dapat mengambil kebijakan (payload JSON) yang dihasilkan oleh skrip ini dan menjalankannya menggunakan perintah `gcloud iam`.
