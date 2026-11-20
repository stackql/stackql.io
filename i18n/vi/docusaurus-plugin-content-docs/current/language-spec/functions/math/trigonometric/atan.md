---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính arctan (tang nghịch đảo) của một số, trả về một góc theo radian, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính arctan (tang nghịch đảo) của một số, tức là góc tính bằng radian có tang bằng số đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính arctan.

## Giá trị trả về {#return-values}
Trả về arctan của giá trị đã chỉ định, tính bằng radian.

* * *

## Ví dụ {#examples}

### Tính arctan của 1 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

Truy vấn này trả về π/4 hay xấp xỉ `0.785`, vì arctan của 1 là π/4 radian.

### Tính arctan của 0 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

Truy vấn này tính arctan của 0, cho kết quả `0`, vì tang của 0 radian bằng 0.
