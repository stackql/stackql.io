---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ NTH_VALUE trong StackQL: giá trị của một biểu thức tại hàng thứ N của khung cửa sổ."
image: "/img/stackql-featured-image.png"
---
Trả về giá trị của biểu thức được tính trên hàng thứ N trong khung cửa sổ.

Hàm cửa sổ tích hợp sẵn này tính khung cửa sổ cho mỗi hàng theo cùng cách với một hàm cửa sổ tổng hợp. Hàm trả về giá trị của biểu thức được tính trên hàng N của khung cửa sổ. Các hàng trong khung cửa sổ được đánh số bắt đầu từ 1 theo thứ tự do mệnh đề `ORDER BY` xác định nếu có, hoặc theo thứ tự tùy ý nếu không có. Nếu không có hàng thứ N trong phân vùng, hàm trả về `NULL`.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## Cú pháp {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expr*__
Biểu thức cần tính trên hàng thứ N trong khung cửa sổ.

__*N*__
Một số nguyên dương chỉ định số thứ tự của hàng cần lấy (đánh số từ 1).

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `NTH_VALUE` được áp dụng riêng trong từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự xử lý các hàng.

__*ROWS BETWEEN frame_start AND frame_end*__
Không bắt buộc. Định nghĩa khung cửa sổ.

## Giá trị trả về {#return-values}

Trả về giá trị của biểu thức được tính trên hàng thứ N trong khung cửa sổ, hoặc `NULL` nếu hàng thứ N không tồn tại.

* * *

## Ví dụ {#examples}

### Lấy ba người đóng góp hàng đầu {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
