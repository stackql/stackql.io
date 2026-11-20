---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ DENSE_RANK trong StackQL: thứ hạng không có khoảng trống của hàng hiện tại trong phân vùng; các hàng đồng hạng có cùng thứ hạng và thứ hạng sau tiếp nối ngay."
image: "/img/stackql-featured-image.png"
---
Trả về thứ hạng của hàng hiện tại mà không có khoảng trống. Đây là số thứ tự của nhóm hàng ngang hàng chứa hàng hiện tại trong phân vùng của nó. Các hàng có giá trị bằng nhau ở các cột sắp xếp nhận cùng một thứ hạng, và giá trị thứ hạng tiếp theo luôn tăng thêm 1 (không có khoảng trống).

Các hàng được đánh số bắt đầu từ 1 theo thứ tự do mệnh đề `ORDER BY` trong định nghĩa cửa sổ xác định. Nếu không có mệnh đề `ORDER BY`, mọi hàng đều được coi là hàng ngang hàng và hàm này luôn trả về 1.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Cú pháp {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `DENSE_RANK` được áp dụng riêng cho từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự gán thứ hạng. Nếu không có mệnh đề `ORDER BY`, mọi hàng đều được coi là hàng ngang hàng và nhận thứ hạng 1.

## Giá trị trả về {#return-values}

Trả về một số nguyên biểu thị thứ hạng liên tục của hàng hiện tại trong phân vùng, không có khoảng trống sau các giá trị đồng hạng.

* * *

## Ví dụ {#examples}

### Xếp hạng người đóng góp theo số lượt đóng góp (không có khoảng trống) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### So sánh `DENSE_RANK` với `RANK` và `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Xếp hạng tổng số lượt đóng góp trên nhiều kho lưu trữ {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
