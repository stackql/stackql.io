---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm tổng hợp MAX trong StackQL: giá trị lớn nhất trong một cột hoặc một nhóm cột."
image: "/img/stackql-featured-image.png"
---
Trả về giá trị lớn nhất dựa trên một cột đầu vào hoặc một nhóm cột.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

### Hàm tổng hợp {#aggregate-function}

__*columnExpression*__  
Một cột hoặc biểu thức, bao gồm cả toán tử (`+`, `-`, ...).

> Hàm `MAX` trả về giá trị lớn nhất của cột đã cho dựa trên kiểu dữ liệu của cột, ví dụ một thao tác `MAX` trên một cột số nguyên trả về số nguyên lớn nhất của cột hoặc nhóm cột đó, còn một thao tác `MAX` trên một cột chuỗi hoặc nhóm cột chuỗi trả về giá trị đứng cuối cùng khi sắp xếp, với chuỗi được so sánh bằng hàm đối chiếu `BINARY` (từng byte một, nên chữ hoa được xếp trước chữ thường).

__*groupByColumn*__  
Một hoặc nhiều cột được dùng để thực hiện các thao tác tóm tắt hoặc tổng hợp.  Mệnh đề `GROUP BY` trả về một hàng cho mỗi nhóm cột.

> Hàm `MAX` bỏ qua các giá trị `NULL`.

> Hàm `MAX` trả về `NULL` nếu tất cả các giá trị trong nhóm đều là `NULL`.

### Hàm vô hướng {#scalar-function}

__*scalarExpression*__  
Một danh sách cột hoặc biểu thức (từ 2 trở lên), từ đó giá trị lớn nhất được xác định và trả về. 

> Hàm vô hướng `MAX` trả về `NULL` nếu bất kỳ đối số nào là `NULL`. 

> Hàm `MAX` dạng vô hướng hoặc nhiều đối số tìm trong các đối số của nó, từ trái sang phải, một đối số có định nghĩa hàm đối chiếu và dùng hàm đối chiếu đó cho mọi phép so sánh chuỗi. 

> Nếu không có đối số nào của hàm vô hướng `MAX` định nghĩa hàm đối chiếu, hàm đối chiếu `BINARY` sẽ được dùng. 

> Nếu chỉ cung cấp một đối số, hàm tổng hợp `MAX` sẽ được gọi.

## Giá trị trả về {#return-values}

Trả về giá trị lớn nhất dựa trên kiểu dữ liệu đầu vào (hoặc nhóm).

* * *

## Ví dụ {#examples}

### Trả về giá trị lớn nhất của một cột {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Trả về giá trị lớn nhất cho mỗi giá trị trong một cột {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Trả về giá trị lớn nhất từ một danh sách giá trị bằng hàm vô hướng `max` {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Dùng `MAX` làm hàm cửa sổ để tìm giá trị lớn nhất trong các phân vùng {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) hoặc [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
