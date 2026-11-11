---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 会话变量
  - unset
  - Provider 会话
  - 机密
description: "StackQL 中的 UNSET：移除会话变量，或清除当前会话中通过 SET 配置的 Provider 别名、凭据字段和机密。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

移除会话变量，或清除当前会话中通过 [`SET`](/language-spec/set) 设置的运行时配置。适用于 StackQL `v1.0` 及更高版本。

另请参阅：  
[[` SET `]](/language-spec/set) [[ 会话变量 ]](/getting-started/session-variables) [[ Provider 会话 ]](/getting-started/provider-sessions)

* * * 

## 语法 {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
`SET` 接受的任意目标：普通的或双引号括起的变量名，或双引号括起、以美元符号为前缀的配置路径。

__*SESSION*__  
可选，并且是唯一存在的作用域：`UNSET SESSION name` 与 `UNSET name` 是同一条语句，因为每个变量和配置设置都属于当前会话。`UNSET GLOBAL` 会被拒绝，并返回 `configuration scope "global" is not supported; use SESSION`。

`UNSET name` 等同于 `SET name = DEFAULT`。同一列表中的目标按顺序移除，移除未设置的名称不是错误。变量被取消设置后再对其解引用，会在发出任何 Provider 请求之前失败。

| 目标 | 效果 |
|--|--|
| `name` | 移除该会话变量。 |
| `"$.auth.<provider>.<field>"` | 从该 Provider 的会话身份验证上下文中删除该字段。覆盖之前生效的值不会恢复。 |
| `"$.providers.<alias>"` | 移除该 Provider 别名绑定。 |
| `"$.secrets.<name>"` | 从会话的存储中删除该机密。对于持久化的 OS 存储，这会删除已存储的值。 |
| `"$.secret_store"` | 丢弃当前的存储绑定，并从空的会话内存开始。已持久化到 OS 存储中的值不会被删除。 |

* * *

## 示例 {#examples}

### 移除变量 {#remove-a-variable}

变量已从会话中移除，因此被注释掉的查询会以 `variable "$region" is not defined` 失败，而不是使用过期值或空值运行。重新设置该名称即可再次使用。

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### 移除多个目标 {#remove-several-targets}

一条语句可以同时移除变量和配置路径，目标按顺序处理。这里在一个列表中移除了一个普通变量、一个名称带点号的变量和一个凭据覆盖项。

```sql
UNSET region, "providers.saved", "$.auth.github.credentialsenvvar";
```

### 显式指定会话作用域 {#make-the-session-scope-explicit}

这两条语句的作用相同：`SESSION` 指明的是唯一存在的作用域。显式形式适用于同时也在区分会话状态和全局状态的数据库上运行的脚本。被注释掉的语句会失败，因为不存在共享状态。

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### 清除凭据覆盖项 {#clear-a-credential-override}

之前通过 `SET "$.auth.github.credentialsenvvar"` 设置的字段会从 GitHub Provider 的会话身份验证上下文中删除，该上下文的其他字段不受影响。覆盖之前生效的值不会恢复，因此如果 Provider 仍需要该字段，请重新设置。

```sql
UNSET "$.auth.github.credentialsenvvar";
```

### 移除 Provider 别名及其机密 {#remove-a-provider-alias-and-its-secret}

别名 `production` 是通过 `SET "$.providers.production"` 绑定的，并通过 `auth_secret` 引用存储在 `$.secrets.production` 的机密。在一条语句中同时移除两者会解除该绑定，因此 `production.s3.buckets` 不再是有效的关系，同时会从会话存储中删除该机密。与其他各处一样，这里的 `SESSION` 是可选的。

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
