---
title: 访问管理
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 访问管理
  - 身份验证
  - 授权
  - jwt
  - mtls
  - bearer 令牌
description: "StackQL 的可选调用方访问管理：通过 JWT、bearer 令牌、双向 TLS 或本地 OS 身份对调用方进行身份验证，在目录中将其映射到主体和组，并用策略规则对语句授权。"
image: "/img/stackql-featured-image.png"
---

调用方访问管理对 StackQL 会话的调用方进行身份验证，并依据策略对每条语句进行授权。它独立于 Provider 凭据，后者仍通过 `--auth`、环境变量或 [Provider 会话](/getting-started/provider-sessions)提供。该功能默认禁用，未启用它的部署行为与以前相同。在 StackQL `v1.0` 及更高版本中可用。

另请参阅：  
[[ 全局标志 ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## 启用访问管理 {#enabling-access-management}

启动 StackQL 时让全局标志 `--access.config` 指向一个 JSON 配置文件，即可开启访问管理。下面的示例以受保护模式启动交互式 shell；`exec` 和 `mcp` 的启用方式相同。

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` 接受一个只含 `path` 键的 JSON 封装对象；也接受直接传入文件路径。配置只在启动时加载一次，因此轮换密钥或策略需通过受控重启完成。

访问管理启用期间，只有 `exec`、`shell`、`mcp` 和 `version` 可以运行；其他命令，包括 `registry`，会拒绝执行，而不是绕过受保护路径。`srv` 和 MCP 的 `reverse_proxy` 服务器类型同样拒绝执行，因为 PostgreSQL 线协议依赖尚未提供每个连接的身份验证证据。SQL 语句 `AUTH` 和 `AUTH REVOKE` 在受保护模式下拒绝执行；请改为通过主机配置提供 Provider 凭据。受保护的 `exec` 失败时，会在显示错误后返回退出状态 `1`。

## 身份验证模式 {#authentication-modes}

配置文件选择一种 `type`：`jwt`（省略时的默认值）、`bearer`、`mtls` 或 `local_os`。配置是严格的 JSON：未知字段以及属于其他模式的字段都会被拒绝。所有模式共用相同的 `rules` 和相同的[主体目录](#principal-directory)。

### JWT {#jwt}

调用方出示一个已签名的令牌，该令牌依据配置中固定的密钥进行离线验证。最小配置指定令牌必须携带的签发者和受众，列出一个公钥，并授予 `readers` 组读取权限：

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

签发者、受众和公钥都固定在配置中；不进行 OIDC 发现。接受的算法为 `EdDSA`、`RS256`、`RS384`、`RS512`、`PS256`、`PS384`、`PS512`、`ES256`、`ES384` 和 `ES512`，RSA 密钥至少为 2048 位。每个密钥都绑定到为其配置的算法。密钥 ID 未知、签名无效、过期时间缺失或已过、`nbf` 或 `iat` 在未来，或者签发者或受众错误的令牌都会被拒绝。令牌中携带的组声明和 URL 不用于信任判定或目录成员身份。

### 静态 bearer 令牌 {#static-bearer-token}

调用方出示一个不透明令牌，配置中列出每个可接受令牌的 SHA-256 摘要及其标识的主题（subject）。这适用于数量少且固定的一组服务身份，在这种情况下没有必要运行令牌签发者：

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

配置中只包含每个令牌的 SHA-256 摘要，且摘要必须唯一。更改已配置的摘要需要重启。

### 双向 TLS {#mutual-tls}

调用方由其在 HTTPS MCP 传输方式上出示的客户端证书标识。配置中包含必须是该证书签发者的 CA，并指明身份取自哪一种主题备用名称：

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

客户端证书链依据 `client_ca_pem` 进行验证，身份主题取自所配置类型的恰好一个 SAN，类型为 `uri_san` 或 `dns_san`。不会回退到 Common Name。双向 TLS 仅在 HTTPS MCP 传输方式上可用。

### 本地 OS 身份 {#local-os-identity}

不交换任何凭据：主题就是 StackQL 进程运行时所用的操作系统身份。配置只需要一个 `issuer` 标签和规则：

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

身份主题是 StackQL 进程的有效 Unix UID 或 Windows SID，包括没有 passwd 条目的数字容器 UID。不出示任何凭据，`STACKQL_CALLER_TOKEN` 会被忽略。此模式仅限本地：对 HTTP 调用方会拒绝。操作系统的组成员身份不授予访问权限；只有目录成员身份才授予。

### 会话截止时间 {#session-deadline}

默认情况下，查询没有由身份验证派生的截止时间。每条新语句都会验证凭据并刷新目录成员身份，因此过期的凭据无法开始新的工作，但过期不会中断已获授权的语句。设置 `"enforce_session_deadline": true` 可在达到 `max_session_seconds`（默认 `300`，最大 `86400`）时取消，JWT 或证书过期会使该时间提前。这不会续期令牌。

## 主体目录 {#principal-directory}

身份到主体、主体到组的映射保存在所配置的 SQLite 或 PostgreSQL 后端的 4 张表中：

| 表 | 内容 |
|--|--|
| `stackql_access_principals` | 主体 ID 和一个由字符串属性组成的 JSON 对象。`tenant` 属性为预览版 IaC 状态划分命名空间。 |
| `stackql_access_identities` | 已验证的 `(issuer, subject)` 对到主体 ID 的映射。 |
| `stackql_access_groups` | 组 ID。 |
| `stackql_access_memberships` | 主体到组的绑定。 |

在提供受保护的查询之前，通过受信任的数据库管理连接预先创建这些表、身份和成员身份：

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

使用该模式实际的签发者和主题：对于本地 OS 身份，可能是 `('this-host', '1000')`，对于双向 TLS，可能是 `('client-certificates', 'spiffe://example/alice')`。显式选择后端：

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

目录为每条语句生成一个不可变快照。撤销就是在管理连接上执行一条 `DELETE FROM stackql_access_memberships`；下一条语句会看到新的快照。未知身份、schema 缺失、数据格式错误和目录不可用都会导致拒绝访问。普通的受保护 SQL 无法访问目录表。成员身份的写入应只在管理连接上进行，应保护好数据库文件，并在后端支持时使用相互独立的管理数据库权限和运行时数据库权限。

## 策略规则 {#policy-rules}

每条规则将一个 `resource` 上的一个 `action` 授予形如 `group:<group id>` 的 `subject`。使用来自受信任 Provider 文档的已编译方法和操作标识符；操作通配符既授予读取，也授予变更方法。

`readers` 组隐式拥有主机的安全读取 SQL 能力。该组之外的主体执行读取需要 `host/sql/read`，其他语句需要对 `host/sql/<statement kind>` 拥有 `execute`。Provider 操作和凭据使用始终需要显式规则，对集合的授权不会授予其底层方法。其他主机检查：

| 资源 | 动作 | 适用范围 |
|--|--|--|
| `host/sql/read` | `execute` | 隐式读取者组之外的读取语句 |
| `host/sql/exec` | `execute` | 原生 `EXEC`，包括嵌套的 `EXEC` |
| `host/iac/run`, `host/iac/destroy` | `execute` | 预览版 IaC 关系，尽管它们使用 `SELECT` 语法 |
| `host/function/<name>` | `execute` | 已知纯函数的小型允许列表之外的函数 |
| `host/backend` | `read` | 不透明的已存储关系和外部后端关系 |
| `host/credentials` | `reload` | MCP `reload_credentials` 工具 |
| `session/variables` | `read` | 解引用[会话变量](/getting-started/session-variables)和机密 |
| `session/config` | `write` | `SET` 和 `UNSET` |

存储查询的展开会在执行前接受检查，受保护路径上禁用计划缓存和行缓存的复用。

## 入口点 {#entry-points}

| 身份验证 | `exec`、`shell`、MCP stdio | HTTPS MCP |
|--|--|--|
| JWT | `STACKQL_CALLER_TOKEN` 环境变量 | `Authorization: Bearer` 请求头 |
| 静态 bearer | `STACKQL_CALLER_TOKEN` 环境变量 | `Authorization: Bearer` 请求头 |
| 本地 OS | 进程的有效 UID 或 SID；无凭据 | 拒绝 |
| 双向 TLS | 拒绝：没有经过验证的 TLS 连接 | 经过验证的客户端证书链和一个已配置的 SAN |

本地令牌凭据应通过进程环境提供，切勿作为 SQL 字面量或命令行参数传入。在 HTTPS 上，调用方验证器取代 [HTTP 客户端身份验证](/command-line-usage/mcp#http-client-authentication)中所述的共享 `auth_token_env_var` bearer 门控；服务器 TLS 证书和密钥配置是必需的，不带凭据的请求绝不会回退到服务器的环境令牌。

## 查看调用方 {#inspecting-the-caller}

在已通过身份验证的会话中，`whoami()` 返回主体 ID，`groups()` 以 JSON 数组形式返回目录组：

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

这两个函数都要求会话以 `--access.config` 启动。

## 演练 {#walkthroughs}

StackQL 仓库附带 [4 个可执行的演练](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md)，每种身份验证模式一个，由一个 Python 脚本驱动，该脚本创建一个演示目录并验证一条查询。这些演练不需要云账号，并由项目的测试套件运行。
