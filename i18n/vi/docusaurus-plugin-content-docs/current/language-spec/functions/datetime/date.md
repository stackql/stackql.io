---
title: DATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm DATE trong StackQL: trả về một ngày dưới dạng chuỗi YYYY-MM-DD từ một giá trị thời gian và các bộ điều chỉnh tùy chọn."
image: "/img/stackql-featured-image.png"
---
Trả về một chuỗi ngày theo định dạng YYYY-MM-DD.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Bộ điều chỉnh ngày ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT DATE(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*datetime_expression*__  
Giá trị datetime.

__*modifier*__  
Không hoặc nhiều bộ điều chỉnh được áp dụng cho *datetime_expression*.  Để biết thêm thông tin về bộ điều chỉnh, hãy xem [**Bộ điều chỉnh ngày**](/language-spec/functions/datetime/date_modifiers)

## Giá trị trả về {#return-values}

Trả về một chuỗi biểu thị thành phần ngày của *datetime_expression* đầu vào sau khi áp dụng *modifier*.

* * *

## Ví dụ {#examples}

### Trả về thành phần ngày của một giá trị datetime {#return-the-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated) as date_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Trả về thành phần ngày theo giờ địa phương của một giá trị datetime {#return-the-local-date-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
date(timeCreated, 'localtime') as local_date
FROM google.storage.buckets WHERE project = 'stackql';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
