---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 組み込み
  - rust
description: サイドカーまたは include_bytes! によるベンダリングで StackQL MCP サーバーを Rust アプリケーションに組み込みます
image: "/img/stackql-featured-image.png"
---

# Rust

StackQL MCP サーバーを Rust アプリケーションに組み込みます。ダウンロードしたサイドカーとして使用するか、`include_bytes!` でバイナリにベンダリングします。クライアントは `rmcp` です。

:::info
組み込みバイナリは、初回実行時にダウンロードされて sha256 で検証されるか、アプリにベンダリングされます。[共通コントラクト](/mcp/embedded#common-contract)を参照してください。
:::

* * *

## インストール {#install}

`stackql-mcp` クレートを追加します：

```toml
stackql-mcp = "0.1"
```

* * *

## クイックスタート {#quick-start}

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

API は、`.mode()`、`.auth()`、`.binary()`、`.bundle_path()`、`.approot()`、`.command()`、`.start()`、`.shutdown()` を備えた `StackqlMcp::builder()` と、`.list_all_tools()` で構成されます。MSRV は Rust 1.88 です。

* * *

## モード {#modes}

ビルダーのデフォルトは `Mode::ReadOnly` です。昇格するには、`Mode::Safe`、`Mode::DeleteSafe`、`Mode::FullAccess` のいずれかを指定して `.mode()` を呼び出します。昇格は明示的にのみ行われます。オプトインしない限り、サーバーはミューテーションツールやライフサイクルツールを有効にしません。

* * *

## ベンダリング / 組み込み {#vendoring--embedding}

バイナリの入手方法は 2 つの機能フラグで制御します：

- `sidecar`（デフォルト）：初回実行時に署名済みバイナリをダウンロードし、検証してキャッシュします。
- `vendored`：`include_bytes!` でバイナリを組み込み、単一の自己完結型実行可能ファイルにします。

ベンダリングしたバイナリにより、ビルドのサイズは約 80 MB 増えます。

* * *

## デモアプリ {#demo-app}

**auditron** はターミナル向けのコンプライアンスコパイロットです。SQL と Claude による説明を表示しながら合否の結果をストリーミングする TUI で、約 80 MB のベンダリング済み単一バイナリとして提供されます。

* * *

## リポジトリへのリンク {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
