---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ ROW_NUMBER trong StackQL: đánh số các hàng trong một phân vùng từ 1 theo thứ tự do mệnh đề ORDER BY của cửa sổ xác định."
image: "/img/stackql-featured-image.png"
---
Trả về số thứ tự của hàng trong phân vùng hiện tại. Các hàng được đánh số bắt đầu từ 1 theo thứ tự do mệnh đề `ORDER BY` trong định nghĩa cửa sổ xác định, hoặc theo thứ tự tùy ý nếu không có mệnh đề đó.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Cú pháp {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*PARTITION BY column*__
Không bắt buộc. Chia tập kết quả thành các phân vùng. Hàm `ROW_NUMBER` được áp dụng riêng cho từng phân vùng và đặt lại số thứ tự hàng cho mỗi phân vùng.

__*ORDER BY column*__
Chỉ định thứ tự gán số thứ tự hàng. Cần thiết để có kết quả có ý nghĩa.

## Giá trị trả về {#return-values}

Trả về một số nguyên biểu thị số thứ tự liên tiếp của hàng trong phân vùng, bắt đầu từ 1.

* * *

## Ví dụ {#examples}

### Xếp hạng người đóng góp trong một kho lưu trữ {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Xác định trình tự issue theo thời gian {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### So sánh `ROW_NUMBER` với `RANK` và `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
