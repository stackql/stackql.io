---
title: How to embed the StackQL MCP server in a Go application
description: Embed the StackQL MCP server inside a compiled Go binary using go:embed or sidecar mode, so an agentic Go app can run cloud queries and provisioning over SQL with no runtime dependency.
keywords: [stackql, mcp, go, golang, go:embed, embedded mcp server, model context protocol, go mcp sdk, agentic infrastructure, sandboxctl]
proficiencyLevel: Intermediate
faq:
  - question: How do I add an embedded StackQL MCP server to a Go app?
    answer: "Run `go get github.com/stackql/stackql-mcp-go`, then call `stackqlmcp.StartServer(ctx, stackqlmcp.Options{Binary: StackqlMCPBinary()})`. The returned client is the official Go MCP SDK client, so you call tools with `client.Session.CallTool`. On first run the signed binary is downloaded and sha256-verified into the shared cache at ~/.stackql/mcp-server-bin/, or vendored into your binary with go:embed."
  - question: Can I ship a single self-contained Go binary with StackQL embedded?
    answer: Yes. Add a go:generate fetch step (`//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main`) that downloads and sha256-verifies the platform binary, then go:embed pulls it into the build. At runtime StackqlMCPBinary() returns the embedded binary, so there is no first-run download and no separate install.
  - question: How do I keep an embedded StackQL MCP server read-only in Go?
    answer: The server starts in read_only mode by default, which refuses INSERT, UPDATE, DELETE, and EXEC. Set Options.Mode explicitly to safe, delete_safe, or full_access only when the app needs to provision. Escalation is always explicit - the server will not enable mutation or lifecycle tools unless you opt in.
  - question: Which MCP client does stackql-mcp-go use?
    answer: The official Go MCP SDK (github.com/modelcontextprotocol/go-sdk/mcp). StartServer returns a client whose Session you call directly, so you are not locking into a StackQL-specific client API.
---

# How to embed the StackQL MCP server in a Go application

StackQL's MCP server can run inside a compiled Go program with no `npx`, no separate install, and no runtime dependency. The `stackql-mcp-go` library spawns the signed StackQL binary over stdio behind the official Go MCP SDK client, so your Go app gets a governed, self-describing SQL interface to every cloud and SaaS provider in the StackQL registry. This is the pattern behind **sandboxctl**, an on-demand infrastructure concierge shipped as one binary.

## Steps

1. **Add the library:**

```
go get github.com/stackql/stackql-mcp-go
```

2. **Start the embedded server and call a tool.** The server starts in `read_only` mode by default:

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

3. **Choose a mode.** `Options.Mode` defaults to `read_only`. Set it to `safe`, `delete_safe`, or `full_access` only when the app needs to provision. Escalation is explicit.

## Ship a single binary with go:embed

For a self-contained executable, add a `go:generate` fetch step that downloads and sha256-verifies the platform binary, then `go:embed` it into the build:

```
//go:generate go run github.com/stackql/stackql-mcp-go/cmd/stackql-mcp-fetch -platform auto -package main
```

At runtime `StackqlMCPBinary()` returns the embedded binary, so there is no first-run download. Without vendoring, the binary is downloaded and verified into the shared cache at `~/.stackql/mcp-server-bin/` on first run.

## Demo app

**sandboxctl** is an infrastructure concierge: an embedded StackQL MCP server plus a Claude agent loop in one Go binary that plans, cost-checks, and provisions ephemeral cloud sandboxes behind an approval gate.

## Related concepts

- [Embedded MCP: Go reference](/docs/mcp/embedded/go) - install, API, and modes
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the MCP model and safety modes
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, and audit internals
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this enables
