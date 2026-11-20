---
title: GROUPS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - quản lý truy cập
  - groups
  - thư mục principal
description: "Hàm GROUPS trong StackQL: trả về các nhóm thư mục của bên gọi đã xác thực dưới dạng mảng JSON khi quản lý truy cập được bật."
image: "/img/stackql-featured-image.png"
---
Trả về tư cách thành viên nhóm thư mục của bên gọi đã xác thực dưới dạng mảng JSON.  Yêu cầu một phiên được khởi động với [quản lý truy cập](/command-line-usage/access-management) đã bật.  Có từ StackQL `v1.0` trở đi.

Xem thêm:  
[[` WHOAMI `]](/language-spec/functions/session/whoami) [[ Quản lý truy cập ]](/command-line-usage/access-management)

* * * 

## Cú pháp {#syntax}

```sql
SELECT GROUPS() [ AS <alias> ];
```

## Đối số {#arguments}

Không có.

## Giá trị trả về {#return-values}

Trả về một mảng JSON gồm các ID nhóm từ ảnh chụp nhanh thư mục principal được lấy cho câu lệnh hiện tại, ví dụ `["readers"]`.  Tư cách thành viên được làm mới cho mỗi câu lệnh, vì vậy việc thu hồi được phản ánh ở câu lệnh tiếp theo.  Hàm bị lỗi trong một phiên được khởi động không có `--access.config`.

* * *

## Ví dụ {#examples}

### Kiểm tra danh tính đã được thiết lập {#inspect-the-established-identity}

Khi dùng cùng nhau, hai hàm này hiển thị ID principal của bên gọi và các nhóm thư mục hiện tại của nó.  Vì tư cách thành viên được đọc lại cho mỗi câu lệnh, việc chạy truy vấn này sau một thay đổi thư mục sẽ xác nhận rằng phiên đã nhìn thấy thay đổi đó.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
