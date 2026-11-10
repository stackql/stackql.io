---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - eingebettet
  - go
description: Betten Sie den StackQL-MCP-Server über go:embed oder als Sidecar in eine Go-Anwendung ein
image: "/img/stackql-featured-image.png"
---

# Go

Betten Sie den StackQL-MCP-Server in eine kompilierte Go-Anwendung ein, entweder als heruntergeladenes Sidecar oder per Vendoring mit `go:embed` in Ihre Binärdatei eingebunden. Der Client ist das offizielle Go MCP SDK.

:::info
Die eingebettete Binärdatei wird beim ersten Start heruntergeladen und per sha256 verifiziert oder per Vendoring in Ihre App eingebunden. Siehe den [gemeinsamen Vertrag](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Schnellstart {#quick-start}

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

Die API ist `StartServer(ctx, Options{Binary, Mode})`; der zurückgegebene Client stammt aus dem offiziellen Go MCP SDK.

* * *

## Modi {#modes}

Der Server startet standardmäßig im `read_only`-Modus. Setzen Sie `Options.Mode`, um auf `safe`, `delete_safe` oder `full_access` zu eskalieren. Die Eskalation ist explizit - der Server aktiviert keine Mutations- oder Lifecycle-Tools, sofern Sie sich nicht ausdrücklich dafür entscheiden.

* * *

## Vendoring / Einbetten {#vendoring--embedding}

Binden Sie die Binärdatei per Vendoring mit `go:embed` über einen `go:generate`-Fetch-Schritt ein; so entsteht eine einzelne eigenständige ausführbare Datei:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

Der Fetch-Schritt lädt die signierte Binärdatei für die Zielplattform herunter, verifiziert sie per sha256 und legt sie dort ab, wo `go:embed` sie in Ihren Build aufnehmen kann. Zur Laufzeit gibt `StackqlMCPBinary()` die eingebettete Binärdatei zurück, sodass kein Download beim ersten Start nötig ist.

* * *

## Demo-App {#demo-app}

**sandboxctl** ist ein Infrastruktur-Concierge: ein eingebetteter Server plus eine Claude-Agentenschleife in einer Binärdatei, die kurzlebige Cloud-Sandboxes plant, auf Kosten prüft und hinter einem Freigabe-Gate bereitstellt.

* * *

## Repository-Link {#repo-link}

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
