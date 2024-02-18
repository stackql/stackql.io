---
title: IAM Bindings Framework
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
  - cspm
  - google
  - google cloud platform
  - gcp
description: Query and Deploy Google Cloud Infrastructure and Resources using SQL
image: "/img/cookbooks/google/stackql-google-provider-featured-image.png"
---

# Google GitOps IAM bindings framework using StackQL

In this guide, we will demonstrate a GitOps framework for IAM Bindings (entitlements) management in GCP using StackQL, a powerful dev tool that enables querying and deploying cloud infrastructure and resources using SQL syntax.  

Tested with <span class="cookbook_tested_on">embedded sql backend</span> <span class="cookbook_tested_on">macos</span> <span class="cookbook_tested_on">linux</span> <span class="cookbook_tested_on">powershell</span>

## Google IAM background

Google Cloud Identity and Access Management (IAM) is a framework for managing access to Google Cloud resources, allowing administrators to define who can take specific actions on resources. It operates around identities (like users and service accounts), roles, and permissions, where roles assigned to these identities govern their access rights. This guide delves into managing IAM bindings, which link these roles to identities, using StackQL, a tool that simplifies cloud resource management with SQL syntax.  

IAM bindings can be created at any of the following levels:

- organization level
- folder level
- project level
- resource level (Buckets, BigQuery Datasets, etc)
- billing account level

IAM bindings applied at organization or folder level are inherited by all child objects.  

## Declarative binding definitions and GitOps

The guide will show you how to master IAM bindings in your source code repository, making it the source of truth for state and history, a concept known as GitOps, and how to use StackQL to apply changes using CI/CD workflows.   

This example shows you how to map *many* members or principals to *many* roles from a `json` manifest stored in source control.  

> The GCP console, `terraform`, `gcloud` utilities only allow you to map *one* member to one or more roles in each operation  

## How it works

The framework that we will walk you through will deploy IAM bindings across all nodes in your GCP heirarchy, as well as creating custom roles (with curated permissions) - as opposed to predefined roles such as `roles/bigquery.dataEditor` for example.

### Project directory structure

The following directory tree is used for this bindings deployment framework:

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

### `iql/iam.iql` file

This is the main file which will be run by the `stackql`, using `data/iam.jsonnet` this will render and apply the transpiled policy bindings generated from the data in `data/iam/bindings.json`

> this file should not need to be modified

<details>

<summary>Expand to see the <b><code>iql/iam.iql</code></b> file</summary>

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


## `data/iam.jsonnet`

This file is preprocessed by `iql/iam.iql` sourcing data from `data/iam/bindings.json`

> this file should not need to be modified

<details>

<summary>Expand to see the <b><code>data/iam.jsonnet</code></b> file</summary>

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

## `data/iam/bindings.json`

> this file __will need to be modified__ to make policy binding changes

This is the master source of policy and binding data, the data structure maps one or many principals (groups, serviceaccount, etc) to one or many roles (predefined or custom) at a particular level (parent) as shown below:

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

for example:

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

## Deployment

This process is intentionally not run through a CI/CD pipeline, as the changes are infrequent, and we wish not to give a service account the levels of priveleges required to deploy this.  This is to be run by a priveleged adminstrator who is a member of the gcp-org-administrators Google group.  

The user needs to be authenticated to GCP, using either a service account or interactive authentication, for more information about authenticating StackQL to Google see [__GCP Authentication__](https://google.stackql.io/providers/google/#authentication).  

Run the following command to perform a dryrun:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

The resultant data in this file can be exectuted as individual statements in `stackql shell` or as a batch by running:

```shell
stackql exec -i iam-TEMPLATED.iql \
```
Alternatively, you could take the policies (JSON payloads) generated by this script and run them using the `gcloud iam` command.

