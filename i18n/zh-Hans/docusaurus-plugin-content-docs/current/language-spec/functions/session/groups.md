---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - 访问管理
  - groups
  - 主体目录
description: "StackQL 中的 GROUPS 函数：启用访问管理时，以 JSON 数组形式返回已通过身份验证的调用方所属的目录组。"
image: "/img/stackql-featured-image.png"
---
以 JSON 数组形式返回已通过身份验证的调用方的目录组成员身份。需要在启用[访问管理](/command-line-usage/access-management)的情况下启动会话。在 StackQL `v1.0` 及更高版本中可用。

另请参阅：  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ 访问管理 ]](/command-line-usage/access-management)

* * * 

## 语法 {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## 参数 {#arguments}

无。

## 返回值 {#return-values}

返回由组 ID 构成的 JSON 数组，取自为当前语句生成的主体目录快照，例如 `["readers"]`。成员身份在每条语句执行时刷新，因此撤销会在下一条语句中体现。在未使用 `--access.config` 启动的会话中，该函数会失败。

* * *

## 示例 {#examples}

### 查看已建立的身份 {#inspect-the-established-identity}

这两个函数一起显示调用方的主体 ID 及其当前的目录组。由于成员身份在每条语句执行时都会重新读取，在目录发生变化后运行此语句，可以确认该变化对会话可见。

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
