---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính arctan hyperbolic (tang hyperbolic nghịch đảo) của một số bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính arctan hyperbolic (tang hyperbolic nghịch đảo) của một số, tức là giá trị có tang hyperbolic bằng số đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính arctan hyperbolic, có giá trị trong khoảng từ -1 đến 1 (không bao gồm hai đầu).

## Giá trị trả về {#return-values}
Trả về arctan hyperbolic của giá trị đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính arctan hyperbolic của 0.5 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

Truy vấn này trả về xấp xỉ `0.5493`, vì arctan hyperbolic của 0.5 vào khoảng 0.5493.

### Tính arctan hyperbolic của -0.5 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

Truy vấn này tính arctan hyperbolic của -0.5, cho kết quả xấp xỉ `-0.5493`, vì đây là giá trị có tang hyperbolic bằng -0.5.
