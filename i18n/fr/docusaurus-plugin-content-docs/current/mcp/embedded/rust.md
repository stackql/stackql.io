---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embarqué
  - rust
description: Embarquez le serveur MCP StackQL dans une application Rust en sidecar ou par vendoring avec include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

Embarquez le serveur MCP StackQL dans une application Rust, soit sous forme de sidecar téléchargé, soit vendored dans votre binaire avec `include_bytes!`. Le client est `rmcp`.

:::info
Le binaire embarqué est téléchargé et vérifié par sha256 à la première exécution, ou vendored dans votre application. Consultez le [contrat commun](/mcp/embedded#common-contract).
:::

* * *

## Installation {#install}

Ajoutez la crate `stackql-mcp` :

```toml
stackql-mcp = "0.1"
```

* * *

## Démarrage rapide {#quick-start}

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

L'API se compose de `StackqlMcp::builder()` avec `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()` et `.shutdown()`, ainsi que de `.list_all_tools()`. La MSRV est Rust 1.88.

* * *

## Modes {#modes}

Le builder utilise `Mode::ReadOnly` par défaut. Appelez `.mode()` avec `Mode::Safe`, `Mode::DeleteSafe` ou `Mode::FullAccess` pour élever le niveau d'accès. L'élévation est explicite - le serveur n'active les outils de mutation ou de cycle de vie que si vous les activez explicitement.

* * *

## Vendoring / intégration {#vendoring--embedding}

Deux fonctionnalités Cargo déterminent la façon dont le binaire est obtenu :

- `sidecar` (par défaut) - télécharge, vérifie et met en cache le binaire signé à la première exécution.
- `vendored` - embarque le binaire avec `include_bytes!` pour obtenir un exécutable unique et autonome.

Le binaire vendored ajoute environ 80 MB au build.

* * *

## Application de démonstration {#demo-app}

**auditron** est un copilote de conformité pour le terminal : une TUI qui diffuse en continu les résultats de réussite ou d'échec, avec le SQL affiché et des explications de Claude, livrée sous la forme d'un binaire unique vendored d'environ 80 MB.

* * *

## Lien du dépôt {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
