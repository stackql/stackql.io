---
title: Cờ toàn cục
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Cờ toàn cục cho tệp nhị phân stackql: thiết lập xác thực, định dạng đầu ra, vị trí registry, ghi nhật ký và backend SQL, dùng được với mọi lệnh."
image: "/img/stackql-featured-image.png"
---

Các lệnh StackQL đều chấp nhận cờ toàn cục, nhưng một số cờ chỉ áp dụng cho một chế độ cụ thể. Đặt cờ trên dòng lệnh hoặc, khi được hỗ trợ, trong tệp khởi tạo StackQL ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)). Các cờ `--pgsrv.*` được liệt kê trong [tùy chọn máy chủ PostgreSQL](/command-line-usage/srv#server-options).

| Cờ | Kiểu dữ liệu | Mô tả | Mặc định |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Cấu hình truy cập của bên gọi, một đối tượng JSON bao ngoài `{"path": "/path/to/access.json"}` (một đường dẫn tệp đơn thuần cũng được chấp nhận).  Bật [quản lý truy cập](/command-line-usage/access-management): bên gọi được xác thực bằng JWT, bearer token tĩnh, TLS hai chiều hoặc danh tính hệ điều hành cục bộ và các câu lệnh được ủy quyền theo các quy tắc chính sách.  Thông tin xác thực token cục bộ được đọc từ `STACKQL_CALLER_TOKEN`.  Có từ StackQL `v1.0` trở đi. | |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Cho phép một phiên chọn kho bí mật bền vững của hệ điều hành (macOS Keychain, Linux Secret Service hoặc Windows Credential Manager) bằng `SET "$.secret_store"`.  Các kho dạng tệp phẳng bị từ chối ngay cả khi có cờ này.  Xem [Phiên provider và bí mật](/getting-started/provider-sessions#persistent-os-secret-stores).  Có từ StackQL `v1.0` trở đi. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Cho phép giải tham chiếu giá trị bí mật và thông tin xác thực trong biểu thức SQL (`"$secrets.name"`, `"$auth.provider.field"`), với hệ quả là các giá trị này có thể xuất hiện trong kết quả, nhật ký và các artifact truy vấn được lưu bền vững.  Không cần thiết cho xác thực provider thông thường.  Xem [Giá trị bí mật trong SQL](/getting-started/provider-sessions#secret-values-in-sql).  Có từ StackQL `v1.0` trở đi. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | Thời gian chờ yêu cầu API tính bằng giây, 0 nghĩa là không giới hạn thời gian chờ | `45` |
| <span class="nowrap">`--approot`</span> | string | Đường dẫn gốc cho cấu hình và cache của ứng dụng | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Chế độ tệp cho cấu hình và cache của ứng dụng | `493` |
| <span class="nowrap">`--auth`</span> | string | Các cặp khóa-giá trị ngữ cảnh `auth` ở dạng json |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Số lượng khóa ban đầu của cache | `100` |
| <span class="nowrap">`--configfile`</span> | string | Đường dẫn đầy đủ của tệp cấu hình | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | Tệp `cpuprofile`, không có nếu để trống | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | integer | Độ sâu phần phụ thuộc luồng dữ liệu tối đa cho một truy vấn | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | integer | Số thành phần liên thông yếu tối đa của luồng dữ liệu cho một truy vấn | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | Chuỗi JSON / YAML để cấu hình cách xử lý truy vấn bảo trì nội bộ của DBMS | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Dấu phân cách cho đầu ra csv. Chỉ một ký tự.  Bị bỏ qua đối với mọi đầu ra không phải csv | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | Cờ `dryrun`; chỉ chạy bộ tiền xử lý, đầu ra đã áp dụng template sẽ được trả về | `false` |
| <span class="nowrap">`--env.file`</span> | string | Tệp kiểu dotenv không bắt buộc, được nạp vào môi trường tiến trình khi khởi động (mọi lệnh); công cụ MCP [`reload_credentials`](/mcp/reload_credentials) nạp lại tệp này theo yêu cầu. Tệp bị thiếu vẫn được chấp nhận; các khóa có giá trị rỗng bị bỏ qua; các biến đã tồn tại được ghi đè. Không lấy giá trị từ `.stackqlrc`. Có trong các bản phát hành StackQL sau `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | integer | Giới hạn đồng thời cho việc thực thi truy vấn | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Tiền tố bí danh xuất (không gian tên hoặc lược đồ) | |
| <span class="nowrap">`--gc`</span> | string | Chuỗi JSON / YAML biểu diễn cấu hình GC | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Tắt tiêu đề cột, chỉ hợp lệ với `--output csv` hoặc `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Hiển thị thông tin yêu cầu http trong terminal | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Máy chủ proxy (để trống nếu không dùng proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Mật khẩu proxy | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Cổng proxy, mọi số nhỏ hơn hoặc bằng 0 sẽ dẫn đến cổng mặc định của giao thức tương ứng (ví dụ `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Giao thức proxy, `http` hoặc `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Người dùng proxy | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Số kết quả tối đa cho mỗi yêu cầu http, mọi số nhỏ hơn hoặc bằng 0 nghĩa là không giới hạn | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Số trang kết quả tối đa được trả về cho mỗi tài nguyên, mọi số nhỏ hơn hoặc bằng 0 nghĩa là không giới hạn | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Độ sâu tối đa cho các truy vấn gián tiếp: dạng xem và truy vấn con | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Tệp đầu vào (tệp IQL) để đọc truy vấn | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Tệp ngữ cảnh (dữ liệu) cho việc sử dụng template (tệp `json` hoặc `jsonnet`)| |
| <span class="nowrap">`--loglevel`</span> | string | Mức nhật ký, phải là một trong \{`panic`, `fatal`, `error`, `warn`, `info`, `debug`, `trace`\} (`warning` được chấp nhận làm bí danh cho `warn`) | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | Đối tượng cấu hình JSON / YAML cho [máy chủ MCP](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` và `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | Kiểu mã hóa nhật ký kiểm tra MCP, `jsonl` hoặc `otel` (bản ghi nhật ký OTLP/JSON); ghi đè `server.audit.format` trong `--mcp.config`.  Xem [Định dạng nhật ký](/command-line-usage/mcp#log-format).  Có từ StackQL `v0.11.660` trở đi. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Bản sửa đổi giao thức MCP mới nhất được công bố, `auto`, `2026-07-28` (chỉ chế độ không có phiên) hoặc một bản sửa đổi cũ hơn như `2025-11-25`; ghi đè `server.protocol_version` trong `--mcp.config`.  Xem [Ghim phiên bản sửa đổi](/command-line-usage/mcp#pinning-the-revision).  Có từ StackQL `v0.12.732` trở đi. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | Loại máy chủ MCP, một trong \{`http`, `stdio`, `reverse_proxy`\}.  Xem [Loại máy chủ MCP](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL cho các tài liệu siêu dữ liệu được lưu trong cache, tính bằng giây | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | Chuỗi JSON / YAML biểu diễn không gian tên cho cache, dạng xem, v.v. | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Làm việc ngoại tuyến, sử dụng dữ liệu đã lưu trong cache | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Tệp đầu ra để ghi kết quả vào | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Định dạng đầu ra, phải là một trong \{`json`, `jsonl`, `table`, `csv`, `text`, `otel`\} (`ndjson` được chấp nhận làm bí danh cho `jsonl`).  `otel` phát ra bản ghi nhật ký OpenTelemetry, xem [Chế độ đầu ra](/getting-started/output-modes#otel-output); có từ StackQL `v0.11.660` trở đi.  Khi đã cấu hình một exporter trong `--otel.config`, các bản ghi `otel` cũng được đẩy tới điểm cuối đó | `table` |
| <span class="nowrap">`--otel.config`</span> | string | Chuỗi JSON / YAML cấu hình định dạng đầu ra `otel`; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` và `exporter.batch_size` đẩy các bản ghi tới một điểm cuối nhật ký OTLP/HTTP, xem [Đẩy đến một điểm cuối OTLP](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  Có từ StackQL `v0.12.732` trở đi | `{}` |
| <span class="nowrap">`--preview`</span> | string | Chuỗi JSON bật tính năng xem trước.  `{"unstable": true}` cung cấp các quan hệ `stackql_unstable_<provider>.<service>.<resource>` dựa trên tài liệu, được `omnisdk` phân giải và chạy trực tiếp từ tài liệu provider, với các hàng được truyền theo luồng ngay khi được tạo ra; các khóa còn lại (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) tinh chỉnh backend đó.  Tính năng xem trước chưa ổn định: hành vi, cú pháp và đầu ra có thể thay đổi hoặc hỏng giữa các bản phát hành mà không báo trước, vì vậy đừng dựa vào tính năng này trong môi trường sản xuất.  Từ `v1.0`, cùng tùy chọn chủ động bật này cũng cung cấp các quan hệ `stackql_preview.iac.run` và `stackql_preview.iac.destroy`, dùng để áp dụng và hủy một tập tài nguyên đã khai báo thông qua `omnisdk`, với trạng thái được lưu dưới thư mục gốc của ứng dụng.  Xem [Tính năng xem trước](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--provider`</span> | string | Provider mặc định ban đầu. Ví dụ, với `--provider github`, `repos.repos` được phân giải thành `github.repos.repos` mà không cần câu lệnh `USE` phía trước. Không xác thực với provider. | không có |
| <span class="nowrap">`--querycachesize`</span> | integer | Kích thước của cache LRU cho các kế hoạch truy vấn, tính bằng số mục | `10000` |
| <span class="nowrap">`--registry`</span> | string | Chuỗi JSON / YAML cấu hình vị trí registry provider và việc xác minh chữ ký tài liệu provider | xem [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | Chuỗi JSON / YAML biểu diễn cấu hình phiên | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | Chuỗi JSON / YAML biểu diễn cấu hình hệ thống backend SQL | xem [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | Chuỗi JSON / YAML biểu diễn cấu hình kho Txn | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Đường dẫn tới CA bundle; nếu không chỉ định thì sử dụng mặc định của hệ thống |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Cho phép tin cậy các chứng chỉ không an toàn (không khuyến nghị) |  |
| <span class="nowrap">`--upstream.errors`</span> | string | Chính sách JSON / YAML để chấp nhận một cách rõ ràng các trạng thái phản hồi HTTP khi đọc từ provider. Xem [`--upstream.errors`](#--upstream-errors). | `{}` |
| <span class="nowrap">`--var`</span> | string | Biến bên ngoài được cung cấp dưới dạng cặp khóa/giá trị để đưa vào truy vấn StackQL thông qua các tệp dữ liệu `jsonnet` |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Đầu ra chi tiết | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Trợ giúp theo ngữ cảnh cho stackql | |
| <span class="nowrap">`--version`</span> | | Hiển thị phiên bản của chương trình StackQL | |

`--colorscheme` đã lỗi thời và không còn thay đổi màu của shell. `--usenonpreferredapis` được chấp nhận nhưng không thay đổi việc lựa chọn API trong công cụ truy vấn hiện tại.

## `--registry` {#--registry}

Cờ `--registry` cấu hình nơi đọc tài liệu provider và việc có xác minh chữ ký của chúng hay không.  Giá trị là một chuỗi JSON với các khóa sau:

| Khóa | Mô tả | Mặc định |
| -- | -- | -- |
| `url` | URL của registry.  Registry provider công khai của StackQL được dùng khi bỏ qua khóa này. | registry công khai |
| `localDocRoot` | Một thư mục cục bộ chứa tài liệu provider, được đọc thay vì tải xuống từ registry.  Dùng khi [phát triển một provider](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Tiền tố đường dẫn bên dưới gốc registry, nơi chứa các tài liệu nguồn của provider. | `src` |
| `verifyConfig.nopVerify` | `true` bỏ qua việc xác minh chữ ký.  Đặt thành `false` để yêu cầu chữ ký ML-DSA-65 (FIPS 204) được nhúng trên mọi tài liệu provider và dịch vụ được đọc, bao gồm cả các quan hệ bản xem trước dựa trên tài liệu. | `true` |
| `verifyConfig.publicKeys` | Một đối tượng JSON ánh xạ ID khóa tới khóa công khai ML-DSA-65 được mã hóa hex, dành cho registry riêng.  Đối tượng này thay thế các khóa sản xuất được nhúng; một đối tượng rỗng được chỉ định rõ ràng là lỗi. | khóa được nhúng |

### Ví dụ: yêu cầu tài liệu provider đã được ký {#example-require-signed-provider-documents}

Bật xác minh cho mọi tài liệu provider và dịch vụ được đọc từ registry công khai, kiểm tra theo các khóa sản xuất được nhúng:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Ví dụ: đọc tài liệu chưa ký từ một thư mục cục bộ {#example-read-unsigned-documents-from-a-local-directory}

Trỏ registry tới một thư mục chứa tài liệu provider đang được phát triển.  Các tài liệu đó chưa được ký, vì vậy việc xác minh vẫn tắt:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

Xác minh chữ ký có từ StackQL `v1.0` trở đi và tắt theo mặc định.  Chữ ký bao phủ chính xác các byte YAML của một tài liệu, vì vậy mọi chỉnh sửa, kể cả thay đổi định dạng hoặc ký tự dòng mới, đều làm chữ ký mất hiệu lực; hãy ký lại bằng công cụ `pqsig sign-doc` của registry.  Các tài liệu phát triển cục bộ chưa ký phải giữ `nopVerify: true`.  Các tùy chọn chứng chỉ trước đây và các tệp sidecar `.sig` không còn được sử dụng, và các tệp nén chỉ được phát hành kèm sidecar phải được phát hành lại trước khi dùng với xác minh.  Việc xác minh xác thực tài liệu khi chúng được đọc; việc tải xuống và giải nén tệp nén không nằm trong phạm vi này.

:::

## `--sqlBackend` {#--sqlbackend}

Cờ `--sqlBackend` cấu hình một backend SQL bên ngoài cho StackQL, cho phép bạn sử dụng một công cụ cơ sở dữ liệu bên ngoài (chẳng hạn PostgreSQL) thay vì công cụ SQL nhúng mặc định. Điều này hữu ích khi tận dụng một cơ sở dữ liệu bên ngoài để xử lý truy vấn, lưu trữ bền vững hoặc tích hợp với hạ tầng cơ sở dữ liệu hiện có.

Giá trị là một chuỗi JSON với các khóa sau:

| Khóa | Mô tả | Giá trị ví dụ |
| -- | -- | -- |
| `dbEngine` | Loại công cụ cơ sở dữ liệu | `postgres_tcp` |
| `sqlDialect` | Phương ngữ SQL cần sử dụng | `postgres` |
| `dsn` | Tên nguồn dữ liệu (chuỗi kết nối) cho cơ sở dữ liệu | Xem các ví dụ bên dưới |

### Ví dụ: sử dụng backend PostgreSQL {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Ví dụ: sử dụng giao diện PostgreSQL của Databricks {#example-using-databricks-postgresql-interface}

Databricks cung cấp một giao diện tương thích PostgreSQL có thể dùng làm backend SQL của StackQL. DSN sử dụng định dạng chuỗi kết nối PostgreSQL tiêu chuẩn với thông tin xác thực Databricks của bạn:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Các ký tự đặc biệt trong tên người dùng hoặc mật khẩu (chẳng hạn `@`) phải được mã hóa URL trong DSN (ví dụ: `@` trở thành `%40`).
:::

## `--upstream.errors` {#--upstream-errors}

Sử dụng `http.ignoreStatusCodes` để liệt kê các trạng thái phản hồi HTTP mà một thao tác đọc từ provider cần chấp nhận một cách rõ ràng. Các mục là chuỗi: dùng một mã chính xác như `"503"` hoặc một lớp như `"5xx"`. Giá trị mặc định `{}` không thêm ngoại lệ nào vào cách xử lý thông thường của công cụ truy vấn đối với các phản hồi upstream. Thiết lập này không thử lại các yêu cầu thất bại và không áp dụng cho các thao tác thay đổi dữ liệu hoặc các trao đổi xác thực.

Một phản hồi bị bỏ qua không đóng góp hàng nào. Các thao tác đọc khác trong truy vấn vẫn có thể trả về hàng, vì vậy một truy vấn thành công có thể trả về kết quả không đầy đủ. StackQL báo cáo trạng thái HTTP bị bỏ qua dưới dạng thông tin chẩn đoán.

Ví dụ, [bài kiểm thử phép nối GitHub dùng dữ liệu giả lập](https://github.com/stackql/stackql/blob/256187fab2410b7c999e7208bd888b12c5df9b9e/test/robot/functional/stackql_mocked_from_cmd_line.robot#L2104) sử dụng tùy chọn này:

```bash
stackql exec --upstream.errors='{"http":{"ignoreStatusCodes":["5xx"]}}' \
  "SELECT r.name, c.login FROM github.repos.repos r
   JOIN github.repos.collaborators c ON c.repo = r.name
   WHERE r.org = 'example-org' AND c.owner = 'example-org'
   ORDER BY r.name;"
```

Trong bài kiểm thử, yêu cầu lấy cộng tác viên cho một kho lưu trữ trả về HTTP `500`. Truy vấn vẫn trả về các hàng cộng tác viên cho những kho lưu trữ khác và stderr chứa `ignoring upstream response: 500 INTERNAL SERVER ERROR`. Nên dùng một mã trạng thái chính xác khi chỉ dự kiến có một lỗi.
