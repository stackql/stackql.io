---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 嵌入式
  - swift
  - macos
description: 将经过公证的 StackQL MCP 服务器二进制文件打包到已签名的 macOS 应用中
image: "/img/stackql-featured-image.png"
---

# Swift

将 StackQL MCP 服务器嵌入 macOS 应用。已签名并经过公证的二进制文件可以打包到你的 `.app` 中，客户端是官方的 Swift MCP SDK。

:::info
嵌入的二进制文件在首次运行时下载并经 sha256 校验，或以 vendoring 方式打包进你的应用。参阅[通用契约](/mcp/embedded#common-contract)。
:::

* * *

## 安装 {#install}

将该包添加到你的 `Package.swift`：

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

需要 macOS 13+ 和 Swift 6.1（Xcode 16.3+）。唯一的依赖是官方的 Swift MCP SDK。

* * *

## 快速入门 {#quick-start}

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

API 为 `StackQLServer.start(Options)`、`.listToolNames()`、`.call(_:_:)` 和 `.stop()`。`Options` 包含 `mode`、`auth`、`binaryOverride` 和 `allowDownload`。对于外部 harness，`StackQLServer.resolveCommand(_:)` 返回解析后的启动命令。

* * *

## 模式 {#modes}

`Options.mode` 默认为 `.readOnly`。将其设置为 `.safe`、`.deleteSafe` 或 `.fullAccess` 即可提升权限。权限提升必须显式进行：除非你明确选择启用，否则服务器不会启用变更或生命周期工具。

* * *

## Vendoring / 嵌入 {#vendoring--embedding}

`darwin-universal` 二进制文件使用 Developer ID 签名并经 Apple 公证，因此可以打包到已签名的 macOS `.app` 中，同时保持应用自身的公证有效。这是 Swift 库独有的特点：你发布的是一个自包含、经过公证的应用，无需在首次运行时下载。

二进制文件解析顺序：

1. 显式指定的 `binaryOverride`。
2. 打包在 `.app` 中的资源。
3. 位于 `~/.stackql/mcp-server-bin/` 的共享缓存。
4. 下载并进行 sha256 校验（设置了 `allowDownload` 时）。

* * *

## 演示应用 {#demo-app}

**CloudLens** 是一个菜单栏云哨兵，它嵌入经过公证的二进制文件，并运行一组 `read_only` 检查。

* * *

## 仓库链接 {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
