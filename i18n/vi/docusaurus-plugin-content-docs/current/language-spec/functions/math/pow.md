---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính lũy thừa của một số với số mũ là một số khác bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính *x* lũy thừa *y*.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*x*__  
Cơ số, là một biểu thức số.

__*y*__  
Số mũ mà cơ số *x* được nâng lên, cũng là một biểu thức số.

## Giá trị trả về {#return-values}
Trả về kết quả của *x* lũy thừa *y*.

* * *

## Ví dụ {#examples}

### Tính x lũy thừa y {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
