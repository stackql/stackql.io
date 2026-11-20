---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính arctan của hai số (y/x), trả về một góc theo radian, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính arctan của hai số, cụ thể là góc tính bằng radian có tang bằng tỷ số của hai số đã chỉ định (y chia cho x), đồng thời xử lý đúng dấu của các đầu vào để xác định đúng góc phần tư.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*y*__  
Tọa độ y (tử số) của điểm.

__*x*__  
Tọa độ x (mẫu số) của điểm.

## Giá trị trả về {#return-values}
Trả về arctan của y/x tính bằng radian, có tính đến góc phần tư đúng của góc.

* * *

## Ví dụ {#examples}

### Tính arctan của điểm (1,1) {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

Truy vấn này trả về π/4 hay xấp xỉ `0.785`, vì arctan của điểm (1,1) tương ứng với góc π/4 radian trong góc phần tư thứ nhất.

### Tính arctan của điểm (-10, 10) {#calculate-the-arc-tangent-of-the-point--10-10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

Truy vấn này tính góc của điểm (-10, 10), với y = 10 và x = -10, cho kết quả 3π/4 hay xấp xỉ `2.356`: tang của góc này bằng -1 và điểm nằm trong góc phần tư thứ hai.
