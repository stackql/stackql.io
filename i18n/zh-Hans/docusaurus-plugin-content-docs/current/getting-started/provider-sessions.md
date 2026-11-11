---
title: Provider 会话与机密
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - Provider 会话
  - 多账号
  - 凭据
  - 机密
  - 钥匙串
description: "StackQL 中的 Provider 会话：绑定带有各自凭据、租户标签和参数默认值的 Provider 别名，按会话覆盖凭据字段，并将机密保存在会话内存或操作系统机密存储中。"
image: "/img/stackql-featured-image.png"
---

另请参阅：  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ 会话变量 ]](/getting-started/session-variables) [[ 全局标志 ]](/command-line-usage/global-flags)

会话可以将 Provider 绑定到一个别名下，该别名拥有自己的凭据、租户标签、参数默认值和请求并发限制，因此可以在一个会话中，并且在一条语句中，查询同一 Provider 的多个账号或租户。还可以在会话范围内覆盖凭据字段；机密可以保存在会话内存中，或在运维人员明确启用后保存在操作系统的机密存储中。以上各项均通过 [`SET`](/language-spec/set) 和 [`UNSET`](/language-spec/unset) 配置，在 StackQL `v1.0` 及更高版本中可用。

此处描述的所有内容都限定在当前会话范围内。到 `stackql srv` 服务器的各个独立连接不共享绑定或机密，而每个 MCP 请求都是一个独立的会话，因此使用 MCP 服务器时，应在同一个语句批次中完成配置和查询。

## Provider 别名 {#provider-aliases}

`SET "$.providers.<alias>"` 将别名绑定到 Provider。其值是保存在字符串中的 JSON 文档：

| 键 | 是否必需 | 说明 |
|--|--|--|
| `provider` | 是 | 别名解析到的规范 Provider，例如 `aws`。 |
| `tenancy` | 是 | 分组标签，例如账号名称或租户名称。StackQL 不会对照 Provider 验证它。 |
| `auth` | `auth` 与 `auth_secret` 二者之一 | 身份验证文档，结构与该 Provider 在 [`--auth`](/command-line-usage/global-flags) 对象中的条目相同。别名不会借用规范 Provider 的凭据。 |
| `auth_secret` | `auth` 与 `auth_secret` 二者之一 | 保存身份验证文档的[会话机密](#session-secrets)的名称。 |
| `params` | 否 | 已声明的 Provider 参数的默认值，包括已声明的服务器参数。查询中的谓词会覆盖默认值。 |
| `tuning` | 否 | 该别名的请求所适用的 `max_concurrent` 和 `max_per_host` 限制。Provider 和租户标签都相同的别名共用这些控制项，并且必须声明相同的调优设置。 |
<br/>
下面的示例将 2 个 AWS 账号绑定到不同的别名下，每个别名从名称不同的环境变量中读取密钥，并在一条语句中查询这 2 个账号：

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

环境变量引用从 StackQL 进程环境中解析；不存在会话本地的环境。AWS 临时凭据还需要会话令牌字段或环境变量引用。

对于元数据语句和数据语句，别名的行为与其 Provider 相同：`SHOW`、`DESCRIBE`、`SELECT`、`INSERT`、`UPDATE`、`DELETE` 和 `EXEC` 都接受用别名代替 Provider 名称。别名不适用于原生内置关系，也不适用于预览版 IaC 关系。使用不同别名的 `UNION ALL` 分支按顺序执行。

每个查询都会获取别名凭据的独立快照。更改绑定会影响之后的查询，不影响已在运行的查询。`UNSET "$.providers.<alias>"` 移除该绑定。

## 覆盖凭据字段 {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` 在当前会话中更改 Provider 身份验证上下文的一个字段，其中字段名是该 Provider 的 `--auth` 条目中的键：

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.credentialsenvvar";
```

与别名一样，更改适用于 `SET` 之后启动的查询。如果 Provider 的身份验证已被取消设置，或其别名没有可用的 `auth`，针对它的查询会在发送任何请求之前失败。

## 会话机密 {#session-secrets}

默认情况下，每个会话都有一个仅存在于内存中的机密存储，无需任何配置。`SET "$.secrets.<name>"` 存储一个字面量字符串，通常是 JSON 身份验证文档，Provider 别名通过 `auth_secret` 引用它：

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

轮换机密会影响之后的查询；删除机密会使之后的使用失败。新会话会获得一个全新的空存储，会话结束时该存储被释放。`UNSET "$.secret_store"` 丢弃当前的存储绑定，并以空的会话内存重新开始。

正常的凭据使用，以及存储变量或配置的 `SET` 语句，都不会将机密写入 SQL 后端。将敏感数据放入普通变量或表中，由调用方自行负责。

## 持久化的操作系统机密存储 {#persistent-os-secret-stores}

持久化存储默认禁用，只能由运维人员使用 `--allow-persistent-secrets` 启动 StackQL 来启用；SQL 无法授予该能力。设置该标志后，会话通过以下语句选择操作系统存储：

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

该存储是 macOS Keychain、Linux Secret Service（`secret-tool`，需要可用的会话服务）或 Windows Credential Manager。仅接受 `os` 后端：即使设置了该标志，平面文件存储、文件路径以及任何隐式的文件回退也会被拒绝；操作系统设施不可用时会失败，而不会回退到文件。

写入操作系统存储的值在 SQL 会话结束后仍然存在，需使用 `UNSET "$.secrets.<name>"` 显式删除。重置存储绑定不会删除已持久化的值。对该存储的访问由 StackQL 进程的操作系统身份决定；命名空间不是按用户的访问控制，因此不要把这项需明确启用的功能当作多用户保险库。

## SQL 中的机密值 {#secret-values-in-sql}

除非运维人员使用 `--allow-secret-dereference` 启动 StackQL，否则机密值绝不会被解引用到 SQL 中。设置该标志后，`"$secrets.production"`、`"$secrets.production.credentials"`、`"$auth.github.password"` 和 `"$providers.production.auth.credentials"` 这样的引用可在表达式中解析：

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

如果启动时未设置该标志，同一语句会在任何求值之前被拒绝，如注释掉的那条语句所示。设置该标志即表示确认：显式解引用的值可能进入 SQL 暂存区、结果、日志和持久化的查询产物。正常的 Provider 身份验证不需要它。

## 权限 {#permissions}

启用[访问管理](/command-line-usage/access-management)后，读取变量和机密需要 `session/variables:read`，`SET` 或 `UNSET` 需要 `session/config:write`。解析后的别名在执行前按其规范 Provider 进行授权。按变量和按机密的所有权与权限授予尚未实现。
