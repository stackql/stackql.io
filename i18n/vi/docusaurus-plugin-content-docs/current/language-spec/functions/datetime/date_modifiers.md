---
title: Bộ điều chỉnh ngày
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Bộ điều chỉnh ngày giờ trong StackQL: dịch kết quả DATE, TIME, DATETIME, JULIANDAY và STRFTIME theo ngày, tháng hoặc năm, về đầu kỳ, tới một thứ trong tuần và hơn thế."
image: "/img/stackql-featured-image.png"
---

## Tổng quan {#overview}

Trong các hàm datetime của StackQL, một giá trị ngày hoặc giờ có thể được theo sau bởi không hoặc nhiều bộ điều chỉnh làm thay đổi ngày và/hoặc giờ. Mỗi bộ điều chỉnh là một phép biến đổi được áp dụng cho giá trị thời gian ở bên trái nó. Các bộ điều chỉnh được áp dụng theo thứ tự từ trái sang phải. Các bộ điều chỉnh hiện có như sau:

| Bộ điều chỉnh | Mô tả |
|----------------------|-----------------------------------------------------------------------|
| NNN day(s)           | Cộng ±NNN ngày vào một giá trị ngày giờ |
| NNN hour(s)          | Cộng ±NNN giờ vào một giá trị ngày giờ |
| NNN minute(s)        | Cộng ±NNN phút vào một giá trị ngày giờ |
| NNN[.NNNN] second(s) | Cộng ±NNN giây vào (hoặc trừ khỏi) một giá trị ngày giờ |
| NNN month(s)         | Cộng ±NNN tháng vào một giá trị ngày giờ |
| NNN year(s)          | Cộng ±NNN năm vào một giá trị ngày giờ |
| start of month       | Lùi về đầu tháng |
| start of year        | Lùi về đầu năm |
| start of day         | Lùi về đầu ngày |
| weekday N            | Đẩy một ngày về phía trước tới ngày kế tiếp có số thứ tự ngày trong tuần là N; một ngày đã rơi vào ngày trong tuần N được trả về nguyên trạng |
| localtime            | Trả về giờ địa phương |
| utc                  | Trả về giờ UTC |
| unixepoch            | Thời gian Unix |

## Ví dụ {#examples}

### Trả về ngày và giờ hiện tại bằng giá trị thời gian `now` {#return-the-current-date-and-time-using-the-now-time-value}

```sql
SELECT name, date('now') as date, time('now') as time
FROM google.storage.buckets WHERE project = 'stackql';
```

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

### Lấy ngày trong tuần được chỉ định vào hoặc sau một ngày cho trước {#get-the-next-specified-weekday-after-a-given-date}

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
