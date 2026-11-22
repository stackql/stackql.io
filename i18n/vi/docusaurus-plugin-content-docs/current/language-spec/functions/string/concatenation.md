---
title: Toán tử nối chuỗi
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Toán tử nối chuỗi || trong StackQL: nối hai hoặc nhiều giá trị chuỗi thành một chuỗi."
image: "/img/stackql-featured-image.png"
---
Trả về chuỗi kết quả của việc nối chuỗi, tức là ghép hai hoặc nhiều giá trị chuỗi lại với nhau.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*character_expression1*__, __*character_expression2*__  
Một literal chuỗi hoặc cột chuỗi.

## Giá trị trả về {#return-values}

Trả về một chuỗi.

:::note

Từ StackQL `v1.0`, trình phân tích cú pháp coi `||` là toán tử nối chuỗi, có độ ưu tiên cao hơn các toán tử so sánh, vì vậy `WHERE id = prefix || suffix` so sánh `id` với chuỗi đã nối.  Các bản phát hành trước đó phân tách `||` thành token `OR` boolean.  Dùng `OR` cho phép tuyển boolean.  [Biến phiên](/getting-started/session-variables) cũng có thể được nối: `$prefix || '-' || $suffix`.

:::

* * *

## Ví dụ {#examples}

### Nối hai trường với nhau {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

Để biết thêm thông tin, hãy xem [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
