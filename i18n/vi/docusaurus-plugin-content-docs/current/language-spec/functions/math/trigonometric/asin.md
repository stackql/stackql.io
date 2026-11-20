---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính arcsin (sin nghịch đảo) của một số, trả về một góc theo radian, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính arcsin (sin nghịch đảo) của một số, tức là góc tính bằng radian có sin bằng số đã chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*value*__  
Một biểu thức số cần tính arcsin, có giá trị trong khoảng từ -1 đến 1, bao gồm cả hai đầu.

## Giá trị trả về {#return-values}
Trả về arcsin của giá trị đã chỉ định, tính bằng radian.

* * *

## Ví dụ {#examples}

### Tính arcsin của 0.5 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

Truy vấn này trả về π/6 hay xấp xỉ `0.5236`, vì arcsin của 0.5 tương ứng với góc π/6 radian.

### Tính arcsin của -1 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

Truy vấn này tính arcsin của -1, cho kết quả -π/2 hay xấp xỉ `-1.5708`, vì sin của -π/2 bằng -1.
