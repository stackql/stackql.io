---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Lệnh stackql mcp chạy StackQL làm máy chủ Model Context Protocol cho tác nhân AI: chế độ triển khai, chế độ an toàn, nạp lại thông tin xác thực và nhật ký kiểm tra."
image: "/img/stackql-featured-image.png"
---

Lệnh dùng để khởi chạy StackQL làm máy chủ Model Context Protocol (MCP), cho phép tác nhân AI và trợ lý AI tương tác với hạ tầng đám mây bằng các khả năng truy vấn của StackQL.

:::info[MCP là gì?]

[Model Context Protocol (MCP)](https://modelcontextprotocol.io/) là một giao thức mở chuẩn hóa cách các ứng dụng AI tương tác với nguồn dữ liệu và công cụ bên ngoài. Khi chạy StackQL làm máy chủ MCP, bạn có thể cho phép tác nhân AI (Claude, Cursor, Continue, tác nhân tùy chỉnh, v.v.) truy vấn và quản lý hạ tầng đám mây trên nhiều provider bằng ngôn ngữ tự nhiên.

:::

* * *

### Cú pháp {#syntax}

`stackql mcp [flags]` cho một máy chủ MCP độc lập.

`stackql srv [flags]` để chạy máy chủ MCP cùng với máy chủ giao thức wire của PostgreSQL trong cùng một tiến trình.

* * *

### Chế độ triển khai {#deployment-modes}

Máy chủ MCP của StackQL có thể được triển khai theo ba cấu hình khác nhau để phù hợp với các yêu cầu kiến trúc khác nhau. Các ví dụ dưới đây chủ động bật chế độ phục vụ không xác thực bằng `"allow_unauthenticated": true`, phù hợp cho phát triển cục bộ; với mọi thứ mà một tiến trình hoặc máy khác có thể truy cập, hãy cấu hình bearer token thay thế (xem [Xác thực ứng dụng khách HTTP](#http-client-authentication)).

#### 1. Máy chủ MCP độc lập {#1-standalone-mcp-server}

Chạy StackQL làm một máy chủ MCP chuyên dụng trên một cổng được chỉ định.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

**Trường hợp sử dụng:** khi bạn chỉ cần truy cập qua giao thức MCP và không cần tương thích với giao thức wire của PostgreSQL.

#### 2. Máy chủ MCP + PostgreSQL (trong bộ nhớ) {#2-mcp--postgresql-server-in-memory}

Chạy đồng thời cả máy chủ MCP và máy chủ PostgreSQL, giao tiếp với nhau trong bộ nhớ.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665
```

**Trường hợp sử dụng:** khi bạn cần cả truy cập qua giao thức MCP cho tác nhân AI và giao thức wire của PostgreSQL cho các ứng dụng khách cơ sở dữ liệu truyền thống, với hiệu năng tối đa nhờ giao tiếp trong bộ nhớ.

#### 3. Máy chủ MCP + PostgreSQL (reverse proxy) {#3-mcp--postgresql-server-reverse-proxy}

Chạy cả hai máy chủ với giao tiếp qua TCP, hỗ trợ triển khai phân tán và mã hóa TLS.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004", "allow_unauthenticated": true}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Với mã hóa TLS:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004", "auth_token_env_var": "STACKQL_MCP_TOKEN"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

TLS mã hóa phương thức truyền tải nhưng không xác thực ứng dụng khách, vì vậy ví dụ này cũng yêu cầu bearer token được lưu trong `STACKQL_MCP_TOKEN`.

**Trường hợp sử dụng:** khi bạn cần tách khối lượng công việc MCP và PostgreSQL ra các tiến trình hoặc máy khác nhau, hoặc khi bạn cần mã hóa TLS cho điểm cuối MCP.

* * *

### Tùy chọn cấu hình {#configuration-options}

#### Loại máy chủ MCP {#mcp-server-type}

| Loại | Mô tả |
|--|--|
|`http`|Chế độ máy chủ HTTP trực tiếp - các yêu cầu MCP được công cụ truy vấn StackQL xử lý ngay trong tiến trình.|
|`reverse_proxy`|Chế độ reverse proxy - các yêu cầu MCP được chuyển tới một máy chủ PostgreSQL bên dưới qua DSN đã cấu hình. Dùng với `stackql srv`.|
|`stdio`|Phương thức truyền tải đầu vào/đầu ra chuẩn - máy chủ đọc yêu cầu từ `stdin` và ghi phản hồi ra `stdout`. Dùng cho các ứng dụng khách MCP nhúng trong trình soạn thảo (Claude Desktop, Cursor, v.v.).|

#### Định dạng nhật ký MCP {#mcp-log-format}

Cờ `--mcp.log.format` chọn cách mã hóa nhật ký kiểm tra: `jsonl` (mặc định) hoặc `otel` (bản ghi nhật ký OTLP/JSON của OpenTelemetry). Cờ này ghi đè `server.audit.format` trong đối tượng cấu hình. Xem [Định dạng nhật ký](#log-format).

#### Phiên bản giao thức MCP {#mcp-protocol-version}

Cờ `--mcp.protocol.version` đặt bản sửa đổi giao thức mới nhất mà máy chủ công bố: `auto` (mặc định, mọi bản sửa đổi được hỗ trợ), `2026-07-28` (chỉ chế độ không có phiên) hoặc một bản sửa đổi cũ hơn như `2025-11-25` (bản sửa đổi đó và các bản trước). Cờ này ghi đè `server.protocol_version` trong đối tượng cấu hình. Xem [Ghim phiên bản sửa đổi](#pinning-the-revision).

#### Đối tượng cấu hình MCP {#mcp-configuration-object}

Cờ `--mcp.config` nhận một đối tượng JSON có cấu trúc như sau.

##### Cấu hình máy chủ {#server-configuration}

| Trường | Mô tả | Bắt buộc |
|--|--|--|
|`server.transport`|Giao thức truyền tải - `http` hoặc `stdio`.|Có|
|`server.address`|Địa chỉ và cổng mà máy chủ MCP liên kết (ví dụ: `127.0.0.1:9912`). Chỉ cho phương thức truyền tải HTTP.|Với `http`|
|`server.tls_cert_file`|Đường dẫn tới tệp chứng chỉ TLS cho HTTPS.|Không|
|`server.tls_key_file`|Đường dẫn tới tệp khóa riêng TLS cho HTTPS.|Không|
|`server.auth_token_env_var`|Tên của một biến môi trường chứa bearer token chia sẻ trước. Khi được đặt, mọi yêu cầu HTTP phải mang `Authorization: Bearer <token>`; máy chủ từ chối khởi động nếu biến chưa được đặt hoặc rỗng. Xem [Xác thực ứng dụng khách HTTP](#http-client-authentication).|Một trong hai: trường này hoặc `allow_unauthenticated`, với `http`|
|`server.allow_unauthenticated`|Bật một cách rõ ràng việc phục vụ phương thức truyền tải HTTP mà không xác thực ứng dụng khách. Mặc định: `false`.|Một trong hai: trường này hoặc `auth_token_env_var`, với `http`|
|`server.mode`|Hợp đồng an toàn kiểm soát các thao tác thay đổi dữ liệu và thao tác vòng đời. Một trong `read_only`, `safe` (mặc định), `delete_safe`, `full_access`. Xem [Chế độ máy chủ](#server-modes).|Không|
|`server.read_only`|Bí danh boolean **cũ** cho `mode: read_only`. Khi cả hai trường đều được đặt, `mode` được ưu tiên.|Không|
|`server.stateless`|Phục vụ Streamable HTTP không có phiên (không có `Mcp-Session-Id`), đây là cách bản sửa đổi giao thức `2026-07-28` được phục vụ qua HTTP. Mặc định: `false`. Bị bỏ qua với `stdio`. Xem [Hỗ trợ bản sửa đổi giao thức](#protocol-revision-support).|Không|
|`server.protocol_version`|Bản sửa đổi giao thức mới nhất được công bố: `auto` (mặc định), `2026-07-28` hoặc một bản sửa đổi cũ hơn như `2025-11-25`. `2026-07-28` ngụ ý `stateless` với Streamable HTTP. Xem [Ghim phiên bản sửa đổi](#pinning-the-revision).|Không|
|`server.audit`|Cấu hình hệ thống con kiểm tra, bao gồm `audit.format` (`jsonl` hoặc `otel`). Xem [Nhật ký kiểm tra](#audit-log).|Không|

##### Cấu hình backend (chỉ chế độ reverse proxy) {#backend-configuration-reverse-proxy-mode-only}

| Trường | Mô tả | Bắt buộc |
|--|--|--|
|`backend.dsn`|Chuỗi kết nối PostgreSQL tới máy chủ StackQL backend.|Có (với `reverse_proxy`)|

<br />

:::info

DSN backend nên bao gồm tham số `default_query_exec_mode=simple_protocol` để có khả năng tương thích tốt nhất.

:::

##### Danh sách cho phép {#allowlists}

| Trường | Mô tả |
|--|--|
|`enabled_tools`|Mảng tên các công cụ mà máy chủ được phép công bố. Khi bị bỏ qua hoặc rỗng, mọi công cụ tích hợp sẵn đều được đăng ký. Dùng để chỉ cung cấp một tập con hẹp (ví dụ: một máy chủ kiểm kê chỉ đọc).|
|`enabled_prompts`|Mảng tên lời nhắc. Cùng ngữ nghĩa với `enabled_tools` nhưng áp dụng cho các lời nhắc.|

<br/>

Ví dụ - một máy chủ chỉ công bố `server_info` và `list_providers`:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### Hỗ trợ bản sửa đổi giao thức {#protocol-revision-support}

Bản sửa đổi giao thức `2026-07-28`, `server.stateless` và định dạng nhật ký kiểm tra `otel` có từ StackQL `v0.11.660` trở đi. `server.protocol_version` và cờ `--mcp.protocol.version` có từ StackQL `v0.12.732` trở đi.

Máy chủ hiểu mọi bản sửa đổi của Model Context Protocol mà [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) hỗ trợ và thương lượng theo từng ứng dụng khách, vì vậy một tập hợp ứng dụng khách hỗn hợp vẫn làm việc được với một máy chủ duy nhất.

| Bản sửa đổi | Vòng đời | Yêu cầu phê duyệt (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (được công bố)|Không có handshake: `server/discover`, sau đó mọi yêu cầu mang `io.modelcontextprotocol/protocolVersion` và `io.modelcontextprotocol/clientCapabilities` trong `_meta` (cùng với các header `Mcp-Protocol-Version`, `Mcp-Method` và `Mcp-Name` trên HTTP).|Kết quả `input_required`, được ứng dụng khách thử lại với `inputResponses` (SEP-2322).|
|`2025-11-25`, `2025-06-18`|Handshake `initialize` / `notifications/initialized`.|Yêu cầu `elicitation/create` do máy chủ khởi tạo.|
|`2025-03-26`, `2024-11-05`|Như trên.|Như trên.|

<br />

**stdio** phục vụ mọi bản sửa đổi trên một tiến trình: yêu cầu đầu tiên của ứng dụng khách `2026-07-28` được phục vụ mà không cần handshake, và `initialize` của ứng dụng khách cũ hơn vẫn hoạt động. Không cần cấu hình gì cho Claude Desktop, các trình khởi chạy npm / PyPI hay image Docker.

**Streamable HTTP** theo mặc định dùng mô hình có trạng thái, mỗi ứng dụng khách một phiên (`Mcp-Session-Id`), mà SDK phục vụ cho các bản sửa đổi tới `2025-11-25`. Một yêu cầu `2026-07-28` được trả lời bằng lỗi JSON-RPC `-32022` (phiên bản giao thức không được hỗ trợ; một lỗi HTTP 400 thông thường trong các bản phát hành tới `v0.12.718`) có `data.supported` liệt kê các bản sửa đổi dùng handshake, và `server/discover` cũng công bố như vậy, nên ứng dụng khách thương lượng xuống bản sửa đổi thấp hơn và các tích hợp HTTP hiện có giữ nguyên phiên cũng như yêu cầu phê duyệt của chúng. Đặt `server.stateless` (hoặc ghim bản sửa đổi ở `2026-07-28`, [bên dưới](#pinning-the-revision)) để phục vụ `2026-07-28` một cách nguyên bản qua HTTP:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "stateless": true}}'
```

Máy chủ không có phiên không cấp `Mcp-Session-Id`, trả lời `GET` và `DELETE` bằng 405 (không có phiên nào để hủy), giữ `tools/list`, `prompts/list` và `resources/list` bất biến theo kết nối, và thực hiện vòng phê duyệt qua `input_required`. Máy chủ này vẫn chấp nhận `initialize` của ứng dụng khách cũ hơn và phục vụ các thao tác đọc cho ứng dụng khách đó, nhưng không thể giữ lại khả năng elicitation mà ứng dụng khách đó đã khai báo khi khởi tạo (mỗi yêu cầu nhận một phiên tạm thời), vì vậy trên máy chủ không có phiên, ứng dụng khách cũ hơn không thể phê duyệt các thao tác ghi cần phê duyệt. Chọn `stateless` cho các host dùng bản sửa đổi hiện tại và giữ mặc định cho tập hợp ứng dụng khách vẫn còn ứng dụng khách cũ hơn.

Máy chủ không giữ trạng thái giữa các lần gọi: chế độ, nhật ký kiểm tra và thông tin xác thực provider là cấu hình ở cấp tiến trình, vì vậy không có gì cần chuyển ra sau các handle tường minh.

#### Ghim phiên bản sửa đổi {#pinning-the-revision}

Theo mặc định, máy chủ công bố mọi bản sửa đổi được hỗ trợ và thương lượng theo từng ứng dụng khách. Cờ `--mcp.protocol.version`, hoặc `server.protocol_version` trong đối tượng cấu hình (cờ được ưu tiên), đặt bản sửa đổi mới nhất được công bố:

```bash
# sessionless only; stateless Streamable HTTP is implied
stackql mcp \
  --mcp.server.type=http \
  --mcp.protocol.version=2026-07-28 \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'

# handshake lifecycle only
stackql mcp --mcp.server.type=stdio --mcp.protocol.version=2025-11-25
```

| Giá trị | Được công bố | Tác dụng |
|--|--|--|
|`auto` (mặc định) hoặc không đặt|Mọi bản sửa đổi được hỗ trợ.|Thương lượng như trên: bản sửa đổi cao nhất mà cả hai bên đều hỗ trợ.|
|`2026-07-28`|Chỉ `2026-07-28`.|Chỉ chế độ không có phiên, và `stateless` được ngụ ý với Streamable HTTP. `initialize` của ứng dụng khách cũ hơn được trả lời bằng `2025-11-25`, tín hiệu để ứng dụng khách đó ngắt kết nối thay vì đọc câu trả lời như vòng đời mới.|
|`2025-11-25` (hoặc bất kỳ bản sửa đổi cũ hơn nào)|Bản sửa đổi đó và mọi bản trước nó.|Chỉ vòng đời dùng handshake: một yêu cầu `2026-07-28` nhận lỗi JSON-RPC `-32022` có `data.supported` liệt kê các bản sửa đổi được công bố, để ứng dụng khách có thể thương lượng lại. Các ứng dụng khách dùng handshake cũ hơn giữ bản sửa đổi của riêng chúng.|

<br />

Mọi giá trị khác đều không vượt qua bước kiểm tra tính hợp lệ của cấu hình khi khởi động, và thông báo lỗi nêu tên các giá trị hợp lệ.

#### Giới hạn truyền tải {#transport-limits}

Phần thân yêu cầu Streamable HTTP bị giới hạn ở 4 MiB (HTTP 413 nếu vượt quá), JSON lồng nhau sâu hơn 1000 cấp bị từ chối trước khi phân tích cú pháp, và một frame `stdio` đơn lẻ bị giới hạn ở 8 MiB. Không giới hạn nào trong số này có thể cấu hình; tất cả đều cao hơn nhiều so với mọi câu lệnh SQL mà máy chủ chấp nhận.

* * *

### Nạp (lại) thông tin xác thực (`--env.file` + `reload_credentials`) {#credential-resourcing---envfile--reload_credentials}

:::note

`--env.file` và công cụ `reload_credentials` có trong các bản phát hành StackQL sau `v0.10.542`.

:::

Khối biến môi trường của một tiến trình được cố định khi tiến trình được tạo. Điều này gây ra hai vấn đề cho máy chủ MCP:

- Một máy chủ MCP `stdio` do một ứng dụng khách trên máy tính (Claude Desktop, Cursor, v.v.) khởi tạo mà không có các biến môi trường chứa thông tin xác thực sẽ không bao giờ thấy được chúng - ứng dụng khách truyền môi trường của chính nó cho tiến trình con.
- Thông tin xác thực ngắn hạn sau khi được xoay vòng không bao giờ đến được một phiên máy chủ chạy lâu dài.

Cờ `--env.file` khắc phục cả hai vấn đề bằng cách chỉ định một tệp kiểu dotenv đóng vai trò kho thông tin xác thực có thể thay đổi:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### Ngữ nghĩa nạp tệp {#sourcing-semantics}

Tệp được nạp vào môi trường tiến trình khi khởi động với mọi điểm vào (`exec`, `shell`, `srv`, `registry`, `mcp`):

- Khi cờ rỗng hoặc không được đặt, không có gì xảy ra - thông tin xác thực được phân giải trễ từ môi trường tiến trình tại thời điểm yêu cầu, đúng như hành vi trước đây.
- Tệp không tồn tại tại đường dẫn đã cho được chấp nhận và không làm gì cả (tệp có thể được tạo sau).
- Tệp sai định dạng hoặc không đọc được gây lỗi nghiêm trọng khi khởi động.
- Chỉ các khóa có giá trị không rỗng mới được đặt; các biến môi trường hiện có của tiến trình **có** bị ghi đè (tệp là nguồn dữ liệu gốc); không bao giờ có biến nào bị hủy thiết lập.
- Cờ này chỉ dùng trên dòng lệnh - cờ không được nạp từ `.stackqlrc`.

Định dạng tệp là một `KEY=VALUE` trên mỗi dòng. Chú thích `#`, dòng trống, tiền tố `export ` không bắt buộc, dấu nháy đơn hoặc nháy kép không bắt buộc quanh giá trị, và ký tự kết thúc dòng CRLF đều được chấp nhận:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials` {#reload_credentials}

Công cụ [`reload_credentials`](/mcp/reload_credentials) nạp lại tệp `--env.file` vào môi trường tiến trình giữa phiên, sau đó báo cáo trạng thái phân giải thông tin xác thực theo từng provider. Giá trị bí mật không bao giờ được trả về, ghi nhật ký hay ghi vào nhật ký kiểm tra - chỉ có tên và trạng thái. Khi chưa cấu hình `--env.file`, công cụ chỉ còn là một phép thăm dò trạng thái.

Khi một truy vấn thất bại do phân giải thông tin xác thực, lỗi MCP mang một gợi ý hướng dẫn tác nhân gọi `reload_credentials` và thử lại, nhờ vậy các ứng dụng khách dựa trên tác nhân tự khắc phục: ghi thông tin xác thực vào tệp, yêu cầu tác nhân tải lại, và truy vấn bị lỗi sẽ thành công khi thử lại - không cần khởi động lại máy chủ.

`reload_credentials` không được hỗ trợ ở chế độ backend `reverse_proxy` - truy vấn được thực thi trong tiến trình máy chủ stackql từ xa, vì vậy công cụ trả về lỗi hướng dẫn người dùng tải lại trên máy chủ bên dưới.

#### Windows và Claude Desktop {#windows-and-claude-desktop}

`setx` ghi vào registry chứ không ghi vào các tiến trình đang chạy, và Claude Desktop truyền môi trường (đã cũ) của chính nó cho các tiến trình con MCP - vì vậy thay đổi thông tin xác thực thường đòi hỏi khởi động lại hoàn toàn Claude Desktop. Với `--env.file`, tệp thông tin xác thực có thể được tạo hoặc xoay vòng bất cứ lúc nào và `reload_credentials` nhận thay đổi mà không cần khởi động lại. Xem [Sử dụng StackQL với Claude Desktop](/getting-started/claude-desktop) để biết một cấu hình mẫu hoàn chỉnh.

* * *

### Xác thực ứng dụng khách HTTP {#http-client-authentication}

:::note

`server.auth_token_env_var` và `server.allow_unauthenticated` có từ StackQL `v0.12.742` trở đi.

:::

Phương thức truyền tải HTTP yêu cầu xác thực ứng dụng khách. `server.auth_token_env_var` chỉ định tên một biến môi trường chứa bearer token chia sẻ trước; mọi yêu cầu phải mang `Authorization: Bearer <token>`, nếu không sẽ được trả lời bằng `401`. Token được đọc từ môi trường, vì vậy token không bao giờ xuất hiện trong `--mcp.config` hay trong đối số của tiến trình.

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}'
```

:::info[Bắt buộc xác thực trên máy chủ]

Phục vụ không có token phải được bật một cách rõ ràng: đặt `"allow_unauthenticated": true` trong `server`. Nếu không có khóa nào trong hai khóa này, máy chủ thoát khi khởi động với thông báo nêu tên cả hai; máy chủ cũng thoát nếu biến token chưa được đặt hoặc rỗng. Kể từ bản phát hành `v0.12.742`, bạn phải thêm `auth_token_env_var` hoặc `"allow_unauthenticated": true` vào `server` khi dùng phương thức truyền tải `http`.

:::

TLS (`tls_cert_file` / `tls_key_file`) mã hóa phương thức truyền tải nhưng không xác thực ứng dụng khách; hãy kết hợp TLS với token cho mọi listener ngoài loopback. Các yêu cầu từ trình duyệt khác nguồn gốc (`Origin` không khớp, hoặc `Sec-Fetch-Site` là cross-site) bị từ chối với `403` bất kể xác thực. `stackql_mcp_client` đi kèm không gửi header `Authorization`, vì vậy máy chủ mà nó điều khiển phải chủ động bật chế độ phục vụ không xác thực.

Từ `v1.0`, khởi động máy chủ với [`--access.config`](/command-line-usage/access-management) sẽ thay cơ chế kiểm soát bằng token dùng chung bằng xác thực theo từng bên gọi: mỗi yêu cầu HTTPS được xác minh bằng cơ chế JWT, bearer tĩnh hoặc TLS hai chiều đã cấu hình và được ủy quyền theo các quy tắc chính sách. Khi đó TLS phía máy chủ là bắt buộc, xác thực `local_os` bị từ chối qua HTTP, và một yêu cầu không có thông tin xác thực không bao giờ quay về dùng token trong môi trường của máy chủ. Quản lý truy cập không khả dụng với loại máy chủ `reverse_proxy`.

* * *

### Chế độ máy chủ {#server-modes}

`server.mode` chọn một trong bốn hợp đồng an toàn. Cả bốn đều cho phép `SELECT` và các thao tác đọc siêu dữ liệu; chúng khác nhau ở cách xử lý các thao tác thay đổi dữ liệu và thao tác vòng đời.

| Chế độ | `SELECT` / siêu dữ liệu | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (vòng đời) |
|--|--|--|--|--|
|`read_only`|cho phép|từ chối|từ chối|từ chối|
|`safe` (mặc định)|cho phép|cần phê duyệt|cần phê duyệt|cần phê duyệt|
|`delete_safe`|cho phép|cho phép|cần phê duyệt|cần phê duyệt|
|`full_access`|cho phép|cho phép|cho phép|cho phép|

<br />

**Từ chối** trả về lỗi ngay lập tức cho ứng dụng khách.

**Cần phê duyệt** dùng luồng elicitation của MCP, được định hình bởi [bản sửa đổi giao thức](#protocol-revision-support) đã thương lượng:

- Nếu ứng dụng khách đã công bố khả năng elicitation (khi khởi tạo, hoặc trong các khả năng của ứng dụng khách ở `_meta` theo từng yêu cầu trên `2026-07-28`), máy chủ yêu cầu người dùng phê duyệt hành động bằng một thông báo ngắn (tên công cụ, loại truy vấn, SQL). Trên `2026-07-28`, yêu cầu phê duyệt được trả về dưới dạng kết quả `input_required` mang một yêu cầu đầu vào `elicitation/create` với ID `stackql_approval`, và ứng dụng khách thử lại lệnh gọi với câu trả lời trong `inputResponses`; trên các bản sửa đổi trước đó, máy chủ tự gửi yêu cầu `elicitation/create` giữa lệnh gọi. Người dùng chấp nhận, từ chối hoặc hủy.
- Nếu ứng dụng khách **không** công bố elicitation, công cụ bị từ chối với một thông báo giải thích điểm thiếu hụt và hướng người vận hành tới chế độ `full_access`.

Ứng dụng khách là bên trả lời yêu cầu phê duyệt, vì vậy cơ chế này bảo vệ trước việc tác nhân hành động mà không có người dùng của nó, chứ không bảo vệ trước một bên gọi không đáng tin cậy. Kết hợp phương thức truyền tải HTTP với [xác thực ứng dụng khách](#http-client-authentication) khi bên gọi không đáng tin cậy.

Chế độ áp dụng toàn cục cho mỗi máy chủ. Không có ghi đè theo từng công cụ.

Từ `v1.0`, [`run_select_query`](/mcp/run_select_query) từ chối `INSERT`, `UPDATE`, `REPLACE`, `DELETE` và `EXEC` ở mọi chế độ, kể cả `full_access`; thông báo lỗi nêu tên `run_mutation_query` hoặc `run_lifecycle_operation`. Mọi công cụ cũng mang một `title` và bốn gợi ý hành vi MCP dưới dạng boolean tường minh (`readOnlyHint`, `destructiveHint`, `idempotentHint`, `openWorldHint`), được suy ra từ cùng cách phân loại mà cơ chế kiểm soát chính sách thực thi. `openWorldHint` là `true` cho `list_registry`, `pull_provider`, `run_select_query`, `run_mutation_query` và `run_lifecycle_operation`, là những công cụ truy cập điểm cuối của registry hoặc provider, và `false` cho các công cụ chỉ đọc trạng thái cục bộ. Các gợi ý chỉ mang tính tham khảo cho trải nghiệm người dùng phía ứng dụng khách; việc thực thi vẫn thuộc về cơ chế kiểm soát chính sách.

:::info[Kiểm soát payload]

Từ bản phát hành `v0.12.742`, một payload chứa nhiều câu lệnh được kiểm soát theo câu lệnh có đặc quyền cao nhất của nó ở mọi chế độ, vì vậy `SELECT 1; DELETE ...` được xử lý như một `DELETE` và bản ghi kiểm tra mang `query_class` `mutation_delete`. `validate_select_query` chỉ chấp nhận đúng một câu lệnh.

:::

#### Thay đổi hành vi của chế độ mặc định {#default-mode-behaviour-change}

Các bản phát hành trước chỉ thực thi an toàn qua một cờ `read_only: true/false` duy nhất có mặc định là "không thực thi". Mặc định hiện tại (`mode: safe`) khiến các thao tác thay đổi dữ liệu cần người dùng phê duyệt ngay từ đầu. Người vận hành chạy một ứng dụng khách MCP hỗ trợ elicitation (Claude Desktop, Cursor, Continue, v.v.) sẽ thấy một yêu cầu phê duyệt cho mỗi thao tác thay đổi dữ liệu. Người vận hành chạy tự động hóa, hoặc `stackql_mcp_client` đi kèm (không công bố elicitation), phải chủ động bật một chế độ ít hạn chế hơn một cách rõ ràng: `mode: delete_safe` để chạy `INSERT`, `UPDATE` và `REPLACE`, hoặc `mode: full_access` để chạy thêm cả `DELETE` và thao tác vòng đời.

Khóa JSON / YAML cũ `read_only: true` vẫn được chấp nhận để tương thích ngược và được xem là tương đương với `mode: read_only`.

#### Ví dụ {#examples}

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE/REPLACE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "mode": "full_access"}}'
```

* * *

### Nhật ký kiểm tra {#audit-log}

Mỗi lệnh gọi công cụ ghi một bản ghi vào đích kiểm tra đã cấu hình, theo mặc định dưới dạng JSONL hoặc dưới dạng bản ghi nhật ký OpenTelemetry (xem [Định dạng nhật ký](#log-format)). Tính năng kiểm tra **được bật theo mặc định**. Nhật ký kiểm tra trả lời câu hỏi "tác nhân đã làm gì", chứ không phải "tác nhân đã thấy gì" - các hàng kết quả từ câu lệnh `SELECT` được cố ý không ghi lại, ở cả hai định dạng.

#### Những gì được ghi lại {#what-gets-recorded}

| Trường | Mô tả |
|--|--|
|`timestamp`|Thời điểm bắt đầu lệnh gọi theo đồng hồ thực (RFC3339).|
|`tool`|Tên công cụ (ví dụ: `run_select_query`).|
|`mode`|Chế độ máy chủ có hiệu lực tại thời điểm gọi.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`. Với payload chứa nhiều câu lệnh, đây là loại của câu lệnh có đặc quyền cao nhất.|
|`sql`|Chuỗi SQL cho các công cụ truy vấn (`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`).|
|`args`|Các trường phân cấp cho công cụ siêu dữ liệu (`list_*`, `describe_method`); SQL + `row_limit` cho công cụ truy vấn.|
|`duration_ms`|Thời lượng thực tế của cơ chế kiểm soát + trình xử lý.|
|`error`|Thông báo lỗi nếu công cụ gặp lỗi hoặc bị từ chối.|

<br />

Các hàng kết quả từ câu lệnh `SELECT` được cố ý loại trừ - chúng có thể rất lớn và có thể chứa dữ liệu nhạy cảm.

#### Đích tệp {#file-sink}

Đích duy nhất hiện có là `file`. Mỗi dòng một đối tượng JSON, fsync sau mỗi bản ghi, xoay vòng kiểu lumberjack theo kích thước / tuổi / số bản sao lưu.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

Có hai cách chỉ định vị trí:

- `audit.file.path` - một đường dẫn tệp đầy đủ (tuyệt đối, hoặc tương đối với cwd).
- `audit.file.dir` - một thư mục; đích chọn tên tệp `stackql_mcp_server_<RFC3339-utc-second>.log` bên trong thư mục đó.

Khi cả hai đều không được đặt trong `mcp.config`, máy chủ MCP đặt mặc định `dir` là cwd để người vận hành hiện tại thấy hành vi như trước. Bản thân gói đích chung bên dưới từ chối tự ý chọn thư mục - quyết định "nhật ký được ghi ở đâu" luôn do bên gọi đưa ra.

Đường dẫn tuyệt đối đã phân giải được ghi ra stderr khi khởi động dưới dạng `sink file: /path/to/file.log`.

#### Định dạng nhật ký {#log-format}

Luồng kiểm tra có hai cách mã hóa, được ghi vào cùng một đích tệp:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

Cờ này ghi đè `server.audit.format` trong `mcp.config` (`"audit": {"format": "otel"}`). `jsonl` tương thích từng byte với các bản ghi mô tả ở trên. `otel` ghi một đối tượng OTLP/JSON `LogsData` trên mỗi dòng, đúng dạng mà receiver `otlp_json_file` của OpenTelemetry Collector tiếp nhận nguyên trạng (đã xác minh với otelcol-contrib 0.160.0), vì vậy luồng này đến được mọi pipeline OTLP mà không cần transform processor:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Mỗi lệnh gọi công cụ tạo ra một bản ghi nhật ký; một lệnh gọi đi qua cơ chế kiểm soát phê duyệt tạo ra bản ghi thứ hai cho quyết định elicitation. Các bản ghi của cùng một lệnh gọi dùng chung `gen_ai.tool.call.id` và một ID trace. `traceparent` W3C do bên gọi cung cấp trong `params._meta` sẽ được tôn trọng; nếu không, một ID trace được tạo cho mỗi phiên MCP để các bản ghi của một phiên tác nhân liên kết được với nhau.

Tập thuộc tính được phát ra là một giao diện có phiên bản: phạm vi instrumentation là `github.com/stackql/stackql/pkg/mcp_server/audit` ở phiên bản `1.0.0`, và `schemaUrl` ghim các quy ước ổn định vào `https://opentelemetry.io/schemas/1.44.0`. Các thuộc tính `gen_ai.*` và `mcp.*` tuân theo quy ước ngữ nghĩa [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) và [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md), vốn đang ở trạng thái Development; phiên bản phạm vi được tăng khi tập thuộc tính thay đổi.

| Thuộc tính | Bản ghi | Giá trị |
|--|--|--|
|`service.name`, `service.version`|tài nguyên|`stackql`, phiên bản StackQL|
|`gen_ai.operation.name`|lệnh gọi công cụ|`execute_tool`|
|`mcp.method.name`|cả hai|`tools/call` trên bản ghi lệnh gọi công cụ, `elicitation/create` trên bản ghi quyết định|
|`gen_ai.tool.name`|cả hai|Tên công cụ|
|`gen_ai.tool.call.id`|cả hai|Được tạo cho mỗi lệnh gọi, dùng chung cho cả hai bản ghi|
|`mcp.protocol.version`|cả hai|Bản sửa đổi đã thương lượng|
|`mcp.session.id`|cả hai|ID phiên (không có với các yêu cầu HTTP không có phiên)|
|`stackql.mode`|cả hai|Chế độ máy chủ|
|`stackql.decision`|cả hai|Giá trị `decision` từ bảng ở trên|
|`stackql.query`|lệnh gọi công cụ|SQL nguyên văn, cho các công cụ truy vấn|
|`stackql.query_class`|lệnh gọi công cụ|Giá trị `query_class`|
|`stackql.provider`|lệnh gọi công cụ|Provider, cho các công cụ siêu dữ liệu|
|`stackql.query.source`|lệnh gọi công cụ|ID mục Query Library, khi được cung cấp|
|`stackql.duration_ms`|lệnh gọi công cụ|Thời lượng thực tế|
|`stackql.rows_returned`|lệnh gọi công cụ|Số hàng, cho các công cụ trả về hàng|
|`error.type`, `error.message`|lệnh gọi công cụ|Khi thất bại: quyết định từ chối, hoặc `tool_error` sau cơ chế kiểm soát|

<br />

Body là `execute_tool <tool>` / `elicitation <tool>`; mức độ nghiêm trọng là `INFO`, hoặc `ERROR` khi lệnh gọi thất bại hoặc bị từ chối. Hợp đồng che giấu dữ liệu giống hệt nhau ở cả hai định dạng: giá trị kết quả không bao giờ được tuần tự hóa, chỉ có câu lệnh đã tạo ra chúng.

#### Chế độ xử lý lỗi {#failure-modes}

Khi đích trả về lỗi, hành vi phản hồi phụ thuộc vào `audit.failure_mode`.

| failure_mode | Tác dụng |
|--|--|
|`strict` (mặc định)|Lệnh gọi công cụ trả về lỗi kiểm tra cho ứng dụng khách ngay cả khi công cụ bên dưới đã thành công. Đây là chủ ý: thà ứng dụng khách nhận một phản hồi không rõ ràng còn hơn một `DELETE` không bị phát hiện.|
|`strict_mutations`|Các thao tác đọc `SELECT` / siêu dữ liệu tiếp tục kèm một ghi chú trên stderr; thao tác thay đổi dữ liệu và thao tác vòng đời trả về lỗi kiểm tra.|
|`best_effort`|Luôn ghi nhật ký ra stderr và tiếp tục.|

#### Trình tự {#sequencing}

Thao tác ghi kiểm tra diễn ra **sau** khi công cụ thực thi (hoặc bị cơ chế kiểm soát chặn lại) nhưng **trước** khi phản hồi được trả về ứng dụng khách. Ở chế độ strict, một lỗi ghi kiểm tra đối với một DELETE thành công nghĩa là hàng đã bị xóa nhưng ứng dụng khách nhận được lỗi - đây là chủ ý thiết kế, để không có thao tác thay đổi dữ liệu nào lọt qua mà không được ghi kiểm tra.

#### Tắt kiểm tra {#disabling-audit}

Để tắt hoàn toàn tính năng kiểm tra, như trong các bản phát hành trước `v0.10.474` vốn không có kiểm tra:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true, "audit": {"disabled": true}}}'
```

* * *

### Các công cụ MCP hiện có {#available-mcp-tools}

Khi chạy làm máy chủ MCP, StackQL cung cấp các công cụ sau, tùy theo danh sách cho phép `enabled_tools` đã cấu hình. Mỗi công cụ trả về cả một bản hiển thị dạng văn bản (cho LLM) và một payload có cấu trúc, có kiểu (cho các ứng dụng khách lập trình). Cách hiển thị văn bản mặc định là một bảng markdown cho kết quả nhiều hàng đồng nhất hoặc một khối KV markdown cho kết quả thưa / chỉ một bản ghi. Các công cụ có đối số chấp nhận ghi đè `format` (`markdown` hoặc `json`) không bắt buộc; nếu không, thiết lập hiển thị của máy chủ được sử dụng. Phần đầu vào bên dưới liệt kê các đối số dành riêng cho tác vụ; `?` đánh dấu đầu vào không bắt buộc.

Nhấp vào tên công cụ bất kỳ để xem trang tham khảo đầy đủ, bao gồm đầu vào, hành vi kiểm soát và một lời nhắc ví dụ.

| Công cụ | Trình hiển thị | Mô tả | Đầu vào |
|--|--|--|--|
|[`server_info`](/mcp/server_info)|KV|Danh tính và runtime của máy chủ: phiên bản stackql, công cụ SQL bên dưới, vị trí registry provider, chế độ, cờ chỉ đọc. Gọi khi cần, ví dụ để chẩn đoán một thao tác bị từ chối hoặc được kiểm soát, hoặc để kiểm tra hành vi phụ thuộc phiên bản.|không có|
|[`list_providers`](/mcp/list_providers)|Bảng|Các provider đã được tải xuống bộ nhớ đệm cục bộ - cấp cao nhất của phân cấp.|không có|
|[`list_services`](/mcp/list_services)|Bảng|Các dịch vụ thuộc một provider.|`provider`|
|[`list_resources`](/mcp/list_resources)|Bảng|Các tài nguyên thuộc một `provider`.`service`.|`provider`, `service`|
|[`list_methods`](/mcp/list_methods)|Bảng|Các phương thức truy cập (thao tác HTTP) của một tài nguyên, kèm động từ SQL mà mỗi phương thức ánh xạ tới và các tham số bắt buộc của phương thức đó. Gọi trước khi viết bất kỳ truy vấn nào - đây là nơi suy ra các tham số `WHERE` bắt buộc.|`provider`, `service`, `resource`|
|[`describe_method`](/mcp/describe_method)|KV|Hợp đồng I/O đầy đủ của một phương thức (luôn EXTENDED): các đầu vào bắt buộc và không bắt buộc, cùng các trường đầu ra mà một `SELECT` có thể tham chiếu.|`provider`, `service`, `resource`, `method`|
|[`query_library_search`](/mcp/query_library_search)|Bảng|Tìm các template truy vấn được tuyển chọn theo ý định diễn đạt bằng ngôn ngữ tự nhiên. Chỉ đọc; các mục thay đổi dữ liệu cần chủ động bật và bị loại trừ ở chế độ `read_only`.|`intent`, `provider?`, `service?`, `tags?`, `include_mutations?`, `limit?`|
|[`query_library_get`](/mcp/query_library_get)|KV|Lấy một template và các khai báo tham số của nó. Với `params` không rỗng, kiểm tra tính hợp lệ của các giá trị và trả về SQL đã được tạo từ template cùng `next_tool`, mà không thực thi.|`id`, `params?`|
|[`validate_select_query`](/mcp/validate_select_query)|KV|Phân tích cú pháp và lập kế hoạch cho một `SELECT` duy nhất mà không thực thi. Trả về `{valid, errors}`. Chỉ `SELECT`; đúng một câu lệnh.|`sql`|
|[`run_select_query`](/mcp/run_select_query)|Bảng|Thực thi một `SELECT`. Trả về `{rows}`. Chỉ đọc.|`sql`, `row_limit?`|
|[`run_mutation_query`](/mcp/run_mutation_query)|KV|Thực thi `INSERT`/`UPDATE`/`REPLACE`/`DELETE` đối với provider. **Có tác dụng phụ thực sự.** Trả về `{messages, timestamp}`. Được kiểm soát bởi [chế độ máy chủ](#server-modes).|`sql`|
|[`run_lifecycle_operation`](/mcp/run_lifecycle_operation)|KV|Thực thi một thao tác vòng đời `EXEC` của stackql. Trả về `{messages, timestamp}`. Được kiểm soát bởi [chế độ máy chủ](#server-modes).|`sql`|
|[`list_registry`](/mcp/list_registry)|Bảng|Các provider (và phiên bản của chúng) có trong registry đã cấu hình. Khác với `list_providers`, công cụ chỉ liệt kê các provider đã tải xuống.|`provider?`|
|[`pull_provider`](/mcp/pull_provider)|KV|Cài đặt một provider từ registry vào bộ nhớ đệm cục bộ. Chỉ ảnh hưởng đến trạng thái bộ nhớ đệm cục bộ - không tác động đến mặt phẳng điều khiển hay mặt phẳng dữ liệu đám mây.|`provider`, `version?`|
|[`reload_credentials`](/mcp/reload_credentials)|Bảng|Nạp lại thông tin xác thực từ tệp dotenv `--env.file` vào môi trường tiến trình và báo cáo trạng thái phân giải theo từng provider (`ok`, `unresolved`, `not_checked`). Không bao giờ trả về giá trị bí mật. Được cho phép ở mọi chế độ.|`provider?`|

Tham khảo `query_library_search` trước khi tự viết SQL từ đầu, sau đó dùng `query_library_get` để xem xét một template phù hợp và tạo SQL từ template đó. Các công cụ truy vấn cũng chấp nhận `source` không bắt buộc chứa ID mục Query Library, chỉ dùng để ghi nhận nguồn; việc thực thi vẫn tuân theo chế độ máy chủ và chính sách phê duyệt thông thường.

:::note

`describe_resource` đã ngừng được hỗ trợ trong StackQL `v0.12.732`; các ứng dụng khách MCP, lời nhắc hoặc chỉ dẫn cho tác nhân từng gọi công cụ này nên gọi [`list_methods`](/mcp/list_methods) rồi đến `describe_method` thay thế. Một tài nguyên không có một danh sách trường duy nhất, vì mỗi phương thức truy cập trả về dạng riêng của nó, nên tên cột lấy từ [`describe_method`](/mcp/describe_method) cho phương thức mà truy vấn được định tuyến tới. Câu lệnh SQL [`DESCRIBE`](/language-spec/describe) không thay đổi.

:::

### Các lời nhắc MCP hiện có {#available-mcp-prompts}

Bảy lời nhắc được công bố, tùy theo danh sách cho phép `enabled_prompts` đã cấu hình. Mỗi lời nhắc là một workflow có hướng dẫn mà tác nhân chạy bằng các công cụ MCP của StackQL, và không lời nhắc nào thay đổi tài nguyên đám mây.

| Lời nhắc | Mô tả |
|--|--|
|`getting_started`|Phần giới thiệu tương tác có hướng dẫn về StackQL và máy chủ MCP StackQL dành cho người dùng mới: các khái niệm, khám phá provider, những truy vấn đầu tiên và nên tìm hiểu gì tiếp theo.|
|`cloud_audit`|Kiểm tra bảo mật và FinOps trên nhiều đám mây (AWS, Google Cloud, Azure và Entra ID); trả về một báo cáo các phát hiện.|
|`cost_cleanup`|Lượt dọn dẹp FinOps tìm các tài nguyên nhàn rỗi và mồ côi, ước tính khoản tiết kiệm hằng tháng và trả về danh sách các tài nguyên có thể xóa an toàn, sắp xếp theo mức độ ưu tiên.|
|`drift_report`|Báo cáo sai lệch so sánh hạ tầng dưới dạng mã đã khai báo (trạng thái hoặc kế hoạch Terraform, hoặc một bản kiểm kê được dán vào) với tài nguyên đám mây thực tế; trả về một bảng sai lệch.|
|`iam_access_review`|Rà soát danh tính và quyền truy cập trên AWS IAM, Entra ID, Okta và GitHub; trả về một bản rà soát quyền truy cập sẵn sàng cho kiểm toán viên (theo kiểu SOC 2 hoặc ISO).|
|`public_exposure_scan`|Quét tìm các tài nguyên có thể truy cập từ internet (bộ lưu trữ công khai, nhóm bảo mật mở, địa chỉ IP công khai, điểm cuối cơ sở dữ liệu công khai, dịch vụ Kubernetes bị lộ ra ngoài); trả về một bảng mức độ phơi bày, sắp xếp theo mức độ ưu tiên.|
|`create_deploy_stack`|Hướng dẫn người dùng tạo một stack `stackql-deploy` (manifest và các tệp tài nguyên `.iql`) với các truy vấn được kiểm thử trực tiếp. Các thao tác thay đổi dữ liệu được tạo cho `stackql-deploy` chứ không bao giờ được thực thi.|

* * *

### Cờ {#flags}

| Cờ | Mô tả |
|--|--|
|`--mcp.server.type`|Loại máy chủ MCP: `http`, `stdio` hoặc `reverse_proxy` (loại sau cùng được dùng với `stackql srv`).|
|`--mcp.config`|Đối tượng cấu hình JSON cho máy chủ MCP. YAML cũng được chấp nhận.|
|`--mcp.log.format`|Cách mã hóa nhật ký kiểm tra: `jsonl` (mặc định) hoặc `otel` (bản ghi nhật ký OTLP/JSON của OpenTelemetry). Ghi đè `server.audit.format`. Xem [Định dạng nhật ký](#log-format).|
|`--mcp.protocol.version`|Bản sửa đổi giao thức mới nhất được công bố: `auto` (mặc định), `2026-07-28` hoặc một bản sửa đổi cũ hơn như `2025-11-25`. Xem [Ghim phiên bản sửa đổi](#pinning-the-revision).|
|`--env.file`|Tệp thông tin xác thực kiểu dotenv được nạp khi khởi động và được công cụ `reload_credentials` nạp lại khi cần. Xem [Nạp (lại) thông tin xác thực](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|Cổng TCP cho máy chủ giao thức wire của PostgreSQL (dùng với `stackql srv`).|
|`-h`, `--help`|In thông tin trợ giúp.|
|`-v`, `--verbose`|Chạy ở chế độ chi tiết với đầu ra bổ sung.|

&nbsp;
&nbsp;
> xem [Cờ toàn cục](/command-line-usage/global-flags) để biết thêm các tùy chọn

:::info

Bạn cần đặt các biến môi trường cần thiết cho việc xác thực provider trước khi khởi động máy chủ MCP, hoặc chỉ định một tệp thông tin xác thực kiểu dotenv bằng [`--env.file`](#credential-resourcing---envfile--reload_credentials). Để biết thêm thông tin, hãy xem [Sử dụng provider](/getting-started/using-a-provider).

:::

* * *

### Ví dụ {#examples-1}

#### Máy chủ MCP độc lập cơ bản {#basic-standalone-mcp-server}

Khởi chạy một máy chủ MCP độc lập với xác thực provider và một bearer token cho các ứng dụng khách của máy chủ:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "auth_token_env_var": "STACKQL_MCP_TOKEN"}}' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Máy chủ MCP với máy chủ PostgreSQL (trong bộ nhớ) {#mcp-server-with-postgresql-server-in-memory}

Chạy cả máy chủ MCP và máy chủ PostgreSQL để truy cập qua hai giao thức:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Máy chủ kiểm kê chỉ đọc ghi nhật ký kiểm tra vào một đường dẫn xác định {#read-only-inventory-server-with-audit-to-a-known-path}

Hữu ích cho một tác nhân quét kiểm kê không bao giờ được phép thay đổi dữ liệu:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "allow_unauthenticated": true,
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Máy chủ tự động hóa có toàn quyền truy cập {#full-access-automation-server}

Dành cho các pipeline tự động hóa đáng tin cậy cần thực hiện thao tác thay đổi dữ liệu và không thể trả lời yêu cầu elicitation. Pipeline xuất trình bearer token được lưu trong `STACKQL_MCP_TOKEN`:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "auth_token_env_var": "STACKQL_MCP_TOKEN",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Máy chủ MCP bảo mật với TLS {#secure-mcp-server-with-tls}

Khởi chạy một máy chủ MCP với mã hóa TLS ở chế độ reverse proxy:

Trước tiên, tạo chứng chỉ TLS (tự ký cho môi trường phát triển):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

Sau đó khởi động máy chủ:

```bash
export STACKQL_MCP_TOKEN="$(openssl rand -hex 32)"

stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004",
      "auth_token_env_var": "STACKQL_MCP_TOKEN"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.app/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Kiểm thử máy chủ MCP của bạn {#testing-your-mcp-server}

Kho lưu trữ đi kèm một ứng dụng khách MCP dành cho phát triển (`stackql_mcp_client`) để viết tập lệnh và chạy kiểm thử hồi quy. Dựng ứng dụng khách này bằng:

```bash
python cicd/python/build.py --build-mcp-client
```

Lệnh này tạo ra `./build/stackql_mcp_client`.

Lưu ý rằng `stackql_mcp_client` không gửi header `Authorization`, vì vậy máy chủ mà nó nhắm tới phải đặt `"allow_unauthenticated": true` (như các ví dụ ở trên); máy chủ được bảo vệ bằng token sẽ trả lời nó bằng `401`. Nó cũng **không** công bố khả năng elicitation của MCP - nó dành cho tập lệnh không tương tác. Với máy chủ `safe`, nó sẽ nhận lỗi từ chối "client does not support elicitation" đối với các lệnh gọi thay đổi dữ liệu và vòng đời; với máy chủ `delete_safe`, đối với các lệnh gọi `DELETE` và `EXEC`. Để kiểm thử tương tác với yêu cầu elicitation, hãy dùng một ứng dụng khách MCP nhúng trong trình soạn thảo (Claude Desktop, Cursor, Continue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode and registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation (a DELETE) - succeeds only against a full_access server:
# stackql_mcp_client does not support elicitation, so safe and delete_safe
# servers refuse it.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### Ví dụ phản hồi {#example-responses}

`server_info` (với máy chủ ở chế độ mặc định, StackQL `v1.0.888` trên Windows qua stdio):

```json
{
  "version": "1.0.888",
  "commit": "f7b4116",
  "build_date": "2026-11-05T00:26:03Z",
  "platform": "Windows",
  "transport": "stdio",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.app/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query` (với máy chủ `full_access`):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

Ví dụ một dòng nhật ký kiểm tra cho cùng thao tác thay đổi dữ liệu đó:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### Tích hợp với trợ lý AI {#integration-with-ai-assistants}

Để tích hợp máy chủ MCP của StackQL với một trợ lý AI, hãy đăng ký `stackql` làm máy chủ MCP trong cấu hình của trợ lý. Hầu hết các ứng dụng khách MCP nhúng trong trình soạn thảo chạy máy chủ qua `stdio`; với các ứng dụng khách đó, dùng `--mcp.server.type=stdio` và trợ lý sẽ trực tiếp khởi chạy tiến trình. Các tác nhân độc lập dùng HTTP có thể kết nối tới một tiến trình `stackql mcp --mcp.server.type=http` chạy lâu dài.

Với Claude Desktop - bao gồm mục trong Anthropic Connector Directory (cách cài đặt được khuyên dùng), MCP Bundle có thể tải xuống (`.mcpb`) và cấu hình thủ công - hãy xem [Sử dụng StackQL với Claude Desktop](/getting-started/claude-desktop).

:::note

Vị trí tệp cấu hình khác nhau tùy theo hệ điều hành và trợ lý AI. Tham khảo tài liệu của trợ lý AI của bạn để biết đường dẫn và định dạng chính xác.

:::

* * *

### Cân nhắc về kiến trúc {#architecture-considerations}

Khi chọn chế độ triển khai, hãy cân nhắc:

1. **Độc lập (`stackql mcp`)**: cách thiết lập đơn giản nhất, lý tưởng cho phát triển và cho các ứng dụng khách nhúng trong trình soạn thảo qua `stdio`.
2. **Hai máy chủ trong bộ nhớ (`stackql srv` với `mcp.server.type=http`)**: một tiến trình duy nhất phục vụ cả giao thức MCP và giao thức wire của PostgreSQL. Hiệu năng tốt nhất, phù hợp với hầu hết các triển khai trong môi trường sản xuất.
3. **Reverse proxy (`stackql srv` với `mcp.server.type=reverse_proxy`)**:
   - cho phép tách khối lượng công việc ra các tiến trình hoặc máy khác nhau
   - hỗ trợ mã hóa TLS cho điểm cuối MCP bảo mật
   - cho phép mở rộng quy mô độc lập cho giao diện MCP và giao diện PostgreSQL
   - mang lại sự linh hoạt cho việc tích hợp CA doanh nghiệp (lưu ý: hỗ trợ CA doanh nghiệp đang ở giai đoạn thử nghiệm)

Và riêng về an toàn:

- Bắt đầu với `mode: safe` mặc định. Các ứng dụng khách MCP hỗ trợ elicitation (Claude Desktop, Cursor, Continue) sẽ hỏi người dùng trước mọi thao tác thay đổi dữ liệu.
- Ghim `mode: read_only` cho các tác nhân kiểm kê hoặc phân tích không bao giờ được phép ghi.
- Ghim `mode: delete_safe` khi bạn muốn tự do tạo/cập nhật nhưng vẫn muốn có con người tham gia quyết định đối với các thao tác phá hủy.
- Chỉ dùng `mode: full_access` cho các pipeline tự động hóa đáng tin cậy, và chỉ khi kết hợp với một nhật ký kiểm tra có người xem xét.
- Đặt `server.auth_token_env_var` trên mọi listener HTTP mà các tiến trình hoặc máy khác có thể truy cập; `allow_unauthenticated` dành cho phát triển cục bộ. Chỉ riêng TLS không xác thực được ứng dụng khách.

:::caution

Khi dùng TLS với các tổ chức phát hành chứng chỉ (CA) doanh nghiệp, có thể cần cấu hình bổ sung. Tính năng này đang ở giai đoạn thử nghiệm và có thể cần điều chỉnh tùy theo hệ thống CA cụ thể của bạn.

:::

* * *

### Đọc thêm {#further-reading}

- Tài liệu tham khảo cho nhà phát triển gói là [README của `pkg/mcp_server`](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) trong kho lưu trữ stackql.
- Tài liệu kỹ thuật dành cho người đóng góp nằm tại [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) trong kho lưu trữ stackql và bao gồm ví dụ phản hồi cho mọi công cụ.
