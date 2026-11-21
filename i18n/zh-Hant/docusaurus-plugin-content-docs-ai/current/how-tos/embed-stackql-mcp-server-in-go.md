---
title: 如何在 Go 應用程式中內嵌 StackQL MCP 伺服器
description: 使用 go:embed 或 sidecar 模式將 StackQL MCP 伺服器內嵌至編譯後的 Go 二進位檔，讓代理式 Go 應用程式無需執行階段相依性即可透過 SQL 執行雲端查詢與佈建。
keywords: [stackql, mcp, go, golang, go:embed, 內嵌式 mcp 伺服器, model context protocol, go mcp sdk, 代理式基礎架構, sandboxctl]
proficiencyLevel: Intermediate
faq:
  - question: 如何將內嵌式 StackQL MCP 伺服器加入 Go 應用程式？
    answer: "執行 `go get github.com/stackql/stackql-mcp-go`，然後呼叫 `stackqlmcp.StartServer(ctx, stackqlmcp.Options{Binary: StackqlMCPBinary()})`。傳回的用戶端就是官方 Go MCP SDK 用戶端，因此您可以使用 `client.Session.CallTool` 呼叫工具。首次執行時，已簽署的二進位檔會下載到位於 ~/.stackql/mcp-server-bin/ 的共用快取並進行 sha256 驗證，或以 go:embed vendor 到您的二進位檔中。"
  - question: 能否發行內嵌 StackQL 的單一獨立式 Go 二進位檔？
    answer: 可以。新增一個 go:generate 擷取步驟（`//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main`），用來下載該平台的二進位檔並進行 sha256 驗證，接著由 go:embed 將它納入建置。在執行階段，StackqlMCPBinary() 會傳回內嵌的二進位檔，因此首次執行時不需下載，也不需另外安裝。
  - question: 如何在 Go 中讓內嵌式 StackQL MCP 伺服器保持唯讀？
    answer: 伺服器預設以 read_only 模式啟動，此模式會拒絕 INSERT、UPDATE、DELETE 與 EXEC。只有在應用程式需要佈建時，才將 Options.Mode 明確設為 safe、delete_safe 或 full_access。權限提升一律需明確指定：除非您選擇加入，否則伺服器不會啟用變更或生命週期工具。
  - question: stackql-mcp-go 使用哪個 MCP 用戶端？
    answer: 官方 Go MCP SDK（github.com/modelcontextprotocol/go-sdk/mcp）。StartServer 會傳回一個用戶端，您可以直接呼叫其 Session，因此不會受限於 StackQL 專屬的用戶端 API。
---

# 如何在 Go 應用程式中內嵌 StackQL MCP 伺服器

StackQL 的 MCP 伺服器可以在編譯後的 Go 程式內執行，不需要 `npx`、無需另外安裝，也沒有執行階段相依性。`stackql-mcp-go` 程式庫會在官方 Go MCP SDK 用戶端背後，透過 stdio 啟動已簽署的 StackQL 二進位檔，讓您的 Go 應用程式取得受治理、可自我描述的 SQL 介面，存取 StackQL 登錄庫中的每個雲端與 SaaS Provider。這正是 **sandboxctl** 背後的模式，它是以單一二進位檔發行的隨需基礎架構管家。

## 步驟 {#steps}

1. **新增程式庫**：

```
go get github.com/stackql/stackql-mcp-go
```

2. **啟動內嵌伺服器並呼叫工具**。伺服器預設以 `read_only` 模式啟動：

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

3. **選擇模式**。`Options.Mode` 預設為 `read_only`。只有在應用程式需要佈建時，才將它設為 `safe`、`delete_safe` 或 `full_access`。權限提升需明確指定。

## 使用 go:embed 發行單一二進位檔 {#ship-a-single-binary-with-goembed}

若要產生獨立式可執行檔，請新增一個 `go:generate` 擷取步驟，下載該平台的二進位檔並進行 sha256 驗證，然後以 `go:embed` 將它內嵌到建置中：

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

在執行階段，`StackqlMCPBinary()` 會傳回內嵌的二進位檔，因此首次執行時不需下載。若不使用 vendoring，二進位檔會在首次執行時下載到位於 `~/.stackql/mcp-server-bin/` 的共用快取並進行驗證。

## 示範應用程式 {#demo-app}

**sandboxctl** 是一個基礎架構管家：在單一 Go 二進位檔中結合內嵌的 StackQL MCP 伺服器與 Claude 代理程式迴圈，負責規劃臨時雲端沙箱並檢查成本，取得核准後才佈建。

## 相關概念 {#related-concepts}

- [內嵌式 MCP：Go 參考](/mcp/embedded/go)：安裝、API 與模式
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型與安全模式
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式與稽核內部機制
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：此做法促成的模式
