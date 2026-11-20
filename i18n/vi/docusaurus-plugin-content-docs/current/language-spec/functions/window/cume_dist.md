---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ CUME_DIST trong StackQL: phân phối tích lũy của hàng hiện tại trong phân vùng, tính bằng số thứ tự hàng chia cho số hàng của phân vùng."
image: "/img/stackql-featured-image.png"
---
Trả về phân phối tích lũy của một giá trị trong một phân vùng. Giá trị này được tính bằng `row-number / partition-rows`, trong đó `row-number` là giá trị mà `ROW_NUMBER()` trả về cho hàng ngang hàng cuối cùng trong nhóm và `partition-rows` là số hàng trong phân vùng.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Cú pháp {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `CUME_DIST` được áp dụng riêng cho từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự tính phân phối tích lũy.

## Giá trị trả về {#return-values}

Trả về một số dấu phẩy động trong khoảng từ 0.0 đến 1.0, biểu thị giá trị phân phối tích lũy.

* * *

## Ví dụ {#examples}

### Tính phân phối tích lũy của những người đóng góp {#calculate-cumulative-distribution-of-contributors}

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

`CUME_DIST` trả về tỷ lệ các hàng có giá trị nhỏ hơn hoặc bằng giá trị của hàng hiện tại. Giá trị 0.75 nghĩa là 75% số hàng có giá trị nhỏ hơn hoặc bằng giá trị của hàng hiện tại.

:::

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
