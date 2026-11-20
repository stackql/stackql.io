---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: Công cụ MCP tìm kiếm các template truy vấn StackQL được tuyển chọn theo ý định bằng ngôn ngữ tự nhiên, provider, dịch vụ và tag
image: "/img/stackql-featured-image.png"
---

Tìm kiếm trong [Query Library StackQL](pathname:///docs/query-library/) các template khớp với một ý định được diễn đạt bằng ngôn ngữ tự nhiên. Hãy tham khảo công cụ này trước khi tự soạn SQL từ đầu, sau đó truyền `id` của một kết quả cho [`query_library_get`](/mcp/query_library_get) để xem chi tiết template và kiểm tra tính hợp lệ của các tham số.

Công cụ này chỉ truy xuất nội dung thư viện. Công cụ không thực thi SQL, không gọi API đám mây và không yêu cầu thông tin xác thực của provider.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`intent`|Có|Mô tả tác vụ bằng ngôn ngữ tự nhiên.|
|`provider`|Không|Lọc theo provider, ví dụ `aws`.|
|`service`|Không|Lọc theo dịch vụ, ví dụ `s3`.|
|`tags`|Không|Mảng các tag. Một mục phải khớp với mọi tag được cung cấp.|
|`include_mutations`|Không|Bao gồm các template thay đổi dữ liệu. Mặc định: `false`; bị bỏ qua ở chế độ `read_only`, khi đó các mục thay đổi dữ liệu luôn bị loại trừ.|
|`limit`|Không|Số kết quả khớp tối đa. Mặc định: `5` khi không được cung cấp hoặc không phải số dương; tối đa là `20`.|
|`format`|Không|Định dạng hiển thị văn bản: `markdown` hoặc `json`. Khi không được cung cấp, công cụ dùng thiết lập hiển thị của máy chủ (theo mặc định là `markdown`).|

* * *

## Đầu ra {#output}

Kết quả dạng văn bản mặc định là một bảng được xếp hạng gồm `id`, `title`, `mutation`, `required_params` và `score`, theo sau là trích dẫn nguồn và hướng dẫn khi không tìm thấy kết quả phù hợp (nếu có). Payload có cấu trúc chứa:

| Trường | Mô tả |
|--|--|
|`hits`|Các mục được xếp hạng với `id`, `title`, `description` (không bắt buộc), `providers`, `services` và `required_params`, cùng với `mutation` và `score`. Gọi tiếp `query_library_get` để lấy đầy đủ các khai báo tham số.|
|`miss`|`true` khi không có kết quả khớp nào hoặc điểm cao nhất thấp hơn ngưỡng khớp. Các kết quả gần đúng nhất có điểm thấp vẫn có thể được trả về.|
|`guidance`|Khi không khớp, hướng tác nhân đến chỉ dẫn của máy chủ, các công cụ khám phá và `validate_select_query` để soạn truy vấn.|
|`source_tier`|Nguồn nội dung: `primary`, `fallback` hoặc `snapshot`.|
|`source_url`|URL hoặc đường dẫn nhúng của danh mục đã dùng, để trích dẫn.|
|`stale`|Khi là `true`, nội dung được lấy từ cache hoặc ảnh chụp nhanh nhúng sau khi tìm nạp thất bại.|

Các mục đã lỗi thời bị loại trừ. Máy chủ lưu danh mục vào cache và có thể chuyển dự phòng từ thư viện được lưu trữ sang nguồn GitHub của thư viện và một ảnh chụp nhanh nhúng.

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ, tùy thuộc vào danh sách cho phép công cụ đã cấu hình. Không cần phê duyệt hay thông tin xác thực của provider. Việc tìm kiếm template thay đổi dữ liệu không ủy quyền thực thi các template đó.

* * *

## Ví dụ {#example}

Đối số của công cụ:

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

Dùng một `id` từ `hits`, chẳng hạn `aws/s3/buckets-list`, với [`query_library_get`](/mcp/query_library_get). Nếu `miss` là `true`, hãy xem các template gần đúng nhất để tham khảo hoặc dùng [`list_methods`](/mcp/list_methods) và [`describe_method`](/mcp/describe_method) trước khi soạn SQL.
