---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 嵌入式
  - rust
description: 通过 sidecar 或 include_bytes! vendoring 将 StackQL MCP 服务器嵌入 Rust 应用
image: "/img/stackql-featured-image.png"
---

# Rust

将 StackQL MCP 服务器嵌入 Rust 应用，可作为下载的 sidecar 运行，或通过 `include_bytes!` 以 vendoring 方式打包进你的二进制文件。客户端是 `rmcp`。

:::info
嵌入的二进制文件在首次运行时下载并经 sha256 校验，或以 vendoring 方式打包进你的应用。参阅[通用契约](/mcp/embedded#common-contract)。
:::

* * *

## 安装 {#install}

添加 `stackql-mcp` crate：

```toml
stackql-mcp = "0.1"
```

* * *

## 快速入门 {#quick-start}

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

API 为 `StackqlMcp::builder()`，搭配 `.mode()`、`.auth()`、`.binary()`、`.bundle_path()`、`.approot()`、`.command()`、`.start()` 和 `.shutdown()`，另有 `.list_all_tools()`。MSRV 为 Rust 1.88。

* * *

## 模式 {#modes}

构建器默认使用 `Mode::ReadOnly`。调用 `.mode()` 并传入 `Mode::Safe`、`Mode::DeleteSafe` 或 `Mode::FullAccess` 即可提升权限。权限提升必须显式进行：除非你明确选择启用，否则服务器不会启用变更或生命周期工具。

* * *

## Vendoring / 嵌入 {#vendoring--embedding}

两个功能标志控制二进制文件的获取方式：

- `sidecar`（默认）：在首次运行时下载、校验并缓存已签名的二进制文件。
- `vendored`：使用 `include_bytes!` 嵌入二进制文件，生成单个自包含的可执行文件。

vendored 二进制文件会使构建体积增加约 80 MB。

* * *

## 演示应用 {#demo-app}

**auditron** 是一个终端合规助手：它是一个 TUI，以流式方式输出通过/失败结果，同时显示所用的 SQL 和 Claude 的解释，以约 80 MB 的 vendored 单一二进制文件发布。

* * *

## 仓库链接 {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
