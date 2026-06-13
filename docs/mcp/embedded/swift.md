---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embedded
  - swift
  - macos
description: Bundle the notarised StackQL MCP server binary inside a signed macOS app
image: "/img/stackql-featured-image.png"
---

# Swift

Embed the StackQL MCP server in a macOS application. The signed, notarised binary can be bundled inside your `.app`, and the client is the official Swift MCP SDK.

:::info
The embedded binary is downloaded and sha256-verified on first run, or vendored into your app. See the [common contract](/docs/mcp/embedded#common-contract).
:::

* * *

## Install

Add the package to your `Package.swift`:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Requires macOS 13+ and Swift 6.1 (Xcode 16.3+). The single dependency is the official Swift MCP SDK.

* * *

## Quick start

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

The API is `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)`, and `.stop()`. `Options` carries `mode`, `auth`, `binaryOverride`, and `allowDownload`. For external harnesses, `StackQLServer.resolveCommand(_:)` returns the resolved launch command.

* * *

## Modes

`Options.mode` defaults to `.readOnly`. Set it to `.safe`, `.deleteSafe`, or `.fullAccess` to escalate. Escalation is explicit - the server will not enable mutation or lifecycle tools unless you opt in.

* * *

## Vendoring / embedding

The `darwin-universal` binary is Developer ID signed and Apple-notarised, so it can be bundled inside a signed macOS `.app` while keeping the app's own notarisation valid. This is unique to the Swift library - you ship one self-contained, notarised application with no first-run download.

Binary resolution order:

1. Explicit `binaryOverride`.
2. Bundled `.app` resources.
3. Shared cache at `~/.stackql/mcp-server-bin/`.
4. Download with sha256 verification (when `allowDownload` is set).

* * *

## Demo app

**CloudLens** is a menu-bar cloud sentinel that embeds the notarised binary and runs a `read_only` check suite.

* * *

## Repo link

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
