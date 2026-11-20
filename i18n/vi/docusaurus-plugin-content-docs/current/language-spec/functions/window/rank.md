---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ RANK trong StackQL: thứ hạng của hàng hiện tại trong phân vùng, có khoảng trống, nên các hàng đồng hạng chung một thứ hạng và các thứ hạng kế tiếp bị bỏ qua."
image: "/img/stackql-featured-image.png"
---
Trả về thứ hạng của hàng hiện tại, có khoảng trống. Đây là `ROW_NUMBER` của hàng ngang hàng đầu tiên trong mỗi nhóm. Các hàng có giá trị bằng nhau ở các cột sắp xếp nhận cùng một thứ hạng, và giá trị thứ hạng tiếp theo được tăng thêm đúng bằng số hàng đồng hạng (để lại khoảng trống).

Nếu không có mệnh đề `ORDER BY`, mọi hàng đều được coi là hàng ngang hàng và hàm này luôn trả về 1.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Cú pháp {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `RANK` được áp dụng riêng cho từng phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự gán thứ hạng. Nếu không có mệnh đề `ORDER BY`, mọi hàng đều được coi là hàng ngang hàng và nhận thứ hạng 1.

## Giá trị trả về {#return-values}

Trả về một số nguyên biểu thị thứ hạng của hàng hiện tại trong phân vùng, có khoảng trống cho các giá trị đồng hạng.

* * *

## Ví dụ {#examples}

### Xếp hạng người đóng góp theo số lần đóng góp (có khoảng trống) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### So sánh `RANK` với `DENSE_RANK` và `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### Xếp hạng người đóng góp trong từng kho lưu trữ bằng `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
