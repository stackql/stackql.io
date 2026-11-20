---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: Công cụ MCP nạp lại thông tin xác thực từ tệp dotenv --env.file và báo cáo trạng thái phân giải theo từng provider
image: "/img/stackql-featured-image.png"
---

Nạp lại tệp kiểu dotenv được chỉ định bởi cờ [`--env.file`](/command-line-usage/global-flags) vào môi trường tiến trình của máy chủ, sau đó báo cáo trạng thái phân giải thông tin xác thực cho từng provider.  Điều này khắc phục vấn đề môi trường bị cố định từ lúc khởi chạy tiến trình: thông tin xác thực được ghi hoặc xoay vòng trên đĩa giữa phiên sẽ đến được máy chủ đang chạy mà không cần khởi động lại.

Tệp env được phủ lên môi trường tiến trình thay vì thay thế nó: quá trình phân giải thông tin xác thực luôn đọc môi trường tiến trình hiện hành tại thời điểm yêu cầu, vì vậy các biến đã có từ lúc khởi chạy (từ shell hoặc khối `env` của ứng dụng khách) vẫn hoạt động dù có hay không có `--env.file`.  Khi nạp lại, chỉ những khóa có trong tệp với giá trị không rỗng mới được ghi và không biến nào bị xóa, vì vậy một lần nạp lại thất bại hoặc chỉ thành công một phần vẫn giữ nguyên thông tin xác thực đang hoạt động trước đó.

Giá trị bí mật không bao giờ được trả về, ghi nhật ký hay ghi vào nhật ký kiểm tra - công cụ chỉ báo cáo tên biến và trạng thái.  Khi không cấu hình `--env.file`, công cụ chỉ còn là một công cụ kiểm tra trạng thái thuần túy.  Khi một truy vấn thất bại ở bước phân giải thông tin xác thực, lỗi MCP kèm theo một gợi ý hướng tác nhân gọi `reload_credentials` và thử lại, nhờ đó các ứng dụng khách dựa trên tác nhân tự khắc phục được.

:::note

Có trong các bản phát hành StackQL sau `v0.10.542`.

:::

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ Nạp (lại) thông tin xác thực ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`provider`|Không|Giới hạn phạm vi báo cáo trạng thái ở một provider.  Khi không được cung cấp, mọi provider đều được báo cáo.|

* * *

## Đầu ra {#output}

| Trình hiển thị | Dạng |
|--|--|
| Bảng | Kết quả nạp tệp env cùng với một hàng cho mỗi provider kèm trạng thái phân giải thông tin xác thực |

<br/>

Payload có cấu trúc chứa:

| Trường | Mô tả |
|--|--|
|`env_file`|Đường dẫn `--env.file` đã cấu hình (rỗng khi không có cấu hình nào).|
|`env_file_sourced`|`true` khi tệp được tìm thấy và được nạp trong lần gọi này.|
|`sourced_vars`|Tên các biến môi trường được thiết lập từ tệp - chỉ có tên, không bao giờ có giá trị.|
|`providers`|Một hàng cho mỗi provider (xem bên dưới).|

<br/>

Mỗi hàng `providers` có:

| Cột | Mô tả |
|--|--|
|`provider`|Tên provider.|
|`auth_type`|Loại xác thực được cấu hình cho provider.|
|`sourced_from`|Nơi thông tin xác thực được phân giải, ví dụ `env:OKTA_SECRET_KEY`, `file:/path/key.json`, `inline` hoặc `none`.|
|`status`|`ok`, `unresolved` hoặc `not_checked`.|
|`detail`|Văn bản lỗi khi `unresolved`.|

<br/>

Các loại xác thực thực hiện xác thực bên ngoài luồng xử lý byte thông tin xác thực (gcloud tương tác, azure CLI, thông tin xác thực ứng dụng khách oauth2, chuỗi đảm nhận vai trò, ký OCI, null) báo cáo `not_checked`.

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ, kể cả `read_only` - công cụ không thay đổi gì ngoài môi trường tiến trình của chính máy chủ.

Không được hỗ trợ ở chế độ backend `reverse_proxy`: truy vấn được thực thi trong tiến trình máy chủ stackql từ xa, vì vậy công cụ trả về lỗi hướng dẫn người dùng nạp lại thông tin xác thực trên máy chủ bên dưới.

* * *

## Ví dụ {#example}

Hãy thử lời nhắc này với bất kỳ ứng dụng khách MCP nào đã đăng ký máy chủ MCP StackQL (được khởi động với `--env.file`):

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
