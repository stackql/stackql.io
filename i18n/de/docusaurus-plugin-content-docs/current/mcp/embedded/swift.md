---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - eingebettet
  - swift
  - macos
description: Bündeln Sie die notarisierte Binärdatei des StackQL-MCP-Servers in einer signierten macOS-App
image: "/img/stackql-featured-image.png"
---

# Swift

Betten Sie den StackQL-MCP-Server in eine macOS-Anwendung ein. Die signierte, notarisierte Binärdatei kann in Ihrer `.app` gebündelt werden, und der Client ist das offizielle Swift MCP SDK.

:::info
Die eingebettete Binärdatei wird beim ersten Start heruntergeladen und per sha256 verifiziert oder per Vendoring in Ihre App eingebunden. Siehe den [gemeinsamen Vertrag](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

Fügen Sie das Paket zu Ihrer `Package.swift` hinzu:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Erfordert macOS 13+ und Swift 6.1 (Xcode 16.3+). Die einzige Abhängigkeit ist das offizielle Swift MCP SDK.

* * *

## Schnellstart {#quick-start}

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

Die API besteht aus `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)` und `.stop()`. `Options` enthält `mode`, `auth`, `binaryOverride` und `allowDownload`. Für externe Harnesses gibt `StackQLServer.resolveCommand(_:)` den aufgelösten Startbefehl zurück.

* * *

## Modi {#modes}

`Options.mode` ist standardmäßig `.readOnly`. Setzen Sie es auf `.safe`, `.deleteSafe` oder `.fullAccess`, um zu eskalieren. Die Eskalation ist explizit - der Server aktiviert keine Mutations- oder Lifecycle-Tools, sofern Sie sich nicht ausdrücklich dafür entscheiden.

* * *

## Vendoring / Einbetten {#vendoring--embedding}

Die `darwin-universal`-Binärdatei ist mit Developer ID signiert und von Apple notarisiert, sodass sie in einer signierten macOS-`.app` gebündelt werden kann, ohne dass die Notarisierung der App selbst ihre Gültigkeit verliert. Das ist eine Besonderheit der Swift-Bibliothek - Sie liefern eine einzige eigenständige, notarisierte Anwendung ohne Download beim ersten Start aus.

Auflösungsreihenfolge der Binärdatei:

1. Explizites `binaryOverride`.
2. Gebündelte `.app`-Ressourcen.
3. Gemeinsamer Cache unter `~/.stackql/mcp-server-bin/`.
4. Download mit sha256-Verifizierung (wenn `allowDownload` gesetzt ist).

* * *

## Demo-App {#demo-app}

**CloudLens** ist ein Cloud-Wächter für die Menüleiste, der die notarisierte Binärdatei einbettet und eine `read_only`-Prüfsuite ausführt.

* * *

## Repository-Link {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
