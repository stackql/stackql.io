---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính logarit của một số với cơ số bất kỳ bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính logarit của một số theo cơ số được chỉ định; đây là dạng tổng quát hơn của hàm logarit so với LN, vốn dùng cơ số e.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*base*__  
Cơ số của logarit, một biểu thức số dương khác 1.

__*numeric_expression*__  
Một biểu thức số dương cần tính logarit.

## Giá trị trả về {#return-values}
Trả về logarit của giá trị số được chỉ định theo cơ số được chỉ định.

* * *

## Ví dụ {#examples}

### Tính logarit cơ số 10 của 1000 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

Truy vấn này trả về `3`, vì 10^3 = 1000.

### Tính logarit cơ số 2 của 16 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
Truy vấn này tính logarit cơ số 2 của 16, cho kết quả `4`, vì 2^4 = 16.
