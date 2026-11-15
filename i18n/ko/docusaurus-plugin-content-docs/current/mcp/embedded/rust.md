---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 임베디드
  - rust
description: 사이드카 또는 include_bytes! 벤더링으로 Rust 애플리케이션에 StackQL MCP 서버를 내장합니다
image: "/img/stackql-featured-image.png"
---

# Rust

StackQL MCP 서버를 Rust 애플리케이션에 내장합니다. 다운로드한 사이드카로 사용하거나 `include_bytes!` 매크로로 바이너리에 벤더링할 수 있습니다. 클라이언트는 `rmcp` 크레이트입니다.

:::info
임베디드 바이너리는 첫 실행 시 다운로드되어 sha256으로 검증되거나, 앱에 벤더링됩니다. [공통 계약](/mcp/embedded#common-contract)을 참조하세요.
:::

* * *

## 설치 {#install}

`stackql-mcp` 크레이트를 추가합니다:

```toml
stackql-mcp = "0.1"
```

* * *

## 빠른 시작 {#quick-start}

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

API는 `StackqlMcp::builder()` 메서드와 `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()`, `.shutdown()` 메서드, 그리고 `.list_all_tools()` 메서드로 구성됩니다. MSRV는 Rust 1.88입니다.

* * *

## 모드 {#modes}

빌더의 기본값은 `Mode::ReadOnly`입니다. 승격하려면 `Mode::Safe`, `Mode::DeleteSafe` 또는 `Mode::FullAccess` 값으로 `.mode()` 메서드를 호출하세요. 승격은 명시적입니다. 옵트인하지 않으면 서버는 변경 작업 도구나 수명 주기 도구를 활성화하지 않습니다.

* * *

## 벤더링 / 내장 {#vendoring--embedding}

2개의 기능 플래그로 바이너리를 확보하는 방식을 제어합니다:

- `sidecar`(기본값): 첫 실행 시 서명된 바이너리를 다운로드, 검증 및 캐시합니다.
- `vendored`: `include_bytes!` 매크로로 바이너리를 내장하여 자체 포함된 단일 실행 파일을 만듭니다.

벤더링된 바이너리는 빌드 크기를 약 80 MB 늘립니다.

* * *

## 데모 앱 {#demo-app}

**auditron**은 터미널 규정 준수 코파일럿입니다. 실행된 SQL과 Claude의 설명을 표시하면서 통과/실패 결과를 스트리밍하는 TUI이며, 약 80 MB의 벤더링된 단일 바이너리로 제공됩니다.

* * *

## 리포지토리 링크 {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
