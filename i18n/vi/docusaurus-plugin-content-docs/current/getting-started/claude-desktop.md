---
title: Sử dụng StackQL với Claude Desktop
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - claude desktop
  - mcp
  - model context protocol
  - ai
  - infrastructure-as-code
  - đa đám mây
description: Sử dụng StackQL với Claude Desktop để truy vấn và cấp phát tài nguyên đám mây bằng ngôn ngữ tự nhiên
image: "/img/stackql-featured-image.png"
---

StackQL tích hợp với Claude Desktop thông qua Model Context Protocol (MCP), cho phép bạn truy vấn và cấp phát tài nguyên đám mây trên nhiều nhà cung cấp đám mây bằng ngôn ngữ tự nhiên. Sự kết hợp mạnh mẽ này cho phép bạn tương tác với hạ tầng đám mây của mình theo kiểu hội thoại, đồng thời tận dụng giao diện dựa trên SQL của StackQL ở bên dưới.

Có ba tùy chọn cài đặt, được trình bày ở đây theo thứ tự khuyên dùng: cài đặt từ Anthropic Connector Directory, cài đặt từ một bundle đã tải xuống, hoặc đăng ký thủ công một tệp nhị phân StackQL đã cài đặt cục bộ.

## Cài đặt từ Connector Directory (khuyên dùng) {#install-from-the-connector-directory-recommended}

Cách dễ nhất để sử dụng máy chủ MCP StackQL với Claude Desktop là cài đặt nó từ Anthropic Connector Directory. Trong Claude Desktop: **Settings -> Connectors -> Add -> Browse Connectors**, tìm **StackQL** và nhấp vào **Install**. Không có gì cần tải xuống và không cần cài đặt StackQL riêng.

![StackQL trong Anthropic Connector Directory](/img/stackql-anthropic-directory-listing.png)

Bạn cũng có thể xem trực tiếp mục này tại [claude.ai/directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).

Cách này cài đặt máy chủ MCP StackQL chạy trong tiến trình cho nền tảng của bạn: mục này cung cấp một bundle đa nền tảng kết hợp, chứa các tệp nhị phân được ký cho macOS universal, Windows x64, cùng Linux x64 và arm64, và Claude Desktop tự động chọn tệp phù hợp với máy của bạn. Đối với Windows ARM64 gốc, hãy cài đặt bundle `windows-arm64` riêng từ [các bản tải xuống bên dưới](#install-from-a-downloaded-bundle). Các tệp nhị phân Windows được ký bằng Authenticode và tệp nhị phân macOS được Apple công chứng - đây là các tệp nhị phân giống hệt bản phát hành StackQL độc lập.

![Cài đặt connector StackQL](/img/stackql-anthropic-directory-install.png)

Connector được cài đặt từ Connector Directory kế thừa môi trường của Claude Desktop, vì vậy hãy đặt các biến môi trường cho provider của bạn ở cấp hệ điều hành trước khi khởi chạy Claude Desktop - xem [Thông tin xác thực theo provider](#provider-specific-credentials). Claude Desktop ghi nhận môi trường của nó khi khởi chạy: sau khi thay đổi biến, bạn phải thoát hoàn toàn Claude Desktop (bao gồm cả biểu tượng ở khay hệ thống trên Windows) rồi khởi chạy lại. Yêu cầu tác nhân chạy [`reload_credentials`](/mcp/reload_credentials) sẽ báo cáo trạng thái thông tin xác thực theo từng provider và là cách nhanh nhất để xác nhận máy chủ có thể và không thể thấy những gì.

Claude Desktop công bố khả năng elicitation của MCP, vì vậy chế độ máy chủ mặc định (`safe`) sẽ hiển thị yêu cầu phê duyệt cho mỗi thao tác thay đổi dữ liệu - xem [Chế độ máy chủ](/command-line-usage/mcp#server-modes).

## Cài đặt từ một bundle đã tải xuống {#install-from-a-downloaded-bundle}

Sử dụng tùy chọn cài đặt này để ghim một phiên bản cụ thể, cài đặt mà không cần quyền truy cập Connector Directory, hoặc tự xác minh checksum trước khi cài đặt.

MCP Bundle (`.mcpb`) dựng sẵn là một gói có thể cài đặt bằng một cú nhấp, chứa tệp nhị phân `stackql` đã được ký và cấu hình máy chủ. Không cần cài đặt StackQL riêng.

Tải xuống bundle cho nền tảng của bạn từ bản phát hành mới nhất:

| Nền tảng | Bundle |
|---|---|
| macOS (universal) | [stackql-mcp-darwin-universal.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-darwin-universal.mcpb) |
| Windows AMD64 (x64) | [stackql-mcp-windows-x64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-x64.mcpb) |
| Windows ARM64 (gốc) | [stackql-mcp-windows-arm64.mcpb](https://releases.stackql.io/stackql/latest/stackql-mcp-windows-arm64.mcpb) |
| Linux x64 | [stackql-mcp-linux-x64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-x64.mcpb) |
| Linux arm64 | [stackql-mcp-linux-arm64.mcpb](https://github.com/stackql/stackql/releases/latest/download/stackql-mcp-linux-arm64.mcpb) |

<br/>

Sau đó, trong Claude Desktop: **Settings -> Extensions -> Install Extension** và chọn tệp đã tải xuống. Tệp nhị phân được nhúng được ký bằng Authenticode (Windows) và được Apple công chứng (macOS); mỗi bundle có một checksum `.sha256` đi kèm trên [trang bản phát hành](https://github.com/stackql/stackql/releases/latest). Để xác minh trước khi cài đặt:

```bash
# macOS / Linux
shasum -a 256 -c stackql-mcp-darwin-universal.mcpb.sha256
```

Máy chủ cũng được liệt kê trên [Official MCP Registry](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) với tên `io.github.stackql/stackql-mcp`, nơi ghim các checksum này.

## Cấu hình thủ công {#manual-configuration}

Nếu bạn đã cài đặt StackQL, bạn có thể đăng ký máy chủ MCP StackQL trực tiếp trong tệp cấu hình của Claude Desktop.

### Điều kiện tiên quyết {#prerequisites}

Trước khi thiết lập StackQL với Claude Desktop, hãy đảm bảo bạn có:

1. **Đã cài đặt StackQL** - Tải xuống và cài đặt StackQL từ [stackql.io](/installing-stackql) hoặc dùng một trình quản lý gói
2. **Claude Desktop** - Tải xuống từ [trang web của Anthropic](https://claude.ai/desktop)
3. **Thông tin xác thực của nhà cung cấp đám mây** - Khóa API hoặc thông tin xác thực cho các nhà cung cấp đám mây mà bạn muốn làm việc cùng

Xác minh StackQL nằm trong PATH của hệ thống bằng cách chạy:

```bash
stackql --version
```

### Thiết lập máy chủ MCP {#setting-up-the-mcp-server}

Để Claude Desktop có thể giao tiếp với StackQL, bạn cần cấu hình máy chủ MCP trong tệp cấu hình Claude Desktop của mình.

#### Windows {#windows}

Chỉnh sửa tệp cấu hình nằm tại:
```
%APPDATA%\Claude\claude_desktop_config.json
```

#### macOS {#macos}

Chỉnh sửa tệp cấu hình nằm tại:
```
~/Library/Application Support/Claude/claude_desktop_config.json
```

#### Linux {#linux}

Chỉnh sửa tệp cấu hình nằm tại:
```
~/.config/Claude/claude_desktop_config.json
```

### Ví dụ cấu hình {#configuration-example}

Thêm cấu hình sau vào tệp `claude_desktop_config.json` của bạn:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure"
      ],
      "env": {
        "GOOGLE_CREDENTIALS": "/path/to/google-credentials.json",
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "AZURE_CLIENT_ID": "your-azure-client-id",
        "AZURE_CLIENT_SECRET": "your-azure-client-secret",
        "AZURE_TENANT_ID": "your-azure-tenant-id",
        "DATABRICKS_CLIENT_ID": "your-databricks-client-id",
        "DATABRICKS_CLIENT_SECRET": "your-databricks-client-secret",
        "OPENAI_API_KEY": "your-openai-api-key"
      }
    }
  }
}
```

:::tip
Bạn chỉ cần thêm biến môi trường cho các nhà cung cấp đám mây mà bạn định sử dụng. Xóa mọi thông tin xác thực provider không dùng đến khỏi cấu hình.
:::

:::warning
Không bao giờ commit tệp `claude_desktop_config.json` chứa thông tin xác thực thực tế vào hệ thống quản lý phiên bản. Áp dụng các phương pháp quản lý thông tin xác thực an toàn.
:::

Claude Desktop công bố khả năng elicitation của MCP, vì vậy chế độ máy chủ mặc định (`safe`) sẽ hiển thị yêu cầu phê duyệt cho mỗi thao tác thay đổi dữ liệu. Chuyển sang `mode: read_only` để chỉ có quyền truy cập kiểm kê hoặc `mode: full_access` để bỏ qua các yêu cầu phê duyệt - xem [Chế độ máy chủ](/command-line-usage/mcp#server-modes).

### Sử dụng tệp thông tin xác thực (`--env.file`) {#using-a-credentials-file---envfile}

Một giải pháp thay thế cho khối `env` - và là giải pháp được khuyên dùng trên Windows - là lưu thông tin xác thực trong một tệp theo kiểu dotenv và truyền tệp đó bằng cờ [`--env.file`](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--tls.allowInsecure",
        "--env.file=C:\\Users\\me\\stackql-credentials.env"
      ]
    }
  }
}
```

Với một tệp thông tin xác thực tối thiểu:

```bash
# stackql-credentials.env
GOOGLE_CREDENTIALS=/path/to/google-credentials.json
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
```

Lý do cách này tốt hơn khối `env`:

- Các thay đổi đối với khối `env` chỉ đến được tiến trình con MCP sau khi khởi động lại hoàn toàn Claude Desktop (bao gồm cả thoát biểu tượng ở khay hệ thống), và trên Windows, `setx` ghi vào registry - giá trị đó không bao giờ đến được tiến trình Desktop đang chạy.
- Tệp `--env.file` có thể được tạo hoặc xoay vòng bất cứ lúc nào - tệp thậm chí không cần tồn tại khi máy chủ khởi động - và yêu cầu tác nhân chạy [`reload_credentials`](/mcp/reload_credentials) sẽ áp dụng các thay đổi mà không cần khởi động lại.

:::note

`--env.file` và `reload_credentials` có trong các bản phát hành StackQL sau `v0.10.542`.

:::

### Thông tin xác thực theo provider {#provider-specific-credentials}

Các nhà cung cấp đám mây khác nhau yêu cầu các phương thức xác thực khác nhau, hãy xem tài liệu provider ([https://stackql.io/providers](/providers)) để biết các biến môi trường cần thiết cho provider bạn muốn dùng.


### Khởi động lại Claude Desktop {#restarting-claude-desktop}

Sau khi cập nhật tệp cấu hình, khởi động lại Claude Desktop để tải cấu hình máy chủ MCP mới. Máy chủ MCP StackQL sẽ tự động khởi động khi bạn bắt đầu một cuộc hội thoại mới.

## Những gì bạn có thể làm {#what-you-can-do}

Sau khi cấu hình xong, bạn có thể dùng ngôn ngữ tự nhiên trong Claude Desktop để:

### Truy vấn tài nguyên đám mây {#query-cloud-resources}

Yêu cầu Claude truy xuất thông tin về hạ tầng đám mây của bạn:

- "Hiển thị tất cả instance compute GCP của tôi trong khu vực us-central1"
- "Liệt kê tất cả bucket S3 trong tài khoản AWS của tôi"
- "Những máy ảo Azure nào đang chạy trong gói đăng ký của tôi?"
- "Hiển thị tất cả pod Kubernetes trong namespace mặc định"

### Cấp phát tài nguyên {#provision-resources}

Tạo và triển khai tài nguyên đám mây bằng các lệnh dạng hội thoại:

- "Tạo một bucket S3 tên là 'my-data-bucket' trong us-west-2"
- "Triển khai một bucket Cloud Storage GCP có bật quản lý phiên bản"
- "Khởi chạy một instance EC2 nhỏ trong us-east-1"
- "Tạo một nhóm tài nguyên Azure trong westus2"

### Phân tích và kiểm tra {#analyze-and-audit}

Thực hiện kiểm tra bảo mật và kiểm tra tuân thủ:

- "Hiển thị tất cả bucket S3 có thể truy cập công khai"
- "Liệt kê những người dùng IAM chưa đăng nhập trong 90 ngày"
- "Tìm tất cả instance GCP không có nhãn phù hợp"
- "Xác định các VM Azure chưa bật sao lưu"

### Thao tác liên đám mây {#cross-cloud-operations}

Truy vấn và so sánh tài nguyên trên nhiều nhà cung cấp đám mây:

- "Hiển thị tất cả instance compute trên AWS, GCP và Azure"
- "So sánh chi phí lưu trữ giữa AWS S3 và GCP Cloud Storage"
- "Liệt kê tất cả cơ sở dữ liệu trên mọi nhà cung cấp đám mây"

### Hạ tầng dưới dạng mã {#infrastructure-as-code}

Tạo và thực thi các truy vấn triển khai hạ tầng:

- "Tạo một stack tài nguyên gồm một VPC, một subnet và một instance EC2"
- "Triển khai một cụm GKE hoàn chỉnh với các node pool"
- "Thiết lập một bản triển khai đa khu vực trên AWS"

### Sửa đổi tài nguyên {#modify-resources}

Cập nhật tài nguyên đám mây hiện có:

- "Thêm tag cho tất cả instance EC2 chưa có tag"
- "Bật mã hóa cho bucket S3 'my-bucket'"
- "Cập nhật loại máy của instance GCP 'web-server-1'"
- "Đổi ACL của bucket Cloud Storage của tôi thành riêng tư"

### Xóa tài nguyên {#delete-resources}

Xóa tài nguyên đám mây khi không còn cần đến:

- "Xóa bucket S3 'temp-bucket'"
- "Xóa tất cả instance EC2 đã dừng"
- "Dọn dẹp các ổ đĩa GCP không sử dụng"

## Ví dụ về cuộc hội thoại {#example-conversation}

Dưới đây là một ví dụ về cách bạn có thể tương tác với Claude Desktop khi sử dụng StackQL:

**Bạn:** "Bạn có thể hiển thị tất cả instance compute GCP của tôi không?"

**Claude:** *Thực thi một truy vấn StackQL và trả về kết quả trong một bảng được định dạng, hiển thị tên instance, zone, loại máy và trạng thái*

**Bạn:** "Tạo một bucket Cloud Storage mới tên là 'analytics-data-2025' trong khu vực us-central1 với quyền truy cập đồng nhất ở cấp bucket"

**Claude:** *Thực thi một câu lệnh INSERT của StackQL để tạo bucket và xác nhận việc tạo*

**Bạn:** "Bây giờ hãy liệt kê tất cả bucket Cloud Storage của tôi"

**Claude:** *Thực thi một truy vấn SELECT và hiển thị tất cả bucket, bao gồm cả bucket vừa được tạo*

## Mẹo và phương pháp hay nhất {#tips-and-best-practices}

1. **Nêu cụ thể**: Yêu cầu của bạn càng cụ thể, kết quả bạn nhận được càng tốt. Đưa vào tên tài nguyên, khu vực và các thuộc tính cụ thể khi có thể.

2. **Dùng ngôn ngữ tự nhiên**: Bạn không cần biết cú pháp SQL - Claude chuyển các yêu cầu bằng ngôn ngữ tự nhiên của bạn thành truy vấn StackQL.

3. **Xem lại trước khi thực thi**: Đối với các thao tác phá hủy (DELETE, UPDATE), Claude thường sẽ cho bạn xem những gì sẽ được thực thi trước khi tiếp tục.

4. **Thao tác nhiều bước**: Bạn có thể nối tiếp các thao tác trong một cuộc hội thoại. Claude duy trì ngữ cảnh trong suốt phiên.

5. **Xử lý lỗi**: Nếu một truy vấn thất bại, Claude có thể giúp khắc phục sự cố và đề xuất cách sửa dựa trên thông báo lỗi.

6. **Khám phá khả năng**: Hỏi Claude "Tôi có thể làm gì với StackQL?" để nhận các gợi ý dành riêng cho những provider bạn đã cấu hình.

## Khắc phục sự cố {#troubleshooting}

### Máy chủ MCP không khởi động {#mcp-server-not-starting}

Nếu Claude Desktop không thể kết nối với máy chủ MCP StackQL:

1. Xác minh StackQL có trong PATH của bạn
2. Kiểm tra rằng tệp `claude_desktop_config.json` của bạn là JSON hợp lệ
3. Đảm bảo đường dẫn tệp trong cấu hình đúng với hệ điều hành của bạn
4. Xem nhật ký của Claude Desktop để tìm thông báo lỗi

### Lỗi xác thực {#authentication-errors}

Nếu bạn gặp lỗi xác thực:

1. Xác minh thông tin xác thực của nhà cung cấp đám mây là chính xác
2. Đảm bảo các tệp thông tin xác thực tồn tại ở các đường dẫn đã chỉ định
3. Kiểm tra rằng các biến môi trường được định dạng đúng trong cấu hình - yêu cầu tác nhân chạy [`reload_credentials`](/mcp/reload_credentials) sẽ báo cáo trạng thái thông tin xác thực theo từng provider và là cách nhanh nhất để biết biến nào bị thiếu
4. Xác nhận thông tin xác thực của bạn có các quyền cần thiết

### Lỗi quyền {#permission-errors}

Nếu các thao tác thất bại do không đủ quyền:

1. Xác minh tài khoản dịch vụ hoặc người dùng IAM của bạn có các quyền cần thiết
2. Kiểm tra các chính sách IAM riêng của từng provider
3. Đảm bảo các dịch vụ API đã được bật trong các dự án đám mây của bạn

## Tìm hiểu thêm {#learn-more}

- [Tài liệu tham khảo lệnh MCP của StackQL](/command-line-usage/mcp)
- [StackQL Provider Registry](/providers)
- [Đặc tả ngôn ngữ](/language-spec/select)
- [Tài liệu Model Context Protocol](https://modelcontextprotocol.io)

## Lưu ý về bảo mật {#security-considerations}

:::caution
- Lưu trữ thông tin xác thực một cách an toàn và không bao giờ commit chúng vào hệ thống quản lý phiên bản
- Áp dụng nguyên tắc truy cập đặc quyền tối thiểu cho tài khoản dịch vụ
- Thường xuyên xoay vòng thông tin xác thực và khóa API
- Giám sát nhật ký của máy chủ MCP để phát hiện hoạt động bất thường
- Cân nhắc sử dụng các công cụ quản lý thông tin xác thực của nhà cung cấp đám mây (ví dụ: Secret Manager, Parameter Store)
:::
