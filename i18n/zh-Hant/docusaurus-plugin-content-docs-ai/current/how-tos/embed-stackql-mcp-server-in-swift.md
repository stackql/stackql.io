---
title: 如何在 macOS Swift 應用程式中內嵌 StackQL MCP 伺服器
description: 將經過公證的 StackQL MCP 伺服器二進位檔封裝在已簽署的 macOS Swift 應用程式中，讓原生選單列或桌面應用程式透過 SQL 執行雲端查詢，同時維持公證有效。
keywords: [stackql, mcp, swift, macos, swiftpm, 公證, 程式碼簽署, 內嵌式 mcp 伺服器, model context protocol, swift mcp sdk, cloudlens]
proficiencyLevel: Intermediate
faq:
  - question: 如何將內嵌式 StackQL MCP 伺服器加入 Swift macOS 應用程式？
    answer: 從 github.com/stackql/stackql-mcp-swift 新增 Swift 套件（版本 0.1.0 起），然後在 Options.mode 設為 .readOnly 的情況下呼叫 `StackQLServer.start(options)`。用戶端是官方的 Swift MCP SDK。需要 macOS 13+ 與 Swift 6.1（Xcode 16.3+）。
  - question: 能否將 StackQL 封裝在已簽署且經過公證的 macOS 應用程式中？
    answer: 可以，而且這是 Swift 程式庫獨有的功能。darwin-universal 二進位檔以 Developer ID 簽署並經過 Apple 公證，因此可以封裝在您已簽署的 .app 中，同時維持應用程式本身的公證有效。您發行的是單一、獨立且經過公證的應用程式，首次執行時不需下載。
  - question: Swift 程式庫以什麼順序解析 StackQL 二進位檔？
    answer: 先使用明確指定的 binaryOverride，其次是封裝在 .app 中的資源，再來是位於 ~/.stackql/mcp-server-bin/ 的共用快取，最後在設定 allowDownload 時下載並進行 sha256 驗證。將經過公證的二進位檔封裝在應用程式資源中，表示會優先使用封裝的路徑，不會下載任何內容。
  - question: 如何在 Swift 中讓內嵌式 StackQL MCP 伺服器保持唯讀？
    answer: 設定 Options.mode = .readOnly，這是預設值。ReadOnly 會拒絕 INSERT、UPDATE、DELETE 與 EXEC。只有在應用程式需要佈建時，才提升至 .safe、.deleteSafe 或 .fullAccess；權限提升一律需明確指定。
---

# 如何在 macOS Swift 應用程式中內嵌 StackQL MCP 伺服器

StackQL 的 MCP 伺服器可以在原生 macOS 應用程式內執行，無需另外安裝，也沒有執行階段相依性。`stackql-mcp-swift` 套件會在官方 Swift MCP SDK 用戶端背後，透過 stdio 啟動已簽署的 StackQL 二進位檔。由於 `darwin-universal` 二進位檔以 Developer ID 簽署並經過 Apple 公證，您可以將它封裝在已簽署的 `.app` 中，並維持應用程式的公證有效。這正是 **CloudLens** 背後的模式，它是一個選單列雲端哨兵。

## 步驟 {#steps}

1. **新增套件**至 `Package.swift`：

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

需要 macOS 13+ 與 Swift 6.1（Xcode 16.3+）。唯一的相依性是官方的 Swift MCP SDK。

2. **啟動伺服器並呼叫工具**。`Options.mode` 預設為 `.readOnly`：

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

對於外部執行框架，`StackQLServer.resolveCommand(_:)` 會傳回解析後的啟動命令。

3. **選擇模式**。`Options.mode` 預設為 `.readOnly`。只有在應用程式需要佈建時，才設定 `.safe`、`.deleteSafe` 或 `.fullAccess`。權限提升需明確指定。

## 將經過公證的二進位檔封裝在已簽署的應用程式中 {#bundle-the-notarised-binary-in-a-signed-app}

`darwin-universal` 二進位檔以 Developer ID 簽署並經過 Apple 公證，因此可以封裝在已簽署的 macOS `.app` 中，同時維持應用程式本身的公證有效。二進位檔解析順序：

1. 明確指定的 `binaryOverride`。
2. 封裝在 `.app` 中的資源。
3. 位於 `~/.stackql/mcp-server-bin/` 的共用快取。
4. 下載並進行 sha256 驗證（設定 `allowDownload` 時）。

將經過公證的二進位檔放入應用程式資源，就會優先使用封裝的路徑，讓應用程式完全獨立，首次執行時不需下載。

## 示範應用程式 {#demo-app}

**CloudLens** 是一個選單列雲端哨兵，它內嵌經過公證的二進位檔，並執行一系列 `read_only` 檢查。

## 相關概念 {#related-concepts}

- [內嵌式 MCP：Swift 參考](/mcp/embedded/swift)：安裝、API 與二進位檔解析
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型與安全模式
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式與稽核內部機制
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：此做法促成的模式
