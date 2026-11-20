---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính sin hyperbolic của một số và trả về kết quả bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính sin hyperbolic của một giá trị đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính sin hyperbolic.

## Giá trị trả về {#return-values}
Trả về sin hyperbolic của giá trị đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính sin hyperbolic của 1 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

Truy vấn này trả về xấp xỉ `1.1752`, vì sin hyperbolic của 1 vào khoảng 1.1752.

### Tính sin hyperbolic của 0 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

Truy vấn này tính sin hyperbolic của 0, cho kết quả `0`, vì sin hyperbolic của 0 bằng đúng 0.
