---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm ROUND trong StackQL: làm tròn một đối số kiểu số đến số chữ số thập phân cho trước, mặc định là 0."
image: "/img/stackql-featured-image.png"
---
Làm tròn một đối số kiểu số.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức thuộc nhóm kiểu dữ liệu số chính xác hoặc số xấp xỉ.

__*precision*__  
Số chữ số ở bên phải dấu thập phân mà *numeric_expression* được làm tròn đến.

> Nếu bỏ qua đối số *precision*, giá trị của đối số này được coi là 0.

## Giá trị trả về {#return-values}
Trả về một giá trị số.

* * *

## Ví dụ {#examples}

### Làm tròn một số dấu phẩy động đến hai chữ số thập phân {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)
