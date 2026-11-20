---
title: Kiểu dữ liệu
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Kiểu dữ liệu StackQL: các kiểu dùng cho thuộc tính và tham số của tài nguyên trong truy vấn và kết quả, cùng cách chúng ánh xạ tới lược đồ API của provider."
image: "/img/stackql-featured-image.png"
---

Kiểu dữ liệu StackQL được dùng để biểu diễn giá trị của các trường đầu vào hoặc đầu ra (thuộc tính hoặc tham số) trong tài nguyên đám mây.  Các kiểu dữ liệu StackQL được liệt kê trong bảng dưới đây:

| Kiểu StackQL | Mô tả | Ví dụ |
|--|--|--|
| `string` | dãy ký tự Unicode được đặt trong dấu nháy đơn | `'mybucket'` |
| `array` | tập hợp giá trị có thứ tự, đặt trong cặp dấu ngoặc vuông `[ ]`, phân cách bằng dấu phẩy | `[0,1,2]` |
| `object` | tập hợp không có thứ tự gồm không hoặc nhiều cặp tên/giá trị, đặt giữa `{ }` và phân cách bằng dấu phẩy | `{"name": "mydb"}` |
| `boolean` | `true` hoặc `false`, không đặt trong dấu nháy (một giá trị có dấu nháy như `'true'` được coi là chuỗi) | `true` |
| `integer` | một số nguyên có thể dương, âm hoặc bằng không | `2` |
| `double` | số dấu phẩy động độ chính xác kép, có thể dương hoặc âm | `0.95` |
<br/>
<br/>
> Kiểu dữ liệu StackQL ánh xạ tới các kiểu dữ liệu mà API của mọi nhà cung cấp đám mây lớn cung cấp.

## Chuyển đổi kiểu {#type-conversion}

`CAST(expression AS type)` và `CONVERT(expression, type)` chuyển đổi một giá trị sang kiểu khác.  Ngoài các kiểu chuyển đổi kế thừa từ ngữ pháp MySQL (`CHAR`, `DATE`, `DATETIME`, `DECIMAL`, `SIGNED`, `UNSIGNED`, `TIME`, `JSON`), StackQL `v1.0` trở đi chấp nhận `TEXT`, `INTEGER`, `INT`, `BIGINT`, `BOOLEAN` và `BOOL`.  Backend SQL thực hiện việc chuyển đổi, nên ngữ nghĩa của nó là ngữ nghĩa của SQLite hoặc PostgreSQL.  Phép chuyển đổi thường được dùng để so sánh một [biến phiên](/getting-started/session-variables) có kiểu với một cột chuỗi:

```sql
SET some_id = 42;

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-east' AS label;
```

Các trường boolean từ các provider XML như AWS được trả về dưới dạng `true` và `false`; các bản phát hành StackQL trước `v1.0` trả về chúng dưới dạng `0` và `1`.
