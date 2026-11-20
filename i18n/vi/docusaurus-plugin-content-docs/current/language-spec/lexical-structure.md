---
title: Cấu trúc từ vựng
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Cấu trúc từ vựng của StackQL: cách trình phân tích cú pháp tách câu lệnh, định danh, dấu ngoặc, literal, chú thích và khoảng trắng thành token từ vựng."
image: "/img/stackql-featured-image.png"
---

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy) [[ Từ khóa ]](/language-spec/keywords) [[ Kiểu dữ liệu ]](/language-spec/data-types) [[ DESCRIBE ]](/language-spec/describe)


Cấu trúc từ vựng và ngữ pháp của StackQL được mô phỏng theo cách hiện thực ngôn ngữ của MySQL (một biến thể của tiêu chuẩn ANSI SQL-92) với một số phần mở rộng. Câu lệnh StackQL được dùng để truy vấn tài nguyên đám mây (bằng ngữ pháp `SELECT`) hoặc để tạo, sửa đổi hoặc xóa hạ tầng bằng các câu lệnh Ngôn ngữ thao tác dữ liệu (DML) tiêu chuẩn như `INSERT`, `UPDATE`, `REPLACE` và `DELETE`. Ngoài ra, các thao tác không thuộc DML, chẳng hạn khởi động một instance, hoặc lấy hay đặt một đối tượng vào một bucket trong AWS S3, được hiện thực dưới dạng các thủ tục provider tích hợp sẵn (tương tự thủ tục lưu trữ trong DBMS). Các thủ tục provider tích hợp sẵn được gọi bằng câu lệnh `EXEC`.

Ngôn ngữ SQL chuyên biệt theo miền của StackQL cho phép lập trình hạ tầng theo cả kiểu khai báo lẫn kiểu thủ tục.

* * * 

## Cú pháp {#syntax}
Các thao tác StackQL gồm một hoặc nhiều câu lệnh kết thúc bằng dấu chấm phẩy (`;`). Câu lệnh bao gồm từ khóa, định danh, tham số và giá trị; ví dụ, một câu lệnh StackQL dùng để tạo một ổ đĩa 10GB trên GCP sẽ như sau:

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb)
SELECT 'myProject', 'australia-southeast1-a', 'my10GbDisk', 10;
```

Trong ví dụ trên, _`INSERT`_, _`INTO`_ và _`SELECT`_ là từ khóa, _google.compute.disks_ là định danh, _project_, _zone_, _name_, _sizeGb_ là tham số và _'myProject'_, _'australia-southeast1-a'_, _'my10GbDisk'_, _10_ là giá trị.

## Từ khóa {#keywords}
Từ khóa không phân biệt chữ hoa chữ thường, nghĩa là `INSERT` và `insert` được xử lý như nhau, tuy nhiên theo quy ước chúng thường được viết hoa. Danh sách đầy đủ các từ khóa có trong phần Từ khóa.

## Định danh {#identifiers}
Định danh là các ký hiệu biểu thị đối tượng trong phân cấp tài nguyên của nhà cung cấp đám mây. Định danh gồm nhiều phần; bạn có thể tìm thêm thông tin về định danh trong chủ đề Phân cấp tài nguyên StackQL.

## Tham số và giá trị {#parameters-and-values}
Tham số do tài nguyên quy định; bạn có thể tìm chúng bằng câu lệnh `DESCRIBE` cho một tài nguyên cụ thể của nhà cung cấp đám mây. Giá trị là các đối tượng vô hướng hoặc phức hợp có kiểu, được ánh xạ tới tham số. Để biết thêm thông tin về tham số và giá trị, hãy xem các chủ đề `DESCRIBE` và Kiểu dữ liệu.

## Biến {#variables}
Một định danh có tiền tố đô la như `$region` giải tham chiếu một biến phiên được gán bằng [`SET`](/language-spec/set); định danh này được thay bằng giá trị có kiểu của biến trước khi câu lệnh được lập kế hoạch. `'$region'` đặt trong dấu nháy đơn là một literal chuỗi. Trong mệnh đề `FROM`, `$variable` được phân giải thành tên quan hệ, còn `IDENTIFIER($variable)` được phân giải thành tên cột ở các vị trí biểu thức. Một đường dẫn có tiền tố đô la đặt trong dấu ngoặc kép như `"$.auth.aws.password"` là một đích cấu hình runtime mà `SET` và `UNSET` chấp nhận. Biến phiên có từ StackQL `v1.0` trở đi, xem [Biến phiên](/getting-started/session-variables).

## Toán tử {#operators}
`||` là toán tử nối chuỗi và có độ ưu tiên cao hơn các toán tử so sánh. `OR` là phép tuyển boolean. Các bản phát hành StackQL trước `v1.0` phân tách `||` thành token `OR`.

## Chú thích {#comments}
StackQL hỗ trợ chú thích nhiều dòng và chú thích trên cùng dòng trong câu lệnh StackQL; ví dụ về chú thích được trình bày dưới đây:

```sql
/* This
Is a
Multiline
Comment
*/

--This is an inline comment
```
