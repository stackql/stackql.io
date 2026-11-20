---
title: Swift
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - nhúng
  - swift
  - macos
description: Đóng gói tệp nhị phân máy chủ MCP StackQL đã được công chứng bên trong một ứng dụng macOS đã được ký
image: "/img/stackql-featured-image.png"
---

# Swift

Nhúng máy chủ MCP StackQL vào một ứng dụng macOS. Tệp nhị phân đã được ký và công chứng có thể được đóng gói bên trong `.app` của bạn, và ứng dụng khách là SDK MCP chính thức cho Swift.

:::info
Tệp nhị phân nhúng được tải xuống và xác minh sha256 trong lần chạy đầu tiên, hoặc được đưa vào ứng dụng của bạn theo kiểu vendoring. Xem [hợp đồng chung](/mcp/embedded#common-contract).
:::

* * *

## Cài đặt {#install}

Thêm gói vào tệp `Package.swift` của bạn:

```swift
.package(url: "https://github.com/stackql/stackql-mcp-swift.git", from: "0.1.0")
```

Yêu cầu macOS 13+ và Swift 6.1 (Xcode 16.3+). Phần phụ thuộc duy nhất là SDK MCP chính thức cho Swift.

* * *

## Bắt đầu nhanh {#quick-start}

```swift
import StackQLMCP

var options = Options()
options.mode = .readOnly
options.auth = ["github": ["type": "null_auth"]]

let server = try await StackQLServer.start(options)
let tools = try await server.listToolNames()
print("\(tools.count) tools available")

let services = try await server.call(
    "list_services", ["provider": "github", "row_limit": 5])
print(services.text)

await server.stop()
```

API gồm `StackQLServer.start(Options)`, `.listToolNames()`, `.call(_:_:)` và `.stop()`. `Options` chứa `mode`, `auth`, `binaryOverride` và `allowDownload`. Đối với các harness bên ngoài, `StackQLServer.resolveCommand(_:)` trả về lệnh khởi chạy đã được phân giải.

* * *

## Chế độ {#modes}

`Options.mode` mặc định là `.readOnly`. Đặt giá trị này thành `.safe`, `.deleteSafe` hoặc `.fullAccess` để nâng mức quyền. Việc nâng mức quyền phải được thực hiện một cách rõ ràng - máy chủ sẽ không bật các công cụ thay đổi dữ liệu hoặc công cụ vòng đời trừ khi bạn chủ động bật.

* * *

## Vendoring / nhúng {#vendoring--embedding}

Tệp nhị phân `darwin-universal` được ký bằng Developer ID và được Apple công chứng, vì vậy có thể được đóng gói bên trong một `.app` macOS đã được ký mà vẫn giữ nguyên hiệu lực công chứng của chính ứng dụng. Điều này chỉ có ở thư viện Swift - bạn phát hành một ứng dụng độc lập, đã được công chứng và không cần tải xuống trong lần chạy đầu tiên.

Thứ tự phân giải tệp nhị phân:

1. `binaryOverride` được chỉ định rõ ràng.
2. Tài nguyên đi kèm trong `.app`.
3. Cache dùng chung tại `~/.stackql/mcp-server-bin/`.
4. Tải xuống với bước xác minh sha256 (khi `allowDownload` được đặt).

* * *

## Ứng dụng minh họa {#demo-app}

**CloudLens** là một trình canh gác đám mây trên thanh menu, nhúng tệp nhị phân đã được công chứng và chạy một bộ kiểm tra `read_only`.

* * *

## Liên kết kho lưu trữ {#repo-link}

[github.com/stackql/stackql-mcp-swift](https://github.com/stackql/stackql-mcp-swift)
