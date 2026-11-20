---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm UPPER trong StackQL: chuyển một chuỗi thành chữ hoa."
image: "/img/stackql-featured-image.png"
---
Trả về chuỗi đầu vào ở dạng chữ hoa.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

## Cú pháp {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> Hàm `upper()` chỉ hoạt động với ký tự ASCII.

## Đối số {#arguments}

__*character_expression*__  
Một chuỗi ASCII.

## Giá trị trả về {#return-values}
Trả về một giá trị chuỗi.

* * *

## Ví dụ {#examples}

### Trả về một chuỗi ở dạng chữ hoa {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
