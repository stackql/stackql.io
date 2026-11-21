---
title: IAM-Bindings-Framework
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
  - cspm
  - google
  - google cloud platform
  - gcp
description: "Ein GitOps-Framework für Google Cloud IAM mit StackQL: benutzerdefinierte Rollen und Rollen-Bindings für Organisation, Ordner, Projekte und Ressourcen in Jsonnet deklarieren, per Dry Run prüfen und anwenden."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# GitOps-Framework für Google-IAM-Bindings mit StackQL

In dieser Anleitung stellen wir ein GitOps-Framework für die Verwaltung von IAM-Bindings (Berechtigungen) in GCP mit StackQL vor, einem leistungsfähigen Entwicklerwerkzeug, mit dem sich Cloud-Infrastruktur und -Ressourcen per SQL-Syntax abfragen und bereitstellen lassen.  

Getestet mit <span class="tutorial_tested_on">eingebettetes SQL-Backend</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Hintergrund zu Google IAM {#google-iam-background}

Google Cloud Identity and Access Management (IAM) ist ein Framework zur Verwaltung des Zugriffs auf Google-Cloud-Ressourcen, mit dem Administratoren festlegen, wer welche Aktionen auf Ressourcen ausführen darf. Es basiert auf Identitäten (wie Benutzern und Dienstkonten), Rollen und Berechtigungen, wobei die diesen Identitäten zugewiesenen Rollen deren Zugriffsrechte bestimmen. Diese Anleitung befasst sich mit der Verwaltung von IAM-Bindings, die diese Rollen mit Identitäten verknüpfen, mithilfe von StackQL, einem Tool, das die Verwaltung von Cloud-Ressourcen per SQL-Syntax vereinfacht.  

IAM-Bindings können auf jeder der folgenden Ebenen erstellt werden:

- Organisationsebene
- Ordnerebene
- Projektebene
- Ressourcenebene (Buckets, BigQuery-Datasets usw.)
- Rechnungskontoebene

IAM-Bindings, die auf Organisations- oder Ordnerebene angewendet werden, werden von allen untergeordneten Objekten geerbt.  

## Deklarative Binding-Definitionen und GitOps {#declarative-binding-definitions-and-gitops}

Die Anleitung zeigt Ihnen, wie Sie IAM-Bindings in Ihrem Quellcode-Repository pflegen, sodass es zur Source of Truth für Zustand und Historie wird, ein Konzept, das als GitOps bekannt ist, und wie Sie Änderungen mit StackQL über CI/CD-Workflows anwenden.   

Dieses Beispiel zeigt Ihnen, wie Sie *viele* Mitglieder oder Principals auf *viele* Rollen aus einem in der Versionsverwaltung gespeicherten `json`-Manifest abbilden.  

> Die GCP-Konsole sowie die Werkzeuge `terraform` und `gcloud` erlauben es nur, in jeder Operation *ein* Mitglied auf eine oder mehrere Rollen abzubilden  

## Funktionsweise {#how-it-works}

Das Framework, durch das wir Sie führen, stellt IAM-Bindings über alle Knoten Ihrer GCP-Hierarchie bereit und erstellt außerdem benutzerdefinierte Rollen (mit kuratierten Berechtigungen) - im Gegensatz zu vordefinierten Rollen wie zum Beispiel `roles/bigquery.dataEditor`.

### Projektverzeichnisstruktur {#project-directory-structure}

Für dieses Bindings-Deployment-Framework wird der folgende Verzeichnisbaum verwendet:

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

### Datei `iql/iam.iql` {#iqliamiql-file}

Dies ist die Hauptdatei, die von `stackql` ausgeführt wird; mithilfe von `data/iam.jsonnet` rendert sie die aus den Daten in `data/iam/bindings.json` erzeugten, transpilierten Policy-Bindings und wendet sie an

> diese Datei sollte nicht geändert werden müssen

<details>

<summary>Aufklappen, um die Datei <b><code>iql/iam.iql</code></b> anzuzeigen</summary>

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

Diese Datei liefert die Daten, mit denen `iql/iam.iql` vorverarbeitet wird, und bezieht sie aus `data/iam/bindings.json`

> diese Datei sollte nicht geändert werden müssen

<details>

<summary>Aufklappen, um die Datei <b><code>data/iam.jsonnet</code></b> anzuzeigen</summary>

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

> diese Datei __muss geändert werden__, um Policy-Bindings zu ändern

Dies ist die zentrale Quelle der Policy- und Binding-Daten; die Datenstruktur bildet einen oder mehrere Principals (Gruppen, Dienstkonten usw.) auf eine oder mehrere Rollen (vordefiniert oder benutzerdefiniert) auf einer bestimmten Ebene (parent) ab, wie unten gezeigt:

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

zum Beispiel:

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

Dieser Prozess wird bewusst nicht über eine CI/CD-Pipeline ausgeführt, da die Änderungen selten sind und wir einem Dienstkonto nicht die für dieses Deployment erforderlichen Berechtigungen geben möchten.  Er ist von einem privilegierten Administrator auszuführen, der Mitglied der Google-Gruppe gcp-org-administrators ist.  

Der Benutzer muss bei GCP authentifiziert sein, entweder über ein Dienstkonto oder über interaktive Authentifizierung; weitere Informationen zur Authentifizierung von StackQL bei Google finden Sie unter [__GCP-Authentifizierung__](https://google-provider.stackql.io/#authentication).  

Führen Sie den folgenden Befehl aus, um einen Dry Run durchzuführen:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Die resultierenden Daten in dieser Datei können als einzelne Anweisungen in `stackql shell` oder als Batch ausgeführt werden, indem Sie Folgendes ausführen:

```shell
stackql exec -i iam-TEMPLATED.iql
```
Alternativ können Sie die von diesem Skript erzeugten Policies (JSON-Payloads) mit dem Befehl `gcloud iam` ausführen.
