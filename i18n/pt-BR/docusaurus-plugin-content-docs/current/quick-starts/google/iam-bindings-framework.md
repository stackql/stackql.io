---
title: Framework de vinculações do IAM
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
  - cspm
  - google
  - google cloud platform
  - gcp
description: "Framework GitOps para o IAM do Google Cloud com o StackQL: declarar em Jsonnet roles personalizadas e vinculações de roles da organização, pastas, projetos e recursos, fazer uma execução de teste e aplicar."
image: "/img/tutorials/google/stackql-google-provider-featured-image.png"
---

# Framework de GitOps para vinculações do IAM do Google com o StackQL

Neste guia, mostramos um framework de GitOps para o gerenciamento de vinculações do IAM (direitos de acesso) no GCP com o StackQL, uma ferramenta de desenvolvimento poderosa que permite consultar e implantar infraestrutura e recursos na nuvem com sintaxe SQL.  

Testado com <span class="tutorial_tested_on">backend SQL incorporado</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Contexto do IAM do Google {#google-iam-background}

O Google Cloud Identity and Access Management (IAM) é um framework para gerenciar o acesso aos recursos do Google Cloud, que permite aos administradores definir quem pode executar ações específicas nos recursos. Ele se baseia em identidades (como usuários e contas de serviço), roles e permissões, e as roles atribuídas a essas identidades determinam os direitos de acesso delas. Este guia aborda em detalhes o gerenciamento de vinculações do IAM, que associam essas roles às identidades, com o StackQL, uma ferramenta que simplifica o gerenciamento de recursos na nuvem com sintaxe SQL.  

As vinculações do IAM podem ser criadas em qualquer um dos seguintes níveis:

- nível de organização
- nível de pasta
- nível de projeto
- nível de recurso (buckets, conjuntos de dados do BigQuery etc.)
- nível de conta de faturamento

Todos os objetos filhos herdam as vinculações do IAM aplicadas no nível de organização ou de pasta.  

## Definições declarativas de vinculações e GitOps {#declarative-binding-definitions-and-gitops}

Este guia mostra como manter a versão mestre das vinculações do IAM no seu repositório de código-fonte, tornando-o a fonte da verdade para o estado e o histórico (um conceito conhecido como GitOps), e como usar o StackQL para aplicar alterações com workflows de CI/CD.   

Este exemplo mostra como mapear *muitos* membros ou principals para *muitas* roles a partir de um manifesto `json` armazenado no controle de código-fonte.  

> O console do GCP e os utilitários `terraform` e `gcloud` só permitem mapear *um* membro para uma ou mais roles em cada operação  

## Como funciona {#how-it-works}

O framework que vamos apresentar implanta vinculações do IAM em todos os nós da sua hierarquia do GCP, além de criar roles personalizadas (com permissões selecionadas) - em vez de roles predefinidas como `roles/bigquery.dataEditor`, por exemplo.

### Estrutura de diretórios do projeto {#project-directory-structure}

Este framework de implantação de vinculações usa a seguinte árvore de diretórios:

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

### Arquivo `iql/iam.iql` {#iqliamiql-file}

Este é o arquivo principal, executado pelo `stackql`; com o `data/iam.jsonnet`, ele renderiza e aplica as vinculações de políticas transpiladas, geradas a partir dos dados de `data/iam/bindings.json`

> não deve ser necessário modificar este arquivo

<details>

<summary>Expanda para ver o arquivo <b><code>iql/iam.iql</code></b></summary>

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

Este arquivo fornece os dados usados para pré-processar o `iql/iam.iql`, obtendo-os de `data/iam/bindings.json`

> não deve ser necessário modificar este arquivo

<details>

<summary>Expanda para ver o arquivo <b><code>data/iam.jsonnet</code></b></summary>

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

> este arquivo __precisará ser modificado__ para alterar as vinculações de políticas

Esta é a fonte mestre dos dados de políticas e vinculações; a estrutura de dados mapeia um ou vários principals (grupos, contas de serviço etc.) para uma ou várias roles (predefinidas ou personalizadas) em um determinado nível (pai), como mostrado abaixo:

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

por exemplo:

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

## Implantação {#deployment}

Este processo intencionalmente não é executado por um pipeline de CI/CD, pois as alterações são pouco frequentes e não queremos conceder a uma conta de serviço os níveis de privilégio necessários para essa implantação.  Ele deve ser executado por um administrador com privilégios que seja membro do grupo do Google gcp-org-administrators.  

O usuário precisa estar autenticado no GCP, com uma conta de serviço ou com autenticação interativa; para obter mais informações sobre a autenticação do StackQL no Google, consulte [__Autenticação no GCP__](https://google-provider.stackql.io/#authentication).  

Execute o comando a seguir para fazer uma execução de teste:
```shell
stackql exec -i ./iql/iam.iql \
--iqldata ./data/iam.jsonnet \
--outfile iam-TEMPLATED.iql \
--dryrun --output text --hideheaders
```

Os dados resultantes neste arquivo podem ser executados como instruções individuais no `stackql shell` ou como um lote, com o comando:

```shell
stackql exec -i iam-TEMPLATED.iql
```
Como alternativa, você pode pegar as políticas (payloads JSON) geradas por este script e executá-las com o comando `gcloud iam`.
