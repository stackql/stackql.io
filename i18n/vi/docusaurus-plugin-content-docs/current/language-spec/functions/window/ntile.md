---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ NTILE trong StackQL: chia một phân vùng thành N nhóm đều nhất có thể và đánh số nhóm của mỗi hàng từ 1 đến N."
image: "/img/stackql-featured-image.png"
---
Chia phân vùng thành N nhóm đều nhất có thể và gán một số nguyên từ 1 đến N cho mỗi nhóm, theo thứ tự do mệnh đề `ORDER BY` xác định, hoặc theo thứ tự tùy ý nếu không có. Nếu cần, các nhóm lớn hơn xuất hiện trước.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Cú pháp {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*N*__
Một số nguyên dương chỉ định số nhóm (tile) mà phân vùng được chia thành.

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `NTILE` được áp dụng riêng cho từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự gán các hàng vào các nhóm.

## Giá trị trả về {#return-values}

Trả về một số nguyên từ 1 đến N cho biết hàng hiện tại thuộc tile (nhóm) nào.

* * *

## Ví dụ {#examples}

### Chia người đóng góp thành các tứ phân vị theo mức độ đóng góp {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Dùng `NTILE` để tạo các phân khúc hoặc cấp bậc trong dữ liệu của bạn. Các trường hợp sử dụng phổ biến gồm chia dữ liệu thành tứ phân vị (N=4), thập phân vị (N=10) hoặc bách phân vị (N=100).

:::

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
