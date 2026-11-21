---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: Công cụ MCP liệt kê các provider cho đám mây và SaaS mà máy chủ MCP StackQL có thể sử dụng
image: "/img/stackql-featured-image.png"
---

Trả về các provider đã được tải xuống vào bộ nhớ đệm cục bộ của StackQL - cấp cao nhất trong [phân cấp tài nguyên](/getting-started/resource-hierarchy) của StackQL.  Dùng công cụ này làm điểm bắt đầu khi tác nhân cần khám phá những gì nó có thể truy vấn.

Đối với các provider hiện có trong *registry* (bao gồm cả các provider chưa được tải xuống), hãy xem [`list_registry`](/mcp/list_registry).

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Đầu vào {#inputs}

Không có.

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| Bảng (bảng markdown cho LLM) | Một hàng cho mỗi provider |

<br />

Mỗi hàng chứa tên provider và siêu dữ liệu phiên bản do registry công bố.

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ (`read_only`, `safe`, `delete_safe`, `full_access`).  Chỉ đọc.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào đã đăng ký máy chủ MCP StackQL:

```
Which cloud providers are available for you to query right now?
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
