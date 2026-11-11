---
title: 如何在 Rust 应用中嵌入 StackQL MCP 服务器
description: 使用 sidecar 功能或 include_bytes! vendoring 将 StackQL MCP 服务器嵌入 Rust 二进制文件，使 Rust 应用无需运行时依赖即可通过 SQL 执行云查询和预配。
keywords: [stackql, mcp, rust, rmcp, include_bytes, 嵌入式 MCP 服务器, model context protocol, tokio, 智能体基础设施, auditron]
proficiencyLevel: Intermediate
faq:
  - question: 如何为 Rust 应用添加嵌入式 StackQL MCP 服务器？
    answer: 添加 `stackql-mcp` crate（stackql-mcp = "0.1"），然后使用 `StackqlMcp::builder().mode(Mode::ReadOnly).start().await?` 构建。客户端是 rmcp。默认情况下，sidecar 功能在首次运行时下载已签名的二进制文件，经 sha256 校验后存入位于 ~/.stackql/mcp-server-bin/ 的共享缓存。
  - question: 能否发布嵌入了 StackQL 的单个自包含 Rust 二进制文件？
    answer: 可以。启用 `vendored` 功能，它使用 include_bytes! 嵌入 StackQL 二进制文件，使整个 MCP 服务器位于你的可执行文件内。默认的 `sidecar` 功能则在运行时下载、校验并缓存二进制文件。vendored 二进制文件会使构建体积增加约 80 MB。
  - question: 如何在 Rust 中让嵌入式 StackQL MCP 服务器保持只读？
    answer: 在构建器上调用 .mode(Mode::ReadOnly)，这也是默认值。ReadOnly 拒绝 INSERT、UPDATE、DELETE 和 EXEC。仅当应用需要预配资源时，才提升到 Mode::Safe、Mode::DeleteSafe 或 Mode::FullAccess，权限提升始终需要显式进行。
  - question: stackql-mcp 支持的最低 Rust 版本是多少？
    answer: MSRV 为 Rust 1.88。该 crate 是异步的，基于 tokio 构建。
---

# 如何在 Rust 应用中嵌入 StackQL MCP 服务器

StackQL 的 MCP 服务器可以在 Rust 二进制文件内运行，无需 `npx`，无需单独安装，也没有运行时依赖。`stackql-mcp` crate 在 `rmcp` 客户端的背后通过 stdio 启动已签名的 StackQL 二进制文件，因此你的 Rust 应用可以获得一个受治理、自描述的 SQL 接口，用于访问 StackQL 注册表中的每个云和 SaaS Provider。这就是 **auditron** 所采用的模式，它是一个终端合规助手，以单个二进制文件发布。

## 步骤 {#steps}

1. **添加 crate** 到 `Cargo.toml`：

```toml
stackql-mcp = "0.1"
```

2. **构建服务器并列出工具。** 构建器默认使用 `Mode::ReadOnly`：

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

构建器还提供 `.binary()`、`.bundle_path()`、`.approot()` 和 `.command()`。MSRV 为 Rust 1.88。

3. **选择模式。** `.mode()` 默认为 `Mode::ReadOnly`。仅当应用需要预配资源时，才设置 `Mode::Safe`、`Mode::DeleteSafe` 或 `Mode::FullAccess`。权限提升必须显式进行。

## 使用 include_bytes! 发布单个二进制文件 {#ship-a-single-binary-with-include_bytes}

两个功能标志控制二进制文件的获取方式：

- `sidecar`（默认）：在首次运行时下载、校验并缓存已签名的二进制文件。
- `vendored`：使用 `include_bytes!` 嵌入二进制文件，生成单个自包含的可执行文件。

vendored 二进制文件会使构建体积增加约 80 MB。

## 演示应用 {#demo-app}

**auditron** 是一个终端合规助手：它是一个 TUI，以流式方式输出通过/失败结果，同时显示所用的 SQL 和 Claude 的解释，以约 80 MB 的 vendored 单个二进制文件发布。

## 相关概念 {#related-concepts}

- [嵌入式 MCP：Rust 参考](/mcp/embedded/rust)：安装、API 和功能标志
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型和安全模式
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式和审计的内部机制
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：由此实现的模式
