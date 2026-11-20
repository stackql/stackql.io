---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: Công cụ MCP liệt kê các provider và phiên bản hiện có trong registry provider StackQL
image: "/img/stackql-featured-image.png"
---

Trả về các provider (và phiên bản của chúng) hiện có trong registry provider StackQL đã cấu hình.  Công cụ này khác với [`list_providers`](/mcp/list_providers), là công cụ chỉ báo cáo các provider đã được tải xuống vào bộ nhớ đệm cục bộ.

Truyền đối số tùy chọn `provider` để liệt kê các phiên bản đã phát hành của một provider.  Dùng kết hợp với [`pull_provider`](/mcp/pull_provider) khi tác nhân cần cài đặt thứ gì đó chưa có cục bộ.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Lệnh registry ]](/command-line-usage/registry)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`provider`|Không|Tên provider.  Khi được cung cấp, kết quả được thu hẹp về các phiên bản đã phát hành của provider đó; khi bỏ qua, mọi mục trong registry đều được trả về.|

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| Bảng | Một hàng cho mỗi mục registry (provider cùng siêu dữ liệu phiên bản) |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ.  Không có tác động lên mặt phẳng điều khiển hoặc mặt phẳng dữ liệu đám mây - chỉ registry được đọc.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
