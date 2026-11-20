---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - hash
  - digest
description: "Hàm MD5 trong StackQL: trả về hash MD5 dạng thập lục phân chữ thường của một chuỗi, để lấy dấu vân tay và phát hiện thay đổi chứ không dùng cho bảo mật."
image: "/img/stackql-featured-image.png"
---
Trả về hash MD5 của một chuỗi dưới dạng 32 ký tự thập lục phân chữ thường.  Có từ StackQL `v1.0` trở đi.

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Nối chuỗi ]](/language-spec/functions/string/concatenation)

* * * 

## Cú pháp {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expression*__  
Một chuỗi, hoặc một giá trị được ép kiểu thành văn bản.  Đối số `NULL` trả về `NULL`.

## Giá trị trả về {#return-values}

Trả về một chuỗi thập lục phân chữ thường gồm 32 ký tự.

> `MD5` được cung cấp để lấy dấu vân tay và phát hiện thay đổi.  Hàm này không phải là một cơ chế dành cho mật khẩu hay bảo mật.  Trên backend PostgreSQL, hàm `md5` của chính cơ sở dữ liệu sẽ được dùng.

* * *

## Ví dụ {#examples}

### Lấy dấu vân tay của một giá trị dẫn xuất {#fingerprint-a-derived-value}

Các biến được thay thế dưới dạng giá trị có kiểu, số nguyên được chuyển thành văn bản và nối với hậu tố, sau đó hash được tính trên kết quả đã chuyển thành chữ thường.  Cùng một đầu vào luôn tạo ra cùng một chuỗi thập lục phân 32 ký tự, vì vậy giá trị này có thể dùng làm khóa ổn định.

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### Phát hiện thay đổi cấu hình {#detect-configuration-changes}

Tính hash của một cột JSON đã tuần tự hóa cho ra một giá trị ngắn để lưu trữ và so sánh giữa các lần chạy.  Một bucket có hash khác với lần chạy trước đã thay đổi cấu hình IAM, mà không cần lưu trữ hay so sánh khác biệt toàn bộ tài liệu.

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
