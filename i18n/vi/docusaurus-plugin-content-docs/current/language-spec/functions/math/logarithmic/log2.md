---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính logarit cơ số 2 của một số bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính logarit cơ số 2 của một số cho trước, tức là trả về số mũ cần nâng số 2 lên để thu được giá trị đó.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức số dương cần tính logarit cơ số 2.

## Giá trị trả về {#return-values}
Trả về logarit cơ số 2 của giá trị số được chỉ định.

* * *

## Ví dụ {#examples}

### Tính logarit cơ số 2 của 16 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

Truy vấn này trả về `4`, vì 2^4 = 16.

### Tính logarit cơ số 2 của 32 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

Truy vấn này tính logarit cơ số 2 của 32, cho kết quả `5`, vì 2^5 = 32.
