---
title: 使用 Provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "在 StackQL 中使用 Provider：从注册表拉取，使用环境变量或凭据进行身份验证，并查询其服务和资源。"
image: "/img/stackql-featured-image.png"
---

另请参阅：  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

StackQL 查询可以针对多个不同的云服务商运行，包括 Google Cloud Platform、Amazon Web Services、Microsoft Azure、Digital Ocean 等。StackQL Provider 规范从 [__StackQL Provider Registry__](/providers) 拉取，可以使用 StackQL 命令 [__`REGISTRY PULL`__](/language-spec/registry) 完成，也可以使用命令行工具 [__`stackql registry`__](/command-line-usage/registry) 完成。


## 安装 Provider {#installing-a-provider}

要查看可用 Provider 的列表，请运行以下命令：  

```sql
REGISTRY LIST;
```

要安装 Provider，请运行以下命令：

```sql
REGISTRY PULL okta;
```

要查看当前系统上已安装的 Provider，请运行以下命令：

```sql
SHOW PROVIDERS;
```

## 向 Provider 进行身份验证 {#authenticating-to-a-provider}

向云服务商进行身份验证通常是 StackQL 例程的第一步。[`SHOW`](/language-spec/show) 和 [`DESCRIBE`](/language-spec/describe) 等元命令无需身份验证即可针对 Provider 运行，但 [`SELECT`](/language-spec/select)、[`INSERT`](/language-spec/insert) 和 [`EXEC`](/language-spec/exec) 命令通常需要经过身份验证的会话。  

身份验证通过系统上设置的环境变量（或在 CI 系统中以机密形式提供的环境变量）完成，每个 Provider 都有各自默认的专用身份验证变量，因此请通过 [StackQL Provider Registry 文档](/providers)查阅你所用 Provider 的文档。也可以使用全局标志 [`--env.file`](/command-line-usage/global-flags) 在启动时从 dotenv 风格的文件中加载这些变量。  

:::tip

通过提供多个身份验证对象，你可以在同一个交互式 shell 或非交互式批处理例程中向多个 Provider 进行身份验证，例如：  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

当你在单个查询中连接多个 Provider 的数据时，这尤其有用。

:::
