---
title: Rust
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - nhúng
  - rust
description: Nhúng máy chủ MCP StackQL vào một ứng dụng Rust qua sidecar hoặc vendoring bằng include_bytes!
image: "/img/stackql-featured-image.png"
---

# Rust

Nhúng máy chủ MCP StackQL vào một ứng dụng Rust, dưới dạng sidecar được tải xuống hoặc được đưa vào tệp nhị phân của bạn theo kiểu vendoring bằng `include_bytes!`. Ứng dụng khách là `rmcp`.

:::info
Tệp nhị phân nhúng được tải xuống và xác minh sha256 trong lần chạy đầu tiên, hoặc được đưa vào ứng dụng của bạn theo kiểu vendoring. Xem [hợp đồng chung](/mcp/embedded#common-contract).
:::

* * *

## Cài đặt {#install}

Thêm crate `stackql-mcp`:

```toml
stackql-mcp = "0.1"
```

* * *

## Bắt đầu nhanh {#quick-start}

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

API gồm `StackqlMcp::builder()` với `.mode()`, `.auth()`, `.binary()`, `.bundle_path()`, `.approot()`, `.command()`, `.start()` và `.shutdown()`, cùng với `.list_all_tools()`. MSRV là Rust 1.88.

* * *

## Chế độ {#modes}

Builder mặc định dùng `Mode::ReadOnly`. Gọi `.mode()` với `Mode::Safe`, `Mode::DeleteSafe` hoặc `Mode::FullAccess` để nâng mức quyền. Việc nâng mức quyền phải được thực hiện một cách rõ ràng - máy chủ sẽ không bật các công cụ thay đổi dữ liệu hoặc công cụ vòng đời trừ khi bạn chủ động bật.

* * *

## Vendoring / nhúng {#vendoring--embedding}

Hai tính năng Cargo kiểm soát cách lấy tệp nhị phân:

- `sidecar` (mặc định) - tải xuống, xác minh và lưu vào cache tệp nhị phân đã ký trong lần chạy đầu tiên.
- `vendored` - nhúng tệp nhị phân bằng `include_bytes!` để có một tệp thực thi độc lập duy nhất.

Tệp nhị phân vendored làm bản dựng tăng thêm khoảng 80 MB.

* * *

## Ứng dụng minh họa {#demo-app}

**auditron** là một trợ lý tuân thủ trên terminal: một TUI truyền trực tiếp kết quả đạt/không đạt kèm câu lệnh SQL được hiển thị và phần giải thích của Claude, được phát hành dưới dạng một tệp nhị phân vendored duy nhất khoảng 80 MB.

* * *

## Liên kết kho lưu trữ {#repo-link}

[github.com/stackql/stackql-mcp-rs](https://github.com/stackql/stackql-mcp-rs)
