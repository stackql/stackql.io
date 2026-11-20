---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: Công cụ MCP thực thi INSERT, UPDATE, REPLACE hoặc DELETE đối với một provider thông qua StackQL
image: "/img/stackql-featured-image.png"
---

Thực thi `INSERT`, `UPDATE`, `REPLACE` hoặc `DELETE` đối với provider.  **Có tác dụng phụ thực sự** - một lệnh gọi thành công sẽ thay đổi trạng thái đám mây.

Được kiểm soát bởi [chế độ máy chủ](/command-line-usage/mcp#server-modes):

| Chế độ | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|bị từ chối|bị từ chối|
|`safe` (mặc định)|cần phê duyệt (elicitation)|cần phê duyệt|
|`delete_safe`|được cho phép|cần phê duyệt|
|`full_access`|được cho phép|được cho phép|

<br/>

:::info

Một payload chứa nhiều câu lệnh được kiểm soát theo câu lệnh có đặc quyền cao nhất trong đó

:::

Khi một thao tác thay đổi dữ liệu cần phê duyệt mà ứng dụng khách không công bố khả năng elicitation của MCP, lệnh gọi sẽ bị từ chối.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT trong đặc tả ngôn ngữ ]](/language-spec/insert) [[ DELETE trong đặc tả ngôn ngữ ]](/language-spec/delete)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`sql`|Có|Một câu lệnh `INSERT`, `UPDATE`, `REPLACE` hoặc `DELETE`.|
|`source`|Không|ID mục Query Library khi SQL đến từ [`query_library_get`](/mcp/query_library_get). Chỉ dùng để ghi nhận nguồn; không thay đổi việc thực thi hay phê duyệt.|
|`format`|Không|Định dạng hiển thị văn bản: `markdown` hoặc `json`. Khi không được cung cấp, công cụ dùng thiết lập hiển thị của máy chủ.|

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| KV | `{messages: [...], timestamp}` - xác nhận đã gửi đi, không phải phần thân phản hồi của API |

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

Ứng dụng khách MCP sẽ hiển thị yêu cầu phê duyệt trước khi thao tác xóa được gửi đi, trừ khi máy chủ đang chạy ở chế độ `full_access`.

Đoạn video sau cho thấy luồng elicitation khi máy chủ MCP StackQL đang chạy ở chế độ `safe` mặc định - ứng dụng khách được yêu cầu phê duyệt `DELETE` trước khi bất kỳ thay đổi nào phía đám mây được gửi đi.  Bạn có thể tái hiện điều này trên máy chủ MCP StackQL của riêng bạn bằng [MCP Inspector](https://github.com/modelcontextprotocol/inspector) chính thức thông qua `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
