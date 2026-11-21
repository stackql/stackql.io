---
title: Quản lý truy cập
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - quản lý truy cập
  - xác thực
  - ủy quyền
  - jwt
  - mtls
  - bearer token
description: "Quản lý truy cập bên gọi cho StackQL, cần chủ động bật: xác thực bên gọi bằng JWT, bearer token, TLS hai chiều hoặc danh tính hệ điều hành cục bộ, ánh xạ tới principal và nhóm trong thư mục, rồi ủy quyền câu lệnh bằng quy tắc chính sách."
image: "/img/stackql-featured-image.png"
---

Quản lý truy cập bên gọi xác thực bên gọi của một phiên StackQL và ủy quyền từng câu lệnh theo một chính sách.  Tính năng này độc lập với thông tin xác thực provider, vốn vẫn được cung cấp qua cờ `--auth`, biến môi trường hoặc [phiên provider](/getting-started/provider-sessions).  Tính năng này tắt theo mặc định và các triển khai không bật nó sẽ hoạt động như trước.  Có từ StackQL `v1.0` trở đi.

Xem thêm:  
[[ Cờ toàn cục ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## Bật quản lý truy cập {#enabling-access-management}

Quản lý truy cập được bật bằng cách khởi động StackQL với cờ toàn cục `--access.config` trỏ tới một tệp cấu hình JSON.  Ví dụ sau khởi động một shell tương tác ở chế độ được bảo vệ; lệnh `exec` và `mcp` được bật theo cùng cách.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

Cờ `--access.config` nhận một đối tượng JSON bao ngoài với một khóa `path` duy nhất; một đường dẫn tệp đơn thuần cũng được chấp nhận.  Cấu hình được nạp một lần khi khởi động, vì vậy hãy xoay vòng khóa hoặc chính sách thông qua một lần khởi động lại có kiểm soát.

Khi quản lý truy cập được bật, chỉ các lệnh `exec`, `shell`, `mcp` và `version` chạy; các lệnh khác, bao gồm `registry`, từ chối chạy thay vì bỏ qua luồng xử lý được bảo vệ.  Lệnh `srv` và loại máy chủ MCP `reverse_proxy` cũng từ chối, vì phần phụ thuộc giao thức wire của PostgreSQL chưa cung cấp bằng chứng xác thực cho từng kết nối.  Các câu lệnh SQL `AUTH` và `AUTH REVOKE` bị từ chối ở chế độ được bảo vệ; thay vào đó, hãy cấp phát thông tin xác thực provider thông qua cấu hình máy chủ.  Một lệnh `exec` được bảo vệ khi thất bại sẽ trả về trạng thái thoát `1` sau khi hiển thị lỗi.

## Chế độ xác thực {#authentication-modes}

Tệp cấu hình chọn một giá trị `type`: `jwt` (mặc định khi bỏ qua), `bearer`, `mtls` hoặc `local_os`.  Cấu hình là JSON nghiêm ngặt: các trường không xác định và các trường thuộc về chế độ khác đều bị từ chối.  Mọi chế độ dùng chung `rules` và cùng một [thư mục principal](#principal-directory).

### JWT {#jwt}

Bên gọi xuất trình một token đã ký, token này được xác minh ngoại tuyến theo các khóa được ghim trong cấu hình.  Một cấu hình tối thiểu nêu bên phát hành và đối tượng nhận mà token phải mang, liệt kê một khóa công khai và cho phép thành viên của nhóm `analysts` chạy các câu lệnh đọc (thành viên của nhóm `readers` có thể làm vậy mà không cần quy tắc, xem [Quy tắc chính sách](#policy-rules)):

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

Bên phát hành, đối tượng nhận và khóa công khai được ghim trong cấu hình; không có cơ chế khám phá OIDC.  Các thuật toán được chấp nhận là `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384` và `ES512`, còn khóa RSA phải có ít nhất 2048 bit.  Mỗi khóa được liên kết với thuật toán đã cấu hình cho nó.  Token bị từ chối nếu có ID khóa không xác định, chữ ký không hợp lệ, thời điểm hết hạn bị thiếu hoặc đã qua, `nbf` hoặc `iat` ở tương lai, bên phát hành sai hoặc đối tượng nhận sai.  Các claim nhóm và URL có trong token không được dùng cho việc tin cậy hoặc cho tư cách thành viên thư mục.

### Bearer token tĩnh {#static-bearer-token}

Bên gọi xuất trình một token không trong suốt và cấu hình liệt kê hash SHA-256 của từng token được chấp nhận cùng với chủ thể mà token đó định danh.  Cách này phù hợp với một tập nhỏ, cố định các danh tính dịch vụ, khi việc vận hành một bên phát hành token là không cần thiết:

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

Chỉ hash SHA-256 của từng token được cấu hình và các hash phải là duy nhất.  Thay đổi các hash đã cấu hình cần khởi động lại.

### TLS hai chiều {#mutual-tls}

Bên gọi được nhận diện bằng chứng chỉ ứng dụng khách mà nó xuất trình trên phương thức truyền tải MCP HTTPS.  Cấu hình chứa CA phải là bên đã phát hành chứng chỉ và chỉ ra tên thay thế của chủ thể (subject alternative name) nào chứa danh tính:

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

Chuỗi chứng chỉ ứng dụng khách được xác minh theo `client_ca_pem` và chủ thể danh tính được lấy từ đúng một SAN thuộc loại đã cấu hình, `uri_san` hoặc `dns_san`.  Không có fallback sang Common Name.  TLS hai chiều chỉ khả dụng qua phương thức truyền tải MCP HTTPS.

### Danh tính hệ điều hành cục bộ {#local-os-identity}

Không có thông tin xác thực nào được trao đổi: chủ thể là danh tính hệ điều hành dùng để chạy tiến trình StackQL.  Cấu hình chỉ cần một nhãn `issuer` và các quy tắc:

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:analysts", "resource": "host/sql/read", "action": "execute"}]
}
```

Chủ thể danh tính là UID Unix hoặc SID Windows hiệu lực của tiến trình StackQL, bao gồm cả UID dạng số trong container không có mục passwd.  Không có thông tin xác thực nào được xuất trình và biến môi trường `STACKQL_CALLER_TOKEN` bị bỏ qua.  Chế độ này chỉ dùng cục bộ: bên gọi HTTP bị từ chối.  Tư cách thành viên nhóm của hệ điều hành không trao quyền truy cập; chỉ tư cách thành viên thư mục mới trao quyền.

### Thời hạn phiên {#session-deadline}

Theo mặc định, truy vấn không có thời hạn nào bắt nguồn từ xác thực.  Thông tin xác thực được xác minh và tư cách thành viên thư mục được làm mới cho mỗi câu lệnh mới, vì vậy một thông tin xác thực đã hết hạn không thể bắt đầu công việc mới, nhưng việc hết hạn không làm gián đoạn một câu lệnh đã được ủy quyền.  Đặt `"enforce_session_deadline": true` để hủy khi đạt `max_session_seconds` (mặc định `300`, tối đa `86400`), được rút ngắn theo thời điểm hết hạn của JWT hoặc chứng chỉ.  Thiết lập này không gia hạn token.

## Thư mục principal {#principal-directory}

Danh tính được ánh xạ tới principal, và principal được ánh xạ tới nhóm, trong bốn bảng thuộc backend SQLite hoặc PostgreSQL đã cấu hình:

| Bảng | Nội dung |
|--|--|
| `stackql_access_principals` | ID principal và một đối tượng JSON gồm các thuộc tính chuỗi.  Thuộc tính `tenant` phân tách không gian tên cho trạng thái IaC bản xem trước. |
| `stackql_access_identities` | Cặp `(issuer, subject)` đã xác minh ánh xạ tới ID principal. |
| `stackql_access_groups` | ID nhóm. |
| `stackql_access_memberships` | Liên kết principal với nhóm. |

Cấp phát các bảng, danh tính và tư cách thành viên thông qua một kết nối quản trị cơ sở dữ liệu đáng tin cậy trước khi phục vụ các truy vấn được bảo vệ:

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

Sử dụng bên phát hành và chủ thể thực tế của chế độ: với danh tính hệ điều hành cục bộ, giá trị này có thể là `('this-host', '1000')`, còn với TLS hai chiều là `('client-certificates', 'spiffe://example/alice')`.  Chọn backend một cách rõ ràng:

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

Thư mục tạo ra một ảnh chụp nhanh bất biến cho mỗi câu lệnh.  Thu hồi là một câu lệnh `DELETE FROM stackql_access_memberships` trên kết nối quản trị; câu lệnh tiếp theo sẽ thấy ảnh chụp nhanh mới.  Danh tính không xác định, lược đồ không tồn tại, dữ liệu sai định dạng và sự cố ngừng hoạt động của thư mục đều dẫn đến từ chối truy cập.  SQL được bảo vệ thông thường không thể truy cập các bảng thư mục.  Giữ các thao tác ghi tư cách thành viên trên kết nối quản trị, bảo vệ tệp cơ sở dữ liệu và sử dụng các đặc quyền cơ sở dữ liệu riêng cho quản trị và cho runtime khi backend hỗ trợ.

## Quy tắc chính sách {#policy-rules}

Mỗi quy tắc trao một `action` trên một `resource` cho một `subject` có dạng `group:<group id>`.  Sử dụng các định danh phương thức và thao tác đã biên dịch từ tài liệu provider đáng tin cậy; một ký tự đại diện cho thao tác trao quyền với cả các phương thức thay đổi dữ liệu lẫn các thao tác đọc.

Nhóm `readers` ngầm định nắm giữ khả năng đọc SQL an toàn của máy chủ.  Các thao tác đọc của principal ngoài nhóm đó cần `host/sql/read`, còn các câu lệnh khác cần `execute` trên `host/sql/<statement kind>`.  Các thao tác provider và việc sử dụng thông tin xác thực luôn cần quy tắc rõ ràng, và quyền được trao trên một tập hợp không trao quyền với các phương thức bên dưới của tập hợp đó.  Các kiểm tra bổ sung của máy chủ:

| Tài nguyên | Hành động | Phạm vi |
|--|--|--|
| `host/sql/read` | `execute` | Câu lệnh đọc ngoài nhóm đọc ngầm định |
| `host/sql/exec` | `execute` | `EXEC` gốc, bao gồm cả `EXEC` lồng nhau |
| `host/iac/run`, `host/iac/destroy` | `execute` | Các quan hệ IaC bản xem trước, dù có cú pháp `SELECT` |
| `host/function/<name>` | `execute` | Các hàm nằm ngoài danh sách cho phép nhỏ gồm các hàm được biết là thuần túy |
| `host/backend` | `read` | Các quan hệ backend lưu trữ và bên ngoài dạng không trong suốt |
| `host/credentials` | `reload` | Công cụ MCP `reload_credentials` |
| `session/variables` | `read` | Giải tham chiếu [biến phiên](/getting-started/session-variables) và bí mật |
| `session/config` | `write` | `SET` và `UNSET` |

Việc mở rộng truy vấn đã lưu được kiểm tra trước khi thực thi, và các luồng xử lý được bảo vệ không tái sử dụng cache kế hoạch và cache hàng.

## Điểm vào {#entry-points}

| Xác thực | `exec`, `shell`, MCP stdio | MCP HTTPS |
|--|--|--|
| JWT | Biến môi trường `STACKQL_CALLER_TOKEN` | Header yêu cầu `Authorization: Bearer` |
| Bearer tĩnh | Biến môi trường `STACKQL_CALLER_TOKEN` | Header yêu cầu `Authorization: Bearer` |
| Hệ điều hành cục bộ | UID hoặc SID hiệu lực của tiến trình; không có thông tin xác thực | Bị từ chối |
| TLS hai chiều | Bị từ chối: không có kết nối TLS đã xác minh | Chuỗi chứng chỉ ứng dụng khách đã xác minh và một SAN đã cấu hình |

Cung cấp thông tin xác thực token cục bộ qua môi trường tiến trình, không bao giờ dưới dạng literal SQL hoặc đối số dòng lệnh.  Qua HTTPS, trình xác minh bên gọi thay thế cơ chế kiểm soát bearer dùng chung `auth_token_env_var` được mô tả trong [Xác thực ứng dụng khách HTTP](/command-line-usage/mcp#http-client-authentication); cấu hình chứng chỉ và khóa TLS của máy chủ là bắt buộc, và một yêu cầu không có thông tin xác thực không bao giờ chuyển sang dùng token môi trường của máy chủ.

## Kiểm tra bên gọi {#inspecting-the-caller}

Trong một phiên đã xác thực, hàm `whoami()` trả về ID principal và hàm `groups()` trả về các nhóm thư mục dưới dạng mảng JSON:

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

Cả hai hàm đều yêu cầu một phiên được khởi động với `--access.config`.

## Hướng dẫn từng bước {#walkthroughs}

Kho lưu trữ StackQL đi kèm [bốn hướng dẫn từng bước có thể thực thi](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md), mỗi hướng dẫn cho một chế độ xác thực, được điều khiển bởi một tập lệnh Python cấp phát một thư mục minh họa và xác minh một truy vấn.  Các hướng dẫn này không cần tài khoản đám mây và được bộ kiểm thử của dự án chạy.
