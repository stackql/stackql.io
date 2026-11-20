---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Chuyển đổi một góc từ độ sang radian bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Chuyển đổi một góc đã chỉ định từ độ sang radian.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*angle_in_degrees*__  
Một biểu thức số biểu diễn một góc tính bằng độ.

## Giá trị trả về {#return-values}
Trả về giá trị tương đương của góc tính bằng radian.

* * *

## Ví dụ {#examples}

### Chuyển đổi 180 độ sang radian {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

Truy vấn này sẽ trả về π (xấp xỉ `3.14159`), vì chuyển đổi 180 độ sang radian cho kết quả π radian.

### Chuyển đổi 90 độ sang radian {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

Truy vấn này tính việc chuyển đổi 90 độ sang radian, cho kết quả π/2 hay xấp xỉ `1.5708`, vì 90 độ bằng một nửa của 180 độ.
