---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "stackql mcp 命令将 StackQL 作为面向 AI 智能体的 Model Context Protocol 服务器运行：部署模式、安全模式、凭据重新加载和审计日志。"
image: "/img/stackql-featured-image.png"
---

用于将 StackQL 作为 Model Context Protocol（MCP）服务器启动的命令，使 AI 智能体和助手能够利用 StackQL 的查询能力与云基础设施交互。

:::info[什么是 MCP？]

[Model Context Protocol（MCP）](https://modelcontextprotocol.io/)是一种开放协议，用于规范 AI 应用与外部数据源和工具的交互方式。将 StackQL 作为 MCP 服务器运行，可以让 AI 智能体（Claude、Cursor、Continue、自定义智能体等）使用自然语言跨多个 Provider 查询和管理云基础设施。

:::

* * *

### 语法 {#syntax}

`stackql mcp [flags]` 用于独立的 MCP 服务器。

`stackql srv [flags]` 用于在同一进程中同时运行 MCP 服务器和 PostgreSQL 线协议服务器。

* * *

### 部署模式 {#deployment-modes}

StackQL 的 MCP 服务器可以按 3 种不同的配置部署，以满足不同的架构需求。下面的示例通过 `"allow_unauthenticated": true` 选择启用未经身份验证的服务，这适合本地开发；对于其他进程或主机可以访问到的任何服务器，请改为配置 bearer 令牌（参阅 [HTTP 客户端身份验证](#http-client-authentication)）。

#### 1. 独立 MCP 服务器 {#1-standalone-mcp-server}

在指定端口上将 StackQL 作为专用 MCP 服务器运行。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**使用场景**：只需要 MCP 协议访问，不需要 PostgreSQL 线协议兼容性时。

#### 2. MCP + PostgreSQL 服务器（内存内） {#2-mcp--postgresql-server-in-memory}

同时运行 MCP 服务器和 PostgreSQL 服务器，两者之间采用内存内通信。

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**使用场景**：既需要为 AI 智能体提供 MCP 协议访问，又需要为传统数据库客户端提供 PostgreSQL 线协议，并通过内存内通信获得最高性能时。

#### 3. MCP + PostgreSQL 服务器（反向代理） {#3-mcp--postgresql-server-reverse-proxy}

运行两个服务器并采用基于 TCP 的通信，支持分布式部署和 TLS 加密。

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**使用 TLS 加密：**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS 对传输进行加密，但不对客户端进行身份验证，因此本示例还要求提供保存在 `STACKQL_MCP_TOKEN` 中的 bearer 令牌。

**使用场景**：需要将 MCP 和 PostgreSQL 工作负载分离到不同的进程或主机，或需要为 MCP 端点启用 TLS 加密时。

* * *

### 配置选项 {#configuration-options}

#### MCP 服务器类型 {#mcp-server-type}

| 类型 | 说明 |
|--|--|
|`http`|直接 HTTP 服务器模式：MCP 请求由 StackQL 引擎在进程内处理。|
|`reverse_proxy`|反向代理模式：MCP 请求通过配置的 DSN 分发到后端 PostgreSQL 服务器。与 `stackql srv` 搭配使用。|
|`stdio`|标准输入/输出传输方式：服务器从 `stdin` 读取请求，并将响应写入 `stdout`。由嵌入编辑器的 MCP 客户端（Claude Desktop、Cursor 等）使用。|

#### MCP 日志格式 {#mcp-log-format}

`--mcp.log.format` 标志选择审计日志的编码：`jsonl`（默认）或 `otel`（OpenTelemetry OTLP/JSON 日志记录）。它会覆盖配置对象中的 `server.audit.format`。参阅[日志格式](#log-format)。

#### MCP 协议版本 {#mcp-protocol-version}

`--mcp.protocol.version` 标志设置服务器通告的最新协议修订版：`auto`（默认，所有受支持的修订版）、`2026-07-28`（仅无会话）或较旧的修订版，例如 `2025-11-25`（该修订版及更早的修订版）。它会覆盖配置对象中的 `server.protocol_version`。参阅[固定修订版](#pinning-the-revision)。

#### MCP 配置对象 {#mcp-configuration-object}

`--mcp.config` 标志接受具有以下结构的 JSON 对象。

##### 服务器配置 {#server-configuration}

| 字段 | 说明 | 必填 |
|--|--|--|
|`server.transport`|传输协议：`http` 或 `stdio`。|是|
|`server.address`|MCP 服务器绑定的地址和端口（例如 `127.0.0.1:9912`）。仅限 HTTP 传输方式。|对于 `http`|
|`server.tls_cert_file`|用于 HTTPS 的 TLS 证书文件的路径。|否|
|`server.tls_key_file`|用于 HTTPS 的 TLS 私钥文件的路径。|否|
|`server.auth_token_env_var`|保存预共享 bearer 令牌的环境变量的名称。设置后，每个 HTTP 请求都必须携带 `Authorization: Bearer <token>`；如果该变量未设置或为空，服务器将拒绝启动。参阅 [HTTP 客户端身份验证](#http-client-authentication)。|对于 `http`，此项与 `allow_unauthenticated` 二选一|
|`server.allow_unauthenticated`|显式选择在不进行客户端身份验证的情况下提供 HTTP 传输方式。默认为 `false`。|对于 `http`，此项与 `auth_token_env_var` 二选一|
|`server.mode`|对变更和生命周期操作进行门控的安全契约。取值为 `read_only`、`safe`（默认）、`delete_safe`、`full_access` 之一。参阅[服务器模式](#server-modes)。|否|
|`server.read_only`|`mode: read_only` 的**旧版**布尔别名。两个字段都设置时，以 `mode` 为准。|否|
|`server.stateless`|以无会话方式提供 Streamable HTTP（没有 `Mcp-Session-Id`），协议修订版 `2026-07-28` 就是以这种方式通过 HTTP 提供的。默认为 `false`。对于 `stdio` 会被忽略。参阅[协议修订版支持](#protocol-revision-support)。|否|
|`server.protocol_version`|通告的最新协议修订版：`auto`（默认）、`2026-07-28` 或较旧的修订版，例如 `2025-11-25`。对于 Streamable HTTP，`2026-07-28` 隐含 `stateless`。参阅[固定修订版](#pinning-the-revision)。|否|
|`server.audit`|审计子系统配置，包括 `audit.format`（`jsonl` 或 `otel`）。参阅[审计日志](#audit-log)。|否|

##### 后端配置（仅反向代理模式） {#backend-configuration-reverse-proxy-mode-only}

| 字段 | 说明 | 必填 |
|--|--|--|
|`backend.dsn`|后端 StackQL 服务器的 PostgreSQL 连接字符串。|是（对于 `reverse_proxy`）|

<br />

:::info

后端 DSN 应包含 `default_query_exec_mode=simple_protocol` 参数，以获得最佳兼容性。

:::

##### 允许列表 {#allowlists}

| 字段 | 说明 |
|--|--|
|`enabled_tools`|允许服务器发布的工具名称数组。省略或为空时，注册所有内置工具。用于公开一个较小的子集（例如只读资源清单服务器）。|
|`enabled_prompts`|提示词名称数组。语义与 `enabled_tools` 相同，但针对所公开的提示词。|

<br/>

示例，一个仅发布 `server_info` 和 `list_providers` 的服务器：

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### 协议修订版支持 {#protocol-revision-support}

协议修订版 `2026-07-28`、`server.stateless` 和 `otel` 审计日志格式自 StackQL 发布版本 `v0.11.660` 起可用。`server.protocol_version` 和 `--mcp.protocol.version` 标志自 StackQL 发布版本 `v0.12.732` 起可用。

服务器支持 [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) 所支持的 Model Context Protocol 的每个修订版，并按客户端分别协商，因此由不同客户端混合组成的一组客户端可以共用一个服务器。

| 修订版 | 生命周期 | 审批请求（`safe` / `delete_safe`） |
|--|--|--|
|`2026-07-28`（已通告）|无握手：先是 `server/discover`，之后每个请求都在 `_meta` 中携带 `io.modelcontextprotocol/protocolVersion` 和 `io.modelcontextprotocol/clientCapabilities`（在 HTTP 上还带有 `Mcp-Protocol-Version`、`Mcp-Method` 和 `Mcp-Name` 请求头）。|`input_required` 结果，由客户端携带 `inputResponses` 重试（SEP-2322）。|
|`2025-11-25`、`2025-06-18`|`initialize` / `notifications/initialized` 握手。|服务器发起的 `elicitation/create` 请求。|
|`2025-03-26`、`2024-11-05`|同上。|同上。|

<br />

**stdio** 在一个进程上提供所有修订版：`2026-07-28` 客户端的第一个请求无需握手即可得到处理，较旧客户端的 `initialize` 也仍然有效。Claude Desktop、npm / PyPI 启动器和 Docker 镜像都无需任何配置。

**Streamable HTTP** 默认采用有状态、每个客户端一个会话的模型（`Mcp-Session-Id`），SDK 以该模型提供 `2025-11-25` 及之前的修订版。`2026-07-28` 请求会收到 JSON-RPC 错误 `-32022`（不支持的协议版本；在 `v0.12.718` 及之前的发布版本中是普通的 HTTP 400），其 `data.supported` 列出握手修订版，`server/discover` 也通告相同的内容，因此客户端会向下协商，现有的 HTTP 集成保持其会话和审批请求不变。设置 `server.stateless`（或将修订版固定为 `2026-07-28`，[见下文](#pinning-the-revision)）即可通过 HTTP 原生提供 `2026-07-28`：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

无会话服务器不签发 `Mcp-Session-Id`，对 `GET` 和 `DELETE` 返回 405（没有可拆除的会话），使 `tools/list`、`prompts/list` 和 `resources/list` 保持与连接无关，并通过 `input_required` 完成审批往返。它仍然接受较旧客户端的 `initialize` 并为其提供读取，但无法保留该客户端在初始化时声明的征询（elicitation）能力（每个请求都得到一个临时会话），因此较旧的客户端无法在无会话服务器上批准受门控的写入。对于使用当前修订版的宿主应用选择 `stateless`，对于仍包含较旧客户端的一组客户端则选择默认设置。

服务器不保存跨调用的状态：模式、审计和 Provider 凭据都是进程级配置，因此没有任何内容需要移到显式句柄之后。

#### 固定修订版 {#pinning-the-revision}

默认情况下，服务器通告所有受支持的修订版，并按客户端分别协商。`--mcp.protocol.version` 标志或配置对象中的 `server.protocol_version`（以标志为准）设置所通告的最新修订版：

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| 值 | 通告内容 | 效果 |
|--|--|--|
|`auto`（默认）或未设置|所有受支持的修订版。|按上文所述协商：双方都支持的最高修订版。|
|`2026-07-28`|仅 `2026-07-28`。|仅无会话，并且对于 Streamable HTTP 隐含 `stateless`。较旧客户端的 `initialize` 会得到 `2025-11-25` 作为应答，这是让该客户端断开连接的信号，而不是让它把该应答当作新的生命周期来解读。|
|`2025-11-25`（或任何更旧的修订版）|该修订版及其之前的所有修订版。|仅握手生命周期：`2026-07-28` 请求会收到 JSON-RPC 错误 `-32022`，其 `data.supported` 列出所通告的修订版，因此客户端可以重新协商。较旧的握手客户端保持各自的修订版。|

<br />

任何其他值都会在启动时导致配置校验失败，错误信息会列出合法的值。

#### 传输方式限制 {#transport-limits}

Streamable HTTP 请求体的上限为 4 MiB（超出则返回 HTTP 413），嵌套超过 1000 层的 JSON 在解析前即被拒绝，单个 `stdio` 帧的上限为 8 MiB。这些限制均不可配置；它们都远高于服务器接受的任何 SQL 语句。

* * *

### 凭据（重新）加载（`--env.file` + `reload_credentials`） {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` 和 `reload_credentials` 工具在 `v0.10.542` 之后的 StackQL 发布版本中可用。

:::

进程的环境块在进程创建时即固定下来。这给 MCP 服务器带来 2 个问题：

- 由桌面客户端（Claude Desktop、Cursor 等）在没有凭据环境变量的情况下启动的 MCP `stdio` 服务器永远看不到这些变量，因为客户端会将自己的环境传给子进程。
- 轮换后的短期凭据永远到不了长时间运行的服务器会话。

`--env.file` 标志通过指定一个 dotenv 风格的文件作为可变的凭据存储，弥补了这两个缺口：

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### 加载语义 {#sourcing-semantics}

对于每个入口点（`exec`、`shell`、`srv`、`registry`、`mcp`），该文件都会在启动时加载到进程环境中：

- 标志为空或未设置时，不执行任何操作，凭据在请求时从进程环境中延迟解析，与以往的行为完全一致。
- 给定路径上的文件不存在时，视为可容忍的空操作（该文件可能稍后创建）。
- 格式错误或不可读的文件会在启动时导致致命错误。
- 只设置值非空的键；已有的进程环境变量**会**被覆盖（以该文件为准）；任何变量都不会被取消设置。
- 该标志仅限命令行使用，不会从 `.stackqlrc` 中读取。

文件格式为每行一个 `KEY=VALUE`。`#` 注释、空行、可选的 `export ` 前缀、值两侧可选的单引号或双引号以及 CRLF 行尾均可接受：

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

[`reload_credentials`](/mcp/reload_credentials) 工具在会话中途将 `--env.file` 文件重新加载到进程环境中，然后报告每个 Provider 的凭据解析状态。机密值绝不会被返回、记录或审计，只有名称和状态。未配置 `--env.file` 时，该工具退化为单纯的状态探测。

当查询因凭据解析而失败时，MCP 错误会附带一条提示信息，指引智能体调用 `reload_credentials` 并重试，因此智能体客户端可以自愈：将凭据写入该文件，让智能体重新加载，失败的查询在重试时即可成功，无需重启服务器。

`reverse_proxy` 后端模式不支持 `reload_credentials`：查询在远程 stackql 服务器进程中执行，因此该工具会返回错误，指引用户在后端服务器上重新加载。

#### Windows 与 Claude Desktop {#windows-and-claude-desktop}

`setx` 写入的是注册表，而不是正在运行的进程，并且 Claude Desktop 会将自己的（过时的）环境传给 MCP 子进程，因此凭据更改通常需要完全重启 Claude Desktop。使用 `--env.file` 时，凭据文件可以随时创建或轮换，`reload_credentials` 无需任何重启即可读取到它。完整的配置示例参阅[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop)。

* * *

### HTTP 客户端身份验证 {#http-client-authentication}

:::note

`server.auth_token_env_var` 和 `server.allow_unauthenticated` 自 StackQL 发布版本 `v0.12.742` 起可用。

:::

HTTP 传输方式要求客户端身份验证。`server.auth_token_env_var` 指定一个保存预共享 bearer 令牌的环境变量；每个请求都必须携带 `Authorization: Bearer <token>`，否则会收到 `401` 应答。令牌从环境中读取，因此绝不会出现在 `--mcp.config` 或进程参数中。

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[需要服务器身份验证]

不使用令牌提供服务需要显式选择启用：在 `server` 中设置 `"allow_unauthenticated": true`。两个键都未设置时，服务器会在启动时退出，并给出一条列明这两个键的消息；如果令牌变量未设置或为空，服务器同样会退出。自发布版本 `v0.12.742` 起，使用 `http` 传输方式时，必须在 `server` 中添加 `auth_token_env_var` 或 `"allow_unauthenticated": true`。

:::

TLS（`tls_cert_file` / `tls_key_file`）对传输进行加密，但不对客户端进行身份验证；对于回环地址以外的任何监听器，应将其与令牌结合使用。跨源的浏览器请求（`Origin` 不匹配，或 `Sec-Fetch-Site` 为跨站）无论是否通过身份验证，都会以 `403` 拒绝。随附的 `stackql_mcp_client` 不发送 `Authorization` 请求头，因此由它驱动的服务器必须选择启用未经身份验证的服务。

自 `v1.0` 起，使用 [`--access.config`](/command-line-usage/access-management) 启动服务器时，共享令牌门控会被按调用方进行的身份验证取代：每个 HTTPS 请求都通过配置的 JWT、静态 bearer 或双向 TLS 方案进行验证，并依据策略规则进行授权。这种情况下服务器 TLS 是必需的，通过 HTTP 进行的 `local_os` 身份验证会被拒绝，不带凭据的请求绝不会回退到服务器的环境令牌。`reverse_proxy` 服务器类型不支持访问管理。

* * *

### 服务器模式 {#server-modes}

`server.mode` 从 4 种安全契约中选择一种。这 4 种都允许 `SELECT` 和元数据读取；区别在于它们如何处理变更和生命周期操作。

| 模式 | `SELECT` / 元数据 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC`（生命周期） |
|--|--|--|--|--|
|`read_only`|允许|拒绝|拒绝|拒绝|
|`safe`（默认）|允许|需要审批|需要审批|需要审批|
|`delete_safe`|允许|允许|需要审批|需要审批|
|`full_access`|允许|允许|允许|允许|

<br />

**拒绝**会立即向客户端返回错误。

**需要审批**使用 MCP 征询流程，其形式取决于协商得到的[协议修订版](#protocol-revision-support)：

- 如果客户端通告了征询能力（在初始化时，或在 `2026-07-28` 上通过每个请求的 `_meta` 客户端能力），服务器会用一条简短的消息（工具名称、查询类别、SQL）请用户批准该操作。在 `2026-07-28` 上，该提示以 `input_required` 结果的形式返回，其中在 id `stackql_approval` 下携带一个 `elicitation/create` 输入请求，客户端在 `inputResponses` 中带上答复后重试该调用；在更早的修订版上，服务器在调用过程中自行发送 `elicitation/create` 请求。用户可以接受、拒绝或取消。
- 如果客户端**未**通告征询能力，该工具会被拒绝，并附带一条消息说明这一缺口，同时指引运维人员使用 `full_access` 模式。

审批由客户端应答，因此它防范的是智能体在没有用户参与的情况下行动，而不是不受信任的调用方。在调用方不受信任的场合，应将 HTTP 传输方式与[客户端身份验证](#http-client-authentication)搭配使用。

模式对每个服务器全局生效。不存在按工具的覆盖设置。

自 `v1.0` 起，[`run_select_query`](/mcp/run_select_query) 在所有模式下（包括 `full_access`）都拒绝 `INSERT`、`UPDATE`、`REPLACE`、`DELETE` 和 `EXEC`；错误信息会指明 `run_mutation_query` 或 `run_lifecycle_operation`。每个工具还带有一个 `title`，并以显式布尔值的形式带有 4 个 MCP 行为提示（`readOnlyHint`、`destructiveHint`、`idempotentHint`、`openWorldHint`），它们源自策略门控所执行的同一套分类。对于会访问注册表或 Provider 端点的 `list_registry`、`pull_provider`、`run_select_query`、`run_mutation_query` 和 `run_lifecycle_operation`，`openWorldHint` 为 `true`；对于只读取本地状态的工具则为 `false`。这些提示仅供客户端 UX 参考；强制执行仍由策略门控负责。

:::info[负载门控]

自发布版本 `v0.12.742` 起，在所有模式下，包含多条语句的负载由其中权限最高的语句门控，因此 `SELECT 1; DELETE ...` 被视为 `DELETE`，审计记录中的 `query_class` 为 `mutation_delete`。`validate_select_query` 只接受恰好一条语句。

:::

#### 默认模式的行为变化 {#default-mode-behaviour-change}

之前的发布版本仅通过单个 `read_only: true/false` 标志来实施安全控制，其默认值为“不强制”。当前的默认值（`mode: safe`）使变更开箱即需要用户审批。使用支持征询的客户端（Claude Desktop、Cursor、Continue 等）的运维人员，每次变更都会看到一次审批请求。运行自动化流程或随附的 `stackql_mcp_client`（它不通告征询能力）的运维人员，必须显式选择启用 `mode: full_access` 才能运行变更和生命周期操作。

为保持向后兼容，旧版的 `read_only: true` JSON / YAML 键仍被接受，并被视为等同于 `mode: read_only`。

#### 示例 {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### 审计日志 {#audit-log}

每次工具调用都会向配置的审计接收器写入一条记录，默认为 JSONL 格式，也可以是 OpenTelemetry 日志记录（参阅[日志格式](#log-format)）。审计**默认开启**。审计回答的是“智能体做了什么”，而不是“智能体看到了什么”：无论哪种格式，`SELECT` 语句的结果行都有意不被记录。

#### 记录的内容 {#what-gets-recorded}

| 字段 | 说明 |
|--|--|
|`timestamp`|调用开始时的挂钟时间（RFC3339）。|
|`tool`|工具名称（例如 `run_select_query`）。|
|`mode`|调用时生效的服务器模式。|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`。对于包含多条语句的负载，取其中权限最高的语句的类别。|
|`sql`|查询工具（`run_select_query`、`run_mutation_query`、`run_lifecycle_operation`、`validate_select_query`）的 SQL 字符串。|
|`args`|元数据工具（`list_*`、`describe_method`）的层次结构字段；查询工具则为 SQL + `row_limit`。|
|`duration_ms`|门控 + 处理程序的挂钟耗时。|
|`error`|工具出错或被拒绝时的错误信息。|

<br />

`SELECT` 语句的结果行被有意排除在外，因为它们可能非常大，并且可能包含敏感数据。

#### 文件接收器 {#file-sink}

目前随附的唯一接收器是 `file`。每行一个 JSON 对象，每条记录之后执行 fsync，并按大小 / 保留时间 / 备份数量进行 lumberjack 风格的轮转。

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

指定位置有 2 种方式：

- `audit.file.path`：完整的文件路径（绝对路径，或相对于 cwd 的路径）。
- `audit.file.dir`：一个目录；接收器在其中选用基本名称 `stackql_mcp_server_<RFC3339-utc-second>.log`。

如果 `mcp.config` 中两者都未设置，MCP 服务器会将 `dir` 默认设为 cwd，因此现有的运维人员看到的行为与以前相同。底层的通用接收器包本身拒绝静默地选择目录：“日志写到哪里”始终由调用方决定。

解析后的绝对路径会在启动时以 `sink file: /path/to/file.log` 的形式记录到 stderr。

#### 日志格式 {#log-format}

审计流有 2 种编码，写入同一个文件接收器：

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

该标志会覆盖 `mcp.config` 中的 `server.audit.format`（`"audit": {"format": "otel"}`）。`jsonl` 与上文所述的记录在字节层面兼容。`otel` 每行写入一个 OTLP/JSON `LogsData` 对象，其结构可被 OpenTelemetry Collector 的 `otlp_json_file` receiver 原样摄取（已针对 otelcol-contrib 0.160.0 验证），因此该流无需 transform processor 即可进入任何 OTLP 管道：

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

每次工具调用产生一条日志记录；经过审批门控的调用会为征询决定产生第二条记录。同一次调用的记录共享 `gen_ai.tool.call.id` 和一个 trace id。调用方在 `params._meta` 中提供的 W3C `traceparent` 会被采用；否则每个 MCP 会话生成一个 trace id，使同一智能体会话的记录能够相互关联。

所输出的属性集是一个带版本的接口：插桩作用域为 `github.com/stackql/stackql/pkg/mcp_server/audit`，版本为 `1.0.0`，`schemaUrl` 将稳定的约定固定为 `https://opentelemetry.io/schemas/1.44.0`。`gen_ai.*` 和 `mcp.*` 属性遵循 [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) 和 [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md) 语义约定，这些约定处于 Development 状态；属性集发生变化时，作用域版本会递增。

| 属性 | 记录 | 值 |
|--|--|--|
|`service.name`、`service.version`|资源|`stackql`、StackQL 版本|
|`gen_ai.operation.name`|工具调用|`execute_tool`|
|`mcp.method.name`|两者|工具调用记录上为 `tools/call`，决定记录上为 `elicitation/create`|
|`gen_ai.tool.name`|两者|工具名称|
|`gen_ai.tool.call.id`|两者|每次调用生成一个，由两条记录共享|
|`mcp.protocol.version`|两者|协商得到的修订版|
|`mcp.session.id`|两者|会话 id（无会话 HTTP 请求没有此属性）|
|`stackql.mode`|两者|服务器模式|
|`stackql.decision`|两者|上表中的 `decision` 值|
|`stackql.query`|工具调用|原样的 SQL，适用于查询工具|
|`stackql.query_class`|工具调用|`query_class` 值|
|`stackql.provider`|工具调用|Provider，适用于元数据工具|
|`stackql.query.source`|工具调用|Query Library 条目 id，提供时才有|
|`stackql.duration_ms`|工具调用|挂钟耗时|
|`stackql.rows_returned`|工具调用|行数，适用于返回行的工具|
|`error.type`、`error.message`|工具调用|失败时：表示拒绝的决定，或在通过门控之后为 `tool_error`|

<br />

正文为 `execute_tool <tool>` / `elicitation <tool>`；严重级别为 `INFO`，调用失败或被拒绝时为 `ERROR`。两种格式的脱敏契约完全相同：结果值绝不会被序列化，只序列化产生这些结果的语句。

#### 失败模式 {#failure-modes}

接收器返回错误时，响应行为取决于 `audit.failure_mode`。

| failure_mode | 效果 |
|--|--|
|`strict`（默认）|即使底层工具已成功，工具调用也会向客户端返回审计错误。这是有意为之：宁可让客户端收到含义不明的响应，也不要出现未被发现的 `DELETE`。|
|`strict_mutations`|`SELECT` / 元数据读取继续执行，并在 stderr 中留下一条说明；变更和生命周期操作则返回审计错误。|
|`best_effort`|始终记录到 stderr 并继续执行。|

#### 时序 {#sequencing}

审计写入发生在工具执行（或被门控拦截）**之后**、响应返回客户端**之前**。在 strict 模式下，如果一次成功的 DELETE 之后审计写入失败，则该行已被删除，但客户端收到的是错误。这是有意设计的，以确保没有任何变更在未经审计的情况下漏过。

#### 禁用审计 {#disabling-audit}

完全关闭审计（PR2 之前的行为）：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### 可用的 MCP 工具 {#available-mcp-tools}

作为 MCP 服务器运行时，StackQL 公开以下工具，具体取决于配置的 `enabled_tools` 允许列表。每个工具都同时返回渲染后的文本视图（供 LLM 使用）和带类型的结构化负载（供程序化客户端使用）。默认的文本渲染方式为：结构一致的多行结果使用 markdown 表格，稀疏结果或单条记录结果使用 markdown KV 块。带参数的工具接受可选的 `format`（`markdown` 或 `json`）覆盖项；否则使用服务器的渲染设置。下面的输入列出了与任务相关的参数；`?` 标记可选输入。

点击任意工具名称可查看完整的参考页面，其中包括输入、门控行为和示例提示词。

| 工具 | 渲染器 | 说明 | 输入 |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|服务器标识和运行时：stackql 版本、后端 SQL 引擎、Provider 注册表位置、模式、只读标志。在会话开始时调用一次。|无|
|[`list_providers`](/mcp/list_providers)|表格|已拉取到本地缓存的 Provider，即层次结构的顶层。|无|
|[`list_services`](/mcp/list_services)|表格|某个 Provider 下的服务。|`provider`|
|[`list_resources`](/mcp/list_resources)|表格|某个 `provider`.`service` 下的资源。|`provider`、`service`|
|[`list_methods`](/mcp/list_methods)|表格|某个资源的访问方法（HTTP 操作），以及每个方法映射到的 SQL 动词及其必需参数。在编写任何查询之前调用，必需的 `WHERE` 参数正是由此推断出来的。|`provider`、`service`、`resource`|
|[`describe_method`](/mcp/describe_method)|KV|单个方法的完整 I/O 契约（始终为 EXTENDED）：必需和可选的输入，以及 `SELECT` 可以引用的输出字段。|`provider`、`service`、`resource`、`method`|
|[`query_library_search`](/mcp/query_library_search)|表格|按自然语言意图查找精选的查询模板。只读；变更条目需要显式选择启用，并且在 `read_only` 模式下被排除。|`intent`、`provider?`、`service?`、`tags?`、`include_mutations?`、`limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|获取一个模板及其参数声明。`params` 非空时，校验参数值并返回渲染后的 SQL 以及 `next_tool`，但不执行。|`id`、`params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|解析并规划单条 `SELECT`，但不执行。返回 `{valid, errors}`。仅限 `SELECT`；恰好一条语句。|`sql`|
|[`run_select_query`](/mcp/run_select_query)|表格|执行 `SELECT`。返回 `{rows}`。仅读取。|`sql`、`row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|对 Provider 执行 `INSERT`/`UPDATE`/`REPLACE`/`DELETE`。**会产生真实的副作用**。返回 `{messages, timestamp}`。由服务器[模式](#server-modes)门控。|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|执行 stackql `EXEC` 生命周期操作。返回 `{messages, timestamp}`。由服务器[模式](#server-modes)门控。|`sql`|
|[`list_registry`](/mcp/list_registry)|表格|配置的注册表中可用的 Provider（及其版本）。与 `list_providers` 不同，后者只列出已拉取的 Provider。|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|将单个 Provider 从注册表安装到本地缓存。仅影响本地缓存状态，对云的控制平面或数据平面没有影响。|`provider`、`version?`|
|[`reload_credentials`](/mcp/reload_credentials)|表格|将 `--env.file` dotenv 文件中的凭据重新加载到进程环境中，并报告每个 Provider 的解析状态（`ok`、`unresolved`、`not_checked`）。绝不返回机密值。在所有模式下均允许。|`provider?`|

从头编写 SQL 之前，先查阅 `query_library_search`，然后使用 `query_library_get` 查看并渲染匹配的模板。查询工具还接受可选的 `source`，其中包含库条目 ID，仅用于标明来源；执行仍受常规的服务器模式和审批策略约束。

:::note

`describe_resource` 已在 StackQL `v0.12.732` 中停用；调用过它的 MCP 客户端、提示词或智能体指令应改为先调用 [`list_methods`](/mcp/list_methods)，再调用 `describe_method`。资源没有单一的字段列表，因为每个访问方法返回各自的结构，所以列名来自查询所路由到的方法的 [`describe_method`](/mcp/describe_method)。SQL [`DESCRIBE`](/language-spec/describe) 语句保持不变。

:::

### 可用的 MCP 提示词 {#available-mcp-prompts}

发布了 1 个静态提示词。

| 提示词 | 说明 |
|--|--|
|`write_safe_select`|针对 stackql 资源编写安全 `SELECT` 查询的指导。说明如何使用 `SHOW METHODS IN <provider>.<service>.<resource>` 找到合适的读取方法和必需的 `WHERE` 参数。|

* * *

### 标志 {#flags}

| 标志 | 说明 |
|--|--|
|`--mcp.server.type`|MCP 服务器类型：`http`、`stdio` 或 `reverse_proxy`（后者与 `stackql srv` 搭配使用）。|
|`--mcp.config`|MCP 服务器的 JSON 配置对象。也接受 YAML。|
|`--mcp.protocol.version`|通告的最新协议修订版：`auto`（默认）、`2026-07-28` 或较旧的修订版，例如 `2025-11-25`。参阅[固定修订版](#pinning-the-revision)。|
|`--env.file`|dotenv 风格的凭据文件，在启动时加载，并由 `reload_credentials` 工具按需重新加载。参阅[凭据（重新）加载](#credential-resourcing---envfile--reload_credentials)。|
|`--pgsrv.port`|PostgreSQL 线协议服务器的 TCP 端口（与 `stackql srv` 搭配使用）。|
|`-H`、`--help`|打印帮助信息。|
|`-v`、`--verbose`|以详细模式运行，输出更多信息。|

&nbsp;
&nbsp;
> 更多选项参阅[全局标志](/command-line-usage/global-flags)

:::info

启动 MCP 服务器之前，需要设置 Provider 身份验证所需的环境变量，或者使用 [`--env.file`](#credential-resourcing---envfile--reload_credentials) 指定一个 dotenv 风格的凭据文件。更多信息参阅[使用 Provider](/getting-started/using-a-provider)。

:::

* * *

### 示例 {#examples-1}

#### 基本的独立 MCP 服务器 {#basic-standalone-mcp-server}

启动一个独立 MCP 服务器，配置 Provider 身份验证以及供其客户端使用的 bearer 令牌：

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### MCP 服务器与 PostgreSQL 服务器（内存内） {#mcp-server-with-postgresql-server-in-memory}

同时运行 MCP 服务器和 PostgreSQL 服务器，以支持双协议访问：

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 将审计写入已知路径的只读资源清单服务器 {#read-only-inventory-server-with-audit-to-a-known-path}

适用于只扫描资源清单、绝不应执行变更的智能体：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 完全访问的自动化服务器 {#full-access-automation-server}

适用于需要执行变更且无法响应征询请求的受信任自动化流水线。流水线出示保存在 `STACKQL_MCP_TOKEN` 中的 bearer 令牌：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### 使用 TLS 的安全 MCP 服务器 {#secure-mcp-server-with-tls}

以反向代理模式启动带 TLS 加密的 MCP 服务器：

首先，生成 TLS 证书（开发环境使用自签名证书）：

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

然后启动服务器：

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### 测试 MCP 服务器 {#testing-your-mcp-server}

仓库中附带一个用于脚本编写和回归测试的开发用 MCP 客户端（`stackql_mcp_client`）。使用以下命令构建：

```bash
python cicd/python/build.py --build-mcp-client
```

这会生成 `./build/stackql_mcp_client`。

请注意，`stackql_mcp_client` 不发送 `Authorization` 请求头，因此它所连接的服务器必须设置 `"allow_unauthenticated": true`（如上面的示例所示）；受令牌保护的服务器会以 `401` 应答。它也**不**通告 MCP 征询能力，因为它用于非交互式脚本。面对 `safe` 或 `delete_safe` 服务器时，它在变更和生命周期调用上会收到“client does not support elicitation”拒绝。如需进行带征询请求的交互式测试，请使用嵌入编辑器的 MCP 客户端（Claude Desktop、Cursor、Continue）。

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation - succeeds only against a full_access server, or against
# delete_safe (for INSERT/UPDATE) or safe with an elicitation-capable client.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### 示例响应 {#example-responses}

`server_info`（针对默认模式的服务器）：

```json
{
  "version": "0.10.444",
  "commit": "abc1234",
  "build_date": "2026-05-16T10:47:33Z",
  "platform": "linux/amd64",
  "transport": "http",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.io/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query`（针对 `full_access` 服务器）：

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

同一变更的审计日志行示例：

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### 与 AI 助手集成 {#integration-with-ai-assistants}

要将 StackQL 的 MCP 服务器与 AI 助手集成，请在该助手的配置中将 `stackql` 注册为 MCP 服务器。大多数嵌入编辑器的 MCP 客户端通过 `stdio` 运行服务器；对于这些客户端，使用 `--mcp.server.type=stdio`，由助手直接启动进程。使用 HTTP 的独立智能体可以连接到长时间运行的 `stackql mcp --mcp.server.type=http` 进程。

关于 Claude Desktop，包括 Anthropic Connector Directory 中的条目（推荐的安装方式）、可下载的 MCP Bundle（`.mcpb`）以及手动配置，参阅[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop)。

:::note

配置文件的位置因操作系统和 AI 助手而异。正确的路径和格式请查阅所用 AI 助手的文档。

:::

* * *

### 架构注意事项 {#architecture-considerations}

选择部署模式时，请考虑：

1. **独立（`stackql mcp`）**：配置最简单，非常适合开发以及通过 `stdio` 连接的嵌入编辑器的客户端。
2. **内存内双服务器（`stackql srv` 搭配 `mcp.server.type=http`）**：单个进程同时提供 MCP 和 PostgreSQL 线协议。性能最佳，适合大多数生产部署。
3. **反向代理（`stackql srv` 搭配 `mcp.server.type=reverse_proxy`）**：
   - 支持跨进程或主机分离工作负载
   - 支持 TLS 加密，以提供安全的 MCP 端点
   - 允许 MCP 和 PostgreSQL 接口独立扩缩容
   - 为企业 CA 集成提供灵活性（注意：企业 CA 支持是实验性的）

另外，在安全方面：

- 从默认的 `mode: safe` 开始。支持征询的客户端（Claude Desktop、Cursor、Continue）会在任何变更之前向用户发出提示。
- 对于绝不应写入的资源清单或分析类智能体，固定使用 `mode: read_only`。
- 如果希望可以自由创建/更新，但破坏性操作需要有人工参与，则固定使用 `mode: delete_safe`。
- `mode: full_access` 只保留给受信任的自动化流水线，并且只与有人审查的审计日志配合使用。
- 对于其他进程或主机可以访问到的任何 HTTP 监听器，都应设置 `server.auth_token_env_var`；`allow_unauthenticated` 用于本地开发。仅靠 TLS 并不能对客户端进行身份验证。

:::caution

将 TLS 与企业证书颁发机构（CA）一起使用时，可能需要额外的配置。此功能是实验性的，可能需要根据具体的 CA 实现进行调整。

:::

* * *

### 延伸阅读 {#further-reading}

- 面向包开发者的参考文档是 stackql 仓库中的 [`pkg/mcp_server` README](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md)。
- 面向贡献者的技术文档位于 stackql 仓库的 [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md)，其中包含每个工具的完整示例响应。
