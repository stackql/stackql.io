---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - integrado
  - rust
description: Integre el servidor MCP de StackQL en una aplicación Rust como sidecar o mediante vendoring con include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

Integre el servidor MCP de StackQL en una aplicación Rust, ya sea como un sidecar descargado o incorporado a su binario mediante vendoring con `include_bytes!`. El cliente es `rmcp`.

:::info
El binario integrado se descarga y se verifica con sha256 en la primera ejecución, o se incorpora a su aplicación mediante vendoring. Consulte el [contrato común](/mcp/embedded#common-contract).
:::

* * *

## Instalación {#install}

Agregue el crate `stackql-mcp`:

```toml
stackql-mcp = "0.1"
```

* * *

## Inicio rápido {#quick-start}

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

La API es `StackqlMcp::builder()` con `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()` y `.shutdown()`, además de `.list_all_tools()`. La MSRV es Rust 1.88.

* * *

## Modos {#modes}

El builder usa `Mode::ReadOnly` de forma predeterminada. Llame a `.mode()` con `Mode::Safe`, `Mode::DeleteSafe` o `Mode::FullAccess` para elevar el modo. La elevación de modo es explícita - el servidor no habilita las herramientas de mutación ni las de ciclo de vida a menos que usted las active explícitamente.

* * *

## Vendoring / integración {#vendoring--embedding}

Dos opciones de funcionalidad controlan cómo se obtiene el binario:

- `sidecar` (predeterminada) - descargar, verificar y almacenar en caché el binario firmado en la primera ejecución.
- `vendored` - integrar el binario con `include_bytes!` para obtener un único ejecutable autónomo.

El binario incorporado mediante vendoring añade unos 80 MB al build.

* * *

## Aplicación de demostración {#demo-app}

**auditron** es un copiloto de cumplimiento para terminal: una TUI que transmite resultados de éxito o fallo con el SQL visible y explicaciones de Claude, distribuida como un único binario vendored de ~80 MB.

* * *

## Enlace al repositorio {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
