---
title: How to embed the StackQL MCP server in a Gleam application
description: Embed the StackQL MCP server in a Gleam app on the Erlang/BEAM target, supervised by OTP, so a BEAM service can run cloud queries over SQL with no runtime dependency.
keywords: [stackql, mcp, gleam, erlang, beam, otp, supervision tree, embedded mcp server, model context protocol, agentic infrastructure, pipewatch]
proficiencyLevel: Intermediate
faq:
  - question: How do I add an embedded StackQL MCP server to a Gleam app?
    answer: Run `gleam add stackql_mcp`, then call `stackql_mcp.start(...)` with a Config built from `stackql_mcp.default_config()`. The library targets Erlang/BEAM only (not the JavaScript target) and exposes the server as an OTP child via child_spec(). On start it downloads and sha256-verifies the signed binary into the shared cache at ~/.stackql/mcp-server-bin/.
  - question: Does the Gleam library integrate with OTP supervision?
    answer: Yes. The server is exposed as an OTP child via child_spec() so it can sit inside your own supervision tree, and there are supervised_list_tools() and supervised_call_tool() variants for calling tools under a supervisor. The library targets the Erlang/BEAM runtime only.
  - question: How do I keep an embedded StackQL MCP server read-only in Gleam?
    answer: "The mode is set on Config.mode and defaults to read_only, which refuses INSERT, UPDATE, DELETE, and EXEC. Build a Config with `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)` to escalate to safe, delete_safe, or full_access - escalation is always explicit."
  - question: How does the Gleam library resolve the StackQL binary?
    answer: Explicit Config.binary first, then the STACKQL_MCP_BIN environment variable, then the shared cache at ~/.stackql/mcp-server-bin/. For offline or air-gapped deployments, set STACKQL_MCP_BUNDLE to point at a local bundle. The downloaded bundle is verified against baked-in sha256 pins.
---

# How to embed the StackQL MCP server in a Gleam application

StackQL's MCP server can run inside a Gleam application on the Erlang/BEAM target with no separate install and no runtime dependency. The `stackql_mcp` library spawns the signed StackQL binary over stdio and integrates with OTP supervision, so a BEAM service gets a governed, self-describing SQL interface to every cloud and SaaS provider in the StackQL registry. The server is exposed as an OTP child via `child_spec()` so it can sit inside your own supervision tree.

## Steps

1. **Add the library:**

```
gleam add stackql_mcp
```

Targets Erlang/BEAM only (not the JavaScript target).

2. **Start the server and list tools.** `Config.mode` defaults to `read_only`:

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

Use `child_spec()` to place the server under a supervisor, with `supervised_list_tools()` and `supervised_call_tool()` for supervised calls.

3. **Choose a mode.** `Config.mode` defaults to `read_only`. Build `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)` to escalate to `safe`, `delete_safe`, or `full_access`. Escalation is explicit.

## Binary sourcing

On start the library downloads the platform's StackQL bundle, verifies it against baked-in sha256 pins, and caches it in the shared family cache. Resolution precedence: explicit `Config.binary`, then `STACKQL_MCP_BIN`, then the shared cache at `~/.stackql/mcp-server-bin/`. For offline or air-gapped deployments, set `STACKQL_MCP_BUNDLE` to point at a local bundle.

## Demo app

**pipewatch** is the planned demo for this library. It is on the roadmap and not yet implemented.

## Related concepts

- [Embedded MCP: Gleam reference](/docs/mcp/embedded/gleam) - install, API, and binary sourcing
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the MCP model and safety modes
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, and audit internals
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this enables
