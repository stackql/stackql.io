---
title: shell
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Lệnh stackql shell khởi chạy shell lệnh StackQL tương tác để chạy truy vấn và lệnh provider trên các tài khoản đám mây và SaaS của bạn."
image: "/img/stackql-featured-image.png"
---

Lệnh dùng để khởi chạy shell lệnh StackQL tương tác.  Để biết thêm thông tin, hãy xem [Sử dụng shell tương tác StackQL](/getting-started/using-stackql#using-the-stackql-interactive-shell).

* * * 

### Cú pháp {#syntax}

`stackql shell [flags]`

* * *

### Cờ {#flags}

| Cờ | Mô tả |
|--|--|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|In thông tin trợ giúp|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Chạy truy vấn ở chế độ chi tiết với đầu ra bổ sung được gửi tới stdout; nếu cờ toàn cục `-f` (`--outfile`) được đặt, thông tin nhật ký bổ sung này sẽ được ghi vào tệp đầu ra cùng với kết quả truy vấn|
<br/>
<br/>
> xem [Cờ toàn cục](/command-line-usage/global-flags) để biết các tùy chọn bổ sung

* * *

### Ví dụ {#examples}

Khởi chạy shell StackQL, sử dụng tệp khóa tài khoản dịch vụ để xác thực.
```shell
stackql shell --auth '{ "google": { "type": "service_account", "credentialsfilepath": "/tmp/stackql-demo.json" } }'
```
