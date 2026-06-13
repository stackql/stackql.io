---
title: How to embed the StackQL MCP server in a macOS Swift application
description: Bundle the notarised StackQL MCP server binary inside a signed macOS Swift app, so a native menu-bar or desktop app can run cloud queries over SQL while keeping its notarisation valid.
keywords: [stackql, mcp, swift, macos, swiftpm, notarisation, code signing, embedded mcp server, model context protocol, swift mcp sdk, cloudlens]
proficiencyLevel: Intermediate
faq:
  - question: How do I add an embedded StackQL MCP server to a Swift macOS app?
    answer: Add the Swift package from github.com/stackql/stackql-mcp-swift (from version 0.1.0), then call `StackQLServer.start(options)` with Options.mode set to .readOnly. The client is the official Swift MCP SDK. Requires macOS 13+ and Swift 6.1 (Xcode 16.3+).
  - question: Can I bundle StackQL inside a signed and notarised macOS app?
    answer: Yes, and this is unique to the Swift library. The darwin-universal binary is Developer ID signed and Apple-notarised, so it can be bundled inside your signed .app while keeping the app's own notarisation valid. You ship one self-contained, notarised application with no first-run download.
  - question: In what order does the Swift library resolve the StackQL binary?
    answer: Explicit binaryOverride first, then bundled .app resources, then the shared cache at ~/.stackql/mcp-server-bin/, then download with sha256 verification when allowDownload is set. Bundling the notarised binary in app resources means the bundled path wins and nothing is downloaded.
  - question: How do I keep an embedded StackQL MCP server read-only in Swift?
    answer: Set Options.mode = .readOnly, which is the default. ReadOnly refuses INSERT, UPDATE, DELETE, and EXEC. Escalate to .safe, .deleteSafe, or .fullAccess only when the app needs to provision - escalation is always explicit.
---

# How to embed the StackQL MCP server in a macOS Swift application

StackQL's MCP server can run inside a native macOS app with no separate install and no runtime dependency. The `stackql-mcp-swift` package spawns the signed StackQL binary over stdio behind the official Swift MCP SDK client. Because the `darwin-universal` binary is Developer ID signed and Apple-notarised, you can bundle it inside a signed `.app` and keep the app's notarisation valid - the pattern behind **CloudLens**, a menu-bar cloud sentinel.

## Steps

1. **Add the package** to `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Requires macOS 13+ and Swift 6.1 (Xcode 16.3+). The single dependency is the official Swift MCP SDK.

2. **Start the server and call a tool.** `Options.mode` defaults to `.readOnly`:

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

For external harnesses, `StackQLServer.resolveCommand(_:)` returns the resolved launch command.

3. **Choose a mode.** `Options.mode` defaults to `.readOnly`. Set `.safe`, `.deleteSafe`, or `.fullAccess` only when the app needs to provision. Escalation is explicit.

## Bundle the notarised binary in a signed app

The `darwin-universal` binary is Developer ID signed and Apple-notarised, so it can be bundled inside a signed macOS `.app` while keeping the app's own notarisation valid. Binary resolution order:

1. Explicit `binaryOverride`.
2. Bundled `.app` resources.
3. Shared cache at `~/.stackql/mcp-server-bin/`.
4. Download with sha256 verification (when `allowDownload` is set).

Put the notarised binary in app resources and the bundled path wins - the app is fully self-contained with no first-run download.

## Demo app

**CloudLens** is a menu-bar cloud sentinel that embeds the notarised binary and runs a `read_only` check suite.

## Related concepts

- [Embedded MCP: Swift reference](/docs/mcp/embedded/swift) - install, API, and binary resolution
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the MCP model and safety modes
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, and audit internals
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this enables
