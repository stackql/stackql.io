---
title: registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Lệnh stackql registry liệt kê, tải xuống và quản lý các provider từ registry provider StackQL qua dòng lệnh."
image: "/img/stackql-featured-image.png"
---

Lệnh dùng để tương tác với StackQL Provider Registry.  Vị trí của registry và việc có xác minh chữ ký của tài liệu provider hay không được cấu hình bằng cờ toàn cục [`--registry`](/command-line-usage/global-flags#--registry).  Lệnh `registry` không khả dụng khi [quản lý truy cập](/command-line-usage/access-management) đang được bật; thay vào đó, hãy dùng `REGISTRY PULL` trong một phiên `exec` hoặc `shell` được bảo vệ.

* * * 

### Cú pháp {#syntax}

`stackql registry {subcommand} [{provider} [{version}]]`

* * *

### Đối số {#arguments}

| Đối số | Mô tả | Ví dụ |
|--|--|--|
|<span class="nowrap">`subcommand`</span>|Thao tác registry cần thực hiện (`list` hoặc `pull`) | `list` hoặc `pull` |    
|<span class="nowrap">`provider`</span>|Provider cần cài đặt bằng lệnh con `pull`| `okta` |
|<span class="nowrap">`version`</span>|Phiên bản provider cần cài đặt bằng lệnh con `pull`, không bắt buộc *(phiên bản tài liệu provider StackQL)* | `v23.03.00121` |


### Cờ {#flags}

| Cờ | Mô tả |
|--|--|
|<span class="nowrap">`-h, --help`</span>|In thông tin trợ giúp|
<br/>
<br/>
> xem [Cờ toàn cục](/command-line-usage/global-flags) để biết các tùy chọn bổ sung

* * *

### Ví dụ {#examples}

Liệt kê các provider hiện có trong registry công khai:
```shell
stackql registry list
```

Tải xuống phiên bản mới nhất của một provider từ registry công khai:
```shell
stackql registry pull okta
```
