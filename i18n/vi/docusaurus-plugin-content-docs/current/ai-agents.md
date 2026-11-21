---
slug: /ai-agents
title: Tác nhân AI
hide_title: false
keywords:
  - stackql
  - tác nhân AI
  - mcp
  - model context protocol
  - dựa trên tác nhân
description: Kết nối tác nhân AI với các dịch vụ đám mây và SaaS bằng StackQL thông qua máy chủ MCP, công cụ, tích hợp nhúng và truy vấn SQL.
image: "/img/stackql-featured-image.png"
---

StackQL cung cấp cho tác nhân AI cùng giao diện SQL mà con người dùng để truy vấn, cấp phát và vận hành các dịch vụ đám mây và SaaS. Một tác nhân khám phá những gì một provider cung cấp, kiểm tra tính hợp lệ của một truy vấn trước khi chạy, đọc trạng thái thực tế bằng `SELECT` và thay đổi trạng thái đó bằng `INSERT`, `UPDATE` và `DELETE`, không cần tệp trạng thái và không cần SDK riêng cho từng đám mây.

## Cách tác nhân kết nối {#how-agents-connect}

- **MCP qua stdio** dành cho các ứng dụng khách trên trình soạn thảo và máy tính để bàn như Claude Desktop, Cursor và Continue. Ứng dụng khách tự khởi chạy `stackql mcp --mcp.server.type=stdio`. Xem [Máy chủ MCP](/command-line-usage/mcp).
- **MCP qua HTTP** dành cho các tác nhân độc lập và tác vụ tự động hóa kết nối tới một tiến trình `stackql mcp` hoặc `stackql srv` chạy lâu dài, với xác thực bằng bearer token và TLS không bắt buộc.
- **MCP nhúng** dành cho các ứng dụng đã biên dịch: máy chủ chạy ngay trong tiến trình của ứng dụng Go, Rust, Swift, Kotlin, .NET và Gleam. Xem [MCP nhúng](/mcp/embedded).
- **Giao thức wire của PostgreSQL** qua [`stackql srv`](/command-line-usage/srv) dành cho các framework tác nhân, notebook và công cụ BI vốn đã hỗ trợ Postgres.

## Bắt đầu nhanh với Claude Desktop {#quick-start-with-claude-desktop}

Cài đặt từ Anthropic Connector Directory (khuyên dùng) hoặc từ MCP Bundle có thể tải xuống, hoặc đăng ký máy chủ thủ công trong tệp `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

Chỉ đưa vào thông tin xác thực của các provider bạn định sử dụng. Hướng dẫn đầy đủ, bao gồm các bản tải xuống bundle và cách dùng tệp thông tin xác thực, có trong [Sử dụng StackQL với Claude Desktop](/getting-started/claude-desktop).

## Những gì tác nhân có thể làm {#what-an-agent-can-do}

Máy chủ MCP cung cấp một bộ công cụ nhỏ, có kiểu dữ liệu rõ ràng. Mỗi công cụ trả về một bản trình bày đã định dạng cho mô hình và một payload có cấu trúc cho các ứng dụng khách xử lý theo chương trình.

- **Khám phá**: `list_providers`, `list_services`, `list_resources`, `list_methods` và `describe_method` duyệt qua phân cấp của provider và trả về các đầu vào và trường đầu ra của từng thao tác, nhờ đó các tham số `WHERE` bắt buộc đã được biết trước khi viết truy vấn.
- **Kiểm tra tính hợp lệ và đọc**: `validate_select_query` phân tích cú pháp và lập kế hoạch cho một câu lệnh `SELECT` mà không chạy câu lệnh đó; `run_select_query` thực thi câu lệnh.
- **Thay đổi**: `run_mutation_query` chạy `INSERT`, `UPDATE`, `REPLACE` và `DELETE`; `run_lifecycle_operation` chạy các thao tác vòng đời `EXEC`. Cả hai đều được kiểm soát bởi chế độ máy chủ bên dưới.
- **Quản trị**: `server_info`, `list_registry`, `pull_provider` và `reload_credentials`.

Tài liệu tham khảo đầy đủ, gồm đầu vào, hành vi kiểm soát và một lời nhắc ví dụ cho từng công cụ, có tại [Công cụ MCP](/mcp).

## Cơ chế bảo vệ {#guardrails}

Chế độ máy chủ thiết lập hợp đồng an toàn cho toàn bộ phiên:

| Chế độ | `SELECT` / siêu dữ liệu | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | cho phép | từ chối | từ chối |
| `safe` (mặc định) | cho phép | cần phê duyệt | cần phê duyệt |
| `delete_safe` | cho phép | cho phép | cần phê duyệt |
| `full_access` | cho phép | cho phép | cho phép |

"Cần phê duyệt" sử dụng elicitation của MCP: các ứng dụng khách hỗ trợ tính năng này (Claude Desktop, Cursor, Continue) hiển thị cho người dùng tên công cụ, loại truy vấn và câu lệnh SQL rồi chờ quyết định trước khi chạy bất cứ thứ gì; các ứng dụng khách không công bố khả năng này sẽ bị từ chối. Mọi lệnh gọi công cụ đều được ghi vào nhật ký kiểm tra, các listener HTTP yêu cầu bearer token (trừ khi được mở một cách rõ ràng bằng `allow_unauthenticated`) và thông tin xác thực được lấy từ môi trường hoặc từ tệp dotenv mà không bao giờ trả giá trị bí mật về cho ứng dụng khách. Chi tiết có trong [Chế độ máy chủ](/command-line-usage/mcp#server-modes), [Xác thực ứng dụng khách HTTP](/command-line-usage/mcp#http-client-authentication) và [Nhật ký kiểm tra](/command-line-usage/mcp#audit-log).

## Các bước tiếp theo {#next-steps}

- [Máy chủ MCP](/command-line-usage/mcp) - các chế độ triển khai, cấu hình, chế độ máy chủ, nhật ký kiểm tra và cờ.
- [Công cụ MCP](/mcp) - tài liệu tham khảo đầy đủ về công cụ với đầu vào, cơ chế kiểm soát và lời nhắc ví dụ.
- [MCP nhúng](/mcp/embedded) - chạy máy chủ bên trong ứng dụng Go, Rust, Swift, Kotlin, .NET hoặc Gleam của riêng bạn.
- [Query Library](pathname:///docs/query-library/) - SQL sẵn sàng để chạy cho các câu hỏi vận hành đám mây phổ biến, theo provider.
- [Tài liệu tham khảo AI](/ai) - khái niệm, hướng dẫn thực hiện, so sánh và khắc phục sự cố, được viết cho tác nhân và những người phát triển chúng.
