---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 SHOW：列出 Provider、服务、资源和方法，生成 INSERT 模板，并查看运行中二进制文件的版本、贡献者和依赖项。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

用于列出集合中的项，例如显示某个 Provider 中所有可用的服务，或显示给定服务中所有可用的资源。还可以查看运行中的 StackQL 二进制文件的版本、贡献者和软件依赖项。

另请参阅：  
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy)

* * * 

## 语法 {#syntax}

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

`VERSION`、`CONTRIBUTORS` 和 `DEPENDENCIES` 不需要任何 Provider 或身份验证配置。`DEPENDENCIES` 仅支持可选的 `LIKE` 子句；不支持 `IN`、`FROM`、`WHERE` 和 `LIMIT`。

* * *

## 示例 {#examples}

### 列出所有 Provider {#list-all-providers}
列出已安装的 StackQL 版本中所有可用的云服务商。
```sql
-- Returns all of the available cloud providers
SHOW PROVIDERS;
```
<details>
<summary><em><strong>返回：</strong></em></summary>

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

### 列出云服务商的所有服务 {#list-all-services-in-a-cloud-provider}
列出给定云服务商中所有可用的服务，使用可选的 EXTENDED 关键字可返回更多信息。
```sql
-- Returns all services available in a cloud provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>返回：</strong></em></summary>

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


### 列出云服务商某个服务中的所有资源 {#list-all-resources-within-a-cloud-provider-service}
列出云服务商的给定服务中所有可用的资源。可选的 `EXTENDED` 关键字除资源名称外还会返回资源描述。
```sql
-- Returns all resources available in a cloud provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>返回：</strong></em></summary>

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

### 显示云服务商某个服务中某个资源的可用方法 {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
列出给定资源的所有可用方法。
```sql
-- Returns all methods available for a cloud provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>返回：</strong></em></summary>

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

### 使用 `SHOW INSERT` 命令生成 `INSERT` 模板 {#generating-an-insert-template-using-the-show-insert-command}

`SHOW` 命令可用于生成 `INSERT` 语句的模板，如下所示：

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>返回：</strong></em></summary>

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

> 返回的字段可以限制为仅包含 Provider 必需的字段，相关示例参阅[__创建基础设施模板__](/getting-started/templating)。

> 之后可以将此模板与 `json` 或 `jsonnet` 数据文件配合使用，在运行时提供值，更多信息参阅[__使用变量__](/getting-started/variables)。

### 显示 StackQL 版本 {#show-the-stackql-version}

返回运行中的 StackQL 进程的版本。在 shell、exec 和 Postgres 线协议服务器模式下，需要确认是哪个构建版本在响应查询时很有用。同样的信息也可通过 MCP `server_info` 工具获取。

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>返回：</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

使用可选的 `EXTENDED` 关键字还可以返回 git 提交、构建时间戳和构建平台：

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>返回：</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` 不需要任何 Provider、注册表或身份验证配置，可以在任何会话中安全执行。

### 列出 StackQL 贡献者 {#list-stackql-contributors}

返回嵌入在运行中的 StackQL 二进制文件里的贡献者排行榜。该快照向已为 StackQL 仓库加星标的 GitHub 真人贡献者致谢，按贡献数量排序，从高到低。它在每次构建时固定，运行时不会查询 GitHub。

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| 列 | 说明 |
| --- | --- |
| `contributor` | GitHub 用户名 |
| `contributions` | 快照中记录的贡献数量；使用 `EXTENDED` 时包含 |

### 列出二进制文件的软件依赖项 {#list-the-binarys-software-dependencies}

返回运行中的 StackQL 可执行文件所包含的 Go 模块的离线清单。它为该二进制文件提供一份轻量级的软件物料清单（SBOM），用户和智能体可在运行时查询。它包含直接和传递的 Go 依赖项；已安装的 Provider 定义、独立的包装器、CI 工具、仅用于测试的模块、容器镜像和操作系统软件包不在其范围内。

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

默认列为 `name`、`version`、`license` 和 `description`。`EXTENDED` 会追加下表中的其余列，但不改变返回的行。

| 列 | 说明 |
| --- | --- |
| `name` | 生效的 Go 模块路径，存在 Go 模块替换时使用替换路径 |
| `version` | 精确的模块版本，包括预发布版本和伪版本 |
| `license` | 检测到的许可证标识符；无法识别或存在歧义时为 `UNKNOWN` |
| `description` | 组件描述（如果有） |
| `source_url` | 源代码仓库 URL；使用 `EXTENDED` 时包含 |
| `license_url` | 固定到模块标签或源代码修订版本的许可证文件 URL；使用 `EXTENDED` 时包含 |
| `purl` | 标识模块及版本的 Package URL；使用 `EXTENDED` 时包含 |
| `checksum` | 带 `h1:` 前缀的 Go 模块校验和，不是可执行文件或归档文件的 SHA-256 摘要；使用 `EXTENDED` 时包含 |
| `original_name` | 存在替换时最初请求的模块路径；使用 `EXTENDED` 时包含 |
| `original_version` | 存在替换时最初请求的版本；使用 `EXTENDED` 时包含 |

行按生效的模块名称排序，然后按版本排序。发生替换的模块使用替换模块的版本、校验和与归属信息。对于没有替换的模块，`original_name` 和 `original_version` 为 `NULL`。其他不可用的元数据同样为 `NULL`，但无法识别的许可证为 `UNKNOWN`。对于本地文件系统替换，不会虚构版本、URL 或校验和。

使用 `LIKE` 搜索生效的模块名称、原始模块名称或描述：

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

匹配不区分大小写。`%` 匹配零个或多个字符，`_` 恰好匹配一个字符。不搜索许可证列和版本列。SQL 字符串字面量中的单引号需要写两次；SQL 字符串解码之后，反斜杠按字面处理，不会转义 `LIKE` 通配符。

没有模块匹配时，结果为零行。CSV 保留列标题，PostgreSQL 线协议客户端保留列元数据，JSON 输出为 `null`。

该清单将构建时嵌入的元数据与可执行文件的 Go 构建信息结合起来。查询不需要网络访问、Go 安装、源代码检出或模块缓存。没有生成元数据的构建仍会报告其链接的模块，缺失的归属信息显示为 `UNKNOWN` 或 `NULL`。

输出仅涵盖该二进制文件的 Go 依赖项。它不包含依赖关系，也不导出 SPDX 或 CycloneDX 文档，既不是完整的部署 SBOM，也不构成许可证合规性判定。

* * *
