---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - nhúng
  - dotnet
  - csharp
description: Nhúng máy chủ MCP StackQL vào một ứng dụng .NET dưới dạng sidecar hoặc bundle vendored
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Nhúng máy chủ MCP StackQL vào một ứng dụng .NET, dưới dạng sidecar được tải xuống hoặc được đưa vào bản dựng của bạn theo kiểu vendoring dưới dạng một tệp thực thi độc lập. Ứng dụng khách là SDK MCP chính thức cho C#.

:::info
Tệp nhị phân nhúng được tải xuống và xác minh sha256 trong lần chạy đầu tiên, hoặc được đưa vào ứng dụng của bạn theo kiểu vendoring. Xem [hợp đồng chung](/mcp/embedded#common-contract).
:::

* * *

## Cài đặt {#install}

```
dotnet add package StackQL.Mcp
```

Gói tùy chọn `StackQL.Mcp.AgentFramework` bổ sung khả năng tích hợp Agent Framework. Yêu cầu .NET 8 trở lên.

* * *

## Bắt đầu nhanh {#quick-start}

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .WithAuth("github", "null_auth")
    .StartAsync();

var tools = await server.ListToolsAsync();
var result = await server.CallToolAsync("list_services",
    new() { ["provider"] = "github", ["row_limit"] = 5 });
```

API gồm `StackqlMcp.CreateBuilder()` với `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()` và `.WithCommand()`, sau đó là `.StartAsync()`, `.ListToolsAsync()` và `.CallToolAsync()`.

* * *

## Chế độ {#modes}

Builder mặc định dùng `StackqlMode.ReadOnly`. Gọi `.WithMode()` với `StackqlMode.Safe`, `StackqlMode.DeleteSafe` hoặc `StackqlMode.FullAccess` để nâng mức quyền. Việc nâng mức quyền phải được thực hiện một cách rõ ràng - máy chủ sẽ không bật các công cụ thay đổi dữ liệu hoặc công cụ vòng đời trừ khi bạn chủ động bật.

* * *

## Vendoring / nhúng {#vendoring--embedding}

Có hai cách để lấy tệp nhị phân:

- **Sidecar (mặc định)** - bundle của nền tảng được tải xuống trong lần chạy đầu tiên, được xác minh sha256, được lưu vào cache tại `~/.stackql/mcp-server-bin/<version>/<platform-key>/` và được khởi chạy qua stdio.
- **Vendored** - nhúng bundle như một tài nguyên của bản dựng để tạo ra một tệp thực thi độc lập:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Bạn cũng có thể ghi đè tệp nhị phân hoặc bundle khi chạy bằng biến môi trường `STACKQL_MCP_BIN` hoặc `STACKQL_MCP_BUNDLE`.

* * *

## Ứng dụng minh họa {#demo-app}

**driftwatch** là một .NET Worker Service nhúng máy chủ StackQL ở chế độ chỉ đọc và chạy theo lịch một bộ kiểm tra sai lệch bằng SQL (phơi bày công khai, thiếu tag hoặc giấy phép, sai lệch so với đường cơ sở), sau đó đăng kết quả lên một Adaptive Card của Teams.

* * *

## Liên kết kho lưu trữ {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
