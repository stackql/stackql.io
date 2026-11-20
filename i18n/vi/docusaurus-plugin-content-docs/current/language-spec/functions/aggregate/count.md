---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm tổng hợp COUNT trong StackQL: số hàng trong một tài nguyên, hoặc số giá trị khác NULL của một cột trong một nhóm."
image: "/img/stackql-featured-image.png"
---
Trả về tổng số bản ghi trong một tài nguyên hoặc số giá trị khác `NULL` của một cột trong một nhóm.  

Xem thêm:  
[[` SELECT `]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Đối số {#arguments}

__*columnExpression*__  hoặc __*__  
Một cột hoặc biểu thức.  Nếu __*__ được chỉ định, mọi bản ghi trong tài nguyên hoặc nhóm đều được đếm.  

> Các giá trị `NULL` của *columnExpression* không được đếm.  Nếu mọi giá trị đều là `NULL`, `COUNT` trả về `0`.

__*groupByColumn*__  
Một hoặc nhiều cột được dùng để thực hiện các thao tác tóm tắt hoặc tổng hợp.  Mệnh đề `GROUP BY` trả về một hàng cho mỗi nhóm cột.

## Giá trị trả về {#return-values}

Trả về một số nguyên biểu thị kết quả đếm.

* * *

## Ví dụ {#examples}

### Trả về số hàng trong toàn bộ một tài nguyên {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về số hàng được nhóm theo một biểu thức cột {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Dùng `COUNT` làm hàm cửa sổ để tính số lượng lũy kế {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
