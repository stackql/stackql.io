---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: Công cụ MCP phân tích cú pháp và lập kế hoạch cho một câu lệnh SELECT của StackQL mà không thực thi
image: "/img/stackql-featured-image.png"
---

Phân tích cú pháp và lập kế hoạch cho một câu lệnh `SELECT` duy nhất mà không thực thi câu lệnh đó.  Trả về `{valid, errors}`.  Hữu ích khi tác nhân muốn kiểm tra rằng truy vấn của mình biên dịch được và liên kết được với phương thức đã khám phá trước khi thực hiện một lệnh gọi API thật.

:::info

Chỉ `SELECT` và đúng một câu lệnh: payload chứa nhiều hơn một câu lệnh sẽ bị từ chối với thông báo `validate_select_query accepts exactly one statement`.

:::

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`sql`|Có|Một câu lệnh `SELECT` duy nhất.|

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| KV | `valid` (bool) và `errors` (mảng chuỗi, rỗng khi hợp lệ) |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ.  Không thực hiện lệnh gọi API nào.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào đã đăng ký máy chủ MCP StackQL:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
