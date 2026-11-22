---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: Công cụ MCP liệt kê các tài nguyên thuộc một provider.service của StackQL
image: "/img/stackql-featured-image.png"
---

Trả về các tài nguyên thuộc một `provider.service` - cấp thứ ba trong [phân cấp tài nguyên](/getting-started/resource-hierarchy) của StackQL.  Tài nguyên là những đích xuất hiện trong mệnh đề `FROM` của một truy vấn StackQL.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`provider`|Có|Tên provider.|
|`service`|Có|Dịch vụ thuộc provider đó.|

<br/>

:::info

Định danh không được chứa khoảng trắng, dấu chấm phẩy hoặc dấu gạch chéo ngược; đầu vào như vậy bị từ chối trước khi bất kỳ câu lệnh nào được tạo.

:::

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| Bảng | Một hàng cho mỗi tài nguyên thuộc `provider.service` |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ.  Chỉ đọc.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào đã đăng ký máy chủ MCP StackQL:

```
List the resources in the google.storage service
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
