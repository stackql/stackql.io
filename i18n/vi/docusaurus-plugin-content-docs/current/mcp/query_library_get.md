---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: Công cụ MCP lấy một template truy vấn StackQL và kiểm tra các tham số được cung cấp để trả về SQL đã tạo mà không thực thi
image: "/img/stackql-featured-image.png"
---

Lấy một mục của [query library](pathname:///docs/query-library/) theo `id` được trả về từ [`query_library_search`](/mcp/query_library_search).

Khi bỏ qua `params` hoặc để trống, công cụ trả về template thô, các khai báo tham số và ghi chú. Khi có đối tượng `params` không rỗng, máy chủ kiểm tra tính hợp lệ của các giá trị và thay thế chúng vào template. Công cụ chỉ trả về SQL khi tạo SQL thành công. Mô hình không cần tự thay thế giá trị.

Công cụ này không thực thi SQL và không kiểm tra thông tin xác thực đám mây. Việc kiểm tra tham số của công cụ này khác với [`validate_select_query`](/mcp/validate_select_query), là công cụ phân tích cú pháp và lập kế hoạch cho một câu lệnh `SELECT` dựa trên provider đã cài đặt.

Xem thêm:
[[ Tổng quan về MCP ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## Đầu vào {#inputs}

| Đối số | Bắt buộc | Mô tả |
|--|--|--|
|`id`|Có|ID truy vấn lấy từ kết quả tìm kiếm, ví dụ `aws/s3/buckets-list`.|
|`params`|Không|Đối tượng ánh xạ tên tham số đã khai báo tới giá trị. Bỏ qua hoặc truyền `{}` để xem template mà không tạo SQL.|
|`format`|Không|Định dạng hiển thị văn bản: `markdown` hoặc `json`. Khi bỏ qua, sử dụng thiết lập hiển thị của máy chủ (theo mặc định là `markdown`).|

Tham số không xác định bị từ chối. Tham số bắt buộc không có giá trị được cung cấp hoặc giá trị mặc định sẽ được báo cáo trong `missing_params`. Giá trị được cung cấp được kiểm tra theo kiểu và giới hạn đã khai báo; placeholder chưa được phân giải là lỗi.

* * *

## Đầu ra {#output}

Dạng hiển thị văn bản mặc định là một khối KV gồm danh tính, trạng thái thay đổi dữ liệu, công cụ tiếp theo, tính hợp lệ, liên kết nguồn và template, SQL đã tạo hoặc lỗi nếu có. Sử dụng payload có cấu trúc hoặc `format: "json"` để lấy toàn bộ siêu dữ liệu:

| Trường | Mô tả |
|--|--|
|`id`, `title`, `description`|Danh tính và tóm tắt của mục.|
|`mutation`, `verb`|Mục có thay đổi tài nguyên hay không và, khi được khai báo, loại thao tác của mục (`select`, `mutation` hoặc `lifecycle`).|
|`next_tool`|Công cụ thực thi cho mục: `run_select_query`, `run_mutation_query` hoặc `run_lifecycle_operation`. Việc truy xuất không gọi công cụ này.|
|`params`|Các khai báo tham số với tên, kiểu, cờ bắt buộc và mọi giá trị mặc định, mô tả, ví dụ, enum hoặc mẫu nếu có.|
|`template`, `notes`|Template thô và ghi chú của tác giả, được trả về khi bỏ qua `params` hoặc để trống.|
|`rendered`, `valid`|Việc tạo SQL đã được thử hay chưa và có thành công hay không. Cả hai đều là `false` khi xem template thô.|
|`sql`|SQL đã tạo, chỉ có sau khi tạo SQL thành công.|
|`missing_params`, `unknown_params`, `errors`|Chi tiết kiểm tra khi tạo SQL thất bại. Không thực thi SQL cho đến khi các vấn đề này được khắc phục.|
|`auth`, `permissions`|Khi được khai báo, các biến môi trường chứa thông tin xác thực và các quyền gốc của provider cần thiết để thực thi. Bản thân việc truy xuất không cần đến thứ nào trong số đó.|
|`cost`, `outputs`, `related`|Khi được khai báo, gợi ý về fan-out và chi phí, mô tả cột kết quả và ID của các mục liên quan.|
|`doc_url`|Trang thư viện dành cho người đọc.|
|`source_tier`, `source_url`, `stale`|Nguồn gốc nội dung: `primary`, `fallback` hoặc `snapshot`, URL hoặc đường dẫn nhúng của nguồn đó và liệu nội dung có được phục vụ từ cache hoặc ảnh chụp nhanh sau khi tìm nạp thất bại hay không.|

ID không xác định hoặc sai định dạng sẽ trả về lỗi. Sử dụng `query_library_search` để tìm ID hợp lệ.

* * *

## Kiểm soát {#gating}

Được cho phép ở mọi chế độ máy chủ, tùy thuộc vào danh sách cho phép công cụ đã cấu hình. Việc truy xuất hoặc tạo SQL từ một template thay đổi dữ liệu hoặc template vòng đời không có tác động phía đám mây và không cần phê duyệt. Việc thực thi SQL được trả về là một lệnh gọi riêng, chịu sự điều chỉnh của [chế độ](/command-line-usage/mcp#server-modes) máy chủ.

* * *

## Ví dụ {#example}

Trước tiên, xem một mục:

```json
{
  "id": "aws/s3/buckets-list"
}
```

Sau đó, cung cấp khu vực định tuyến bắt buộc của mục đó:

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

Sau khi kiểm tra `rendered` và `valid`, hãy kiểm tra tính hợp lệ của câu lệnh `SELECT` được trả về bằng [`validate_select_query`](/mcp/validate_select_query). Thực thi câu lệnh bằng công cụ có tên trong `next_tool`, truyền `sql` được trả về và đặt `source` thành `id` của mục thư viện để ghi nhận nguồn gốc. Thông tin xác thực và quyền của provider là bắt buộc tại thời điểm thực thi.
