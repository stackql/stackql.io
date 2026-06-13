---
title: Go
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embedded
  - go
description: Embed the StackQL MCP server in a Go application via go:embed or sidecar
image: "/img/stackql-featured-image.png"
---

# Go

Embed the StackQL MCP server in a compiled Go application, either as a downloaded sidecar or vendored into your binary with `go:embed`. The client is the official Go MCP SDK.

:::info
The embedded binary is downloaded and sha256-verified on first run, or vendored into your app. See the [common contract](/docs/mcp/embedded#common-contract).
:::

* * *

## Install

```
go get github.com/stackql/stackql-mcp-go
```

* * *

## Quick start

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

The API is `StartServer(ctx, Options{Binary, Mode})`; the returned client comes from the official Go MCP SDK.

* * *

## Modes

The server starts in `read_only` mode by default. Set `Options.Mode` to escalate to `safe`, `delete_safe`, or `full_access`. Escalation is explicit - the server will not enable mutation or lifecycle tools unless you opt in.

* * *

## Vendoring / embedding

Vendor the binary with `go:embed` via a `go:generate` fetch step, producing a single self-contained executable:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

The fetch step downloads and sha256-verifies the signed binary for the target platform, then writes it where `go:embed` can pull it into your build. At runtime `StackqlMCPBinary()` returns the embedded binary so there is no first-run download.

* * *

## Demo app

**sandboxctl** is an infrastructure concierge: an embedded server plus a Claude agent loop in one binary that plans, cost-checks, and provisions ephemeral cloud sandboxes behind an approval gate.

* * *

## Repo link

[github.com/stackql/stackql-mcp-go](https://github.com/stackql/stackql-mcp-go)
