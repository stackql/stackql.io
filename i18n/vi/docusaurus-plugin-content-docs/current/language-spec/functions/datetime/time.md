---
title: TIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm TIME trong StackQL: trả về thời gian dưới dạng chuỗi HH:MM:SS từ một giá trị thời gian và các bộ điều chỉnh không bắt buộc."
image: "/img/stackql-featured-image.png"
---
Trả về một chuỗi thời gian theo định dạng HH:MM:SS.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Bộ điều chỉnh ngày ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT TIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*datetime_expression*__  
Giá trị datetime.

__*modifier*__  
Không hoặc nhiều bộ điều chỉnh được áp dụng cho *datetime_expression*.  Để biết thêm thông tin về bộ điều chỉnh, hãy xem [**Bộ điều chỉnh ngày**](/language-spec/functions/datetime/date_modifiers)

## Giá trị trả về {#return-values}

Trả về một chuỗi biểu diễn thành phần thời gian của *datetime_expression* đầu vào sau khi áp dụng *modifier*.

* * *

## Ví dụ {#examples}

### Trả về thành phần thời gian của một giá trị datetime {#return-the-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated) as time_only
FROM google.storage.buckets WHERE project = 'stackql';
```

### Trả về thành phần giờ địa phương của một giá trị datetime {#return-the-local-time-component-of-a-datetime-value}

```sql
SELECT name, timeCreated,
time(timeCreated, 'localtime') as local_time
FROM google.storage.buckets WHERE project = 'stackql';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
