---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính arcsin hyperbolic (sin hyperbolic nghịch đảo) của một số bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính arcsin hyperbolic (sin hyperbolic nghịch đảo) của một số, tức là giá trị có sin hyperbolic bằng số đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính arcsin hyperbolic.

## Giá trị trả về {#return-values}
Trả về arcsin hyperbolic của giá trị đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính arcsin hyperbolic của 0.5 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

Truy vấn này trả về xấp xỉ `0.4812`, vì arcsin hyperbolic của 0.5 vào khoảng 0.4812.

### Tính arcsin hyperbolic của -1 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

Truy vấn này tính arcsin hyperbolic của -1, cho kết quả xấp xỉ `-0.8814`, vì đây là giá trị có sin hyperbolic bằng -1.
