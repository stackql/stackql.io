---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính tang hyperbolic của một số và trả về kết quả bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính tang hyperbolic của một giá trị đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính tang hyperbolic.

## Giá trị trả về {#return-values}
Trả về tang hyperbolic của giá trị đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính tang hyperbolic của 1 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

Truy vấn này trả về xấp xỉ `0.7616`, vì tang hyperbolic của 1 vào khoảng 0.7616.

### Tính tang hyperbolic của 0 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

Truy vấn này tính tang hyperbolic của 0, cho kết quả `0`, vì tang hyperbolic của 0 bằng đúng 0.
