---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm tổng hợp SUM trong StackQL: tổng các giá trị khác NULL trong một cột hoặc một nhóm, trả về dạng số nguyên khi mọi đầu vào đều là số nguyên."
image: "/img/stackql-featured-image.png"
---
Trả về tổng của tất cả các giá trị khác `NULL` trong một cột hoặc một nhóm cột.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Dùng hàm [**TOTAL**](/language-spec/functions/aggregate/total) để tính tổng các số dấu phẩy động hoặc các giá trị kiểu long.

:::

## Cú pháp {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Đối số {#arguments}

__*columnExpression*__  
Một cột hoặc biểu thức.

> Nếu tất cả các giá trị trong *columnExpression* đều là `NULL`, `SUM` trả về `NULL`.  

> `SUM` báo ngoại lệ "integer overflow" nếu xảy ra tràn số nguyên tại bất kỳ thời điểm nào trong quá trình tính toán.  Hàm [`TOTAL`](/language-spec/functions/aggregate/total) không bao giờ báo lỗi tràn số nguyên.  

__*groupByColumn*__  
Một hoặc nhiều cột được dùng để thực hiện các thao tác tóm tắt hoặc tổng hợp.  Mệnh đề `GROUP BY` trả về một hàng cho mỗi nhóm cột.

## Giá trị trả về {#return-values}

Trả về một số nguyên biểu thị tổng của *columnExpression* nếu tất cả các đầu vào khác `NULL` đều là số nguyên. Nếu có đầu vào khác `NULL` nào không phải số nguyên, hàm trả về một giá trị dấu phẩy động, có thể là giá trị xấp xỉ của tổng thực.

* * *

## Ví dụ {#examples}

### Trả về tổng của một biểu thức cột trên toàn bộ một tài nguyên {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về tổng của một biểu thức cột được nhóm theo một biểu thức cột khác {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### Dùng `SUM` làm hàm cửa sổ để tính tổng lũy kế {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
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

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
