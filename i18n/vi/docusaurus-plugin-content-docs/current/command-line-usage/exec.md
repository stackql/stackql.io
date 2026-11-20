---
title: exec
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Lệnh stackql exec chạy một câu lệnh StackQL hoặc một tập lệnh gồm nhiều câu lệnh, với các cờ cho định dạng đầu ra, xác thực và registry provider."
image: "/img/stackql-featured-image.png"
---

Lệnh dùng để chạy một truy vấn StackQL, dưới dạng một câu lệnh hoặc một lô câu lệnh từ tập lệnh StackQL (tệp IQL).  

* * * 

### Cú pháp {#syntax}

`stackql exec [query] [flags]`

* * *

### Đối số {#arguments}

| Đối số | Mô tả | Ví dụ |
|--|--|--|
|`query`|Truy vấn StackQL cần thực hiện<br/>hoặc lệnh cần thực thi | `"SELECT * FROM google.compute.instances`<br/>`WHERE project = 'myproject' AND zone = 'us-east1-a'"`<br/>_Lưu ý rằng truy vấn phải được đặt trong dấu nháy đơn hoặc nháy kép_ |    


### Cờ {#flags}

| Cờ | Mô tả |
|--|--|
|<span class="nowrap">`-o`</span><br/><span class="nowrap">`--output`</span>|Chế độ đầu ra, các giá trị hợp lệ gồm: `table` (mặc định), `json`, `jsonl` (JSON phân tách bằng ký tự dòng mới, `ndjson` được chấp nhận làm bí danh), `csv`, `text` và `otel` (bản ghi nhật ký OpenTelemetry, xem [Chế độ đầu ra](/getting-started/output-modes#otel-output)), ví dụ: `--output jsonl` |
|<span class="nowrap">`-i`</span><br/><span class="nowrap">`--infile`</span>|Chạy truy vấn từ tệp được chỉ định nếu cần; nếu cờ này được chỉ định, mọi truy vấn được cung cấp dưới dạng đối số sẽ bị bỏ qua, ví dụ: `--infile myquery.iql` |
|<span class="nowrap">`-q`</span><br/><span class="nowrap">`--iqldata`</span>|Dữ liệu nguồn cho lệnh, sử dụng tệp `json` hoặc `jsonnet`, ví dụ: `--iqldata vars.jsonnet`|
|<span class="nowrap">`--dryrun`</span>|Cờ chạy thử; chỉ bộ tiền xử lý chạy và đầu ra sẽ được trả về |
|<span class="nowrap">`--var`</span>|Biến bên ngoài (có thể là biến môi trường hoặc bí mật) được các tệp jsonnet chỉ định bằng cờ `--iqldata` sử dụng, ví dụ `--var region=${AWS_REGION}`|
|<span class="nowrap">`-f`</span><br/><span class="nowrap">`--outfile`</span>|Tệp để ghi kết quả vào nếu cần (hành vi mặc định là chỉ xuất kết quả ra bảng điều khiển), ví dụ `--outfile myoutput.csv`|
|<span class="nowrap">`-h`</span><br/><span class="nowrap">`--help`</span>|In thông tin trợ giúp|
|<span class="nowrap">`-v`</span><br/><span class="nowrap">`--verbose`</span>|Chạy truy vấn ở chế độ chi tiết với đầu ra bổ sung được gửi tới stdout; nếu chọn tùy chọn `-f`, thông tin nhật ký bổ sung này sẽ được ghi vào tệp đầu ra cùng với kết quả truy vấn|
<br/>
<br/>
> xem [Cờ toàn cục](/command-line-usage/global-flags) để biết các tùy chọn bổ sung


* * *

### Ví dụ {#examples}

Xuất kết quả truy vấn ra bảng điều khiển ở định dạng bảng:
```shell
stackql exec "SHOW services IN google"
```

Xuất kết quả truy vấn ra bảng điều khiển ở định dạng json:
```shell
stackql exec "SHOW services IN google" -o json
```

Xuất kết quả truy vấn ra tệp ở định dạng json:
```shell
stackql exec "SHOW services IN google" -o json -f services.json
```

Thực thi truy vấn từ tệp đầu vào (tệp IQL) và xuất kết quả ra tệp ở định dạng json:
```shell
stackql exec -i getservices.iql -o json -f services.json
```

* * *

### Trạng thái thoát {#exit-status}

Lệnh `exec` thoát với trạng thái `0` sau khi đã xử lý xong đầu vào; lỗi từ từng câu lệnh riêng lẻ được ghi ra bảng điều khiển, vì vậy các tập lệnh nên kiểm tra đầu ra.  Từ `v1.0`, lệnh thoát với trạng thái `1` khi một câu lệnh cấu hình [`SET`](/language-spec/set) hoặc [`UNSET`](/language-spec/unset) thất bại, khi [quản lý truy cập](/command-line-usage/access-management) được bật và một câu lệnh bị từ chối hoặc khi một lần chạy IaC bản xem trước báo cáo một tài nguyên thất bại.
