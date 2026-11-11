---
title: 全局标志
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "stackql 二进制文件的全局标志：身份验证、输出格式、注册表位置、日志和 SQL 后端设置，可用于每个命令。"
image: "/img/stackql-featured-image.png"
---

全局标志指定 StackQL 应用程序的运行时行为，这些标志在启动 StackQL 应用程序时（交互式 shell 模式或非交互式 exec 模式）在命令行中设置，或者从 StackQL 初始化文件（[`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)）中读取。标志包括：  

| 标志 | 数据类型 | 说明 | 默认值 |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | 调用方访问配置，一个 JSON 封装对象 `{"path": "/path/to/access.json"}`（也接受直接传入文件路径）。启用[访问管理](/command-line-usage/access-management)：通过 JWT、静态 bearer 令牌、双向 TLS 或本地 OS 身份对调用方进行身份验证，并依据策略规则对语句进行授权。本地令牌凭据从 `STACKQL_CALLER_TOKEN` 读取。在 `v1.0` 及之后的 StackQL 发布版本中可用。 | |
| <span class="nowrap">`--acid`</span> | string | 表示 ACID 配置的 JSON / YAML 字符串 | `{}` |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | 允许会话通过 `SET "$.secret_store"` 选择持久化的 OS 机密存储（macOS Keychain、Linux Secret Service 或 Windows Credential Manager）。即使设置了该标志，平面文件存储也会被拒绝。参阅 [Provider 会话与机密](/getting-started/provider-sessions#persistent-os-secret-stores)。在 `v1.0` 及之后的 StackQL 发布版本中可用。 | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | 允许在 SQL 表达式中解引用机密和凭据的值（`"$secrets.name"`、`"$auth.provider.field"`），其后果是这些值可能进入结果、日志和持久化的查询产物。正常的 Provider 身份验证不需要此标志。参阅 [SQL 中的机密值](/getting-started/provider-sessions#secret-values-in-sql)。在 `v1.0` 及之后的 StackQL 发布版本中可用。 | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | API 请求超时时间，以秒为单位，0 表示不超时 | `45` |
| <span class="nowrap">`--approot`</span> | string | 应用程序配置和缓存的根路径 | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | 应用程序配置和缓存的文件模式 | `493` |
| <span class="nowrap">`--auth`</span> | string | json 形式的 `auth` 上下文键值对 |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | 缓存的初始键数量 | `100` |
| <span class="nowrap">`--configfile`</span> | string | 配置文件的完整路径 | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile` 文件，为空表示无 | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | 单个查询的最大数据流依赖深度 | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | 单个查询的最大数据流弱连通分量数 | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | 用于配置 DBMS 内务查询处理方式的 JSON / YAML 字符串 | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | csv 输出的分隔符。仅限单个字符。对所有非 csv 输出均忽略 | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | `dryrun` 标志；只运行预处理器，返回模板化后的输出 | `false` |
| <span class="nowrap">`--env.file`</span> | string | 可选的 dotenv 风格文件，启动时加载到进程环境中（所有命令）；MCP [`reload_credentials`](/mcp/reload_credentials) 工具按需重新加载它。文件不存在时不报错；值为空的键会被跳过；已有的变量会被覆盖。不从 `.stackqlrc` 读取。在 `v0.10.542` 之后的 StackQL 发布版本中可用。 | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | 查询执行的并发上限 | `1` |
| <span class="nowrap">`--export.alias`</span> | string | 导出别名前缀（命名空间或 schema） | |
| <span class="nowrap">`--gc`</span> | string | 表示 GC 配置的 JSON / YAML 字符串 | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | 禁用列标题，仅在使用 `--output csv` 或 `--output text` 时有效 | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | 在终端中显示 http 请求信息 | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | 代理主机（留空表示不使用代理） | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | 代理密码 | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | 代理端口，任何小于或等于 0 的数字都会使用给定协议方案的默认端口（例如 `http -> 80`） | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | 代理协议方案，`http` 或 `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | 代理用户 | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | 每个 http 请求的最大结果数，任何小于或等于 0 的数字表示不限制 | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | 每个资源返回的最大结果页数，任何小于或等于 0 的数字表示不限制 | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | 间接查询的最大深度：视图和子查询 | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | 从中读取查询的输入文件（IQL 文件） | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | 用于模板化的上下文（数据）文件（`json` 或 `jsonnet` 文件）| |
| <span class="nowrap">`--loglevel`</span> | string | 日志级别，必须是 {`info`, `warn`, `debug`, `fatal`} 之一 | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | [MCP 服务器](/command-line-usage/mcp#mcp-configuration-object)的 JSON / YAML 配置对象（`stackql mcp` 和 `stackql srv`） | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | MCP 审计日志的编码，`jsonl` 或 `otel`（OTLP/JSON 日志记录）；覆盖 `--mcp.config` 中的 `server.audit.format`。参阅[日志格式](/command-line-usage/mcp#log-format)。在 `v0.11.660` 及之后的 StackQL 发布版本中可用。 | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | 对外通告的最新 MCP 协议修订版本，`auto`、`2026-07-28`（仅无会话）或更早的修订版本，如 `2025-11-25`；覆盖 `--mcp.config` 中的 `server.protocol_version`。参阅[固定修订版本](/command-line-usage/mcp#pinning-the-revision)。在 `v0.12.732` 及之后的 StackQL 发布版本中可用。 | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | MCP 服务器类型，{`http`, `stdio`, `reverse_proxy`} 之一。参阅 [MCP 服务器类型](/command-line-usage/mcp#mcp-server-type)。 | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | 已缓存元数据文档的 TTL，以秒为单位 | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | 表示缓存、视图等所用命名空间的 JSON / YAML 字符串 | `{}` |
| <span class="nowrap">`--offline`</span> | flag | 离线工作，使用缓存数据 | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | 写入结果的输出文件 | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | 输出格式，必须是 {`json`, `jsonl`, `table`, `csv`, `text`, `otel`} 之一（`ndjson` 可作为 `jsonl` 的别名）。`otel` 输出 OpenTelemetry 日志记录，参阅[输出模式](/getting-started/output-modes#otel-output)；在 `v0.11.660` 及之后的 StackQL 发布版本中可用。在 `--otel.config` 中配置了导出器时，`otel` 记录还会推送到该端点 | `table` |
| <span class="nowrap">`--otel.config`</span> | string | 用于配置 `otel` 输出格式的 JSON / YAML 字符串；`exporter.endpoint`、`exporter.headers`、`exporter.timeout_ms` 和 `exporter.batch_size` 将记录推送到 OTLP/HTTP 日志端点，参阅[推送到 OTLP 端点](/getting-started/output-modes#pushing-to-an-otlp-endpoint)。在 `v0.12.732` 及之后的 StackQL 发布版本中可用 | `{}` |
| <span class="nowrap">`--preview`</span> | string | 启用预览功能的 JSON 字符串。`{"unstable": true}` 会开放文档驱动的 `stackql_unstable_<provider>.<service>.<resource>` 关系，这些关系由 `omnisdk` 直接根据 Provider 文档解析并运行，行在生成时即以流式返回；其余的键（`batchSize`、`flushInterval`、`endpoint`、`insecureSkipTLSVerify`）用于调整该后端。预览功能并不稳定：行为、语法和输出可能在不同发布版本之间发生变化或失效，且不另行通知，因此不要在生产环境中依赖它。自 `v1.0` 起，同一启用选项还会开放 `stackql_preview.iac.run` 和 `stackql_preview.iac.destroy` 关系，它们通过 `omnisdk` 应用和拆除一组声明的资源，状态保存在应用程序根路径下。参阅[预览功能](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--querycachesize`</span> | integer | 查询计划 LRU 缓存的大小，以条目数计 | `10000` |
| <span class="nowrap">`--registry`</span> | string | 用于配置 Provider 注册表位置和 Provider 文档签名验证的 JSON / YAML 字符串 | 参阅 [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | 表示会话配置的 JSON / YAML 字符串 | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | 表示 SQL 后端系统配置的 JSON / YAML 字符串 | 参阅 [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | 表示 Txn 存储配置的 JSON / YAML 字符串 | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | CA 证书包的路径，未指定时使用系统默认值 |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | 允许信任不安全的证书（不推荐） |  |
| <span class="nowrap">`--var`</span> | string | 以键/值对形式提供的外部变量，通过 `jsonnet` 数据文件引入 StackQL 查询 |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | 详细输出 | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | stackql 的上下文相关帮助 | |
| <span class="nowrap">`--version`</span> | | 显示 StackQL 程序的版本 | |

## `--registry` {#--registry}

`--registry` 标志配置从何处读取 Provider 文档以及是否验证其签名。其值是一个包含以下键的 JSON 字符串：

| 键 | 说明 | 默认值 |
| -- | -- | -- |
| `url` | 注册表 URL。省略时使用公共 StackQL Provider 注册表。 | 公共注册表 |
| `localDocRoot` | 存放 Provider 文档的本地目录，直接从中读取而不从注册表下载。在[开发 Provider](/developers/developing-stackql-providers#testing-a-provider-locally) 时使用。 | |
| `srcPrefix` | 注册表根目录下存放 Provider 源文档的路径前缀。 | `src` |
| `verifyConfig.nopVerify` | `true` 跳过签名验证。设置为 `false` 时，读取的每个 Provider 和服务文档都必须带有嵌入的 ML-DSA-65（FIPS 204）签名，包括由文档支持的预览关系。 | `true` |
| `verifyConfig.publicKeys` | 一个 JSON 对象，将密钥 ID 映射到十六进制编码的 ML-DSA-65 公钥，用于私有注册表。它会替换嵌入的生产密钥；显式给出空对象属于错误。 | 嵌入的密钥 |

### 示例：要求 Provider 文档带签名 {#example-require-signed-provider-documents}

对从公共注册表读取的每个 Provider 和服务文档开启验证，并依据嵌入的生产密钥进行检查：

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### 示例：从本地目录读取未签名的文档 {#example-read-unsigned-documents-from-a-local-directory}

将注册表指向一个存放开发中 Provider 文档的目录。这些文档未签名，因此验证保持关闭：

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

签名验证在 `v1.0` 及之后的 StackQL 发布版本中可用，默认关闭。签名覆盖文档的确切 YAML 字节，因此任何编辑，包括格式或换行的改动，都会使签名失效；请使用注册表的 `pqsig sign-doc` 工具重新签名。未签名的本地开发文档必须保持 `nopVerify: true`。以前的证书选项和 `.sig` sidecar 文件已不再使用，仅随 sidecar 发布的归档必须重新发布后才能在启用验证的情况下使用。验证在读取文档时确认其真实性；归档的下载和解压不在覆盖范围内。

:::

## `--sqlBackend` {#--sqlbackend}

`--sqlBackend` 标志为 StackQL 配置外部 SQL 后端，让你可以使用外部数据库引擎（如 PostgreSQL）代替默认的嵌入式 SQL 引擎。这适用于借助外部数据库进行查询处理、持久化存储，或与现有数据库基础设施集成。

其值是一个包含以下键的 JSON 字符串：

| 键 | 说明 | 示例值 |
| -- | -- | -- |
| `dbEngine` | 数据库引擎类型 | `postgres_tcp` |
| `sqlDialect` | 要使用的 SQL 方言 | `postgres` |
| `dsn` | 数据库的数据源名称（连接字符串） | 参阅下面的示例 |

### 示例：使用 PostgreSQL 后端 {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### 示例：使用 Databricks PostgreSQL 接口 {#example-using-databricks-postgresql-interface}

Databricks 提供了一个兼容 PostgreSQL 的接口，可用作 StackQL 的 SQL 后端。DSN 使用标准的 PostgreSQL 连接字符串格式，并带上你的 Databricks 凭据：

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
用户名或密码中的特殊字符（如 `@`）在 DSN 中必须进行 URL 编码（例如，`@` 变为 `%40`）。
:::
