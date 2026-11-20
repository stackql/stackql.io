---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính tang của một góc, cho theo radian, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính tang của một góc đã chỉ định, cho theo radian.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*radian_value*__  
Một biểu thức số chỉ định góc tính bằng radian cần tính tang.

## Giá trị trả về {#return-values}
Trả về tang của góc đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính tang của π/4 radian {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

Truy vấn này trả về `1`, vì tang của π/4 radian bằng 1.

### Tính tang của π radian {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

Truy vấn này tính tang của π radian, cho kết quả xấp xỉ `0`, vì tang của π rất gần với 0.
