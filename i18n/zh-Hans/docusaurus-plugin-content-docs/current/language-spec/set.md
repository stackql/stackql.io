---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 会话变量
  - set
  - Provider 会话
  - 机密
description: "StackQL 中的 SET：为带类型的会话变量赋值，并通过以美元符号为前缀的目标为当前会话配置 Provider 别名、凭据字段和机密。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

为会话变量赋值，或为当前会话配置运行时设置。适用于 StackQL `v1.0` 及更高版本。

另请参阅：  
[[` UNSET `]](/language-spec/unset) [[ 会话变量 ]](/getting-started/session-variables) [[ Provider 会话 ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## 语法 {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
普通标识符（`region`）、双引号括起的名称（`"providers.saved"`），或双引号括起、以美元符号为前缀的配置路径（`"$.auth.aws.password"`）。

__*literal*__  
字符串、数字、布尔值或 `null`。字符串使用单引号。数组和对象不是标量值；JSON 文档以字符串形式保存。

__*DEFAULT*__  
重置目标，等同于 [`UNSET`](/language-spec/unset)。显式的 `null` 是一个被存储的值，而不是重置。

__*reference*__  
对已有变量的引用，写作双引号括起的 `"$name"`，例如 `SET "$.providers.production" = "$providers.saved";`。

__*SESSION*__  
可选，并且是 StackQL 唯一的作用域。`SET SESSION name = ...` 与 `SET name = ...` 是同一条语句：每次赋值都只属于当前会话。保留该关键字是为了让脚本的写法与在区分 `SET SESSION` 和 `SET GLOBAL` 的数据库上相同。`SET GLOBAL` 能够解析，但会被拒绝，并返回 `configuration scope "global" is not supported; use SESSION`，因为不存在跨会话共享的状态。

同一个 `SET` 列表中的赋值按顺序生效，且不构成事务：如果列表中的第二个赋值失败，第一个赋值仍然有效。

## 目标 {#targets}

| 目标 | 效果 |
|--|--|
| `name` 或 `"name"` | 存储一个标量会话变量，在后续语句中以 `$name` 解引用。仅有点号并不表示配置：`"providers.saved"` 是一个名为 `providers.saved` 的变量。较早的 `"variables.name"` 写法是 `name` 的别名。 |
| `"$.auth.<provider>.<field>"` | 为本会话设置该 Provider 身份验证上下文中的一个字段。字段是该 Provider 的 [`--auth`](/command-line-usage/global-flags) 对象的键，例如 `credentialsenvvar`、`password` 或 `token_url`。 |
| `"$.providers.<alias>"` | 绑定一个 Provider 别名，该别名拥有自己的凭据、租户标签、参数默认值和调优设置。值是字符串形式的 JSON 文档，参阅 [Provider 会话](/getting-started/provider-sessions#provider-aliases)。 |
| `"$.secrets.<name>"` | 在会话的机密存储中存储一个机密。值是字符串字面量，通常是 JSON `auth` 文档，在 Provider 别名中通过 `auth_secret` 引用。 |
| `"$.secret_store"` | 选择持久化的 OS 机密存储，`{"backend":"os","namespace":"..."}`。需要运维人员标志 `--allow-persistent-secrets`。 |

每种形式都只影响当前会话。连接到 [`srv`](/command-line-usage/srv) 服务器的各个独立连接看不到彼此的赋值，并且每个 MCP 请求都是一个独立的会话，因此在使用 MCP 服务器时，应在同一批语句中完成配置和查询。

* * *

## 示例 {#examples}

### 设置会话变量 {#set-session-variables}

每条语句以给定的名称存储一个标量，并且值保留其类型：`region` 是字符串，`minimum_count` 是整数，`enabled` 是布尔值。`null` 作为值被存储，因此之后 `$optional_value` 会解析为 SQL `NULL`，而不会因未定义而失败。最后一条语句在一个列表中为两个变量赋值。

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### 显式指定会话作用域 {#make-the-session-scope-explicit}

前两条语句完全相同：`SESSION` 指明的是唯一存在的作用域，两个赋值都会持续到变量被取消设置或会话结束。如果脚本的写法需要与在区分会话设置和全局设置的数据库上相同，请使用该关键字。被注释掉的语句用于对比：它不会赋任何值，并以 `configuration scope "global" is not supported; use SESSION` 失败，因为会话之间不共享任何状态。

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### 在查询中使用变量 {#use-a-variable-in-a-query}

在任意表达式位置使用不带引号的 `$name` 解引用变量。值保留其类型：字符串会被转义为 SQL 字面量，数字、布尔值和 `null` 则原样传递。`WHERE` 子句中的变量在请求规划之前解析，因此会像字面量一样下推到 Provider。

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### 重置变量 {#reset-a-variable}

`DEFAULT` 会移除变量，与 [`UNSET`](/language-spec/unset) 完全相同，因此在重新设置之前，之后的 `$region` 会以 `variable "$region" is not defined` 失败；被注释掉的查询展示了这一失败。这与赋值 `null` 不同，后者会保留变量，其值为 `NULL`。

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 从另一个变量复制值 {#copy-a-value-from-another-variable}

右侧可以是对已有变量的双引号引用，它会复制该变量当时持有的值。这里先将一个 Provider 绑定存储为名为 `providers.saved` 的普通变量（点号并不会使其成为配置），然后通过将其复制到 `$.providers.production` 路径来激活。之后移除保存的副本不会影响已生效的绑定。

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### 为本会话覆盖凭据字段 {#override-a-credential-field-for-this-session}

Provider 的身份验证上下文是启动时通过 [`--auth`](/command-line-usage/global-flags) 为其提供的对象。`$.auth` 下的路径会在会话的剩余时间内替换该对象的一个字段，其他字段保持不变。这里将 GitHub Provider 指向另一个保存只读令牌的环境变量；该变量从 StackQL 进程环境中读取，之后针对 `github.*` 的查询都会使用它，直到该字段被取消设置或会话结束。

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
```

### 绑定 Provider 别名 {#bind-a-provider-alias}

Provider 别名是某个 Provider 的第二个独立配置的实例，在查询中用来代替 Provider 名称。值是字符串形式的 JSON 文档：`provider` 指定底层 Provider，`tenancy` 标记账号，`auth` 是与 `--auth` 结构相同的凭据块，`params` 为 `region` 等请求参数提供默认值。此后针对 `production.s3.buckets` 的查询使用这些凭据运行，而 `aws.s3.buckets` 保持启动时的配置。完整的键列表见 [Provider 会话](/getting-started/provider-sessions#provider-aliases)。

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
