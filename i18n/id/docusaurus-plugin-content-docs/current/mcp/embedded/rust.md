---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - tersemat
  - rust
description: Sematkan server MCP StackQL di aplikasi Rust melalui sidecar atau vendoring dengan include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

Sematkan server MCP StackQL di aplikasi Rust, baik sebagai sidecar yang diunduh maupun disertakan dalam biner Anda melalui vendoring dengan `include_bytes!`. Kliennya adalah `rmcp`.

:::info
Biner tersemat diunduh dan diverifikasi dengan sha256 saat pertama kali dijalankan, atau disertakan dalam aplikasi Anda melalui vendoring. Lihat [kontrak umum](/mcp/embedded#common-contract).
:::

* * *

## Instalasi {#install}

Tambahkan crate `stackql-mcp`:

```toml
stackql-mcp = "0.1"
```

* * *

## Mulai cepat {#quick-start}

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

API library ini terdiri atas `StackqlMcp::builder()` dengan `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()`, dan `.shutdown()`, ditambah `.list_all_tools()`. MSRV adalah Rust 1.88.

* * *

## Mode {#modes}

Builder menggunakan `Mode::ReadOnly` secara default. Panggil `.mode()` dengan `Mode::Safe`, `Mode::DeleteSafe`, atau `Mode::FullAccess` untuk melakukan eskalasi. Eskalasi bersifat eksplisit - server tidak akan mengaktifkan alat mutasi atau siklus hidup kecuali Anda mengaktifkannya secara eksplisit.

* * *

## Vendoring / penyematan {#vendoring--embedding}

Dua fitur Cargo mengatur cara biner diperoleh:

- `sidecar` (default) - mengunduh, memverifikasi, dan menyimpan biner yang ditandatangani di cache saat pertama kali dijalankan.
- `vendored` - menyematkan biner dengan `include_bytes!` untuk menghasilkan satu file executable mandiri.

Biner yang disertakan melalui vendoring menambah sekitar 80 MB pada build.

* * *

## Aplikasi demo {#demo-app}

**auditron** adalah copilot kepatuhan di terminal: TUI yang menampilkan hasil lulus/gagal secara streaming beserta SQL yang digunakan dan penjelasan dari Claude, didistribusikan sebagai satu biner vendored berukuran ~80 MB.

* * *

## Tautan repositori {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
