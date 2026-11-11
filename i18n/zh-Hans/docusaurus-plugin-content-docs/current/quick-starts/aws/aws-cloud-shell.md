---
title: AWS Cloud Shell 中的 StackQL
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
  - cspm
  - aws
  - amazon web services
description: "以登录用户身份在 AWS CloudShell 中运行 StackQL：下载 Linux 软件包、使用 stackql shell、担任 IAM 角色，并用 exec 运行批量查询。"
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# 在 AWS Cloud Shell 中使用 StackQL

在本指南中，我们将演示如何借助 AWS Console 登录会话的用户上下文或通过担任另一个 IAM 角色，在 AWS Cloud Shell 中使用 StackQL。StackQL 是一款功能强大的开发工具，可使用 SQL 语法查询和部署云基础设施与资源。    

测试环境：<span class="tutorial_tested_on">嵌入式 SQL 后端</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>什么是 <b>AWS Cloud Shell</b>？</summary>

> AWS Cloud Shell 是 Amazon Web Services（AWS）提供的一项基于浏览器、预先完成身份验证的 shell 服务。它让开发者和 IT 专业人员可以直接在 Web 浏览器中管理云资源。借助 AWS Cloud Shell，用户无需在本地计算机上安装或配置 AWS CLI（Command Line Interface），即可快速运行与 AWS 服务相关的命令。该服务提供开箱即用的环境，预装了 AWS CLI、Python、Node.js 等常用的开发和管理工具，便于与 AWS 服务交互并自动执行任务。AWS Cloud Shell 与 AWS Management Console 直接集成，为执行管理任务、探索 AWS 服务和管理基础设施提供无缝的体验。由于 AWS Cloud Shell 会通过用户的 AWS Console 登录自动完成身份验证，它简化了在其 AWS 账号上下文中运行命令和脚本的过程，从而提高云运维的生产力和效率。

</details>

## 工作原理 {#how-it-works}

StackQL Linux 软件包包含一些脚本，可在各大云服务商内置的云 shell 环境中直接使用 `stackql`。AWS 对应的脚本是 __`stackql-aws-cloud-shell.sh`__。__`stackql-aws-cloud-shell.sh`__ 脚本的用法如下：  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

`stackql-aws-cloud-shell.sh` 脚本会自动下载 StackQL 最新的 AWS Provider。所含云 shell 脚本的源代码位于 [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts)。  

## 在 AWS Cloud Shell 中下载 `stackql` Linux 软件包 {#download-the-stackql-linux-package-from-aws-cloud-shell}

首先，在 AWS Cloud Shell 中使用以下命令下载 StackQL 最新的 Linux 二进制文件：  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

这会下载 `stackql` 二进制文件和 `stackql-aws-cloud-shell.sh` 脚本，并将其解压到你的 AWS Cloud Shell 环境的主目录中。现在一切就绪！  

## 在 AWS Cloud Shell 中使用 `stackql shell` {#using-the-stackql-shell-from-aws-cloud-shell}

在 AWS Cloud Shell 中启动已通过身份验证的 StackQL 命令 shell，只需运行：  

```shell
sh stackql-aws-cloud-shell.sh
```

现在可以使用 StackQL Provider 开始运行基于 SQL 的交互式 AWS 查询，如下所示：  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  你的浏览器不支持 video 标签。
</video>

## 使用 StackQL 担任 IAM 角色 {#assuming-an-iam-role-using-stackql}

在 AWS 中担任 IAM 角色后，你可以执行默认用户权限可能不允许的操作，并访问默认用户权限可能无法访问的资源。当你需要临时访问资源，或需要以不同于主账号的权限执行操作时，这尤其有用。借助 StackQL，你可以无缝地担任 IAM 角色，在该角色授予的权限下查询和管理 AWS 资源。

<details>
<summary>了解担任 IAM 角色</summary>

> 担任 IAM 角色是授予 AWS 资源临时访问权限的一种安全方式。担任角色时，AWS STS 会向你提供临时安全凭据，让你可以访问该角色有权使用的 AWS 服务和资源。这种机制非常适合跨账号访问、为用户提供临时访问权限，或遵循最小权限原则。

> 可以通过 AWS Management Console、AWS CLI 或 AWS CLI 担任角色，也可以直接在 StackQL 中担任角色。这可确保用户和应用程序仅在所需的时间内拥有必要的权限，从而增强你的 AWS 环境的安全态势。

</details>

要在 AWS Cloud Shell 中使用 StackQL 担任 IAM 角色，可以在 `stackql-aws-cloud-shell.sh` 脚本中使用 `--role-arn` 选项。该选项需要提供要担任的角色的 Amazon Resource Name（ARN）。提供该选项后，StackQL 会在执行任何查询或命令之前，使用 AWS Security Token Service（STS）担任指定的角色。

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

通过担任角色，你可以利用 StackQL 与默认用户可能无权访问的 AWS 资源交互，以更灵活、更安全的方式管理云环境。  

使用所担任的 IAM 角色执行 StackQL 命令或脚本时，请确保该角色拥有执行所需操作和访问指定资源的必要权限。这种方式有助于实现细粒度的访问控制，并符合 AWS 在安全和治理方面的最佳实践。  

## 在 AWS Cloud Shell 中运行 `stackql exec`（批量操作） {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

StackQL 查询也可以使用 `exec` 命令执行；这种方式适用于：

- 指定其他输出格式，例如 `json` 或 `csv`
- 为 StackQL 查询的结果生成输出文件
- 使用通过输入文件提供的 StackQL 查询（按惯例为 `.iql` 文件）
- 执行复杂操作，包括引入变量或预处理查询（使用 `json` 或 `jsonnet`）

在 AWS Cloud Shell 中使用 `exec` 的示例如下：  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instanceType FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

上面的示例运行指定的 StackQL 查询并生成一个 `CSV` 输出文件，该文件可以从 AWS Cloud Shell 下载。此外，还可以使用 `--role-arn` 参数提供一个 IAM 角色，以便为查询或变更操作担任另一个角色；示例如下（在本例中，查询来自输入文件）：  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::824532806693:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

有关 `exec` 命令的更多信息，请参阅[__此处__](/command-line-usage/exec)
