---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embedded
  - gleam
  - erlang
  - beam
description: Embed the StackQL MCP server in a Gleam application on the Erlang/BEAM target
image: "/img/stackql-featured-image.png"
---

# Gleam

Embed the StackQL MCP server in a Gleam application on the Erlang/BEAM target. The library integrates with OTP supervision, downloads and verifies the signed binary, and spawns it over stdio.

:::info
The embedded binary is downloaded and sha256-verified on first run, or vendored into your app. See the [common contract](/docs/mcp/embedded#common-contract).
:::

* * *

## Install

```
gleam add stackql_mcp
```

Targets Erlang/BEAM only (not the JavaScript target). The server is exposed as an OTP child via `child_spec()` so it can sit inside your own supervision tree.

* * *

## Quick start

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

The API is `stackql_mcp.start()`, `stop()`, and `child_spec()` for lifecycle; `list_tools()` and `call_tool()` for operations (with `supervised_list_tools()` and `supervised_call_tool()` variants for use under a supervisor); and `Config`, `default_config()`, and `auth_for()` for configuration.

* * *

## Modes

The mode is set on `Config.mode` and defaults to `read_only`. Set it to `safe`, `delete_safe`, or `full_access` to escalate:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

Escalation is explicit - the server will not enable mutation or lifecycle tools unless you opt in.

* * *

## Vendoring / embedding

On start the library downloads the platform's StackQL bundle, verifies it against baked-in sha256 pins, and caches it in the shared family cache. Resolution precedence:

1. Explicit `Config.binary`.
2. `STACKQL_MCP_BIN` environment variable.
3. Shared cache at `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

For offline or air-gapped deployments, set `STACKQL_MCP_BUNDLE` to point at a local bundle.

* * *

## Demo app

**pipewatch** is the planned demo for this library. It is on the roadmap and not yet implemented.

* * *

## Repo link

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
