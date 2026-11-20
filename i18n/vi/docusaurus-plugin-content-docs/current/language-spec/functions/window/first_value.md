---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ FIRST_VALUE trong StackQL: giá trị của một biểu thức tại hàng đầu tiên của khung cửa sổ."
image: "/img/stackql-featured-image.png"
---
Trả về giá trị của biểu thức được tính trên hàng đầu tiên trong khung cửa sổ.

Hàm cửa sổ tích hợp sẵn này tính khung cửa sổ cho mỗi hàng theo cùng cách với một hàm cửa sổ tổng hợp. Với mỗi hàng, hàm trả về giá trị của biểu thức được tính trên hàng đầu tiên trong khung cửa sổ.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Cú pháp {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expr*__
Biểu thức cần tính trên hàng đầu tiên trong khung cửa sổ.

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `FIRST_VALUE` được áp dụng riêng trong từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự xử lý các hàng.

__*ROWS BETWEEN frame_start AND frame_end*__
Không bắt buộc. Định nghĩa khung cửa sổ. Các tùy chọn phổ biến gồm:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Khi không có mệnh đề khung, khung là `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: khung kết thúc tại hàng cuối cùng đồng hạng với hàng hiện tại trên các cột `ORDER BY`, không phải tại hàng hiện tại.

## Giá trị trả về {#return-values}

Trả về giá trị của biểu thức được tính trên hàng đầu tiên trong khung cửa sổ.

* * *

## Ví dụ {#examples}

### Tìm bản phát hành đầu tiên để làm ngữ cảnh {#find-the-first-release-for-context}

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

Theo mặc định, khung cửa sổ kéo dài từ đầu phân vùng đến hàng cuối cùng đồng hạng với hàng hiện tại trên các cột `ORDER BY`. `FIRST_VALUE` đọc hàng đầu tiên của khung, vì vậy hàm trả về cùng một giá trị với khung mặc định và với `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`; khung được chỉ định rõ ràng có ý nghĩa đối với `LAST_VALUE` và `NTH_VALUE`, là các hàm đọc sâu hơn vào khung.

:::

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
