---
title: Sử dụng provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Sử dụng provider trong StackQL: tải provider xuống từ registry, xác thực bằng biến môi trường hoặc thông tin xác thực và truy vấn các dịch vụ, tài nguyên của provider."
image: "/img/stackql-featured-image.png"
---

Xem thêm:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell) [[ StackQL Provider Registry ]](/providers)

Truy vấn StackQL có thể được chạy trên nhiều nhà cung cấp đám mây khác nhau, bao gồm Google Cloud Platform, Amazon Web Services, Microsoft Azure, DigitalOcean và nhiều nhà cung cấp khác. Đặc tả provider StackQL được tải xuống từ [__StackQL Provider Registry__](/providers), việc này có thể thực hiện bằng lệnh StackQL [__`REGISTRY PULL`__](/language-spec/registry), hoặc bằng tiện ích dòng lệnh [__`stackql registry`__](/command-line-usage/registry).


## Cài đặt provider {#installing-a-provider}

Để xem danh sách các provider hiện có, hãy chạy lệnh sau:  

```sql
REGISTRY LIST;
```

Để cài đặt một provider, hãy chạy lệnh sau:

```sql
REGISTRY PULL okta;
```

Để xem các provider đã cài đặt trên hệ thống hiện tại, hãy chạy lệnh sau:

```sql
SHOW PROVIDERS;
```

## Xác thực với provider {#authenticating-to-a-provider}

Xác thực với một nhà cung cấp đám mây thường là bước đầu tiên trong một quy trình StackQL. Các lệnh meta như [`SHOW`](/language-spec/show) và [`DESCRIBE`](/language-spec/describe) có thể được chạy trên một provider mà không cần xác thực, nhưng các lệnh [`SELECT`](/language-spec/select), [`INSERT`](/language-spec/insert) và [`EXEC`](/language-spec/exec) thường cần một phiên đã xác thực.  

Việc xác thực được thực hiện bằng các biến môi trường được đặt trên hệ thống (hoặc được lấy từ bí mật trong một hệ thống CI), mỗi provider có các biến mặc định riêng dùng để xác thực, vì vậy hãy tham khảo tài liệu của provider bạn dùng trong [tài liệu StackQL Provider Registry](/providers).  Các biến này cũng có thể được nạp từ một tệp kiểu dotenv khi khởi động bằng cờ toàn cục [`--env.file`](/command-line-usage/global-flags).  

:::tip

Bạn có thể xác thực với nhiều provider trong cùng một shell tương tác hoặc quy trình lô không tương tác bằng cách đặt các biến xác thực của từng provider, ví dụ:  

```
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
export DIGITALOCEAN_TOKEN=dop_v1_yourdigitaloceantoken
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

Điều này đặc biệt hữu ích khi bạn nối dữ liệu giữa nhiều provider trong một truy vấn duy nhất.

:::
