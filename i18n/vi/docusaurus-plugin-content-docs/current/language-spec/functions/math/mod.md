---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính phần dư của phép chia một số cho một số khác bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính phần dư của *x* chia cho *y* và trả về modulo. Hàm này tương tự như dùng toán tử '%' nhưng tương thích với các đối số không phải số nguyên.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*x*__  
Số bị chia, là một biểu thức số.

__*y*__  
Số chia, cũng là một biểu thức số.

## Giá trị trả về {#return-values}
Trả về phần dư của *x* chia cho *y*.

* * *

## Ví dụ {#examples}

### Tính modulo của 10 chia cho 3 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
