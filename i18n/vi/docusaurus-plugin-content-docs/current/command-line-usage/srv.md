---
title: srv
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Lệnh stackql srv chạy StackQL như một máy chủ giao thức wire của PostgreSQL, để psql, công cụ BI và trình điều khiển ngôn ngữ truy vấn API đám mây và SaaS bằng SQL."
image: "/img/stackql-featured-image.png"
---

Lệnh dùng để khởi chạy StackQL dưới dạng một dịch vụ, sau đó các ứng dụng khách [giao thức wire của Postgres](https://www.postgresql.org/docs/current/protocol.html) được xác thực bằng mTLS có thể truy cập dịch vụ này. Lệnh `srv` cũng có thể chạy một máy chủ [Model Context Protocol (MCP)](/command-line-usage/mcp) song song với máy chủ giao thức wire của PostgreSQL, cho phép truy cập qua hai giao thức cho cả ứng dụng khách cơ sở dữ liệu truyền thống lẫn tác nhân AI.

* * * 

### Cú pháp {#syntax}

`stackql srv [server_options] [flags]`

* * *

:::note

Mặc dù máy chủ StackQL sử dụng giao thức wire của Postgres, nó không phải là máy chủ Postgres và cũng không cần đến một máy chủ Postgres.  Ứng dụng khách dùng giao thức wire để giao tiếp với máy chủ; máy chủ này dùng StackQL để thực hiện truy vấn hoặc thao tác DML đối với các nhà cung cấp đám mây và SaaS từ xa, rồi trả kết quả về cho ứng dụng khách.

:::

Các tham số được gửi qua giao thức truy vấn mở rộng (`Parse` / `Bind` / `Execute`, như `psycopg` 3, `pgx` và JDBC sử dụng) được liên kết ở phía máy chủ dưới dạng literal chuỗi.  Từ `v0.12.742`, placeholder `$n` nằm bên trong một literal chuỗi, định danh trong dấu ngoặc kép hoặc chú thích sẽ được giữ nguyên, dấu gạch chéo ngược trong giá trị tham số được thoát và giá trị văn bản `NULL` vẫn là chuỗi `NULL` (`NULL` của SQL được truyền riêng).

### Tùy chọn máy chủ {#server-options}

| Tùy chọn | Mô tả | Mặc định |
|--|--|--|
|<span class="nowrap">`--pgsrv.address`</span>|Địa chỉ mà máy chủ lắng nghe.|`0.0.0.0`|
|<span class="nowrap">`--pgsrv.port`</span>|Cổng TCP mà máy chủ lắng nghe.|`5466`|
|<span class="nowrap">`--pgsrv.tls`</span>|Đối tượng cấu hình TLS dạng JSON. Xem [ví dụ mTLS](#examples) bên dưới.|không có|
|<span class="nowrap">`--pgsrv.debug.enable`</span>|Bật đầu ra gỡ lỗi truy vấn cho các ứng dụng khách PostgreSQL, ví dụ với `stackql srv --pgsrv.debug.enable=true`. Hãy để tắt, trừ khi ứng dụng khách cần nhận thông tin gỡ lỗi.|`false`|
|<span class="nowrap">`--pgsrv.sundry`</span>|Ánh xạ JSON hoặc YAML nâng cao, chỉ được truyền cho máy chủ wire PostgreSQL khi `--pgsrv.tls` không được đặt.|`{}`|
|<span class="nowrap">`--pgsrv.loglevel`</span>|Được chấp nhận nhưng không đặt mức của trình ghi nhật ký trong công cụ hiện tại. Thay vào đó, hãy dùng `--loglevel`.|`WARN`|

<br/>

> xem [Cờ toàn cục](/command-line-usage/global-flags) để biết các tùy chọn bổ sung

### Tùy chọn máy chủ MCP {#mcp-server-options}

Khi chạy lệnh `srv` có hỗ trợ MCP, bạn có thể dùng các tùy chọn bổ sung sau:

| Tùy chọn | Mô tả |
|--|--|
|<span class="nowrap">`--mcp.server.type`</span>|Loại máy chủ MCP: `http` (trong bộ nhớ) hoặc `reverse_proxy` (dựa trên TCP)|
|<span class="nowrap">`--mcp.config`</span>|Đối tượng cấu hình JSON cho máy chủ MCP (xem [tài liệu MCP](/command-line-usage/mcp))|
|<span class="nowrap">`--mcp.log.format`</span>|Mã hóa nhật ký kiểm tra MCP: `jsonl` (mặc định) hoặc `otel` (bản ghi nhật ký OTLP/JSON); ghi đè `server.audit.format` trong đối tượng cấu hình|
|<span class="nowrap">`--mcp.protocol.version`</span>|Bản sửa đổi giao thức MCP mới nhất được công bố: `auto` (mặc định), `2026-07-28` (chỉ chế độ không có phiên) hoặc một bản sửa đổi cũ hơn như `2025-11-25`; ghi đè `server.protocol_version` trong đối tượng cấu hình|

<br/>

:::info[Thay đổi không tương thích ngược trong v0.12.742]

Từ `v0.12.742`, phương thức truyền tải HTTP của MCP (`--mcp.server.type=http` hoặc `reverse_proxy`) từ chối khởi động trừ khi đối tượng cấu hình chỉ định một bearer token bằng `server.auth_token_env_var` hoặc chủ động tắt yêu cầu này một cách rõ ràng bằng `"allow_unauthenticated": true`.  Xem [Xác thực ứng dụng khách HTTP](/command-line-usage/mcp#http-client-authentication).

:::

:::tip

Xem [tài liệu về lệnh MCP](/command-line-usage/mcp) để biết thông tin chi tiết về cách cấu hình và sử dụng máy chủ MCP, bao gồm các chế độ triển khai, tùy chọn cấu hình và tích hợp với trợ lý AI.

:::

:::info

Bạn cần đặt các biến môi trường cần thiết cho việc xác thực provider trước khi khởi động máy chủ; xem [Sử dụng provider](/getting-started/using-a-provider) để biết thêm thông tin.

:::

Đối với máy chủ chạy lâu dài, cờ [`--env.file`](/command-line-usage/global-flags) cho phép bạn đặt sẵn hoặc xoay vòng thông tin xác thực trong một tệp kiểu dotenv trên đĩa trước khi khởi động, thay vì xuất biến vào shell:

```bash
stackql srv --pgsrv.port 5444 --env.file /etc/stackql/credentials.env
```

Với `stackql srv`, tệp được đọc một lần khi khởi động; cách nạp lại giữa phiên là công cụ MCP [`reload_credentials`](/mcp/reload_credentials).

### Cờ {#flags}

| Cờ | Mô tả |
|--|--|
|`-h,--help`|In thông tin trợ giúp|
|`-v,--verbose`|Chạy truy vấn ở chế độ chi tiết với đầu ra bổ sung được gửi tới stdout; nếu cờ toàn cục `-f` (`--outfile`) được đặt,<br/>thông tin nhật ký bổ sung này sẽ được ghi vào tệp đầu ra cùng với kết quả truy vấn|

<br/>

> xem [Cờ toàn cục](/command-line-usage/global-flags) để biết các tùy chọn bổ sung

* * *

### Ví dụ {#examples}

Khởi chạy một tiến trình máy chủ StackQL và kết nối bằng ứng dụng khách dòng lệnh `psql`.  

Trước tiên, tải xuống tệp cấu hình `openssl.cnf` từ [stackql/stackql](https://raw.githubusercontent.com/stackql/stackql/main/test/server/mtls/openssl.cnf).  

Tiếp theo, tạo các tệp chứng chỉ và khóa mà máy chủ và ứng dụng khách sẽ dùng cho xác thực TLS hai chiều (mTLS).  

```bash
openssl req -x509 -keyout ~/stackqlcreds/server_key.pem -out ~/stackqlcreds/server_cert.pem -config ./openssl.cnf -days 365
openssl req -x509 -keyout ~/stackqlcreds/client_key.pem -out ~/stackqlcreds/client_cert.pem -config ./openssl.cnf -days 365
chmod 400 ~/stackqlcreds/client_key.pem
```

Bây giờ, đặt các biến môi trường; nếu ứng dụng khách và máy chủ ở trên cùng một máy, có thể dùng chung các biến này.  

```bash
export PGPORT=5444
export PGSSLCERT=~/stackqlcreds/client_cert.pem
export PGSSLKEY=~/stackqlcreds/client_key.pem
export PGSSLROOTCERT=~/stackqlcreds/server_cert.pem
export PGSSLSRVKEY=~/stackqlcreds/server_key.pem
export CLIENT_CERT=$(base64 -w 0 ~/stackqlcreds/client_cert.pem)
export PGSSLMODE=allow
```

Thiết lập thông tin xác thực cho các provider mà máy chủ cần. Với GitHub, provider đọc tên người dùng và token truy cập cá nhân từ hai biến sau:  

```bash
export STACKQL_GITHUB_USERNAME=youruser
export STACKQL_GITHUB_PASSWORD=ghp_youraccesstoken
```

Bây giờ, khởi động máy chủ trong một terminal.  

```bash
stackql srv \
--pgsrv.address=0.0.0.0 \
--pgsrv.port=$PGPORT \
--pgsrv.tls='{ "keyFilePath": "'${PGSSLSRVKEY}'", "certFilePath": "'${PGSSLROOTCERT}'", "clientCAs": [ "'${CLIENT_CERT}'" ] }'
```

Trong một terminal khác, kết nối tới máy chủ bằng ứng dụng khách dòng lệnh `psql`.  

```bash
psql -h 127.0.0.1 -d stackql
```

Bây giờ, chạy một truy vấn StackQL đối với một provider đã được xác thực.  

```
psql (12.11 (Ubuntu 12.11-0ubuntu0.20.04.1), server 0.0.0)
Type "help" for help.

stackql=> SHOW SERVICES IN github LIKE '%repos%';
      id      | name  |           title
--------------+-------+----------------------------
 repos:v0.3.3 | repos | GitHub v3 REST API - repos
(1 row)

stackql=>
```
