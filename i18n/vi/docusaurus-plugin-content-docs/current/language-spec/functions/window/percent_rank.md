---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ PERCENT_RANK trong StackQL: thứ hạng tương đối của hàng hiện tại trong phân vùng của nó, dưới dạng một giá trị từ 0.0 đến 1.0."
image: "/img/stackql-featured-image.png"
---
Trả về một giá trị từ 0.0 đến 1.0 biểu thị thứ hạng tương đối của hàng hiện tại trong phân vùng của nó.

Mặc dù có tên gọi như vậy, hàm này trả về một giá trị bằng `(rank - 1) / (partition-rows - 1)`, trong đó `rank` là giá trị do hàm cửa sổ tích hợp sẵn `RANK()` trả về và `partition-rows` là tổng số hàng trong phân vùng. Nếu phân vùng chỉ chứa một hàng, hàm này trả về 0.0.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Cú pháp {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `PERCENT_RANK` được áp dụng riêng cho từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự tính các thứ hạng phân vị.

## Giá trị trả về {#return-values}

Trả về một số dấu phẩy động từ 0.0 đến 1.0 biểu thị thứ hạng phân vị.

* * *

## Ví dụ {#examples}

### Tính thứ hạng phân vị của những người đóng góp {#calculate-percentile-ranking-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

Giá trị `PERCENT_RANK` bằng 0.0 cho biết hàng có thứ hạng thấp nhất, còn 1.0 cho biết hàng có thứ hạng cao nhất. Dùng hàm này để xác định vị trí của một giá trị cụ thể trong một phân phối.

:::

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
