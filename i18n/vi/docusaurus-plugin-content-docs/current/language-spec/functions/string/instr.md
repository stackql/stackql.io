---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm INSTR trong StackQL: vị trí đánh số từ 1 của lần xuất hiện đầu tiên của một chuỗi bên trong chuỗi khác, hoặc 0 nếu không tìm thấy."
image: "/img/stackql-featured-image.png"
---
Trả về vị trí ký tự của lần xuất hiện đầu tiên của một chuỗi bên trong một chuỗi khác, hoặc 0 nếu không tìm thấy chuỗi cần tìm.

Xem thêm:  
[[` SELECT `]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*string*__  
Chuỗi nguồn được tìm kiếm.

__*searchstring*__  
Chuỗi con cần tìm.

> Nếu *string* và *searchstring* đều là BLOB, `instr(<string>, <searchstring>)` trả về số byte đứng trước lần xuất hiện đầu tiên của *searchstring* cộng thêm một, hoặc 0 nếu *searchstring* không xuất hiện ở bất kỳ đâu trong *string*. Nếu cả hai đối số đều không phải `NULL` và không phải BLOB, cả hai được hiểu là chuỗi. 

## Giá trị trả về {#return-values}
Trả về một số nguyên.

> Nếu giá trị *string* hoặc *searchstring* là `NULL`, kết quả là `NULL`.

* * *

## Ví dụ {#examples}

### Tìm một chuỗi trong một trường {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
