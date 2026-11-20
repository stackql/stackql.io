---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính căn bậc hai của một số bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính căn bậc hai của một biểu thức số không âm.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức số không âm cần tính căn bậc hai.

## Giá trị trả về {#return-values}
Trả về căn bậc hai của giá trị số không âm đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính căn bậc hai của một số {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
