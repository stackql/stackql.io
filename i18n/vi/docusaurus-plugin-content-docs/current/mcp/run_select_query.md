---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: Công cụ MCP thực thi một câu lệnh SELECT của StackQL đối với một provider
image: "/img/stackql-featured-image.png"
---

Thực thi một câu lệnh `SELECT` đối với các provider đã cấu hình và trả về các hàng kết quả.  Chỉ đọc - không chấp nhận thao tác thay đổi dữ liệu và thao tác vòng đời; hãy dùng [`run_mutation_query`](/mcp/run_mutation_query) hoặc [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) cho các thao tác đó.

Các hàng kết quả được trả về cho ứng dụng khách nhưng được cố ý loại khỏi nhật ký kiểm tra.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT trong đặc tả ngôn ngữ ]](/language-spec/select)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`sql`|Có|Một câu lệnh `SELECT`.  Các câu lệnh siêu dữ liệu (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) được cơ chế kiểm soát chính sách xếp vào loại chỉ đọc và cũng được chấp nhận; `DESC` được nhận diện từ StackQL `v0.12.732` trở đi.|
|`row_limit`|Không|Giới hạn số hàng trả về cho ứng dụng khách.  `0` (mặc định) nghĩa là không giới hạn ở phía ứng dụng khách.|
|`source`|Không|ID mục Query Library khi SQL đến từ [`query_library_get`](/mcp/query_library_get). Chỉ dùng để ghi nhận nguồn; không thay đổi việc thực thi hay phê duyệt.|
|`format`|Không|Định dạng hiển thị văn bản: `markdown` hoặc `json`. Khi không được cung cấp, công cụ dùng thiết lập hiển thị của máy chủ.|

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| Bảng | `{rows: [...]}` - một đối tượng cho mỗi hàng, với khóa là tên cột |

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ (`read_only`, `safe`, `delete_safe`, `full_access`) khi mọi câu lệnh trong payload đều chỉ đọc.  Từ `v1.0`, công cụ này từ chối `INSERT`, `UPDATE`, `REPLACE`, `DELETE` và `EXEC` ở mọi chế độ, kể cả `full_access`, với một lỗi nêu tên [`run_mutation_query`](/mcp/run_mutation_query) hoặc [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).  Một payload chứa nhiều câu lệnh được phân loại theo câu lệnh có đặc quyền cao nhất, vì vậy một thao tác thay đổi dữ liệu nằm sau một câu lệnh `SELECT` ở đầu cũng bị từ chối theo cách tương tự.  Công cụ khai báo `destructiveHint: false` và `openWorldHint: true`.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
