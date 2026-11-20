---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Chuyển đổi một góc từ radian sang độ bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Chuyển đổi một góc đã chỉ định từ radian sang độ.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*angle_in_radians*__  
Một biểu thức số biểu diễn một góc tính bằng radian.

## Giá trị trả về {#return-values}
Trả về giá trị tương đương của góc tính bằng độ.

* * *

## Ví dụ {#examples}

### Chuyển đổi π radian sang độ {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

Truy vấn này sẽ trả về xấp xỉ `180`, vì chuyển đổi π radian (xấp xỉ `3.14159`) cho kết quả khoảng 180 độ.

### Chuyển đổi π/2 radian sang độ {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

Truy vấn này tính việc chuyển đổi π/2 radian (xấp xỉ `1.5708`) sang độ, cho kết quả khoảng `90`, vì π/2 radian tương đương với một góc 90 độ.
