---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính arccos hyperbolic (cosin hyperbolic nghịch đảo) của một số bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính arccos hyperbolic (cosin hyperbolic nghịch đảo) của một số, tức là giá trị không âm có cosin hyperbolic bằng số đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số lớn hơn hoặc bằng 1, cần tính arccos hyperbolic.

## Giá trị trả về {#return-values}
Trả về arccos hyperbolic của giá trị đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính arccos hyperbolic của 1 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

Truy vấn này trả về `0`, vì arccos hyperbolic của 1 là 0.

### Tính arccos hyperbolic của 10 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

Truy vấn này tính arccos hyperbolic của 10, cho kết quả xấp xỉ `2.9932`, vì đây là giá trị có cosin hyperbolic bằng 10.
