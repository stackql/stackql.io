---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 內嵌式
  - swift
  - macos
description: 將經過公證的 StackQL MCP 伺服器二進位檔打包到已簽署的 macOS 應用程式中
image: "/img/stackql-featured-image.png"
---

# Swift

在 macOS 應用程式中內嵌 StackQL MCP 伺服器。已簽署且經過公證的二進位檔可以打包到您的 `.app` 中，用戶端則是官方的 Swift MCP SDK。

:::info
內嵌的二進位檔會在首次執行時下載並經過 sha256 驗證，或透過 vendoring 納入您的應用程式。請參閱[共同契約](/mcp/embedded#common-contract)。
:::

* * *

## 安裝 {#install}

將套件加入您的 `Package.swift`：

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

需要 macOS 13+ 與 Swift 6.1（Xcode 16.3+）。唯一的相依套件是官方的 Swift MCP SDK。

* * *

## 快速入門 {#quick-start}

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

API 包括 `StackQLServer.start(Options)`、`.listToolNames()`、`.call(_:_:)` 與 `.stop()`。`Options` 包含 `mode`、`auth`、`binaryOverride` 與 `allowDownload`。對於外部執行框架，`StackQLServer.resolveCommand(_:)` 會傳回解析後的啟動命令。

* * *

## 模式 {#modes}

`Options.mode` 預設為 `.readOnly`。若要提升權限，請將其設為 `.safe`、`.deleteSafe` 或 `.fullAccess`。權限提升必須明確指定：除非您選擇加入，否則伺服器不會啟用變更或生命週期工具。

* * *

## Vendoring / 內嵌 {#vendoring--embedding}

`darwin-universal` 二進位檔已使用 Developer ID 簽署並經過 Apple 公證，因此可以打包到已簽署的 macOS `.app` 中，同時讓應用程式本身的公證保持有效。這是 Swift 程式庫獨有的功能：您發行的是單一、獨立且經過公證的應用程式，首次執行時不需要下載。

二進位檔解析順序：

1. 明確指定的 `binaryOverride`。
2. 打包在 `.app` 中的資源。
3. 位於 `~/.stackql/mcp-server-bin/` 的共用快取。
4. 下載並進行 sha256 驗證（設定 `allowDownload` 時）。

* * *

## 示範應用程式 {#demo-app}

**CloudLens** 是一個選單列雲端哨兵，內嵌經過公證的二進位檔，並執行一組 `read_only` 檢查。

* * *

## 存放庫連結 {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
