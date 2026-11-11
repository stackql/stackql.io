---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 访问管理
  - whoami
  - 主体
description: "StackQL 中的 WHOAMI 函数：启用访问管理时，返回已通过身份验证的调用方的主体 ID。"
image: "/img/stackql-featured-image.png"
---
返回已通过身份验证的调用方的主体 ID。需要在启用[访问管理](/command-line-usage/access-management)的情况下启动会话。在 StackQL `v1.0` 及更高版本中可用。

另请参阅：  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ 访问管理 ]](/command-line-usage/access-management)

* * * 

## 语法 {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## 参数 {#arguments}

无。

## 返回值 {#return-values}

返回为当前语句建立的主体 ID 字符串。在未使用 `--access.config` 启动的会话中，该函数会失败。

* * *

## 示例 {#examples}

### 查看已建立的身份 {#inspect-the-established-identity}

这两个函数都不接受参数，描述的是服务器为本条语句完成身份验证的调用方。`whoami()` 返回主体 ID，`groups()` 返回调用方的目录组，因此在运行其他任何内容之前一起运行它们，可以快速检查令牌或证书是否映射到预期的主体。

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
