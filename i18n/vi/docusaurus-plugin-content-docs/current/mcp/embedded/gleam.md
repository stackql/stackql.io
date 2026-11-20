---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - nhúng
  - gleam
  - erlang
  - beam
description: Nhúng máy chủ MCP StackQL vào một ứng dụng Gleam trên đích biên dịch Erlang/BEAM
image: "/img/stackql-featured-image.png"
---

# Gleam

Nhúng máy chủ MCP StackQL vào một ứng dụng Gleam trên đích biên dịch Erlang/BEAM. Thư viện tích hợp với cơ chế giám sát OTP, tải xuống và xác minh tệp nhị phân đã ký, sau đó khởi chạy tệp qua stdio.

:::info
Tệp nhị phân nhúng được tải xuống và xác minh sha256 trong lần chạy đầu tiên, hoặc được đưa vào ứng dụng của bạn theo kiểu vendoring. Xem [hợp đồng chung](/mcp/embedded#common-contract).
:::

* * *

## Cài đặt {#install}

```
gleam add stackql_mcp
```

Chỉ nhắm đến Erlang/BEAM (không phải đích biên dịch JavaScript). Máy chủ được cung cấp dưới dạng một tiến trình con OTP thông qua `child_spec()` để có thể nằm trong cây giám sát của riêng bạn.

* * *

## Bắt đầu nhanh {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

API gồm `stackql_mcp.start()`, `stop()` và `child_spec()` cho vòng đời; `list_tools()` và `call_tool()` cho các thao tác (cùng các biến thể `supervised_list_tools()` và `supervised_call_tool()` để dùng dưới một supervisor); và `Config`, `default_config()` và `auth_for()` cho cấu hình.

* * *

## Chế độ {#modes}

Chế độ được đặt trong `Config.mode` và mặc định là `read_only`. Đặt chế độ thành `safe`, `delete_safe` hoặc `full_access` để nâng mức quyền:

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

Việc nâng mức quyền phải được thực hiện một cách rõ ràng - máy chủ sẽ không bật các công cụ thay đổi dữ liệu hoặc công cụ vòng đời trừ khi bạn chủ động bật.

* * *

## Vendoring / nhúng {#vendoring--embedding}

Khi khởi động, thư viện tải xuống bundle StackQL của nền tảng, xác minh bundle theo các giá trị ghim sha256 được tích hợp sẵn và lưu bundle vào cache dùng chung của cả họ thư viện. Thứ tự ưu tiên khi phân giải:

1. `Config.binary` được chỉ định rõ ràng.
2. Biến môi trường `STACKQL_MCP_BIN`.
3. Cache dùng chung tại `~/.stackql/mcp-server-bin/<version>/<platform-key>/`.

Đối với các triển khai ngoại tuyến hoặc cách ly mạng (air-gapped), đặt `STACKQL_MCP_BUNDLE` trỏ đến một bundle cục bộ.

* * *

## Ứng dụng minh họa {#demo-app}

**pipewatch** là ứng dụng minh họa dự kiến cho thư viện này. Ứng dụng này nằm trong lộ trình và chưa được phát triển.

* * *

## Liên kết kho lưu trữ {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
