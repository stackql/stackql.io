---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính logarit tự nhiên của một số bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính logarit tự nhiên (logarit cơ số e) của một số cho trước.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức số dương cần tính logarit tự nhiên.

## Giá trị trả về {#return-values}
Trả về logarit tự nhiên của giá trị số dương được chỉ định.

* * *

## Ví dụ {#examples}

### Tính logarit tự nhiên của 2.71828 (xấp xỉ e) {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

Truy vấn này trả về một giá trị gần bằng `1`, vì logarit tự nhiên của e (xấp xỉ 2.71828) bằng 1.

### Tính logarit tự nhiên của một số lớn hơn {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

Truy vấn này tính logarit tự nhiên của 10, tức là giá trị logarit cơ số e của 10.
