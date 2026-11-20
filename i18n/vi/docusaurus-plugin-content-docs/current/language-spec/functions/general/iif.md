---
title: IIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm IIF trong StackQL: trả về một trong hai giá trị tùy theo điều kiện có đúng hay không, là một biểu thức if-then-else nội tuyến."
image: "/img/stackql-featured-image.png"
---
Trả về một giá trị dựa trên kết quả đánh giá một biểu thức đầu vào.  

Xem thêm:  
[[` SELECT `]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT IIF(expression, result_if_true, result_if_false) 
FROM <multipartIdentifier>;
```
> Về mặt logic, hàm `iif()` tương đương với biểu thức `CASE` dạng `CASE WHEN expression THEN result_if_true ELSE result_if_false END`

## Đối số {#arguments}

__*expression*__  
Một biểu thức boolean cần được đánh giá.

:::tip 

Đối số *expression* có thể là một hàm lồng nhau như `JSON_EXTRACT`.

:::

__*result_if_true*__  
Kết quả được trả về nếu *expression* được đánh giá là `true`.

__*result_if_false*__  
Kết quả được trả về nếu *expression* được đánh giá là `false`.

## Giá trị trả về {#return-values}
Kiểu dữ liệu của đối số *result_if_true* hoặc *result_if_false*.

* * *

## Ví dụ {#examples}

### Phân loại kích thước đĩa của các instance compute engine {#categorize-disk-sizes-of-compute-engine-instances}

```sql
SELECT iif(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER) > 10, '> 10GB', '<= 10GB') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#iif](https://www.sqlite.org/lang_corefunc.html#iif)
