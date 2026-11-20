---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm tổng hợp AVG trong StackQL: giá trị trung bình của các giá trị khác NULL trong một cột hoặc một nhóm."
image: "/img/stackql-featured-image.png"
---
Trả về giá trị trung bình của tất cả các giá trị khác `NULL` trong một cột hoặc một nhóm cột.  

Xem thêm:  
[[` SELECT `]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Đối số {#arguments}

__*columnExpression*__  
Một cột hoặc biểu thức.

> Các giá trị chuỗi và BLOB không thể biểu diễn dưới dạng giá trị số sẽ được hiểu là `0`.  

> Phải có ít nhất một giá trị của *columnExpression* khác `NULL`, nếu không hàm sẽ trả về `NULL`.

__*groupByColumn*__  
Một hoặc nhiều cột được dùng để thực hiện các thao tác tóm tắt hoặc tổng hợp.  Mệnh đề `GROUP BY` trả về một hàng cho mỗi nhóm cột.

## Giá trị trả về {#return-values}

Trả về một giá trị dấu phẩy động biểu thị giá trị trung bình của *columnExpression*.

* * *

## Ví dụ {#examples}

### Trả về giá trị trung bình của một biểu thức cột trên toàn bộ một tài nguyên {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về giá trị trung bình của một biểu thức cột được nhóm theo một biểu thức cột khác {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Dùng `AVG` làm hàm cửa sổ để tính trung bình động {#use-avg-as-a-window-function-to-calculate-moving-averages}

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
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
