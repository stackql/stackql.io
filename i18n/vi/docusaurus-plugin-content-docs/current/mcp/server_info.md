---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: Công cụ MCP trả về danh tính và runtime của máy chủ MCP StackQL
image: "/img/stackql-featured-image.png"
---

Trả về danh tính và runtime của máy chủ MCP StackQL: phiên bản, công cụ SQL bên dưới, vị trí registry provider, chế độ máy chủ và cờ chỉ đọc cũ.  Hãy gọi công cụ này khi cần, ví dụ để chẩn đoán một thao tác bị từ chối hoặc được kiểm soát, hay để kiểm tra hành vi theo từng phiên bản; không cần gọi công cụ này khi bắt đầu phiên hoặc trước các truy vấn.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Đầu vào {#inputs}

Không có.

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| KV (khối khóa/giá trị markdown cho LLM) | Payload có cấu trúc với các trường bên dưới |

<br/><br/>

| Trường | Mô tả |
|--|--|
|`version`|Phiên bản bản dựng StackQL.|
|`commit`|Hash rút gọn của commit Git.|
|`build_date`|Dấu thời gian bản dựng theo RFC3339.|
|`platform`|`os/arch` (ví dụ: `linux/amd64`).|
|`transport`|`http` hoặc `stdio`.|
|`sql_backend`|Công cụ SQL bên dưới (ví dụ: `sqlite3`).|
|`provider_registry`|URL của registry provider.|
|`mode`|Chế độ máy chủ đang hoạt động - `read_only`, `safe`, `delete_safe` hoặc `full_access`.|
|`is_read_only`|Cờ boolean cũ (`true` khi và chỉ khi `mode == read_only`).|

* * *

## Kiểm soát {#gating}

Luôn được cho phép ở mọi chế độ máy chủ.  Không được ghi vào nhật ký kiểm tra như một thao tác thay đổi dữ liệu.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào hỗ trợ elicitation và đã đăng ký máy chủ MCP StackQL:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Dưới đây là một ví dụ trong Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
