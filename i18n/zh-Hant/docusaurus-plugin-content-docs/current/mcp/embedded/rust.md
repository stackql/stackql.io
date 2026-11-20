---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 內嵌式
  - rust
description: 透過 sidecar 或 include_bytes! vendoring，在 Rust 應用程式中內嵌 StackQL MCP 伺服器
image: "/img/stackql-featured-image.png"
---

# Rust

在 Rust 應用程式中內嵌 StackQL MCP 伺服器，可以是下載的 sidecar，也可以使用 `include_bytes!` 透過 vendoring 納入您的二進位檔。用戶端是 `rmcp`。

:::info
內嵌的二進位檔會在首次執行時下載並經過 sha256 驗證，或透過 vendoring 納入您的應用程式。請參閱[共同契約](/mcp/embedded#common-contract)。
:::

* * *

## 安裝 {#install}

加入 `stackql-mcp` crate：

```toml
stackql-mcp = "0.1"
```

* * *

## 快速入門 {#quick-start}

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

API 為 `StackqlMcp::builder()`，搭配 `.mode()`、`.auth()`、`.binary()`、`.bundle_path()`、`.approot()`、`.command()`、`.start()` 與 `.shutdown()`，另有 `.list_all_tools()`。MSRV 為 Rust 1.88。

* * *

## 模式 {#modes}

建立器預設為 `Mode::ReadOnly`。若要提升權限，請以 `Mode::Safe`、`Mode::DeleteSafe` 或 `Mode::FullAccess` 呼叫 `.mode()`。權限提升必須明確指定：除非您選擇加入，否則伺服器不會啟用變更或生命週期工具。

* * *

## Vendoring / 內嵌 {#vendoring--embedding}

有兩個功能旗標控制二進位檔的取得方式：

- `sidecar`（預設）：在首次執行時下載、驗證並快取已簽署的二進位檔。
- `vendored`：使用 `include_bytes!` 內嵌二進位檔，產生單一的獨立可執行檔。

vendored 二進位檔會讓建置增加約 80 MB。

* * *

## 示範應用程式 {#demo-app}

**auditron** 是終端機合規性 copilot：一個 TUI，以串流方式顯示通過/失敗結果，並附上所執行的 SQL 與 Claude 的說明，以約 80 MB 的 vendored 單一二進位檔形式提供。

* * *

## 存放庫連結 {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
