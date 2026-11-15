---
title: Framework de vinculaciones de IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
  - cspm
  - google
  - google cloud platform
  - gcp
description: "Framework de GitOps para IAM de Google Cloud con StackQL: declarar en Jsonnet roles personalizados y vinculaciones de roles para la organización, carpetas, proyectos y recursos, hacer una ejecución de prueba y aplicarlos."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# Framework de GitOps para vinculaciones de IAM de Google con StackQL

En esta guía mostraremos un framework de GitOps para la gestión de vinculaciones de IAM (derechos de acceso) en GCP con StackQL, una potente herramienta de desarrollo que permite consultar y desplegar infraestructura y recursos en la nube con sintaxis SQL.  

Probado con <span class="tutorial_tested_on">backend SQL integrado</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Contexto de IAM de Google {#google-iam-background}

Google Cloud Identity and Access Management (IAM) es un marco para gestionar el acceso a los recursos de Google Cloud que permite a los administradores definir quién puede realizar acciones específicas sobre los recursos. Se basa en identidades (como usuarios y cuentas de servicio), roles y permisos, de modo que los roles asignados a estas identidades determinan sus derechos de acceso. Esta guía profundiza en la gestión de las vinculaciones de IAM, que asocian estos roles a las identidades, con StackQL, una herramienta que simplifica la gestión de recursos en la nube con sintaxis SQL.  

Las vinculaciones de IAM se pueden crear en cualquiera de los siguientes niveles:

- nivel de organización
- nivel de carpeta
- nivel de proyecto
- nivel de recurso (buckets, conjuntos de datos de BigQuery, etc.)
- nivel de cuenta de facturación

Todos los objetos secundarios heredan las vinculaciones de IAM aplicadas a nivel de organización o de carpeta.  

## Definiciones declarativas de vinculaciones y GitOps {#declarative-binding-definitions-and-gitops}

La guía le mostrará cómo mantener la copia maestra de las vinculaciones de IAM en su repositorio de código fuente, de modo que este sea la fuente de verdad del estado y del historial (un concepto conocido como GitOps), y cómo usar StackQL para aplicar cambios con workflows de CI/CD.   

Este ejemplo le muestra cómo asociar *muchos* miembros o principales a *muchos* roles a partir de un manifiesto `json` almacenado en el control de código fuente.  

> La consola de GCP y las utilidades `terraform` y `gcloud` solo permiten asociar *un* miembro a uno o más roles en cada operación  

## Funcionamiento {#how-it-works}

El framework que le presentaremos paso a paso desplegará vinculaciones de IAM en todos los nodos de su jerarquía de GCP y también creará roles personalizados (con permisos seleccionados) - a diferencia de los roles predefinidos, como `roles/bigquery.dataEditor`, por ejemplo.

### Estructura de directorios del proyecto {#project-directory-structure}

Este framework de despliegue de vinculaciones usa el siguiente árbol de directorios:

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

### Archivo `iql/iam.iql` {#iqliamiql-file}

Este es el archivo principal que ejecutará `stackql`; con `data/iam.jsonnet`, renderiza y aplica las vinculaciones de políticas transpiladas que se generan a partir de los datos de `data/iam/bindings.json`

> no debería ser necesario modificar este archivo

<details>

<summary>Expanda para ver el archivo <b><code>iql/iam.iql</code></b></summary>

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

`iql/iam.iql` preprocesa este archivo con los datos de `data/iam/bindings.json`

> no debería ser necesario modificar este archivo

<details>

<summary>Expanda para ver el archivo <b><code>data/iam.jsonnet</code></b></summary>

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

> __será necesario modificar__ este archivo para cambiar las vinculaciones de políticas

Esta es la fuente maestra de los datos de políticas y vinculaciones; la estructura de datos asocia uno o varios principales (grupos, cuentas de servicio, etc.) a uno o varios roles (predefinidos o personalizados) en un nivel concreto (el elemento superior), como se muestra a continuación:

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

por ejemplo:

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

## Despliegue {#deployment}

Intencionadamente, este proceso no se ejecuta en un pipeline de CI/CD, ya que los cambios son poco frecuentes y no queremos conceder a una cuenta de servicio el nivel de privilegios necesario para desplegarlo.  Debe ejecutarlo un administrador con privilegios que sea miembro del grupo de Google gcp-org-administrators.  

El usuario debe estar autenticado en GCP, ya sea con una cuenta de servicio o con autenticación interactiva; para obtener más información sobre la autenticación de StackQL en Google, consulte [__Autenticación en GCP__](https://google-provider.stackql.io/#authentication).  

Ejecute el siguiente comando para realizar una ejecución de prueba:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Los datos resultantes de este archivo se pueden ejecutar como instrucciones individuales en `stackql shell` o como un lote, con el siguiente comando:

```shell
stackql exec -i iam-TEMPLATED.iql
```
Como alternativa, puede tomar las políticas (cargas útiles JSON) que genera este script y ejecutarlas con el comando `gcloud iam`.
