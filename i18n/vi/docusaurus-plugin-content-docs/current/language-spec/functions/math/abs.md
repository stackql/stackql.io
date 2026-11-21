---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm ABS trong StackQL: giá trị tuyệt đối của một đối số dạng số."
image: "/img/stackql-featured-image.png"
---
Trả về giá trị tuyệt đối của đối số dạng số.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `abs(X)` trả về `NULL` nếu `X` là `NULL`. `abs(X)` trả về `0.0` nếu `X` là một chuỗi hoặc blob không thể chuyển đổi thành giá trị số. Nếu `X` là số nguyên -9223372036854775808, `abs(X)` báo lỗi tràn số nguyên vì không có giá trị dương tương đương ở dạng bù hai 64 bit.

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức thuộc nhóm kiểu dữ liệu số chính xác hoặc số gần đúng.

## Giá trị trả về {#return-values}

Trả về một giá trị số không âm.

* * *

## Ví dụ {#examples}

### Trả về giá trị tuyệt đối của một số {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs)
