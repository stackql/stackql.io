---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính logarit cơ số 10 của một số bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính logarit cơ số 10 của một số cho trước, tức là xác định số mũ cần nâng số 10 lên để đạt được giá trị đó.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức số dương cần tính logarit cơ số 10.

## Giá trị trả về {#return-values}
Trả về logarit cơ số 10 của giá trị số được chỉ định.

* * *

## Ví dụ {#examples}

### Tính logarit cơ số 10 của 100 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

Truy vấn này trả về `2`, vì 10^2 = 100.

### Tính logarit cơ số 10 của 1000 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

Truy vấn này tính logarit cơ số 10 của 1000, cho kết quả `3`, vì 10^3 = 1000.
