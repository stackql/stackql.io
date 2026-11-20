---
title: JULIANDAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm JULIANDAY trong StackQL: số ngày, kèm phần lẻ, kể từ giữa trưa ngày 24 tháng 11 năm 4714 TCN cho một giá trị thời gian, hữu ích cho phép tính ngày."
image: "/img/stackql-featured-image.png"
---
Trả về ngày Julian - số ngày kể từ giữa trưa tại Greenwich ngày 24 tháng 11 năm 4714 TCN [(lịch Gregory đón trước)](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Bộ điều chỉnh ngày ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT JULIANDAY(datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*datetime_expression*__  
Giá trị datetime.

__*modifier*__  
Không hoặc nhiều bộ điều chỉnh được áp dụng cho *datetime_expression*.  Để biết thêm thông tin về bộ điều chỉnh, hãy xem [**Bộ điều chỉnh ngày**](/language-spec/functions/datetime/date_modifiers)

## Giá trị trả về {#return-values}

Trả về [ngày Julian](https://en.wikipedia.org/wiki/Julian_day) của *datetime_expression* đầu vào sau khi áp dụng *modifier*: số ngày, bao gồm cả phần lẻ ứng với thời điểm trong ngày, kể từ giữa trưa tại Greenwich ngày 24 tháng 11 năm 4714 TCN theo [lịch Gregory đón trước](https://en.wikipedia.org/wiki/Proleptic_Gregorian_calendar).

* * *

## Ví dụ {#examples}

### Tính số ngày giữa hai ngày {#calculate-the-days-between-two-dates}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```

:::tip 

Sử dụng hàm [**ROUND**](/language-spec/functions/math/round) để chuyển đổi số dấu phẩy động thành số nguyên gần nhất.

:::

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
