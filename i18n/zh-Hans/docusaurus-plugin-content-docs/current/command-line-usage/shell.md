---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "stackql shell 命令启动交互式 StackQL 命令 shell，用于对你的云和 SaaS 账号运行查询和 Provider 命令。"
image: "/img/stackql-featured-image.png"
---

用于启动交互式 StackQL 命令 shell 的命令。更多信息参阅[使用 StackQL 交互式 shell](/getting-started/using-stackql#using-the-stackql-interactive-shell)。

* * * 

### 语法 {#syntax}

`stackql shell [flags]`

* * *

### 标志 {#flags}

| 标志 | 说明 |
|--|--|
|<span class="nowrap">`-H`</span><br/><span class="nowrap">`--help`</span>|打印帮助信息|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|以详细模式运行查询，并将额外输出发送到 stdout，如果选择了 `-f` 选项，这些额外的日志信息将与查询结果一起写入输出文件|
<br/>
<br/>
> 更多选项参阅[全局标志](/command-line-usage/global-flags)

* * *

### 示例 {#examples}

启动 StackQL shell，使用服务账号密钥文件进行身份验证。
```shell
stackql shell --keyfilepath /tmp/stackql-demo.json
```
