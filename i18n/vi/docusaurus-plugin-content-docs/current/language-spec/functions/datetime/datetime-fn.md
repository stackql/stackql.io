---
title: DATETIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm DATETIME trong StackQL: tính một giá trị datetime từ một đầu vào và một hoặc nhiều bộ điều chỉnh, trả về dưới dạng YYYY-MM-DD HH:MM:SS."
image: "/img/stackql-featured-image.png"
---
Trả về một giá trị datetime được tính từ một datetime đầu vào và một hoặc nhiều bộ điều chỉnh.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Bộ điều chỉnh ngày ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT DATETIME(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*datetime_expression*__  
Giá trị datetime.

__*modifier*__  
Không hoặc nhiều bộ điều chỉnh được áp dụng cho *datetime_expression*.  Để biết thêm thông tin về bộ điều chỉnh, hãy xem [**Bộ điều chỉnh ngày**](/language-spec/functions/datetime/date_modifiers)

## Giá trị trả về {#return-values}

Trả về một chuỗi biểu diễn datetime của *datetime_expression* đầu vào sau khi áp dụng *modifier*.

* * *

## Ví dụ {#examples}

### Chuyển đổi UTC sang giờ địa phương bằng bộ điều chỉnh `localtime` {#convert-utc-to-localtime-using-the-localtime-modifier}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'localtime') as local_time_created
FROM google.storage.buckets WHERE project = 'stackql';
```

### Chuyển đổi giờ địa phương sang UTC {#convert-datetime-to-utc}

Bộ điều chỉnh `utc` coi đầu vào của nó là giờ địa phương trên máy chạy StackQL và chuyển đổi sang UTC.  Một giá trị đã ở UTC, chẳng hạn như `'now'` hoặc một dấu thời gian có hậu tố `Z`, được trả về không đổi.

```sql
SELECT datetime('2026-11-20 17:59:24', 'utc') as utc_time;
```

Trên máy có múi giờ UTC+11, truy vấn này trả về `2026-11-20 06:59:24`.

### Tính một ngày tương đối so với một ngày khác {#calculate-a-date-relative-to-another-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, '+24 hours') as a_day_later
FROM google.storage.buckets WHERE project = 'stackql';
```

### Lấy ngày trong tuần được chỉ định tiếp theo sau một ngày cho trước {#get-the-next-specified-weekday-after-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'weekday 0') as following_sunday
FROM google.storage.buckets WHERE project = 'stackql';
```

### Lấy ngày đầu tháng của một ngày cho trước {#get-the-start-of-the-month-for-a-given-date}

```sql
SELECT name, timeCreated,
datetime(timeCreated, 'start of month') as start_of_month
FROM google.storage.buckets WHERE project = 'stackql';
```

### Chuyển đổi dấu thời gian Unix sang datetime {#convert-a-unix-timestamp-to-a-datetime}

```sql
SELECT datetime(1576417943, 'unixepoch') as date_from_ts
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Để chuyển đổi một datetime sang dấu thời gian Unix epoch, hãy xem [**STRFTIME**](/language-spec/functions/datetime/strftime).  Để tính chênh lệch giữa hai đối tượng datetime, hãy xem [**STRFTIME**](/language-spec/functions/datetime/strftime) hoặc [**JULIANDAY**](/language-spec/functions/datetime/julianday).

:::

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
