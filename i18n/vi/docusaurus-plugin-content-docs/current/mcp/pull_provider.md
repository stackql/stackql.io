---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: Công cụ MCP cài đặt một provider StackQL từ registry vào bộ nhớ đệm cục bộ
image: "/img/stackql-featured-image.png"
---

Cài đặt một provider từ registry StackQL vào bộ nhớ đệm approot cục bộ.  Khi bỏ qua `version`, phiên bản mới nhất đã phát hành sẽ được tải xuống.

Công cụ chỉ ghi trạng thái bộ nhớ đệm cục bộ - không có tác động lên mặt phẳng điều khiển hoặc mặt phẳng dữ liệu đám mây, vì vậy công cụ này được cho phép ở mọi chế độ máy chủ (bao gồm cả `read_only`).  Dùng [`list_registry`](/mcp/list_registry) để tìm tên provider và các phiên bản hiện có trước khi tải xuống.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Lệnh registry ]](/command-line-usage/registry)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`provider`|Có|Tên provider như đã phát hành trong registry (ví dụ: `aws`, `google`, `cloudflare`).|
|`version`|Không|Phiên bản đã phát hành cần tải xuống.  Khi bỏ qua, phiên bản mới nhất đã phát hành sẽ được cài đặt.|

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| KV | Xác nhận rằng provider (và phiên bản) đã được cài đặt vào bộ nhớ đệm cục bộ |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ.  Không có tác động phía đám mây; chỉ trạng thái bộ nhớ đệm cục bộ thay đổi.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
