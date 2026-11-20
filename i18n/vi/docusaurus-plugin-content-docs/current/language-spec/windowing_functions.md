---
title: Hàm cửa sổ
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm cửa sổ trong StackQL: thứ hạng, tổng lũy kế, độ lệch và phân phối trên các hàng liên quan, với PARTITION BY, ORDER BY và mệnh đề khung."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Hàm cửa sổ thực hiện phép tính trên một tập hợp các hàng có liên quan đến hàng hiện tại. Khác với hàm tổng hợp trả về một kết quả duy nhất cho một nhóm hàng, hàm cửa sổ trả về một giá trị cho mỗi hàng trong khi xem xét một "cửa sổ" gồm các hàng liên quan.

Xem thêm:
[[` SELECT `]](/language-spec/select) [[` WITH (CTE) `]](/language-spec/with) [[ Tài liệu tham khảo hàm cửa sổ ]](/language-spec/functions/window/row_number)

* * *

## Cú pháp {#syntax}

Hàm cửa sổ dùng mệnh đề `OVER` để định nghĩa đặc tả cửa sổ:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Hàm cửa sổ {#window-function}

Hàm cửa sổ là các hàm chuyên biệt thực hiện phép tính trên một tập hợp các hàng do đặc tả cửa sổ xác định. Chúng có thể là hàm cửa sổ chuyên dụng (như `ROW_NUMBER` hoặc `LAG`) hoặc hàm tổng hợp (như `SUM` hoặc `COUNT`) được dùng với mệnh đề `OVER`.

```sql
{ <aggregateFunction> | ROW_NUMBER() | RANK() | DENSE_RANK() | PERCENT_RANK() 
  | CUME_DIST() | NTILE(<int>) | LAG(<expr> [,<offset> [,<default>]]) 
  | LEAD(<expr> [,<offset> [,<default>]]) | FIRST_VALUE(<expr>) 
  | LAST_VALUE(<expr>) | NTH_VALUE(<expr>, <int>) }
```  

*windowFunction::=*

<RailroadDiagram 
type="windowFunction"
/>

### Đặc tả cửa sổ {#window-specification}

Đặc tả cửa sổ xác định cách các hàng được phân vùng, sắp xếp và đóng khung cho phép tính của hàm cửa sổ. `PARTITION BY` chia các hàng thành các nhóm, `ORDER BY` xác định thứ tự trong mỗi phân vùng và mệnh đề khung không bắt buộc chỉ định những hàng nào, tương đối so với hàng hiện tại, được đưa vào phép tính.

```sql
[ <windowName> ]
[ PARTITION BY <fieldList> ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ { ROWS | RANGE | GROUPS } 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW }
  | { ROWS | RANGE | GROUPS } BETWEEN 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW | <int> FOLLOWING }
    AND
    { <int> PRECEDING | CURRENT ROW | <int> FOLLOWING | UNBOUNDED FOLLOWING } ]
```

*windowSpec::=*

<RailroadDiagram 
type="windowSpec"
/>

## Các hàm cửa sổ hiện có {#available-window-functions}

| Danh mục | Hàm |
|----------|-----------|
| **Xếp hạng** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Độ lệch** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Phân phối** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Tổng hợp** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) với mệnh đề __`OVER`__ |

* * *

## Ví dụ {#examples}

### Xếp hạng với `ROW_NUMBER`, `RANK` và `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Dùng các hàm xếp hạng để gán thứ hạng cho các hàng dựa trên giá trị cột.

```sql
-- Rank contributors by contribution count
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Tổng lũy kế và tỷ lệ phần trăm {#running-totals-and-percentages}

Có thể dùng các hàm tổng hợp như `SUM` và `COUNT` làm hàm cửa sổ để tính tổng lũy kế và tỷ lệ phần trăm.

```sql
-- Running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### So sánh các hàng với `LAG` và `LEAD` {#comparing-rows-with-lag-and-lead}

Dùng `LAG` và `LEAD` để truy cập giá trị từ các hàng trước hoặc sau.

```sql
-- Compare each release to previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Tạo nhóm với `NTILE` {#creating-groups-with-ntile}

Dùng `NTILE` để chia các hàng thành một số nhóm được chỉ định.

```sql
-- Group contributors into quartiles
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Sử dụng `PARTITION BY` để phân tích theo nhóm {#using-partition-by-for-grouped-analysis}

Dùng `PARTITION BY` để thực hiện phép tính cửa sổ trong từng nhóm.

```sql
-- Track cumulative issues by state
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Sử dụng khung cửa sổ {#using-window-frames}

Chỉ định một mệnh đề khung để kiểm soát những hàng nào được đưa vào phép tính cửa sổ.

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg,
    SUM(commits_this_week) OVER (ORDER BY week) as cumulative_commits
FROM weekly_totals
ORDER BY week;
```

Để biết tài liệu chi tiết về từng hàm cửa sổ, hãy xem [Tài liệu tham khảo hàm cửa sổ](/language-spec/functions/window/row_number).

Để biết thêm thông tin, hãy xem [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
