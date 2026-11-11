---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "stackql srv 命令将 StackQL 作为 PostgreSQL 线协议服务器运行，使 psql、BI 工具和语言驱动程序可以用 SQL 查询云和 SaaS API。"
image: "/img/stackql-featured-image.png"
---

用于将 StackQL 作为服务启动的命令，之后客户端可以使用通过 mTLS 进行身份验证的 [Postgres 线协议](https://www.postgresql.org/docs/current/protocol.html)客户端访问该服务。`srv` 命令还可以在 PostgreSQL 线协议服务器之外同时运行一个 [Model Context Protocol（MCP）](/command-line-usage/mcp)服务器，为传统数据库客户端和 AI 智能体提供双协议访问。

* * * 

### 语法 {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

虽然 StackQL 服务器使用 Postgres 线协议，但它不是 Postgres 服务器，也不需要 Postgres 服务器。客户端使用该线协议与服务器通信，服务器使用 StackQL 对远程的云服务商和 SaaS 服务商执行查询或 DML 操作，并将结果返回给客户端。

:::

通过扩展查询协议发送的参数（`Parse` / `Bind` / `Execute`，`psycopg` 3、`pgx` 和 JDBC 使用该协议）在服务器端绑定为字符串字面量。从 `v0.12.742` 起，位于字符串字面量、带引号的标识符或注释中的 `$n` 占位符保持原样，参数值中的反斜杠会被转义，文本值 `NULL` 仍是字符串 `NULL`（SQL `NULL` 单独传递）。

### 服务器选项 {#server-options}

| 选项 | 说明 |
|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|服务器监听的地址（通常为 `0.0.0.0`）|
|<span class="nowrap">`--pgsrv.port`</span>|服务器监听的端口（例如 `5444`）|
|<span class="nowrap">`--pgsrv.tls`</span>|mTLS 配置对象，参阅下面的[示例](#examples)|
|<span class="nowrap">`--pgsrv.loglevel`</span>|日志级别（默认 `WARN`）|

<br/>

> 更多选项参阅[全局标志](/command-line-usage/global-flags)

### MCP 服务器选项 {#mcp-server-options}

运行带 MCP 支持的 `srv` 命令时，可以使用以下附加选项：

| 选项 | 说明 |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|MCP 服务器类型：`http`（内存中）或 `reverse_proxy`（基于 TCP）|
|<span class="nowrap">`--mcp.config`</span>|MCP 服务器的 JSON 配置对象（参阅 [MCP 文档](/command-line-usage/mcp)）|
|<span class="nowrap">`--mcp.log.format`</span>|MCP 审计日志的编码：`jsonl`（默认）或 `otel`（OTLP/JSON 日志记录）；覆盖配置对象中的 `server.audit.format`|
|<span class="nowrap">`--mcp.protocol.version`</span>|对外通告的最新 MCP 协议修订版本：`auto`（默认）、`2026-07-28`（仅无会话）或更早的修订版本，如 `2025-11-25`；覆盖配置对象中的 `server.protocol_version`|

<br/>

:::info[v0.12.742 中的破坏性变更]

从 `v0.12.742` 起，MCP HTTP 传输方式（`--mcp.server.type=http` 或 `reverse_proxy`）会拒绝启动，除非配置对象通过 `server.auth_token_env_var` 指定了 bearer 令牌，或通过 `"allow_unauthenticated": true` 显式选择退出。参阅 [HTTP 客户端身份验证](/command-line-usage/mcp#http-client-authentication)。

:::

:::tip

有关配置和使用 MCP 服务器的详细信息，包括部署模式、配置选项以及与 AI 助手的集成，参阅 [MCP 命令文档](/command-line-usage/mcp)。

:::

:::info

启动服务器之前，你需要设置 Provider 身份验证所需的环境变量，更多信息参阅[使用 Provider](/getting-started/using-a-provider)。

:::

对于长时间运行的服务器，[`--env.file`](/command-line-usage/global-flags) 标志让你可以在启动前把凭据写入磁盘上的 dotenv 风格文件或在其中轮换凭据，而不必将变量导出到 shell 中：

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

对于 `stackql srv`，该文件只在启动时读取一次；会话中途重新加载的途径是 MCP [`reload_credentials`](/mcp/reload_credentials) 工具。

### 标志 {#flags}

| 标志 | 说明 |
|--|--|
|`-H,--help`|打印帮助信息|
|`-v,--verbose`|以详细模式运行查询，并将额外输出发送到 stdout，如果选择了 `-f` 选项，<br/>这些额外的日志信息将与查询结果一起写入输出文件|

<br/>

> 更多选项参阅[全局标志](/command-line-usage/global-flags)

* * *

### 示例 {#examples}

启动 StackQL 服务器进程，并使用 `psql` 命令行客户端进行连接。  

首先从 [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf) 下载 `openssl.cnf` 配置文件。  

接下来生成服务器和客户端进行双向 TLS 身份验证（mTLS）所用的证书和密钥文件。  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

现在设置环境变量，如果客户端和服务器位于同一主机上，可以共用这些变量。  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

为服务器需要访问的 Provider 设置身份验证对象，例如 GitHub。  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

现在在一个终端中启动服务器。  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

在另一个终端中，使用 `psql` 命令行客户端连接到服务器。  

```bash
psql 127.0.0.1
```

现在对已通过身份验证的 Provider 运行 StackQL 查询。  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

127.0.0.1=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

127.0.0.1=>
```
