---
title: 如何在 Rust 應用程式中內嵌 StackQL MCP 伺服器
description: 使用 sidecar 功能或 include_bytes! vendoring 將 StackQL MCP 伺服器內嵌至 Rust 二進位檔，讓 Rust 應用程式無需執行階段相依性即可透過 SQL 執行雲端查詢與佈建。
keywords: [stackql, mcp, rust, rmcp, include_bytes, 內嵌式 mcp 伺服器, model context protocol, tokio, 代理式基礎架構, auditron]
proficiencyLevel: Intermediate
faq:
  - question: 如何將內嵌式 StackQL MCP 伺服器加入 Rust 應用程式？
    answer: 新增 `stackql-mcp` crate（stackql-mcp = "0.1"），然後使用 `StackqlMcp::builder().mode(Mode::ReadOnly).start().await?` 建立伺服器。用戶端是 rmcp。預設情況下，sidecar 功能會在首次執行時將已簽署的二進位檔下載到位於 ~/.stackql/mcp-server-bin/ 的共用快取並進行 sha256 驗證。
  - question: 能否發行內嵌 StackQL 的單一獨立式 Rust 二進位檔？
    answer: 可以。啟用 `vendored` 功能，它會以 include_bytes! 內嵌 StackQL 二進位檔，讓整個 MCP 伺服器位於您的可執行檔內。預設的 `sidecar` 功能則改為在執行階段下載、驗證並快取二進位檔。vendored 二進位檔會使建置增加約 80 MB。
  - question: 如何在 Rust 中讓內嵌式 StackQL MCP 伺服器保持唯讀？
    answer: 在建立器上呼叫 .mode(Mode::ReadOnly)，這也是預設值。ReadOnly 會拒絕 INSERT、UPDATE、DELETE 與 EXEC。只有在應用程式需要佈建時，才提升至 Mode::Safe、Mode::DeleteSafe 或 Mode::FullAccess；權限提升一律需明確指定。
  - question: stackql-mcp 的最低支援 Rust 版本是多少？
    answer: MSRV 為 Rust 1.88。此 crate 為非同步，並以 tokio 為基礎建構。
---

# 如何在 Rust 應用程式中內嵌 StackQL MCP 伺服器

StackQL 的 MCP 伺服器可以在 Rust 二進位檔內執行，不需要 `npx`、無需另外安裝，也沒有執行階段相依性。`stackql-mcp` crate 會在 `rmcp` 用戶端背後，透過 stdio 啟動已簽署的 StackQL 二進位檔，讓您的 Rust 應用程式取得受治理、可自我描述的 SQL 介面，存取 StackQL 登錄庫中的每個雲端與 SaaS Provider。這正是 **auditron** 背後的模式，它是以單一二進位檔發行的終端機合規性助手。

## 步驟 {#steps}

1. **新增 crate** 至 `Cargo.toml`：

```toml
stackql-mcp = "0.1"
```

2. **建立伺服器並列出工具**。建立器預設為 `Mode::ReadOnly`：

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

建立器還提供 `.binary()`、`.bundle_path()`、`.approot()` 與 `.command()`。MSRV 為 Rust 1.88。

3. **選擇模式**。`.mode()` 預設為 `Mode::ReadOnly`。只有在應用程式需要佈建時，才設定 `Mode::Safe`、`Mode::DeleteSafe` 或 `Mode::FullAccess`。權限提升需明確指定。

## 使用 include_bytes! 發行單一二進位檔 {#ship-a-single-binary-with-include_bytes}

兩個功能旗標控制二進位檔的取得方式：

- `sidecar`（預設）：在首次執行時下載、驗證並快取已簽署的二進位檔。
- `vendored`：以 `include_bytes!` 內嵌二進位檔，產生單一獨立式可執行檔。

vendored 二進位檔會使建置增加約 80 MB。

## 示範應用程式 {#demo-app}

**auditron** 是一個終端機合規性助手：這個 TUI 會以串流方式顯示通過/失敗結果，並附上所執行的 SQL 與 Claude 的說明，以約 80 MB 的 vendored 單一二進位檔發行。

## 相關概念 {#related-concepts}

- [內嵌式 MCP：Rust 參考](/mcp/embedded/rust)：安裝、API 與功能旗標
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型與安全模式
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式與稽核內部機制
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：此做法促成的模式
