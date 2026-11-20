---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính arccos (cosin nghịch đảo) của một số, trả về một góc theo radian, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính arccos (cosin nghịch đảo) của một số, tức là góc tính bằng radian có cosin bằng số đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính arccos, có giá trị trong khoảng từ -1 đến 1, bao gồm cả hai đầu.

## Giá trị trả về {#return-values}
Trả về arccos của giá trị đã chỉ định, tính bằng radian.

* * *

## Ví dụ {#examples}

### Tính arccos của 0.5 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

Truy vấn này trả về xấp xỉ `1.047`, vì arccos của 0.5 là π/3 radian (60 độ), tức khoảng 1.047 radian.

### Tính arccos của 1 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

Truy vấn này tính arccos của 1, cho kết quả `0`, vì cosin của 0 radian bằng 1.
