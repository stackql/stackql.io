---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - incorporado
  - rust
description: Incorpore o servidor MCP do StackQL em uma aplicação Rust via sidecar ou vendoring com include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

Incorpore o servidor MCP do StackQL em uma aplicação Rust, seja como um sidecar baixado, seja incluído no seu binário por vendoring com `include_bytes!`. O cliente é o `rmcp`.

:::info
O binário incorporado é baixado e verificado com sha256 na primeira execução, ou incluído na sua aplicação por vendoring. Consulte o [contrato comum](/mcp/embedded#common-contract).
:::

* * *

## Instalação {#install}

Adicione o crate `stackql-mcp`:

```toml
stackql-mcp = "0.1"
```

* * *

## Início rápido {#quick-start}

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

A API é `StackqlMcp::builder()` com `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()` e `.shutdown()`, além de `.list_all_tools()`. A MSRV é o Rust 1.88.

* * *

## Modos {#modes}

O builder usa `Mode::ReadOnly` por padrão. Chame `.mode()` com `Mode::Safe`, `Mode::DeleteSafe` ou `Mode::FullAccess` para elevar o modo. A elevação é explícita - o servidor não habilita as ferramentas de mutação nem as de ciclo de vida, a menos que você as ative explicitamente.

* * *

## Vendoring / incorporação {#vendoring--embedding}

Duas opções de funcionalidade controlam a origem do binário:

- `sidecar` (padrão) - baixar, verificar e armazenar em cache o binário assinado na primeira execução.
- `vendored` - incorporar o binário com `include_bytes!` para obter um único executável autocontido.

O binário vendored adiciona cerca de 80 MB ao build.

* * *

## Aplicativo de demonstração {#demo-app}

**auditron** é um copiloto de conformidade para o terminal: uma TUI que transmite resultados de aprovação/reprovação com o SQL exibido e explicações do Claude, distribuída como um único binário vendored de ~80 MB.

* * *

## Link do repositório {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
