---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: Công cụ MCP liệt kê các phương thức truy cập cho một tài nguyên StackQL
image: "/img/stackql-featured-image.png"
---

Trả về các phương thức truy cập (thao tác HTTP) được liên kết với một tài nguyên - các phương thức đọc, thao tác thay đổi dữ liệu và thao tác vòng đời hiện có.  Đây là công cụ mà tác nhân nên gọi **trước khi viết bất kỳ truy vấn nào**, vì các tham số `WHERE` bắt buộc được suy ra từ phương thức đọc đã chọn.  Chọn phương thức có động từ SQL khớp với ý định và có các tham số bắt buộc mà bạn có thể cung cấp, sau đó gọi [`describe_method`](/mcp/describe_method) cho phương thức đó để lấy hợp đồng đầy đủ, bao gồm các trường đầu ra.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`provider`|Có|Tên provider.|
|`service`|Có|Dịch vụ thuộc provider đó.|
|`resource`|Có|Tài nguyên thuộc `provider.service`.|

<br/>

:::info

Định danh không được chứa khoảng trắng, dấu chấm phẩy hoặc dấu gạch chéo ngược; đầu vào như vậy bị từ chối trước khi bất kỳ câu lệnh nào được tạo.

:::

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| Bảng | Một hàng cho mỗi phương thức truy cập, bao gồm động từ SQL mà phương thức ánh xạ tới và các tham số bắt buộc |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ.  Chỉ đọc.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
