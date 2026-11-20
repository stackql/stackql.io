---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm FLOOR trong StackQL: làm tròn xuống một đối số dạng số đến số nguyên gần nhất."
image: "/img/stackql-featured-image.png"
---
Làm tròn xuống một đối số dạng số đến số nguyên gần nhất.  

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức thuộc nhóm kiểu dữ liệu số chính xác hoặc số gần đúng.

## Giá trị trả về {#return-values}
Trả về số nguyên lớn nhất nhỏ hơn hoặc bằng biểu thức số được chỉ định.

* * *

## Ví dụ {#examples}

### Làm tròn xuống một số dấu phẩy động đến số nguyên gần nhất (nhỏ hơn) {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_floor
FROM google.storage.buckets WHERE project = 'stackql';
```
