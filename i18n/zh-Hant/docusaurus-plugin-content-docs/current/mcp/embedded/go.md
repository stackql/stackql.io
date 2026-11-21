---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 內嵌式
  - go
description: 透過 go:embed 或 sidecar，在 Go 應用程式中內嵌 StackQL MCP 伺服器
image: "/img/stackql-featured-image.png"
---

# Go

在編譯式 Go 應用程式中內嵌 StackQL MCP 伺服器，可以是下載的 sidecar，也可以使用 `go:embed` 透過 vendoring 納入您的二進位檔。用戶端是官方的 Go MCP SDK。

:::info
內嵌的二進位檔會在首次執行時下載並經過 sha256 驗證，或透過 vendoring 納入您的應用程式。請參閱[共同契約](/mcp/embedded#common-contract)。
:::

* * *

## 安裝 {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## 快速入門 {#quick-start}

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

API 為 `StartServer(ctx, Options{Binary, Mode})`；傳回的用戶端來自官方的 Go MCP SDK。

* * *

## 模式 {#modes}

伺服器預設以 `read_only` 模式啟動。設定 `Options.Mode` 即可提升至 `safe`、`delete_safe` 或 `full_access`。權限提升必須明確指定：除非您選擇加入，否則伺服器不會啟用變更或生命週期工具。

* * *

## Vendoring / 內嵌 {#vendoring--embedding}

使用 `go:embed` 搭配 `go:generate` 擷取步驟來 vendor 二進位檔，產生單一的獨立可執行檔：

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

擷取步驟會下載目標平台的已簽署二進位檔並進行 sha256 驗證，然後將其寫入 `go:embed` 能納入建置的位置。在執行階段，`StackqlMCPBinary()` 會傳回內嵌的二進位檔，因此首次執行時不需要下載。

* * *

## 示範應用程式 {#demo-app}

**sandboxctl** 是一個基礎架構管家：在單一二進位檔中結合內嵌的伺服器與 Claude 代理程式迴圈，負責規劃臨時雲端沙箱、檢查其成本，並在通過核准關卡後加以佈建。

* * *

## 存放庫連結 {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
