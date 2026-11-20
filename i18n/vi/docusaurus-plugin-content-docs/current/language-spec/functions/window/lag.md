---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ LAG trong StackQL: tính một biểu thức trên một hàng đứng trước trong phân vùng, với độ lệch và giá trị mặc định không bắt buộc."
image: "/img/stackql-featured-image.png"
---
Trả về kết quả của việc tính một biểu thức trên một hàng đứng trước trong phân vùng.

Dạng thứ nhất của `LAG()` trả về kết quả của việc tính biểu thức trên hàng liền trước trong phân vùng. Hoặc, nếu không có hàng liền trước (vì hàng hiện tại là hàng đầu tiên), hàm trả về `NULL`.

Nếu có cung cấp đối số độ lệch, đối số này phải là một số nguyên không âm. Giá trị trả về là kết quả của việc tính biểu thức trên hàng đứng trước hàng hiện tại *offset* hàng trong phân vùng. Nếu độ lệch là 0, biểu thức được tính trên hàng hiện tại. Nếu không có hàng nào đứng trước hàng hiện tại *offset* hàng, hàm trả về `NULL`.

Nếu giá trị mặc định cũng được cung cấp, giá trị này được trả về thay cho `NULL` khi hàng được xác định bởi độ lệch không tồn tại.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Cú pháp {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expr*__
Biểu thức cần tính trên hàng đứng trước.

__*offset*__
Không bắt buộc. Một số nguyên không âm chỉ định số hàng cần lùi lại. Mặc định: 1.

__*default*__
Không bắt buộc. Giá trị cần trả về nếu hàng tại độ lệch không tồn tại. Mặc định: `NULL`.

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `LAG` được áp dụng riêng trong từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự xử lý các hàng.

## Giá trị trả về {#return-values}

Trả về giá trị của biểu thức được tính trên hàng đứng trước đã chỉ định, hoặc `NULL` (hoặc giá trị mặc định) nếu không có hàng như vậy.

* * *

## Ví dụ {#examples}

### So sánh mỗi bản phát hành với bản phát hành trước đó {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to the previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Phân tích khoảng cách giữa các lần tạo issue {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
