---
title: SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - biến phiên
  - set
  - phiên provider
  - bí mật
description: "SET trong StackQL: gán biến phiên có kiểu và cấu hình bí danh provider, trường thông tin xác thực và bí mật cho phiên hiện tại bằng các đích có tiền tố đô la."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Gán một biến phiên, hoặc cấu hình các thiết lập runtime cho phiên hiện tại. Có từ StackQL `v1.0` trở đi.

Xem thêm:  
[[` UNSET `]](/language-spec/unset) [[ Biến phiên ]](/getting-started/session-variables) [[ Phiên provider ]](/getting-started/provider-sessions) [[` IDENTIFIER `]](/language-spec/functions/general/identifier)

* * * 

## Cú pháp {#syntax}

*setStatement::=*

<RailroadDiagram
type="set"
/>

*setExpression::=*

<RailroadDiagram
type="setExpression"
/>

&nbsp;  
&nbsp;  

```sql
SET [ SESSION ] <target> = { <literal> | DEFAULT | <reference> } [, <target> = { <literal> | DEFAULT | <reference> } ...];
```

__*target*__  
Một định danh thuần (`region`), một tên đặt trong dấu ngoặc kép (`"providers.saved"`) hoặc một đường dẫn cấu hình có tiền tố đô la đặt trong dấu ngoặc kép (`"$.auth.aws.password"`).

__*literal*__  
Một chuỗi, số, boolean hoặc `null`. Chuỗi được đặt trong dấu nháy đơn. Mảng và đối tượng không phải là giá trị vô hướng; hãy lưu tài liệu JSON dưới dạng chuỗi.

__*DEFAULT*__  
Đặt lại đích, tương đương với [`UNSET`](/language-spec/unset). Một `null` tường minh là một giá trị được lưu, không phải một phép đặt lại.

__*reference*__  
Một tham chiếu `"$name"` đặt trong dấu ngoặc kép tới một biến hiện có, ví dụ `SET "$.providers.production" = "$providers.saved";`.

__*SESSION*__  
Không bắt buộc, và là phạm vi duy nhất mà StackQL có. `SET SESSION name = ...` và `SET name = ...` là cùng một câu lệnh: mọi phép gán đều thuộc về phiên hiện tại và không thuộc về gì khác. Từ khóa này tồn tại để tập lệnh đọc giống như trên các cơ sở dữ liệu nơi `SET SESSION` và `SET GLOBAL` khác nhau. `SET GLOBAL` được phân tích cú pháp nhưng bị từ chối với lỗi `configuration scope "global" is not supported; use SESSION`, vì không có trạng thái nào được chia sẻ giữa các phiên.

Các phép gán trong một danh sách `SET` được áp dụng theo thứ tự và không phải là một giao dịch: nếu phép gán thứ hai trong danh sách thất bại, phép gán thứ nhất vẫn có hiệu lực.

## Đích {#targets}

| Đích | Tác dụng |
|--|--|
| `name` hoặc `"name"` | Lưu một biến phiên vô hướng, được giải tham chiếu dưới dạng `$name` trong các câu lệnh sau. Chỉ riêng dấu chấm không chọn cấu hình: `"providers.saved"` là một biến có tên `providers.saved`. Cách viết cũ `"variables.name"` là bí danh của `name`. |
| `"$.auth.<provider>.<field>"` | Đặt một trường của ngữ cảnh xác thực của provider cho phiên này. Các trường là các khóa của đối tượng [`--auth`](/command-line-usage/global-flags) cho provider đó, ví dụ `credentialsenvvar`, `password` hoặc `token_url`. |
| `"$.providers.<alias>"` | Liên kết một bí danh provider với thông tin xác thực, nhãn tenancy, giá trị mặc định của tham số và thiết lập tinh chỉnh riêng. Giá trị là một tài liệu JSON trong một chuỗi, xem [Phiên provider](/getting-started/provider-sessions#provider-aliases). |
| `"$.secrets.<name>"` | Lưu một bí mật trong kho bí mật của phiên. Giá trị là một literal chuỗi, thường là một tài liệu JSON `auth`, được tham chiếu từ bí danh provider dưới dạng `auth_secret`. |
| `"$.secret_store"` | Chọn một kho bí mật bền vững của hệ điều hành, `{"backend":"os","namespace":"..."}`. Yêu cầu cờ dành cho người vận hành `--allow-persistent-secrets`. |

Mọi dạng chỉ ảnh hưởng đến phiên hiện tại. Các kết nối độc lập tới một máy chủ [`srv`](/command-line-usage/srv) không thấy phép gán của nhau, và mỗi yêu cầu MCP là một phiên riêng, vì vậy khi dùng máy chủ MCP, hãy cấu hình và truy vấn trong cùng một lô câu lệnh.

* * *

## Ví dụ {#examples}

### Đặt biến phiên {#set-session-variables}

Mỗi câu lệnh lưu một giá trị vô hướng dưới tên đã cho, và giá trị giữ nguyên kiểu của nó: `region` là chuỗi, `minimum_count` là số nguyên và `enabled` là boolean. `null` được lưu như một giá trị, nên về sau `$optional_value` được phân giải thành `NULL` của SQL thay vì thất bại vì chưa được định nghĩa. Câu lệnh cuối cùng gán hai biến trong một danh sách.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
SET suffix = 'East', some_id = 42;
```

### Chỉ rõ phạm vi phiên {#make-the-session-scope-explicit}

Hai câu lệnh đầu tiên giống hệt nhau: `SESSION` chỉ phạm vi duy nhất hiện có, và cả hai phép gán đều kéo dài cho đến khi biến bị hủy thiết lập hoặc phiên kết thúc. Dùng từ khóa này khi một tập lệnh cần đọc giống như trên một cơ sở dữ liệu có phân biệt thiết lập phiên với thiết lập toàn cục. Câu lệnh nằm trong khối chú thích được đưa ra để đối chiếu: câu lệnh này không gán gì và thất bại với lỗi `configuration scope "global" is not supported; use SESSION`, vì không có trạng thái nào được chia sẻ giữa các phiên.

```sql
SET region = 'ap-southeast-2';
SET SESSION region = 'ap-southeast-2';

/*
SET GLOBAL region = 'ap-southeast-2';
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Dùng biến trong truy vấn {#use-a-variable-in-a-query}

Giải tham chiếu một biến bằng `$name`, không đặt trong dấu nháy, ở bất kỳ vị trí biểu thức nào. Giá trị giữ nguyên kiểu: chuỗi được thoát thành literal SQL, còn số, boolean và `null` được truyền nguyên trạng. Biến trong mệnh đề `WHERE` được phân giải trước khi yêu cầu được lập kế hoạch, nên chúng được đẩy xuống provider giống như literal.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

### Đặt lại biến {#reset-a-variable}

`DEFAULT` xóa biến, đúng như [`UNSET`](/language-spec/unset), nên một `$region` về sau sẽ thất bại với lỗi `variable "$region" is not defined` cho đến khi biến được gán lại; truy vấn nằm trong khối chú thích cho thấy lỗi đó. Điều này khác với việc gán `null`, vốn giữ nguyên biến với giá trị `NULL`.

```sql
SET region = 'ap-southeast-2';
SET region = DEFAULT;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Sao chép giá trị từ một biến khác {#copy-a-value-from-another-variable}

Vế phải có thể là một tham chiếu đặt trong dấu ngoặc kép tới một biến hiện có; tham chiếu này sao chép giá trị mà biến đó đang giữ tại thời điểm đó. Ở đây, một liên kết provider trước tiên được lưu dưới dạng một biến thông thường có tên `providers.saved` (dấu chấm không biến nó thành cấu hình), sau đó được đưa vào sử dụng bằng cách sao chép sang đường dẫn `$.providers.production`. Việc xóa bản sao đã lưu sau đó không ảnh hưởng đến liên kết đang có hiệu lực.

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production-account","auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}}';
SET "$.providers.production" = "$providers.saved";
UNSET "providers.saved";
```

### Ghi đè một trường thông tin xác thực cho phiên này {#override-a-credential-field-for-this-session}

Ngữ cảnh xác thực của một provider là đối tượng được cung cấp cho provider đó qua [`--auth`](/command-line-usage/global-flags) khi khởi động. Một đường dẫn dưới `$.auth` thay thế một trường của đối tượng đó trong phần còn lại của phiên và giữ nguyên các trường khác. Ở đây, provider GitHub được trỏ tới một biến môi trường khác chứa một token chỉ đọc; biến này được đọc từ môi trường của tiến trình StackQL, và các truy vấn sau đó đối với `github.*` dùng biến này cho đến khi trường bị hủy thiết lập hoặc phiên kết thúc.

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
```

### Liên kết một bí danh provider {#bind-a-provider-alias}

Bí danh provider là một instance thứ hai, được cấu hình độc lập, của một provider, được dùng thay cho tên provider trong truy vấn. Giá trị là một tài liệu JSON trong một chuỗi: `provider` chỉ tên provider bên dưới, `tenancy` gắn nhãn cho tài khoản, `auth` là một khối thông tin xác thực theo định dạng của bí danh (với AWS là `access_key_id_env_var` và `secret_access_key_env_var`, không phải các khóa `keyIDenvvar` và `credentialsenvvar` của `--auth`) và `params` cung cấp giá trị mặc định cho các tham số yêu cầu như `region`. Khi đó, các truy vấn đối với `production.s3.buckets` chạy với thông tin xác thực đó, trong khi `aws.s3.buckets` giữ cấu hình lúc khởi động. Danh sách đầy đủ các khóa có trong [Phiên provider](/getting-started/provider-sessions#provider-aliases).

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"}
}';

SELECT name FROM production.s3.buckets;
```
