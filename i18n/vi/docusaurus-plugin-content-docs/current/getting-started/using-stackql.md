---
title: Sử dụng StackQL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Sử dụng StackQL: chạy truy vấn tương tác trong shell, không tương tác với exec hoặc dưới dạng máy chủ, và khám phá provider, dịch vụ và tài nguyên trong quá trình làm việc."
image: "/img/stackql-featured-image.png"
---
Xem thêm:  
[[` exec `]](/command-line-usage/exec) [[` shell `]](/command-line-usage/shell)

StackQL có một số chế độ sử dụng, bao gồm: shell tương tác StackQL, dòng lệnh StackQL với các câu lệnh StackQL hoặc thực thi một hoặc nhiều câu lệnh StackQL trong một tệp đầu vào (tệp IQL).  Từng chế độ được mô tả chi tiết hơn bên dưới:

- Sử dụng shell tương tác StackQL ([`stackql shell`](/command-line-usage/shell))
- Sử dụng tiện ích dòng lệnh StackQL ([`stackql exec`](/command-line-usage/exec))
- Sử dụng tệp đầu vào (tệp IQL) ([`stackql exec -i`](/command-line-usage/exec))

### Sử dụng shell tương tác StackQL {#using-the-stackql-interactive-shell}
Shell StackQL cung cấp một môi trường lập trình tương tác để các kỹ sư triển khai và tương tác với tài nguyên đám mây, đồng thời cho phép phân tích tương tác (bằng các câu lệnh [`SELECT`](/language-spec/select)) môi trường đám mây của họ.  Shell này hoạt động tương tự shell MySQL hoặc các shell REPL (Read Evaluate Print Loop) khác.  Shell chấp nhận các câu lệnh StackQL hợp lệ và trả về kết quả cho người dùng.  Câu lệnh có thể trải trên nhiều dòng và được thực thi khi nhập dấu chấm phẩy kết thúc.

Có thể khởi chạy shell StackQL bằng lệnh `shell` như minh họa dưới đây (xem [StackQL Provider Registry](/providers) để biết thông tin về xác thực provider):

```shell
# Launching the StackQL Interactive Shell
stackql shell --auth="${AUTH}"
```
Khi đã ở trong một shell đã xác thực với một nhà cung cấp đám mây, bạn có thể bắt đầu chạy các truy vấn StackQL (truy vấn `SELECT` hoặc thao tác DDL - hạ tầng dưới dạng mã).

Theo mặc định, shell StackQL trả về kết quả dạng bảng, tuy nhiên có thể dùng các định dạng đầu ra `json`, `jsonl`, `csv` và `text` thông qua cờ `--output`, xem [Chế độ đầu ra](/getting-started/output-modes).  

### Tệp khởi tạo `.stackqlrc` {#the-stackqlrc-initialization-file}

Giá trị mặc định cho các tùy chọn lệnh StackQL, chẳng hạn như định dạng đầu ra, có thể được đặt bằng tệp khởi tạo `.stackqlrc`.  Nếu tệp này tồn tại trong thư mục hiện tại mà tệp thực thi StackQL đang dùng, tệp sẽ được tự động nạp, hoặc bạn có thể tham chiếu đến tệp bằng cờ `--configfile` như minh họa dưới đây:

```shell
# Specifying an Initialization (.stackqlrc) File
./stackql shell --configfile /tmp/.stackqlrc
```
Thông tin thêm về các tùy chọn lệnh có trong [Cờ toàn cục](/command-line-usage/global-flags).

### Sử dụng tiện ích dòng lệnh StackQL {#using-the-stackql-command-line-utility}
Có thể thực thi các lệnh StackQL một cách không tương tác bằng lệnh exec của tệp thực thi StackQL.  Có thể thực thi nhiều lệnh trong một lô, miễn là mỗi lệnh riêng lẻ kết thúc bằng dấu chấm phẩy (`;`).  Các cờ lệnh, chẳng hạn như cờ `--output`, có thể được chỉ định tại dòng lệnh hoặc bằng tệp khởi tạo `.stackqlrc` đã đề cập ở trên.  

Đầu ra từ tệp thực thi StackQL được gửi đến stdout trên hệ thống đang dùng, đầu ra này có thể được phân tích cú pháp trong một tập lệnh BASH hoặc PowerShell.  

Dưới đây là một ví dụ về tập lệnh Windows PowerShell có chứa một lệnh StackQL:  

```shell
$gcp_project = "stackql-demo"
$gcp_zone = "australia-southeast1-a"
$env:GOOGLE_CREDENTIALS = Get-Content -Raw "C:\tmp\stackql-demo.json"

Write-Output "Selecting instances in ${gcp_zone}..."

$query = "`"select id, name, machineType `
			from google.compute.instances `
			where project = '${gcp_project}' `
			and zone = '${gcp_zone}';`""
$flags = "--output json"
$cmd = "stackql.exe exec"

$select = $cmd, $query, $flags -join " "

(iex $select | ConvertFrom-Json) | Out-GridView -Title 'StackQL Query Results'
```
Lệnh PowerShell ở trên minh họa việc chọn các instance Compute Engine và hiển thị đầu ra bằng `Out-GridView`.  

### Sử dụng tệp đầu vào (tệp IQL) {#using-an-input-file-iql-file}
Có thể dùng tệp đầu vào StackQL để thực thi nhiều lệnh được kết thúc bằng dấu chấm phẩy.  Đây là một lựa chọn khác cho việc lập trình hạ tầng không tương tác hoặc theo lô.  Tệp đầu vào được chỉ định bằng tùy chọn `-i` hoặc `--infile` như minh họa dưới đây:

```shell
# create an iql file
echo "select id, name from google.compute.instances \
where project = 'stackql-demo' \
and zone = 'australia-southeast1-a'" > listinstances.iql
# execute an iql file
./stackql exec -i listinstances.iql \
--auth '{ "google": { "type": "service_account", "credentialsfilepath": "/mnt/c/tmp/stackql-demo.json" } }' \
--output json | jq '.[] | .name'
```
