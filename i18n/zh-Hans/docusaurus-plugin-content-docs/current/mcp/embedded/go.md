---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 嵌入式
  - go
description: 通过 go:embed 或 sidecar 将 StackQL MCP 服务器嵌入 Go 应用
image: "/img/stackql-featured-image.png"
---

# Go

将 StackQL MCP 服务器嵌入已编译的 Go 应用，可作为下载的 sidecar 运行，或通过 `go:embed` 以 vendoring 方式打包进你的二进制文件。客户端是官方的 Go MCP SDK。

:::info
嵌入的二进制文件在首次运行时下载并经 sha256 校验，或以 vendoring 方式打包进你的应用。参阅[通用契约](/mcp/embedded#common-contract)。
:::

* * *

## 安装 {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## 快速入门 {#quick-start}

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

API 为 `StartServer(ctx, Options{Binary, Mode})`；返回的客户端来自官方的 Go MCP SDK。

* * *

## 模式 {#modes}

服务器默认以 `read_only` 模式启动。设置 `Options.Mode` 可提升到 `safe`、`delete_safe` 或 `full_access`。权限提升必须显式进行：除非你明确选择启用，否则服务器不会启用变更或生命周期工具。

* * *

## Vendoring / 嵌入 {#vendoring--embedding}

通过 `go:generate` 获取步骤，使用 `go:embed` 以 vendoring 方式打包二进制文件，生成单个自包含的可执行文件：

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

获取步骤会下载目标平台的已签名二进制文件并进行 sha256 校验，然后将其写入 `go:embed` 能够纳入你的构建的位置。运行时 `StackqlMCPBinary()` 返回嵌入的二进制文件，因此无需在首次运行时下载。

* * *

## 演示应用 {#demo-app}

**sandboxctl** 是一个基础设施管家：将嵌入式服务器和 Claude 智能体循环集成在一个二进制文件中，在审批门控之下规划、检查成本并预配临时云沙箱。

* * *

## 仓库链接 {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
