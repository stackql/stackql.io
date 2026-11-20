---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm SUBSTRING trong StackQL (bí danh SUBSTR): trả về một phần của chuỗi hoặc giá trị nhị phân theo vị trí bắt đầu và độ dài."
image: "/img/stackql-featured-image.png"
---
Trả về một phần của biểu thức ký tự hoặc biểu thức nhị phân.  `SUBSTR` là bí danh của `SUBSTRING`.

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

:::info

Để trích xuất một trường từ một đối tượng JSON, hãy dùng hàm [**JSON_EXTRACT**](/language-spec/functions/json/json_extract).

:::

## Cú pháp {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expression*__  
Một chuỗi ASCII hoặc một BLOB.

> Nếu *expression* là chuỗi, các chỉ số ký tự tham chiếu đến các ký tự UTF-8 thực tế. Nếu *expression* là BLOB, các chỉ số tham chiếu đến byte.

__*start*__  
Một số nguyên biểu thị ký tự hoặc byte đầu tiên của dãy con cần trả về trong biểu thức đầu vào.  Nếu *start* âm, ký tự đầu tiên của chuỗi con được xác định bằng cách đếm từ phải sang thay vì từ trái sang.

> Ký tự ngoài cùng bên trái của *expression* có số thứ tự 1.

__*length*__  
Một giá trị số nguyên không bắt buộc, biểu thị độ dài của dãy con cần trả về tính từ giá trị *start*.  Nếu bỏ qua *length*, mọi ký tự cho đến cuối chuỗi hoặc BLOB do *expression* chỉ định đều được trả về.

> Nếu *length* âm, giá trị tuyệt đối của *length* được sử dụng.

## Giá trị trả về {#return-values}

Trả về một giá trị chuỗi hoặc BLOB tùy theo kiểu của *expression*.

* * *

## Ví dụ {#examples}

### Trả về *n* ký tự đầu tiên từ một cột {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Cắt bỏ *n* ký tự đầu tiên khỏi một cột {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về *n* ký tự cuối cùng từ một cột {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
