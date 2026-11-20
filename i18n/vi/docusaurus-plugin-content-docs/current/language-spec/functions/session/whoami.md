---
title: WHOAMI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - quản lý truy cập
  - whoami
  - principal
description: "Hàm WHOAMI trong StackQL: trả về ID principal của bên gọi đã xác thực khi quản lý truy cập được bật."
image: "/img/stackql-featured-image.png"
---
Trả về ID principal của bên gọi đã xác thực.  Yêu cầu một phiên được khởi động với [quản lý truy cập](/command-line-usage/access-management) đã bật.  Có từ StackQL `v1.0` trở đi.

Xem thêm:  
[[` GROUPS `]](/language-spec/functions/session/groups) [[ Quản lý truy cập ]](/command-line-usage/access-management)

* * * 

## Cú pháp {#syntax}

```sql
SELECT WHOAMI() [ AS <alias> ];
```

## Đối số {#arguments}

Không có.

## Giá trị trả về {#return-values}

Trả về chuỗi ID principal được thiết lập cho câu lệnh hiện tại.  Hàm bị lỗi trong một phiên được khởi động không có `--access.config`.

* * *

## Ví dụ {#examples}

### Kiểm tra danh tính đã được thiết lập {#inspect-the-established-identity}

Cả hai hàm đều không nhận đối số và mô tả bên gọi mà máy chủ đã xác thực cho câu lệnh này.  `whoami()` trả về ID principal và `groups()` trả về các nhóm thư mục của bên gọi, vì vậy chạy chúng cùng nhau là cách kiểm tra nhanh rằng một token hoặc chứng chỉ ánh xạ đến đúng principal mong đợi trước khi chạy bất kỳ thứ gì khác.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```
