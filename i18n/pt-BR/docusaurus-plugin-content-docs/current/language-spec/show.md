---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: "SHOW no StackQL: listar providers, serviços, recursos e métodos, gerar templates de INSERT e inspecionar versão, colaboradores e dependências do binário em execução."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Usado para listar os itens de uma coleção, por exemplo, para mostrar todos os serviços disponíveis em um provider ou todos os recursos disponíveis em um determinado serviço. Também expõe a versão, os colaboradores e as dependências de software do binário do StackQL em execução.

Veja também:  
[[ Hierarquia de recursos do StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Sintaxe {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`, `CONTRIBUTORS` e `DEPENDENCIES` não exigem configuração de provider nem de autenticação. `DEPENDENCIES` oferece suporte apenas a uma cláusula `LIKE` opcional; não há suporte a `IN`, `FROM`, `WHERE` e `LIMIT`.

* * *

## Exemplos {#examples}

### Listar todos os providers {#list-all-providers}
Liste os providers disponíveis no ambiente local do StackQL: os baixados do registro de providers, mais os providers integrados ao StackQL.
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>Retorna:</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### Listar todos os serviços de um provider {#list-all-services-in-a-cloud-provider}
Liste todos os serviços disponíveis em um determinado provider; use a palavra-chave opcional EXTENDED para retornar informações adicionais.
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>Retorna:</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### Listar todos os recursos de um serviço de um provider {#list-all-resources-within-a-cloud-provider-service}
Liste todos os recursos disponíveis em um determinado serviço de um provider.  A palavra-chave opcional `EXTENDED` retorna a descrição do recurso além do nome do recurso.
```sql
-- Returns all resources available in a provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>Retorna:</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### Mostrar os métodos disponíveis para um recurso de um serviço de um provider {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
Liste todos os métodos disponíveis para um determinado recurso.
```sql
-- Returns all methods available for a provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>Retorna:</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### Gerar um template de `INSERT` com o comando `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

É possível usar o comando `SHOW` para gerar templates de instruções `INSERT`, como mostrado aqui:

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>Retorna:</strong></em></summary>

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

</details>

> É possível limitar os campos retornados apenas aos exigidos pelo provider; consulte [__Criar templates de infraestrutura__](/getting-started/templating) para ver exemplos disso.

> Esse template pode então ser usado com um arquivo de dados `json` ou `jsonnet` para fornecer valores em tempo de execução; para obter mais informações, consulte [__Usar variáveis__](/getting-started/variables).

### Mostrar a versão do StackQL {#show-the-stackql-version}

Retorna a versão do processo do StackQL em execução. Útil nos modos shell, exec e servidor com protocolo de comunicação do Postgres, quando você precisa confirmar qual build está respondendo às consultas. A mesma informação também é exposta pela ferramenta MCP `server_info`.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>Retorna:</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

Use a palavra-chave opcional `EXTENDED` para retornar também o commit do git, o carimbo de data/hora do build e a plataforma do build:

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>Retorna:</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` não exige configuração de provider, de registro nem de autenticação e pode ser executado com segurança em qualquer sessão.

### Listar os colaboradores do StackQL {#list-stackql-contributors}

Retorna o ranking de colaboradores incorporado ao binário do StackQL em execução. O snapshot dá crédito aos colaboradores humanos do GitHub que marcaram o repositório do StackQL com estrela, ordenados pela contagem de contribuições, da maior para a menor. Ele é fixo para cada build e não consulta o GitHub em tempo de execução.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| Coluna | Descrição |
| --- | --- |
| `contributor` | Nome de usuário do GitHub |
| `contributions` | Contagem de contribuições registrada no snapshot; incluída com `EXTENDED` |

### Listar as dependências de software do binário {#list-the-binarys-software-dependencies}

Retorna um inventário offline dos módulos Go incluídos no executável do StackQL em execução. Isso fornece uma lista de materiais de software (SBOM) leve para o binário, que usuários e agentes podem consultar em tempo de execução. O inventário inclui as dependências Go diretas e transitivas; definições de providers instaladas, wrappers separados, ferramentas de CI, módulos usados apenas em testes, imagens de contêiner e pacotes do sistema operacional estão fora do seu escopo.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

As colunas padrão são `name`, `version`, `license` e `description`. `EXTENDED` acrescenta as demais colunas abaixo sem alterar quais linhas são retornadas.

| Coluna | Descrição |
| --- | --- |
| `name` | Caminho efetivo do módulo Go, usando o caminho de substituição quando uma substituição de módulo Go se aplica |
| `version` | Versão exata do módulo, incluindo versões prévias e pseudoversões |
| `license` | Identificador de licença detectado; `UNKNOWN` quando não identificado ou ambíguo |
| `description` | Descrição do componente, quando disponível |
| `source_url` | URL do repositório de origem; incluída com `EXTENDED` |
| `license_url` | URL do arquivo de licença fixada na tag do módulo ou na revisão de origem; incluída com `EXTENDED` |
| `purl` | URL de pacote que identifica o módulo e a versão; incluída com `EXTENDED` |
| `checksum` | Soma de verificação do módulo Go com o prefixo `h1:`, não um hash SHA-256 de executável ou de arquivo compactado; incluída com `EXTENDED` |
| `original_name` | Caminho do módulo solicitado originalmente quando uma substituição se aplica; incluída com `EXTENDED` |
| `original_version` | Versão solicitada originalmente quando uma substituição se aplica; incluída com `EXTENDED` |

As linhas são ordenadas pelo nome efetivo do módulo e, em seguida, pela versão. As substituições usam a versão, a soma de verificação e a atribuição da substituição. `original_name` e `original_version` são `NULL` para módulos sem substituições. Outros metadados indisponíveis também são `NULL`, exceto as licenças não identificadas, que são `UNKNOWN`. Substituições do sistema de arquivos local não recebem versão, URLs nem soma de verificação fictícias.

Use `LIKE` para pesquisar no nome efetivo do módulo, no nome original do módulo ou na descrição:

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

A correspondência não diferencia maiúsculas de minúsculas. `%` corresponde a zero ou mais caracteres e `_` corresponde a exatamente um caractere. As colunas de licença e de versão não são pesquisadas. Duplique uma aspa simples dentro de um literal de string SQL; após a decodificação da string SQL, as barras invertidas são literais e não fazem escape dos curingas de `LIKE`.

Quando nenhum módulo corresponde, o resultado tem zero linhas. O CSV mantém os cabeçalhos das colunas, os clientes do protocolo de comunicação do PostgreSQL mantêm os metadados das colunas e a saída JSON é `null`.

O inventário combina metadados incorporados no momento do build com as informações de build Go do executável. As consultas não precisam de acesso à rede, de uma instalação do Go, de um checkout do código-fonte nem de um cache de módulos. Builds sem metadados gerados ainda informam seus módulos vinculados, com a atribuição ausente mostrada como `UNKNOWN` ou `NULL`.

A saída abrange apenas as dependências Go do binário. Ela não inclui as relações entre dependências nem exporta um documento SPDX ou CycloneDX, e não é um SBOM completo de implantação nem uma determinação de conformidade de licenças.

* * *
