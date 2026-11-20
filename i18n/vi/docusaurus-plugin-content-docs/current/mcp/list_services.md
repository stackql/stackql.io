---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: Công cụ MCP liệt kê các dịch vụ thuộc một provider StackQL
image: "/img/stackql-featured-image.png"
---

Trả về các dịch vụ do một provider cung cấp - cấp thứ hai trong [phân cấp tài nguyên](/getting-started/resource-hierarchy) của StackQL.  Gọi công cụ này sau [`list_providers`](/mcp/list_providers) để thu hẹp phạm vi trước khi đi sâu vào tài nguyên.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`provider`|Có|Tên provider (ví dụ: `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Định danh không được chứa khoảng trắng, dấu chấm phẩy hoặc dấu gạch chéo ngược; đầu vào như vậy bị từ chối trước khi bất kỳ câu lệnh nào được tạo.

:::

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| Bảng | Một hàng cho mỗi dịch vụ thuộc provider đã cho |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ.  Chỉ đọc.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
List the services available in Google Cloud
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
