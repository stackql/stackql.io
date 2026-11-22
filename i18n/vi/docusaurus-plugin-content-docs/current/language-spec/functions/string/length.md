---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm LENGTH trong StackQL: số ký tự trong một chuỗi, hoặc số byte trong một BLOB."
image: "/img/stackql-featured-image.png"
---
Trả về số ký tự trong một biểu thức đầu vào kiểu chuỗi tính đến trước ký tự NUL đầu tiên (`char(0)`), hoặc số byte trong một biểu thức BLOB.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Để lấy độ dài của một mảng JSON, hãy dùng hàm [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Cú pháp {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expression*__  
Một chuỗi hoặc một BLOB.

> Trả về `NULL` nếu *expression* là `NULL`.

> Nếu *expression* là số, hàm trả về độ dài của biểu diễn dạng chuỗi của *expression*.

## Giá trị trả về {#return-values}
Trả về một số nguyên.

* * *

## Ví dụ {#examples}

### Trả về độ dài của một cột chuỗi {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
