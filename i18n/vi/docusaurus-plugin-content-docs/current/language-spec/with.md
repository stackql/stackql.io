---
title: WITH (CTE)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "WITH trong StackQL: định nghĩa biểu thức bảng chung (CTE), tức các tập kết quả tạm thời có tên, có phạm vi trong câu lệnh SELECT theo sau chúng."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Định nghĩa một biểu thức bảng chung (CTE) để dùng trong câu lệnh `SELECT` theo sau nó. CTE hoạt động như các tập kết quả tạm thời có tên, chỉ tồn tại trong suốt thời gian thực hiện truy vấn.

Xem thêm:
[[` SELECT `]](/language-spec/select)

* * *

## Cú pháp {#syntax}

*withClause::=*

<RailroadDiagram
type="withClause"
/>

*cteDefinition::=*

<RailroadDiagram
type="cteDefinition"
/>

&nbsp;
&nbsp;

```sql
WITH [RECURSIVE] cte_name [(column_name, ...)] AS (
    select_statement
)
[, cte_name [(column_name, ...)] AS (
    select_statement
)]
SELECT ... FROM cte_name ...;
```

## Đối số {#arguments}

__*RECURSIVE*__  
Từ khóa không bắt buộc cho phép CTE tham chiếu đến chính nó, giúp thực hiện các truy vấn đệ quy như duyệt dữ liệu phân cấp.

__*cte_name*__  
Tên được gán cho CTE. Có thể tham chiếu tên này trong truy vấn chính như thể đó là một bảng.

__*column_name*__  
Không bắt buộc. Tên cột cho tập kết quả của CTE. Nếu không được chỉ định, tên cột được lấy từ câu lệnh SELECT.

__*select_statement*__  
Truy vấn định nghĩa tập kết quả của CTE.

## Giá trị trả về {#return-values}

CTE không trả về giá trị trực tiếp. Chúng định nghĩa các tập kết quả tạm thời có thể được tham chiếu trong truy vấn chính.

* * *

## Ví dụ {#examples}

### CTE cơ bản để tổ chức truy vấn phức tạp {#basic-cte-for-organizing-complex-queries}

CTE có thể đơn giản hóa truy vấn phức tạp bằng cách chia chúng thành các thành phần logic có tên.

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

### Kết hợp dữ liệu từ nhiều nguồn với `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

CTE hữu ích để kết hợp dữ liệu từ nhiều nguồn trước khi thực hiện phân tích.

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

### Sử dụng CTE với hàm cửa sổ để xếp hạng {#using-ctes-with-window-functions-for-ranking}

```sql
-- Rank contributors within each repo using CTE and window functions
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo,
        SUM(contributions) OVER (PARTITION BY repo) as repo_total_contributions
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

### Nhiều CTE trong một truy vấn {#multiple-ctes-in-a-single-query}

Bạn có thể định nghĩa nhiều CTE, phân cách bằng dấu phẩy.

```sql
-- Analyze issues and releases together
WITH open_issues AS (
    SELECT number, title, created_at
    FROM github.issues.issues
    WHERE owner = 'stackql'
      AND repo = 'stackql'
      AND state = 'open'
),
recent_releases AS (
    SELECT tag_name, published_at
    FROM github.repos.releases
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    ORDER BY published_at DESC
    LIMIT 5
)
SELECT
    'Open Issues' as category,
    COUNT(*) as count
FROM open_issues
UNION ALL
SELECT
    'Recent Releases' as category,
    COUNT(*) as count
FROM recent_releases;
```

## CTE đệ quy {#recursive-ctes}

CTE đệ quy cho phép một CTE tham chiếu đến chính nó, giúp thực hiện các truy vấn duyệt cấu trúc dữ liệu phân cấp hoặc dạng đồ thị.

```sql
WITH RECURSIVE cte_name (columns) AS (
    -- Base case (anchor member)
    SELECT ...
    UNION ALL
    -- Recursive case (references cte_name)
    SELECT ... FROM cte_name WHERE ...
)
SELECT * FROM cte_name;
```

:::tip

CTE đệ quy đặc biệt hữu ích cho dữ liệu phân cấp như sơ đồ tổ chức, hệ thống tệp hoặc cây phụ thuộc. Luôn đưa một điều kiện dừng vào trường hợp đệ quy để tránh vòng lặp vô hạn.

:::

## Phương pháp hay nhất {#best-practices}

1. **Dùng CTE để dễ đọc hơn**: Chia truy vấn phức tạp thành các thành phần logic có tên.
2. **Kết hợp với hàm cửa sổ**: CTE hoạt động tốt với hàm cửa sổ cho các phân tích nâng cao.
3. **Dùng nhiều CTE nối tiếp nhau**: Tham chiếu các CTE trước trong các CTE sau để từng bước tạo nên logic phức tạp.
4. **Cân nhắc hiệu năng**: Mặc dù CTE giúp truy vấn dễ đọc hơn, các CTE rất lớn có thể ảnh hưởng đến hiệu năng. Hãy kiểm thử với khối lượng dữ liệu thực tế của bạn.

Để biết thêm thông tin, hãy xem [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
