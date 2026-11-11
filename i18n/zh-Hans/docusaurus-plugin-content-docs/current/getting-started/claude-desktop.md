---
title: 在 Claude Desktop 中使用 StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ai
  - infrastructure-as-code
  - 多云
description: 在 Claude Desktop 中使用 StackQL，以自然语言查询和预配云资源
image: "/img/stackql-featured-image.png"
---

StackQL 通过 Model Context Protocol（MCP）与 Claude Desktop 集成，让你能够使用自然语言查询和预配多个云服务商的云资源。这一强大的组合让你可以通过对话与云基础设施交互，同时在底层利用 StackQL 基于 SQL 的接口。

共有 3 种安装方式，这里按推荐顺序列出：从 Anthropic Connector Directory 安装、从下载的安装包安装，或手动注册本地已安装的 StackQL 二进制文件。

## 从 Connector Directory 安装（推荐） {#install-from-the-connector-directory-recommended}

在 Claude Desktop 中使用 StackQL MCP 服务器最简单的方式，是从 Anthropic Connector Directory 安装。在 Claude Desktop 中：**Settings -> Connectors -> Add -> Browse Connectors**，搜索 **StackQL**，然后点击 **Install**。无需下载任何内容，也无需单独安装 StackQL。

![Anthropic Connector Directory 中的 StackQL](/img/stackql-anthropic-directory-listing.png)

也可以直接在 [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql) 查看该条目。

这会为你的平台以进程内方式安装 StackQL MCP 服务器：该条目提供一个合并的多平台安装包，其中包含适用于 macOS universal、Windows x64 以及 Linux x64 和 arm64 的已签名二进制文件，Claude Desktop 会自动为你的机器选择合适的那一个。对于原生 Windows ARM64，请从[下方的下载](#install-from-a-downloaded-bundle)安装单独的 `windows-arm64` 安装包。Windows 二进制文件带有 Authenticode 签名，macOS 二进制文件经过 Apple 公证，它们与独立的 StackQL 发布版本是相同的二进制文件。

![安装 StackQL 连接器](/img/stackql-anthropic-directory-install.png)

从目录安装的连接器会继承 Claude Desktop 的环境，因此请在启动 Claude Desktop 之前，在操作系统级别为你的 Provider 设置环境变量，参阅[特定 Provider 的凭据](#provider-specific-credentials)。Claude Desktop 在启动时捕获其环境：更改变量后，必须完全退出 Claude Desktop（在 Windows 上包括托盘图标）并重新启动。让智能体运行 [`reload_credentials`](/mcp/reload_credentials) 会报告每个 Provider 的凭据状态，这是确认服务器能看到和不能看到哪些内容的最快方式。

Claude Desktop 通告了 MCP 征询（elicitation）能力，因此默认的服务器模式（`safe`）会在每次变更时请求你审批，参阅[服务器模式](/command-line-usage/mcp#server-modes)。

## 从下载的安装包安装 {#install-from-a-downloaded-bundle}

使用这种安装方式可以固定特定版本、在无法访问 Connector Directory 的情况下安装，或在安装前自行验证校验和。

预构建的 MCP Bundle（`.mcpb`）是一个可一键安装的包，其中包含已签名的 `stackql` 二进制文件和服务器配置。无需单独安装 StackQL。

从最新的发布版本下载适用于你的平台的安装包：

| 平台 | 安装包 |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64（原生） | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

然后在 Claude Desktop 中：**Settings -> Extensions -> Install Extension**，并选择下载的文件。嵌入的二进制文件带有 Authenticode 签名（Windows）并经过 Apple 公证（macOS）；在[发布页面](https://github.com/stackql/stackql/releases/latest)上，每个安装包旁边都有一个 `.sha256` 校验和。要在安装前验证：

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

该服务器还以 `io.github.stackql/stackql-mcp` 的名称列在[官方 MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) 上，后者固定了这些校验和。

## 手动配置 {#manual-configuration}

如果已经安装了 StackQL，可以直接在 Claude Desktop 的配置文件中注册 StackQL MCP 服务器。

### 前提条件 {#prerequisites}

在为 Claude Desktop 设置 StackQL 之前，请确保具备：

1. **已安装 StackQL**：从 [stackql.io](https://stackql.io/downloads) 下载并安装 StackQL，或使用包管理器
2. **Claude Desktop**：从 [Anthropic 网站](https://claude.ai/desktop)下载
3. **云服务商凭据**：要使用的云服务商的 API 密钥或凭据

运行以下命令，验证 StackQL 在系统 PATH 中：

```bash
stackql --version
```

### 设置 MCP 服务器 {#setting-up-the-mcp-server}

要让 Claude Desktop 能够与 StackQL 通信，需要在 Claude Desktop 配置文件中配置 MCP 服务器。

#### Windows {#windows}

编辑位于以下位置的配置文件：
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

编辑位于以下位置的配置文件：
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

编辑位于以下位置的配置文件：
```
~/.config/Claude/claude_desktop_config.json
```

### 配置示例 {#configuration-example}

将以下配置添加到 `claude_desktop_config.json` 文件中：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
只需包含计划使用的云服务商的环境变量。从配置中删除所有未使用的 Provider 凭据。
:::

:::warning
切勿将包含真实凭据的 `claude_desktop_config.json` 文件提交到版本控制。请采用安全的凭据管理做法。
:::

Claude Desktop 通告了 MCP 征询能力，因此默认的服务器模式（`safe`）会在每次变更时请求你审批。切换到 `mode: read_only` 可获得仅限资源清单的访问，切换到 `mode: full_access` 可跳过审批请求，参阅[服务器模式](/command-line-usage/mcp#server-modes)。

### 使用凭据文件（`--env.file`） {#using-a-credentials-file---envfile}

`env` 块的一种替代方式（在 Windows 上也是推荐的方式）是将凭据保存在 dotenv 风格的文件中，并通过 [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) 标志传入：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

配合一个最小的凭据文件：

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

这种方式优于 `env` 块的原因：

- 对 `env` 块的更改需要完全重启 Claude Desktop（包括退出托盘图标）后，MCP 子进程才能看到；而在 Windows 上，`setx` 写入的是注册表，永远不会到达正在运行的 Desktop 进程。
- `--env.file` 指定的文件可以随时创建或轮换，服务器启动时它甚至不必存在；让智能体运行 [`reload_credentials`](/mcp/reload_credentials) 即可获取更改，无需任何重启。

:::note

`--env.file` 和 `reload_credentials` 在 `v0.10.542` 之后的 StackQL 发布版本中可用。

:::

### 特定 Provider 的凭据 {#provider-specific-credentials}

不同的云服务商需要不同的身份验证方法，所需 Provider 对应的环境变量请参阅 Provider 文档（[https://stackql.io/providers](/providers)）。


### 重启 Claude Desktop {#restarting-claude-desktop}

更新配置文件后，重启 Claude Desktop 以加载新的 MCP 服务器配置。开始新对话时，StackQL MCP 服务器会自动启动。

## 你可以做什么 {#what-you-can-do}

配置完成后，可以在 Claude Desktop 中使用自然语言：

### 查询云资源 {#query-cloud-resources}

让 Claude 检索有关云基础设施的信息：

- “显示我在 us-central1 区域的所有 GCP 计算实例”
- “列出我的 AWS 账号中的所有 S3 存储桶”
- “我的订阅中有哪些 Azure 虚拟机正在运行？”
- “显示 default 命名空间中的所有 Kubernetes pod”

### 预配资源 {#provision-resources}

使用对话式命令创建和部署云资源：

- “在 us-west-2 中创建名为‘my-data-bucket’的 S3 存储桶”
- “部署一个启用了版本控制的 GCP Cloud Storage 存储桶”
- “在 us-east-1 中启动一个小型 EC2 实例”
- “在 westus2 中创建一个 Azure 资源组”

### 分析和审计 {#analyze-and-audit}

执行安全审计和合规检查：

- “显示所有可公开访问的 S3 存储桶”
- “列出 90 天内未登录的 IAM 用户”
- “查找所有没有正确标签的 GCP 实例”
- “找出未启用备份的 Azure VM”

### 跨云操作 {#cross-cloud-operations}

跨多个云服务商查询和比较资源：

- “显示 AWS、GCP 和 Azure 中的所有计算实例”
- “比较 AWS S3 和 GCP Cloud Storage 的存储成本”
- “列出所有云服务商中的所有数据库”

### 基础设施即代码 {#infrastructure-as-code}

生成并执行基础设施部署查询：

- “创建一个包含 VPC、子网和 EC2 实例的堆栈”
- “部署一个带节点池的完整 GKE 集群”
- “在 AWS 中设置多区域部署”

### 修改资源 {#modify-resources}

更新现有的云资源：

- “为所有未打标签的 EC2 实例添加标签”
- “为 S3 存储桶‘my-bucket’启用加密”
- “更新 GCP 实例‘web-server-1’的机器类型”
- “将我的 Cloud Storage 存储桶的 ACL 改为私有”

### 删除资源 {#delete-resources}

删除不再需要的云资源：

- “删除 S3 存储桶‘temp-bucket’”
- “移除所有已停止的 EC2 实例”
- “清理未使用的 GCP 磁盘”

## 示例对话 {#example-conversation}

下面是使用 StackQL 与 Claude Desktop 交互的一个示例：

**你：**“能显示我所有的 GCP 计算实例吗？”

**Claude：** *执行 StackQL 查询，并以格式化的表格返回结果，显示实例名称、可用区、机器类型和状态*

**你：**“在 us-central1 区域创建一个名为‘analytics-data-2025’的新 Cloud Storage 存储桶，并启用统一存储桶级访问权限”

**Claude：** *执行 StackQL INSERT 语句创建存储桶，并确认已创建*

**你：**“现在列出我所有的 Cloud Storage 存储桶”

**Claude：** *执行 SELECT 查询，并显示所有存储桶，包括新创建的那个*

## 提示和最佳实践 {#tips-and-best-practices}

1. **具体明确**：请求越具体，得到的结果越好。尽可能包含资源名称、区域和具体属性。

2. **使用自然语言**：不需要了解 SQL 语法，Claude 会将你的自然语言请求转换为 StackQL 查询。

3. **执行前先检查**：对于破坏性操作（DELETE、UPDATE），Claude 通常会在继续之前向你展示将要执行的内容。

4. **多步操作**：可以在一次对话中将多个操作串联起来。Claude 会在整个会话中保持上下文。

5. **错误处理**：如果查询失败，Claude 可以帮助排查问题，并根据错误消息建议修正方法。

6. **探索能力**：向 Claude 提问“我可以用 StackQL 做什么？”，获取针对已配置 Provider 的建议。

## 故障排查 {#troubleshooting}

### MCP 服务器无法启动 {#mcp-server-not-starting}

如果 Claude Desktop 无法连接到 StackQL MCP 服务器：

1. 验证 StackQL 在 PATH 中
2. 检查 `claude_desktop_config.json` 文件是否为有效的 JSON
3. 确保配置中的文件路径对你的操作系统是正确的
4. 查看 Claude Desktop 日志中的错误消息

### 身份验证错误 {#authentication-errors}

如果收到身份验证错误：

1. 验证云服务商凭据是否正确
2. 确保凭据文件存在于指定的路径
3. 检查配置中的环境变量格式是否正确：让智能体运行 [`reload_credentials`](/mcp/reload_credentials) 会报告每个 Provider 的凭据状态，这是查看缺少哪个变量的最快方式
4. 确认凭据具有必要的权限

### 权限错误 {#permission-errors}

如果操作因权限不足而失败：

1. 验证服务账号或 IAM 用户具有所需的权限
2. 检查特定云服务商的 IAM 策略
3. 确保云项目中已启用 API 服务

## 了解更多 {#learn-more}

- [StackQL MCP 命令参考](/command-line-usage/mcp)
- [StackQL Provider 注册表](/providers)
- [语言规范](/language-spec/select)
- [Model Context Protocol 文档](https://modelcontextprotocol.io)

## 安全注意事项 {#security-considerations}

:::caution
- 安全地存储凭据，切勿将其提交到版本控制
- 对服务账号采用最小权限访问原则
- 定期轮换凭据和 API 密钥
- 监控 MCP 服务器日志中的异常活动
- 考虑使用云服务商的凭据管理工具（例如 Secret Manager、Parameter Store）
:::
