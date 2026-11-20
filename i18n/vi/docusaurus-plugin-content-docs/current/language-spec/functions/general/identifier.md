---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - biến phiên
  - identifier
description: "Hàm IDENTIFIER trong StackQL: phân giải biến phiên chứa tên cột thành tham chiếu cột, để biến có thể chỉ định cột trong phép chiếu và ORDER BY."
image: "/img/stackql-featured-image.png"
---
Phân giải một giá trị chuỗi, thường là một biến phiên, thành một tên cột.  Sử dụng hàm này ở những vị trí mà một `$variable` đứng riêng sẽ được đọc như một giá trị vô hướng.  Có từ StackQL `v1.0` trở đi.

Xem thêm:  
[[` SET `]](/language-spec/set) [[ Biến phiên ]](/getting-started/session-variables)

* * * 

## Cú pháp {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## Đối số {#arguments}

__*variable*__  
Một biến phiên, hoặc một literal chuỗi, chứa tên cột.  Có thể dùng tên đủ điều kiện như `t.name`.  Hàm chấp nhận đúng một đối số và đối số đó phải là một giá trị chuỗi duy nhất; một biểu thức nối chuỗi như `'t.' || $column_name`, một đối số thứ hai hoặc một giá trị số đều bị từ chối với lỗi `IDENTIFIER requires one string value`.  Một giá trị rỗng, hoặc một giá trị chứa câu lệnh, bị từ chối với lỗi `invalid identifier value`, còn một biến chưa được định nghĩa sẽ báo lỗi chưa được định nghĩa.  Tất cả các bước kiểm tra này diễn ra trước khi bất kỳ yêu cầu nào được gửi tới provider.

## Giá trị trả về {#return-values}

Trong câu lệnh đã phân tích cú pháp, hàm được thay thế bằng một tham chiếu tới cột được nêu tên, vì vậy hàm trả về giá trị của cột đó.  Tên được chuyển thành định danh và không bao giờ được ghép trực tiếp vào văn bản SQL.  Tham chiếu cột thu được có thể dùng ở mọi nơi mà một cột có thể dùng: danh sách chọn, các mệnh đề `WHERE`, `GROUP BY` và `ORDER BY`, đối số hàm, cũng như bên trong CTE và truy vấn con.

* * *

## Ví dụ {#examples}

### Chiếu và sắp xếp theo cột có tên lưu trong biến {#project-and-order-by-a-column-named-in-a-variable}

Cột cần trả về và cột dùng để sắp xếp đều được lấy từ `column_name`, còn cột đầu ra được đổi tên theo `output_name`.  Cần dùng `IDENTIFIER` trong danh sách chọn và trong `ORDER BY` vì ở các vị trí đó, một `$column_name` đứng riêng sẽ là chuỗi `'name'`, không phải cột.  Còn bí danh dùng dạng đặt trong dấu ngoặc kép.  Sau khi các biến được phân giải, câu lệnh giống hệt `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name`.

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### Lọc theo cột có tên lưu trong biến {#filter-on-a-column-named-in-a-variable}

Tham chiếu đã phân giải hoạt động trong vị từ cũng như trong phép chiếu.  Ở đây, trường cần lọc và giá trị cần khớp đều lấy từ biến: `IDENTIFIER($filter_column)` trở thành cột `status`, còn `$filter_value` được truyền dưới dạng literal chuỗi `'RUNNING'`.  Hai dạng này được cố ý làm khác nhau, vì dạng thứ nhất chỉ định một cột còn dạng thứ hai cung cấp một giá trị.

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### Thêm bí danh bảng vào trước tên cột {#qualify-the-column-with-a-table-alias}

Một giá trị có dấu chấm được phân giải thành một tham chiếu cột đủ điều kiện.  Bảng được đặt bí danh là `w`, bản thân bí danh này cũng lấy từ một biến, và `IDENTIFIER($column_name)` trở thành `w.name`.  Đây là dạng nên dùng khi cùng một tên cột tồn tại ở cả hai phía của một phép nối và nếu không, tham chiếu sẽ bị mơ hồ.

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### Giá trị bị từ chối {#rejected-values}

Vì giá trị trở thành một tên chứ không phải văn bản truy vấn, mọi giá trị không phải là tên dùng được đều bị từ chối trước khi câu lệnh được lập kế hoạch, và không có yêu cầu nào tới được provider.  Hai câu lệnh đầu tiên bên dưới báo lỗi `invalid identifier value` và câu lệnh thứ ba báo lỗi `IDENTIFIER requires one string value`.

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> Một `$variable` trong mệnh đề `FROM` được phân giải thành một quan hệ mà không cần `IDENTIFIER`, còn dạng `"$variable"` đặt trong dấu ngoặc kép được dùng cho bí danh, tên cột DDL, danh sách cột của `INSERT` và đích của `UPDATE`.
