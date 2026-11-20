---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm LOWER trong StackQL: chuyển một chuỗi thành chữ thường."
image: "/img/stackql-featured-image.png"
---
Trả về chuỗi đầu vào ở dạng chữ thường.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

## Cú pháp {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> Hàm `lower()` chỉ hoạt động với ký tự ASCII.

## Đối số {#arguments}

__*character_expression*__  
Một chuỗi ASCII.

## Giá trị trả về {#return-values}
Trả về một giá trị chuỗi.

* * *

## Ví dụ {#examples}

### Trả về một chuỗi ở dạng chữ thường {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
