---
title: How to embed the StackQL MCP server in a Rust application
description: Embed the StackQL MCP server inside a Rust binary using the sidecar feature or include_bytes! vendoring, so a Rust app can run cloud queries and provisioning over SQL with no runtime dependency.
keywords: [stackql, mcp, rust, rmcp, include_bytes, embedded mcp server, model context protocol, tokio, agentic infrastructure, auditron]
proficiencyLevel: Intermediate
faq:
  - question: How do I add an embedded StackQL MCP server to a Rust app?
    answer: Add the `stackql-mcp` crate (stackql-mcp = "0.1"), then build with `StackqlMcp::builder().mode(Mode::ReadOnly).start().await?`. The client is rmcp. By default the sidecar feature downloads and sha256-verifies the signed binary into the shared cache at ~/.stackql/mcp-server-bin/ on first run.
  - question: Can I ship a single self-contained Rust binary with StackQL embedded?
    answer: Yes. Enable the `vendored` feature, which embeds the StackQL binary with include_bytes! so the whole MCP server lives inside your executable. The default `sidecar` feature downloads, verifies, and caches the binary at runtime instead. The vendored binary adds roughly 80 MB to the build.
  - question: How do I keep an embedded StackQL MCP server read-only in Rust?
    answer: Call .mode(Mode::ReadOnly) on the builder, which is also the default. ReadOnly refuses INSERT, UPDATE, DELETE, and EXEC. Escalate to Mode::Safe, Mode::DeleteSafe, or Mode::FullAccess only when the app needs to provision - escalation is always explicit.
  - question: What is the minimum supported Rust version for stackql-mcp?
    answer: MSRV is Rust 1.88. The crate is async and built on tokio.
---

# How to embed the StackQL MCP server in a Rust application

StackQL's MCP server can run inside a Rust binary with no `npx`, no separate install, and no runtime dependency. The `stackql-mcp` crate spawns the signed StackQL binary over stdio behind an `rmcp` client, so your Rust app gets a governed, self-describing SQL interface to every cloud and SaaS provider in the StackQL registry. This is the pattern behind **auditron**, a terminal compliance copilot shipped as one binary.

## Steps

1. **Add the crate** to `Cargo.toml`:

```toml
stackql-mcp = "0.1"
```

2. **Build the server and list tools.** The builder defaults to `Mode::ReadOnly`:

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

The builder also exposes `.binary()`, `.bundle_path()`, `.approot()`, and `.command()`. MSRV is Rust 1.88.

3. **Choose a mode.** `.mode()` defaults to `Mode::ReadOnly`. Set `Mode::Safe`, `Mode::DeleteSafe`, or `Mode::FullAccess` only when the app needs to provision. Escalation is explicit.

## Ship a single binary with include_bytes!

Two feature flags control how the binary is sourced:

- `sidecar` (default) - download, verify, and cache the signed binary on first run.
- `vendored` - embed the binary with `include_bytes!` for a single self-contained executable.

The vendored binary adds roughly 80 MB to the build.

## Demo app

**auditron** is a terminal compliance copilot: a TUI that streams pass/fail results with the SQL shown and Claude explanations, shipped as an ~80 MB vendored single binary.

## Related concepts

- [Embedded MCP: Rust reference](/docs/mcp/embedded/rust) - install, API, and feature flags
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the MCP model and safety modes
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, and audit internals
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this enables
