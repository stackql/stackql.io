---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: Công cụ MCP thực thi một thao tác vòng đời EXEC của StackQL
image: "/img/stackql-featured-image.png"
---

Thực thi một thao tác vòng đời `EXEC` của StackQL - các hành động của provider không thuộc CRUD (khởi động/dừng một VM, xoay vòng một khóa, khởi chạy một workflow, v.v.).  **Có tác dụng phụ thực sự.**

Được kiểm soát bởi [chế độ máy chủ](/command-line-usage/mcp#server-modes):

| Chế độ | `EXEC` vòng đời |
|--|--|
|`read_only`|bị từ chối|
|`safe` (mặc định)|cần phê duyệt (elicitation)|
|`delete_safe`|cần phê duyệt|
|`full_access`|được cho phép|

<br/>

:::info

Một payload chứa nhiều câu lệnh được kiểm soát theo câu lệnh có đặc quyền cao nhất trong đó

:::

Khi cần phê duyệt mà ứng dụng khách không công bố hỗ trợ elicitation, lệnh gọi sẽ bị từ chối.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC trong đặc tả ngôn ngữ ]](/language-spec/exec)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`sql`|Có|Một câu lệnh `EXEC` của StackQL nhắm đến một phương thức vòng đời.|
|`source`|Không|ID mục Query Library khi SQL đến từ [`query_library_get`](/mcp/query_library_get). Chỉ dùng để ghi nhận nguồn; không thay đổi việc thực thi hay phê duyệt.|
|`format`|Không|Định dạng hiển thị văn bản: `markdown` hoặc `json`. Khi không được cung cấp, công cụ dùng thiết lập hiển thị của máy chủ.|

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

Ứng dụng khách MCP sẽ hiển thị yêu cầu phê duyệt trước khi thao tác được gửi đi, trừ khi máy chủ đang chạy ở chế độ `full_access`.

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
