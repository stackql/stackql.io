---
title: UNSET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - biến phiên
  - unset
  - phiên provider
  - bí mật
description: "UNSET trong StackQL: xóa biến phiên, hoặc xóa bí danh provider, trường thông tin xác thực và bí mật đã cấu hình bằng SET cho phiên hiện tại."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Xóa một biến phiên, hoặc xóa cấu hình runtime đã đặt bằng [`SET`](/language-spec/set) cho phiên hiện tại.  Có từ StackQL `v1.0` trở đi.

Xem thêm:  
[[` SET `]](/language-spec/set) [[ Biến phiên ]](/getting-started/session-variables) [[ Phiên provider ]](/getting-started/provider-sessions)

* * * 

## Cú pháp {#syntax}

*unsetStatement::=*

<RailroadDiagram
type="unset"
/>

&nbsp;  
&nbsp;  

```sql
UNSET [ SESSION ] <target> [, <target> ...];
```

__*target*__  
Bất kỳ đích nào mà `SET` chấp nhận: một tên biến thông thường hoặc đặt trong dấu ngoặc kép, hoặc một đường dẫn cấu hình đặt trong dấu ngoặc kép và có tiền tố dấu đô la.

__*SESSION*__  
Không bắt buộc, và là phạm vi duy nhất hiện có: `UNSET SESSION name` và `UNSET name` là cùng một câu lệnh, vì mọi biến và thiết lập cấu hình đều thuộc về phiên hiện tại.  `UNSET GLOBAL` bị từ chối với lỗi `configuration scope "global" is not supported; use SESSION`.

`UNSET name` tương đương với `SET name = DEFAULT`.  Các đích trong một danh sách được xóa theo thứ tự, và việc xóa một tên chưa được đặt không phải là lỗi.  Việc giải tham chiếu một biến sau khi biến đó đã được xóa sẽ thất bại trước khi bất kỳ yêu cầu nào tới provider được gửi đi.

| Đích | Tác dụng |
|--|--|
| `name` | Xóa biến phiên. |
| `"$.auth.<provider>.<field>"` | Xóa trường đó khỏi ngữ cảnh xác thực phiên của provider.  Giá trị có hiệu lực trước khi ghi đè không được khôi phục. |
| `"$.providers.<alias>"` | Xóa liên kết bí danh provider. |
| `"$.secrets.<name>"` | Xóa bí mật khỏi kho của phiên.  Với một kho lưu trữ lâu dài của hệ điều hành, thao tác này xóa giá trị đã lưu. |
| `"$.secret_store"` | Hủy liên kết kho hiện tại và bắt đầu với bộ nhớ phiên trống.  Các giá trị đã được lưu lâu dài trong kho của hệ điều hành không bị xóa. |

* * *

## Ví dụ {#examples}

### Xóa một biến {#remove-a-variable}

Biến được xóa khỏi phiên, nên truy vấn trong khối chú thích sẽ thất bại với lỗi `variable "$region" is not defined` thay vì chạy với một giá trị lỗi thời hoặc trống.  Đặt lại tên này để dùng lại.

```sql
SET region = 'ap-southeast-2';
UNSET region;

/*
SELECT name FROM aws.s3.buckets WHERE region = $region;
-- FAILS with variable "$region" is not defined
*/
```

### Xóa nhiều đích {#remove-several-targets}

Một câu lệnh có thể xóa cùng lúc cả biến và đường dẫn cấu hình, và các đích được xử lý theo thứ tự.  Ở đây, một biến thông thường, một biến có tên chứa dấu chấm và một ghi đè thông tin xác thực được xóa trong cùng một danh sách.

```sql
UNSET region, "providers.saved", "$.auth.github.password_var";
```

### Ghi rõ phạm vi phiên {#make-the-session-scope-explicit}

Hai câu lệnh này có cùng tác dụng: `SESSION` chỉ định phạm vi duy nhất hiện có.  Dạng tường minh phù hợp với các tập lệnh cũng chạy trên những cơ sở dữ liệu phân biệt trạng thái phiên với trạng thái toàn cục.  Câu lệnh trong khối chú thích thất bại vì không tồn tại trạng thái dùng chung nào.

```sql
UNSET region;
UNSET SESSION region;

/*
UNSET GLOBAL region;
-- FAILS with configuration scope "global" is not supported; use SESSION
*/
```

### Xóa một ghi đè thông tin xác thực {#clear-a-credential-override}

Trường đã đặt trước đó bằng `SET "$.auth.github.password_var"` được xóa khỏi ngữ cảnh xác thực phiên của provider GitHub, và các trường khác của ngữ cảnh đó không bị ảnh hưởng.  Giá trị có hiệu lực trước khi ghi đè không được khôi phục, vì vậy hãy đặt lại trường này nếu provider vẫn cần nó.

```sql
UNSET "$.auth.github.password_var";
```

### Xóa một bí danh provider và bí mật của nó {#remove-a-provider-alias-and-its-secret}

Bí danh `production` đã được liên kết bằng `SET "$.providers.production"` và tham chiếu, thông qua `auth_secret`, tới một bí mật được lưu tại `$.secrets.production`.  Việc xóa cả hai trong một câu lệnh sẽ hủy liên kết, nên `production.s3.buckets` không còn là một quan hệ hợp lệ, đồng thời xóa bí mật khỏi kho của phiên.  `SESSION` ở đây là không bắt buộc, như ở mọi nơi khác.

```sql
UNSET SESSION "$.providers.production", "$.secrets.production";
```
