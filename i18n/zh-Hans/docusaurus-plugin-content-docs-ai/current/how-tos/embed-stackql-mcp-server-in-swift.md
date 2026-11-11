---
title: 如何在 macOS Swift 应用中嵌入 StackQL MCP 服务器
description: 将经过公证的 StackQL MCP 服务器二进制文件打包到已签名的 macOS Swift 应用中，使原生菜单栏或桌面应用可以通过 SQL 执行云查询，同时保持其公证有效。
keywords: [stackql, mcp, swift, macos, swiftpm, 公证, 代码签名, 嵌入式 MCP 服务器, model context protocol, swift mcp sdk, cloudlens]
proficiencyLevel: Intermediate
faq:
  - question: 如何为 Swift macOS 应用添加嵌入式 StackQL MCP 服务器？
    answer: 添加来自 github.com/stackql/stackql-mcp-swift 的 Swift 包（从 0.1.0 版本起），然后在 Options.mode 设置为 .readOnly 的情况下调用 `StackQLServer.start(options)`。客户端是官方的 Swift MCP SDK。需要 macOS 13+ 和 Swift 6.1（Xcode 16.3+）。
  - question: 能否将 StackQL 打包到已签名并经过公证的 macOS 应用中？
    answer: 可以，这是 Swift 库独有的特点。darwin-universal 二进制文件使用 Developer ID 签名并经 Apple 公证，因此可以打包到你已签名的 .app 中，同时保持应用自身的公证有效。你发布的是一个自包含、经过公证的应用，无需在首次运行时下载。
  - question: Swift 库按什么顺序解析 StackQL 二进制文件？
    answer: 首先是显式指定的 binaryOverride，然后是打包在 .app 中的资源，再然后是位于 ~/.stackql/mcp-server-bin/ 的共享缓存，最后在设置了 allowDownload 时下载并进行 sha256 校验。将经过公证的二进制文件打包到应用资源中，意味着打包的路径优先，不会下载任何内容。
  - question: 如何在 Swift 中让嵌入式 StackQL MCP 服务器保持只读？
    answer: 设置 Options.mode = .readOnly，这是默认值。ReadOnly 拒绝 INSERT、UPDATE、DELETE 和 EXEC。仅当应用需要预配资源时，才提升到 .safe、.deleteSafe 或 .fullAccess，权限提升始终需要显式进行。
---

# 如何在 macOS Swift 应用中嵌入 StackQL MCP 服务器

StackQL 的 MCP 服务器可以在原生 macOS 应用内运行，无需单独安装，也没有运行时依赖。`stackql-mcp-swift` 包在官方 Swift MCP SDK 客户端的背后通过 stdio 启动已签名的 StackQL 二进制文件。由于 `darwin-universal` 二进制文件使用 Developer ID 签名并经 Apple 公证，你可以将它打包到已签名的 `.app` 中，并保持应用的公证有效。这就是 **CloudLens** 所采用的模式，它是一个菜单栏云哨兵。

## 步骤 {#steps}

1. **添加包**到 `Package.swift`：

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

需要 macOS 13+ 和 Swift 6.1（Xcode 16.3+）。唯一的依赖是官方的 Swift MCP SDK。

2. **启动服务器并调用工具。** `Options.mode` 默认为 `.readOnly`：

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

对于外部 harness，`StackQLServer.resolveCommand(_:)` 返回解析后的启动命令。

3. **选择模式。** `Options.mode` 默认为 `.readOnly`。仅当应用需要预配资源时，才设置 `.safe`、`.deleteSafe` 或 `.fullAccess`。权限提升必须显式进行。

## 将经过公证的二进制文件打包到已签名的应用中 {#bundle-the-notarised-binary-in-a-signed-app}

`darwin-universal` 二进制文件使用 Developer ID 签名并经 Apple 公证，因此可以打包到已签名的 macOS `.app` 中，同时保持应用自身的公证有效。二进制文件解析顺序：

1. 显式指定的 `binaryOverride`。
2. 打包在 `.app` 中的资源。
3. 位于 `~/.stackql/mcp-server-bin/` 的共享缓存。
4. 下载并进行 sha256 校验（设置了 `allowDownload` 时）。

将经过公证的二进制文件放入应用资源，打包的路径即优先：应用完全自包含，无需在首次运行时下载。

## 演示应用 {#demo-app}

**CloudLens** 是一个菜单栏云哨兵，它嵌入经过公证的二进制文件，并运行一组 `read_only` 检查。

## 相关概念 {#related-concepts}

- [嵌入式 MCP：Swift 参考](/mcp/embedded/swift)：安装、API 和二进制文件解析
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型和安全模式
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式和审计的内部机制
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：由此实现的模式
