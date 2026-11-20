---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ LAST_VALUE trong StackQL: giá trị của một biểu thức tại hàng cuối cùng của khung cửa sổ."
image: "/img/stackql-featured-image.png"
---
Trả về giá trị của biểu thức được tính trên hàng cuối cùng trong khung cửa sổ.

Hàm cửa sổ tích hợp sẵn này tính khung cửa sổ cho mỗi hàng theo cùng cách với một hàm cửa sổ tổng hợp. Với mỗi hàng, hàm trả về giá trị của biểu thức được tính trên hàng cuối cùng trong khung cửa sổ.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Cú pháp {#syntax}

```sql
SELECT LAST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expr*__
Biểu thức cần tính trên hàng cuối cùng trong khung cửa sổ.

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `LAST_VALUE` được áp dụng riêng trong từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự xử lý các hàng.

__*ROWS BETWEEN frame_start AND frame_end*__
Không bắt buộc. Định nghĩa khung cửa sổ. Các tùy chọn phổ biến gồm:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Khi không có mệnh đề khung, khung là `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: khung kết thúc tại hàng cuối cùng đồng hạng với hàng hiện tại trên các cột `ORDER BY`, không phải tại hàng hiện tại.

## Giá trị trả về {#return-values}

Trả về giá trị của biểu thức được tính trên hàng cuối cùng trong khung cửa sổ.

* * *

## Ví dụ {#examples}

### Tìm bản phát hành mới nhất để làm ngữ cảnh {#find-the-latest-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Theo mặc định, khung cửa sổ kéo dài từ đầu phân vùng đến hàng cuối cùng đồng hạng với hàng hiện tại trên các cột `ORDER BY`, vì vậy `LAST_VALUE` trả về giá trị của hàng đồng hạng cuối cùng đó (chính hàng hiện tại khi giá trị `ORDER BY` của hàng này là duy nhất), không phải giá trị cuối cùng trong phân vùng. Để lấy giá trị cuối cùng trong phân vùng, hãy dùng `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
