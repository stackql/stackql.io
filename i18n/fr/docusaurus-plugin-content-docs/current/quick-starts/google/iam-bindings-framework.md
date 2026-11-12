---
title: Framework de liaisons IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
  - cspm
  - google
  - google cloud platform
  - gcp
description: Interrogez et déployez l'infrastructure et les ressources Google Cloud en SQL
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# Framework GitOps de liaisons IAM Google avec StackQL

Dans ce guide, nous présentons un framework GitOps de gestion des liaisons IAM (droits d'accès) dans GCP avec StackQL, un outil de développement puissant qui permet d'interroger et de déployer l'infrastructure et les ressources cloud à l'aide de la syntaxe SQL.  

Testé avec <span class="tutorial_tested_on">backend SQL embarqué</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Rappels sur Google IAM {#google-iam-background}

Google Cloud Identity and Access Management (IAM) est un cadre de gestion des accès aux ressources Google Cloud, qui permet aux administrateurs de définir qui peut effectuer des actions données sur les ressources. Il s'articule autour d'identités (comme les utilisateurs et les comptes de service), de rôles et de permissions, les rôles attribués à ces identités déterminant leurs droits d'accès. Ce guide explique comment gérer les liaisons IAM, qui associent ces rôles à des identités, avec StackQL, un outil qui simplifie la gestion des ressources cloud grâce à la syntaxe SQL.  

Des liaisons IAM peuvent être créées à chacun des niveaux suivants :

- niveau de l'organisation
- niveau du dossier
- niveau du projet
- niveau de la ressource (buckets, jeux de données BigQuery, etc.)
- niveau du compte de facturation

Les liaisons IAM appliquées au niveau de l'organisation ou d'un dossier sont héritées par tous les objets enfants.  

## Définitions déclaratives de liaisons et GitOps {#declarative-binding-definitions-and-gitops}

Ce guide vous montre comment tenir la copie de référence des liaisons IAM dans votre dépôt de code source, ce qui en fait la source de vérité pour l'état et l'historique, un concept appelé GitOps, et comment utiliser StackQL pour appliquer les changements au moyen de workflows CI/CD.   

Cet exemple vous montre comment associer *plusieurs* membres ou principaux à *plusieurs* rôles à partir d'un manifeste `json` stocké dans le système de gestion de versions.  

> La console GCP et les utilitaires `terraform` et `gcloud` ne permettent d'associer qu'*un seul* membre à un ou plusieurs rôles par opération  

## Fonctionnement {#how-it-works}

Le framework que nous allons parcourir avec vous déploie des liaisons IAM sur tous les nœuds de votre hiérarchie GCP et crée des rôles personnalisés (avec des permissions choisies) - par opposition aux rôles prédéfinis comme `roles/bigquery.dataEditor`, par exemple.

### Structure des répertoires du projet {#project-directory-structure}

L'arborescence de répertoires suivante est utilisée pour ce framework de déploiement de liaisons :

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

### Fichier `iql/iam.iql` {#iqliamiql-file}

C'est le fichier principal exécuté par `stackql` ; à l'aide de `data/iam.jsonnet`, il génère et applique les liaisons de politique transpilées, produites à partir des données de `data/iam/bindings.json`

> ce fichier ne devrait pas avoir besoin d'être modifié

<details>

<summary>Développer pour voir le fichier <b><code>iql/iam.iql</code></b></summary>

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

Ce fichier est prétraité par `iql/iam.iql` et tire ses données de `data/iam/bindings.json`

> ce fichier ne devrait pas avoir besoin d'être modifié

<details>

<summary>Développer pour voir le fichier <b><code>data/iam.jsonnet</code></b></summary>

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

> ce fichier __devra être modifié__ pour changer les liaisons de politique

C'est la source de référence des données de politiques et de liaisons ; la structure de données associe un ou plusieurs principaux (groupes, comptes de service, etc.) à un ou plusieurs rôles (prédéfinis ou personnalisés) à un niveau donné (parent), comme illustré ci-dessous :

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

par exemple :

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

## Déploiement {#deployment}

Ce processus n'est volontairement pas exécuté dans un pipeline CI/CD, car les changements sont peu fréquents et nous ne souhaitons pas accorder à un compte de service les niveaux de privilèges nécessaires à ce déploiement.  Il doit être exécuté par un administrateur disposant de privilèges élevés, membre du groupe Google gcp-org-administrators.  

L'utilisateur doit être authentifié auprès de GCP, avec un compte de service ou par authentification interactive ; pour plus d'informations sur l'authentification de StackQL auprès de Google, consultez [__Authentification GCP__](https://google.stackql.io/providers/google/#authentication).  

Exécutez la commande suivante pour effectuer une exécution à blanc :
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Les données obtenues dans ce fichier peuvent être exécutées sous forme d'instructions individuelles dans `stackql shell` ou par lot en exécutant :

```shell
stackql exec -i iam-TEMPLATED.iql \
```
Vous pouvez aussi prendre les politiques (charges utiles JSON) générées par ce script et les exécuter avec la commande `gcloud iam`.
