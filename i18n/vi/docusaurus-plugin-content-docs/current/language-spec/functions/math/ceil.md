---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm CEIL trong StackQL: làm tròn lên một đối số dạng số đến số nguyên gần nhất."
image: "/img/stackql-featured-image.png"
---
Làm tròn lên một đối số dạng số đến số nguyên gần nhất.  

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức thuộc nhóm kiểu dữ liệu số chính xác hoặc số gần đúng.

## Giá trị trả về {#return-values}
Trả về số nguyên nhỏ nhất lớn hơn hoặc bằng biểu thức số được chỉ định.

* * *

## Ví dụ {#examples}

### Làm tròn lên một số dấu phẩy động đến số nguyên gần nhất (lớn hơn) {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
