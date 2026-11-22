---
title: Phiên provider và bí mật
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - phiên provider
  - đa tài khoản
  - thông tin xác thực
  - bí mật
  - keychain
description: "Phiên provider trong StackQL: bí danh với thông tin xác thực, tenancy và tham số mặc định riêng, ghi đè trường xác thực và lưu bí mật trong phiên hoặc kho của hệ điều hành."
image: "/img/stackql-featured-image.png"
---

Xem thêm:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Biến phiên ]](/getting-started/session-variables) [[ Cờ toàn cục ]](/command-line-usage/global-flags)

Một phiên có thể liên kết một provider dưới một bí danh với thông tin xác thực, nhãn tenancy, giá trị mặc định cho tham số và giới hạn số yêu cầu đồng thời riêng, nhờ đó có thể truy vấn nhiều tài khoản hoặc tenant của cùng một provider trong một phiên, và trong một câu lệnh.  Các trường thông tin xác thực cũng có thể được ghi đè cho phiên, và bí mật có thể được lưu trong bộ nhớ phiên hoặc, khi người vận hành chủ động bật, trong kho bí mật của hệ điều hành.  Tất cả những điều này được cấu hình bằng [`SET`](/language-spec/set) và [`UNSET`](/language-spec/unset) và có từ StackQL `v1.0` trở đi.

Mọi thứ được mô tả ở đây đều có phạm vi là phiên hiện tại.  Các kết nối độc lập đến một máy chủ `stackql srv` không dùng chung liên kết hoặc bí mật, và mỗi yêu cầu MCP là một phiên riêng, vì vậy hãy cấu hình và truy vấn trong cùng một lô câu lệnh khi dùng máy chủ MCP.

## Bí danh provider {#provider-aliases}

Câu lệnh `SET "$.providers.<alias>"` liên kết một bí danh với một provider.  Giá trị là một tài liệu JSON được lưu trong một chuỗi:

| Khóa | Bắt buộc | Mô tả |
|--|--|--|
| `provider` | Có | Provider chuẩn tắc mà bí danh được phân giải thành, ví dụ `aws`. |
| `tenancy` | Có | Một nhãn nhóm như tên tài khoản hoặc tên tenant.  StackQL không đối chiếu nhãn này với provider. |
| `auth` | Một trong `auth` hoặc `auth_secret` | Một tài liệu xác thực có cùng dạng với mục của provider trong đối tượng [`--auth`](/command-line-usage/global-flags).  Bí danh không mượn thông tin xác thực của provider chuẩn tắc. |
| `auth_secret` | Một trong `auth` hoặc `auth_secret` | Tên của một [bí mật phiên](#session-secrets) chứa tài liệu xác thực. |
| `params` | Không | Giá trị mặc định cho các tham số provider đã khai báo, bao gồm cả các tham số máy chủ đã khai báo.  Một vị từ trong truy vấn sẽ ghi đè giá trị mặc định. |
| `tuning` | Không | Giới hạn `max_concurrent` và `max_per_host` cho các yêu cầu của bí danh.  Các bí danh dùng chung provider và tenancy sẽ dùng chung các cơ chế kiểm soát này và phải khai báo thiết lập tuning giống hệt nhau. |
<br/>
Ví dụ sau liên kết hai tài khoản AWS dưới các bí danh riêng, mỗi bí danh đọc khóa của nó từ các biến môi trường có tên khác nhau, và truy vấn cả hai trong một câu lệnh:

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

Các tham chiếu biến môi trường được phân giải từ môi trường của tiến trình StackQL; không có môi trường cục bộ riêng cho phiên.  Thông tin xác thực AWS tạm thời cũng cần trường token phiên hoặc tham chiếu biến môi trường tương ứng.

Một bí danh hoạt động giống provider của nó đối với các câu lệnh siêu dữ liệu và câu lệnh dữ liệu: `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE` và `EXEC` đều chấp nhận bí danh thay cho tên provider.  Bí danh không áp dụng cho các quan hệ tích hợp sẵn gốc hoặc các quan hệ IaC đang ở giai đoạn xem trước.  Các nhánh `UNION ALL` dùng các bí danh khác nhau sẽ chạy tuần tự.

Mỗi truy vấn lấy một ảnh chụp nhanh tách rời của thông tin xác thực của bí danh.  Việc thay đổi một liên kết ảnh hưởng đến các truy vấn sau đó, không ảnh hưởng đến truy vấn đang chạy.  `UNSET "$.providers.<alias>"` xóa liên kết.

## Ghi đè trường thông tin xác thực {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` thay đổi một trường trong ngữ cảnh xác thực của một provider cho phiên, trong đó tên trường là các khóa trong mục `--auth` của provider:

```sql
SET "$.auth.github.password_var" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.password_var";
```

Giống như với bí danh, thay đổi áp dụng cho các truy vấn bắt đầu sau `SET`.  Các truy vấn đối với một provider có phần xác thực đã bị hủy thiết lập, hoặc có bí danh không có `auth` dùng được, sẽ thất bại trước khi bất kỳ yêu cầu nào được gửi đi.

## Bí mật phiên {#session-secrets}

Theo mặc định, mọi phiên đều có một kho bí mật chỉ nằm trong bộ nhớ, không cần cấu hình.  `SET "$.secrets.<name>"` lưu một chuỗi literal, thường là một tài liệu xác thực JSON, và một bí danh provider tham chiếu đến chuỗi đó bằng `auth_secret`:

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

Việc xoay vòng một bí mật ảnh hưởng đến các truy vấn sau đó; việc xóa bí mật khiến các lần dùng sau thất bại.  Một phiên mới nhận được một kho mới và trống, và việc kết thúc phiên sẽ giải phóng kho đó.  `UNSET "$.secret_store"` hủy liên kết kho hiện tại và bắt đầu lại với bộ nhớ phiên trống.

Việc sử dụng thông tin xác thực thông thường, và các câu lệnh `SET` lưu biến hoặc cấu hình, không ghi bí mật vào backend SQL.  Việc đưa dữ liệu nhạy cảm vào một biến hoặc bảng thông thường là trách nhiệm của bên gọi.

## Kho bí mật bền vững của hệ điều hành {#persistent-os-secret-stores}

Lưu trữ bền vững tắt theo mặc định và chỉ người vận hành mới có thể bật bằng cách khởi động StackQL với `--allow-persistent-secrets`; SQL không thể trao quyền này.  Khi đã đặt cờ, một phiên chọn kho của hệ điều hành bằng:

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

Kho đó là macOS Keychain, Linux Secret Service (`secret-tool` với một dịch vụ phiên đang hoạt động) hoặc Windows Credential Manager.  Chỉ backend `os` được chấp nhận: kho dạng tệp phẳng, đường dẫn tệp và mọi fallback ngầm định sang tệp đều bị từ chối ngay cả khi đã đặt cờ, và khi tiện ích của hệ điều hành không khả dụng thì thao tác sẽ thất bại thay vì chuyển sang dùng tệp.

Các giá trị được ghi vào kho của hệ điều hành tồn tại lâu hơn phiên SQL và được xóa một cách rõ ràng bằng `UNSET "$.secrets.<name>"`.  Việc đặt lại liên kết kho không xóa các giá trị đã được lưu bền vững.  Quyền truy cập kho được quản lý bởi danh tính hệ điều hành của tiến trình StackQL; một không gian tên không phải là cơ chế kiểm soát truy cập theo từng người dùng, vì vậy đừng coi tùy chọn chủ động bật này là một kho bảo mật nhiều người dùng.

## Giá trị bí mật trong SQL {#secret-values-in-sql}

Giá trị bí mật không bao giờ được giải tham chiếu vào SQL trừ khi người vận hành khởi động StackQL với `--allow-secret-dereference`.  Với cờ đó, các tham chiếu như `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"` và `"$providers.production.auth.credentials"` được phân giải trong biểu thức:

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

Khi khởi động không có cờ này, cùng câu lệnh đó bị từ chối trước khi bất cứ thứ gì được đánh giá, như bản sao được đặt trong chú thích cho thấy.  Cờ này là sự thừa nhận rằng các giá trị được giải tham chiếu một cách rõ ràng có thể đi vào vùng staging của SQL, kết quả, nhật ký và các artifact truy vấn được lưu bền vững.  Xác thực provider thông thường không cần cờ này.

## Quyền {#permissions}

Khi [quản lý truy cập](/command-line-usage/access-management) được bật, việc đọc biến và bí mật cần quyền `session/variables:read`, còn `SET` hoặc `UNSET` cần quyền `session/config:write`.  Một bí danh đã phân giải được ủy quyền như provider chuẩn tắc của nó trước khi thực thi.  Chưa có quyền sở hữu và việc trao quyền theo từng biến và từng bí mật.
