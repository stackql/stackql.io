---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embedded
  - rust
description: Embed the StackQL MCP server in a Rust application via sidecar or include_bytes! vendoring
image: "/img/stackql-featured-image.png"
---

# Rust

Embed the StackQL MCP server in a Rust application, either as a downloaded sidecar or vendored into your binary with `include_bytes!`. The client is `rmcp`.

:::info
The embedded binary is downloaded and sha256-verified on first run, or vendored into your app. See the [common contract](/docs/mcp/embedded#common-contract).
:::

* * *

## Install

Add the `stackql-mcp` crate:

```toml
stackql-mcp = "0.1"
```

* * *

## Quick start

```rust
use stackql_mcp::{Mode, StackqlMcp};

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let server = StackqlMcp::builder()
        .mode(Mode::ReadOnly)
        .auth(serde_json::json!({"github": {"type": "null_auth"}}))
        .start()
        .await?;
    let tools = server.list_all_tools().await?;
    println!("{} tools available", tools.len());
    server.shutdown().await?;
    Ok(())
}
```

The API is `StackqlMcp::builder()` with `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()`, and `.shutdown()`, plus `.list_all_tools()`. MSRV is Rust 1.88.

* * *

## Modes

The builder defaults to `Mode::ReadOnly`. Call `.mode()` with `Mode::Safe`, `Mode::DeleteSafe`, or `Mode::FullAccess` to escalate. Escalation is explicit - the server will not enable mutation or lifecycle tools unless you opt in.

* * *

## Vendoring / embedding

Two feature flags control how the binary is sourced:

- `sidecar` (default) - download, verify, and cache the signed binary on first run.
- `vendored` - embed the binary with `include_bytes!` for a single self-contained executable.

The vendored binary adds roughly 80 MB to the build.

* * *

## Demo app

**auditron** is a terminal compliance copilot: a TUI that streams pass/fail results with the SQL shown and Claude explanations, shipped as an ~80 MB vendored single binary.

* * *

## Repo link

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
