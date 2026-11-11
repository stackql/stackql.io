---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "stackql registry 命令用于在命令行中列出、拉取和管理 StackQL Provider 注册表中的 Provider。"
image: "/img/stackql-featured-image.png"
---

用于与 StackQL Provider 注册表交互的命令。注册表位置以及是否验证 Provider 文档签名，通过全局标志 [`--registry`](/command-line-usage/global-flags#--registry) 配置。启用[访问管理](/command-line-usage/access-management)期间，`registry` 命令不可用；请改为在受保护的 `exec` 或 `shell` 会话中使用 `REGISTRY PULL`。

* * * 

### 语法 {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### 参数 {#arguments}

| 参数 | 说明 | 示例 |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|要执行的注册表操作（`list` 或 `pull`） | `list` 或 `pull` |    
|<span class="nowrap">`provider`</span>|使用 `pull` 子命令安装的 Provider| `okta` |
|<span class="nowrap">`version`</span>|使用 `pull` 子命令安装的可选 Provider 版本 *（StackQL Provider 文档版本）* | `v23.03.00121` |


### 标志 {#flags}

| 标志 | 说明 |
|--|--|
|<span class="nowrap">`-H, --help`</span>|打印帮助信息|
<br/>
<br/>
> 更多选项参阅[全局标志](/command-line-usage/global-flags)

* * *

### 示例 {#examples}

列出公共注册表中可用的 Provider：
```shell
stackql registry list
```

从公共注册表拉取某个 Provider 的最新版本：
```shell
stackql registry pull okta
```
