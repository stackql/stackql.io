---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: Công cụ MCP trả về hợp đồng I/O đầy đủ cho một phương thức truy cập StackQL
image: "/img/stackql-featured-image.png"
---

Trả về hợp đồng I/O đầy đủ cho một phương thức truy cập - luôn ở dạng EXTENDED, nghĩa là gồm đầu vào, lược đồ phần thân yêu cầu và dạng đầu ra đầy đủ.  Đây là nơi tác nhân lấy tên cột: mỗi hàng mang một `param_type` là `input_required` (một vị từ `WHERE` bắt buộc, khớp chính xác), `input_optional` hoặc `output` (một trường mà `SELECT` hoặc `RETURNING` có thể tham chiếu).  Một tài nguyên không có một danh sách trường duy nhất, vì mỗi phương thức trả về dạng riêng của nó, nên hãy gọi công cụ này cho phương thức đã chọn từ [`list_methods`](/mcp/list_methods) trước truy vấn đầu tiên nhắm vào phương thức đó.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`provider`|Có|Tên provider.|
|`service`|Có|Dịch vụ thuộc provider đó.|
|`resource`|Có|Tài nguyên thuộc `provider.service`.|
|`method`|Có|Tên phương thức do [`list_methods`](/mcp/list_methods) trả về.|

<br/>

:::info

Định danh không được chứa khoảng trắng, dấu chấm phẩy hoặc dấu gạch chéo ngược; đầu vào như vậy bị từ chối trước khi bất kỳ câu lệnh nào được tạo.

:::

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| KV | Một hàng cho mỗi trường đầu vào và đầu ra: tên, kiểu, `param_type` (`input_required`, `input_optional` hoặc `output`), dạng và mô tả |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ.  Chỉ đọc.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào đã đăng ký máy chủ MCP StackQL:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
