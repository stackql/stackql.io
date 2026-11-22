---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ LEAD trong StackQL: tính một biểu thức trên một hàng đứng sau trong phân vùng, với độ lệch và giá trị mặc định không bắt buộc."
image: "/img/stackql-featured-image.png"
---
Trả về kết quả của việc tính một biểu thức trên một hàng đứng sau trong phân vùng.

Dạng thứ nhất của `LEAD()` trả về kết quả của việc tính biểu thức trên hàng liền sau trong phân vùng. Hoặc, nếu không có hàng liền sau (vì hàng hiện tại là hàng cuối cùng), hàm trả về `NULL`.

Nếu có cung cấp đối số độ lệch, đối số này phải là một số nguyên không âm. Giá trị trả về là kết quả của việc tính biểu thức trên hàng đứng sau hàng hiện tại *offset* hàng trong phân vùng. Nếu độ lệch là 0, biểu thức được tính trên hàng hiện tại. Nếu không có hàng nào đứng sau hàng hiện tại *offset* hàng, hàm trả về `NULL`.

Nếu giá trị mặc định cũng được cung cấp, giá trị này được trả về thay cho `NULL` khi hàng được xác định bởi độ lệch không tồn tại.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Cú pháp {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expr*__
Biểu thức cần tính trên hàng đứng sau.

__*offset*__
Không bắt buộc. Một số nguyên không âm chỉ định số hàng cần tiến tới. Mặc định: 1.

__*default*__
Không bắt buộc. Giá trị cần trả về nếu hàng tại độ lệch không tồn tại. Mặc định: `NULL`.

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `LEAD` được áp dụng riêng trong từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự xử lý các hàng.

## Giá trị trả về {#return-values}

Trả về giá trị của biểu thức được tính trên hàng đứng sau đã chỉ định, hoặc `NULL` (hoặc giá trị mặc định) nếu không có hàng như vậy.

* * *

## Ví dụ {#examples}

### Nhìn tới bản phát hành tiếp theo {#look-ahead-to-the-next-release}

```sql
-- Compare each release to the previous and next release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
