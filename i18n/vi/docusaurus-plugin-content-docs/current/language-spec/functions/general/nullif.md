---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm NULLIF trong StackQL: trả về NULL khi hai đối số bằng nhau, ngược lại trả về đối số thứ nhất."
image: "/img/stackql-featured-image.png"
---
Nhận hai đối số và trả về giá trị `NULL` nếu chúng bằng nhau, ngược lại trả về đối số thứ nhất.  

> Hàm `NULLIF` tương đương với `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> `NULLIF` hữu ích với các hàm tổng hợp như [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum) và [`COUNT`](/language-spec/functions/aggregate/count), khi tập kết quả chứa các giá trị đặc biệt không phải `NULL` nhưng bạn muốn xử lý như `NULL` cho mục đích tổng hợp.

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Cú pháp {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expression*__  
Hai biểu thức thuộc kiểu dữ liệu bất kỳ, được phân giải thành giá trị `NULL` hoặc khác `NULL`.

> Hàm `NULLIF` tìm trong các đối số của nó từ trái sang phải một đối số có định nghĩa hàm đối chiếu và dùng hàm đối chiếu đó cho mọi phép so sánh chuỗi.  Nếu không đối số nào định nghĩa hàm đối chiếu, kiểu đối chiếu `BINARY` sẽ được dùng.

## Giá trị trả về {#return-values}

Trả về `NULL` nếu hai đối số *expression* được phân giải thành cùng một giá trị, ngược lại trả về giá trị thứ nhất. 

* * *

## Ví dụ {#examples}

### Đếm các bản ghi có một trường khác với một giá trị cho trước {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a' 
AND name != 'controller-0';
*/
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
