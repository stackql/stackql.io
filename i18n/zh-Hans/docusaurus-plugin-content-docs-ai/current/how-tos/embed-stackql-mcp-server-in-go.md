---
title: 如何在 Go 应用中嵌入 StackQL MCP 服务器
description: 使用 go:embed 或 sidecar 模式将 StackQL MCP 服务器嵌入已编译的 Go 二进制文件，使智能体式 Go 应用无需运行时依赖即可通过 SQL 执行云查询和预配。
keywords: [stackql, mcp, go, golang, go:embed, 嵌入式 MCP 服务器, model context protocol, go mcp sdk, 智能体基础设施, sandboxctl]
proficiencyLevel: Intermediate
faq:
  - question: 如何为 Go 应用添加嵌入式 StackQL MCP 服务器？
    answer: "运行 `go get github.com/stackql/stackql-mcp-go`，然后调用 `stackqlmcp.StartServer(ctx, stackqlmcp.Options{Binary: StackqlMCPBinary()})`。返回的客户端是官方的 Go MCP SDK 客户端，因此使用 `client.Session.CallTool` 调用工具。首次运行时，已签名的二进制文件会被下载，经 sha256 校验后存入位于 ~/.stackql/mcp-server-bin/ 的共享缓存，或者通过 go:embed 以 vendoring 方式打包进你的二进制文件。"
  - question: 能否发布嵌入了 StackQL 的单个自包含 Go 二进制文件？
    answer: 可以。添加一个 go:generate 获取步骤（`//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main`），由它下载平台二进制文件并进行 sha256 校验，然后 go:embed 将其纳入构建。运行时 StackqlMCPBinary() 返回嵌入的二进制文件，因此无需在首次运行时下载，也无需单独安装。
  - question: 如何在 Go 中让嵌入式 StackQL MCP 服务器保持只读？
    answer: 服务器默认以 read_only 模式启动，该模式拒绝 INSERT、UPDATE、DELETE 和 EXEC。仅当应用需要预配资源时，才将 Options.Mode 显式设置为 safe、delete_safe 或 full_access。权限提升始终需要显式进行：除非你明确选择启用，否则服务器不会启用变更或生命周期工具。
  - question: stackql-mcp-go 使用哪个 MCP 客户端？
    answer: 官方的 Go MCP SDK（github.com/modelcontextprotocol/go-sdk/mcp）。StartServer 返回一个客户端，你可以直接调用它的 Session，因此不会被绑定到 StackQL 专用的客户端 API。
---

# 如何在 Go 应用中嵌入 StackQL MCP 服务器

StackQL 的 MCP 服务器可以在已编译的 Go 程序内运行，无需 `npx`，无需单独安装，也没有运行时依赖。`stackql-mcp-go` 库在官方 Go MCP SDK 客户端的背后通过 stdio 启动已签名的 StackQL 二进制文件，因此你的 Go 应用可以获得一个受治理、自描述的 SQL 接口，用于访问 StackQL 注册表中的每个云和 SaaS Provider。这就是 **sandboxctl** 所采用的模式，它是一个按需基础设施管家，以单个二进制文件发布。

## 步骤 {#steps}

1. **添加库：**

```
go get github.com/stackql/stackql-mcp-go
```

2. **启动嵌入式服务器并调用工具。** 服务器默认以 `read_only` 模式启动：

```go
import (
    "context"
    "github.com/modelcontextprotocol/go-sdk/mcp"
    stackqlmcp "github.com/stackql/stackql-mcp-go/embed"
)

func main() {
    ctx := context.Background()
    client, err := stackqlmcp.StartServer(ctx, stackqlmcp.Options{
        Binary: StackqlMCPBinary(),
    })
    if err != nil {
        // handle error
    }
    defer client.Close()

    res, err := client.Session.CallTool(ctx, &mcp.CallToolParams{
        Name: "run_select_query",
        Arguments: map[string]any{"sql": "SELECT name FROM google.compute.regions WHERE project = 'my-project'"},
    })
    _ = res
}
```

3. **选择模式。** `Options.Mode` 默认为 `read_only`。仅当应用需要预配资源时，才将其设置为 `safe`、`delete_safe` 或 `full_access`。权限提升必须显式进行。

## 使用 go:embed 发布单个二进制文件 {#ship-a-single-binary-with-goembed}

要生成自包含的可执行文件，添加一个 `go:generate` 获取步骤，由它下载平台二进制文件并进行 sha256 校验，然后使用 `go:embed` 将其嵌入构建：

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

运行时 `StackqlMCPBinary()` 返回嵌入的二进制文件，因此无需在首次运行时下载。如果不使用 vendoring，二进制文件会在首次运行时下载，经校验后存入位于 `~/.stackql/mcp-server-bin/` 的共享缓存。

## 演示应用 {#demo-app}

**sandboxctl** 是一个基础设施管家：将嵌入式 StackQL MCP 服务器和 Claude 智能体循环集成在一个 Go 二进制文件中，在审批门控之下规划、检查成本并预配临时云沙箱。

## 相关概念 {#related-concepts}

- [嵌入式 MCP：Go 参考](/mcp/embedded/go)：安装、API 和模式
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型和安全模式
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式和审计的内部机制
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：由此实现的模式
