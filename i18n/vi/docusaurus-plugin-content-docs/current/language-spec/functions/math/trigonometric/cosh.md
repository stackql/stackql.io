---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính cosin hyperbolic của một số và trả về kết quả bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính cosin hyperbolic của một giá trị đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính cosin hyperbolic.

## Giá trị trả về {#return-values}
Trả về cosin hyperbolic của giá trị đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính cosin hyperbolic của 1 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

Truy vấn này trả về xấp xỉ `1.5431`, vì cosin hyperbolic của 1 vào khoảng 1.5431.

### Tính cosin hyperbolic của 0 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

Truy vấn này tính cosin hyperbolic của 0, cho kết quả `1`, vì cosin hyperbolic của 0 bằng đúng 1.
