---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - eingebettet
  - rust
description: Betten Sie den StackQL-MCP-Server per Sidecar oder include_bytes!-Vendoring in eine Rust-Anwendung ein
image: "/img/stackql-featured-image.png"
---

# Rust

Betten Sie den StackQL-MCP-Server in eine Rust-Anwendung ein, entweder als heruntergeladenes Sidecar oder per Vendoring mit `include_bytes!` in Ihre Binärdatei eingebunden. Der Client ist `rmcp`.

:::info
Die eingebettete Binärdatei wird beim ersten Start heruntergeladen und per sha256 verifiziert oder per Vendoring in Ihre App eingebunden. Siehe den [gemeinsamen Vertrag](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

Fügen Sie das Crate `stackql-mcp` hinzu:

```toml
stackql-mcp = "0.1"
```

* * *

## Schnellstart {#quick-start}

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

Die API besteht aus `StackqlMcp::builder()` mit `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()` und `.shutdown()` sowie `.list_all_tools()`. Die MSRV ist Rust 1.88.

* * *

## Modi {#modes}

Der Builder verwendet standardmäßig `Mode::ReadOnly`. Rufen Sie `.mode()` mit `Mode::Safe`, `Mode::DeleteSafe` oder `Mode::FullAccess` auf, um zu eskalieren. Die Eskalation ist explizit - der Server aktiviert keine Mutations- oder Lifecycle-Tools, sofern Sie sich nicht ausdrücklich dafür entscheiden.

* * *

## Vendoring / Einbetten {#vendoring--embedding}

Zwei Feature-Flags steuern, woher die Binärdatei bezogen wird:

- `sidecar` (Standard) - lädt die signierte Binärdatei beim ersten Start herunter, verifiziert sie und legt sie im Cache ab.
- `vendored` - bettet die Binärdatei mit `include_bytes!` ein und erzeugt eine einzelne eigenständige ausführbare Datei.

Die per Vendoring eingebundene Binärdatei vergrößert den Build um etwa 80 MB.

* * *

## Demo-App {#demo-app}

**auditron** ist ein Compliance-Copilot für das Terminal: eine TUI, die Pass/Fail-Ergebnisse mit dem zugehörigen SQL und Erklärungen von Claude streamt, ausgeliefert als einzelne, per Vendoring gebaute Binärdatei von ~80 MB.

* * *

## Repository-Link {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
