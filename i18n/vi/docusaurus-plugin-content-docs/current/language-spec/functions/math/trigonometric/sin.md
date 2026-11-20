---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính sin của một góc, cho theo radian, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính sin của một góc đã chỉ định, cho theo radian.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*radian_value*__  
Một biểu thức số chỉ định góc tính bằng radian cần tính sin.

## Giá trị trả về {#return-values}
Trả về sin của góc đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính sin của π/2 radian {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

Truy vấn này trả về `1`, vì sin của π/2 radian bằng 1.

### Tính sin của π radian {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

Truy vấn này tính sin của π radian, cho kết quả xấp xỉ `0`, vì sin của π bằng 0.
