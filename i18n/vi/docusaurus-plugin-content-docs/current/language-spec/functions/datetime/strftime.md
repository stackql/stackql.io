---
title: STRFTIME
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm STRFTIME trong StackQL: định dạng một giá trị datetime bằng một chuỗi định dạng, dùng cùng các phép thay thế như hàm strftime của C."
image: "/img/stackql-featured-image.png"
---
Trả về một giá trị datetime đã được định dạng theo một định dạng chỉ định.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Bộ điều chỉnh ngày ]](/language-spec/functions/datetime/date_modifiers) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT STRFTIME(format, datetime_expression [, modifier, ...]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*format*__  
Định dạng mong muốn cho giá trị datetime được chỉ định bởi giá trị *datetime_expression*.  Xem bảng định dạng bên dưới.

__*datetime_expression*__  
Giá trị ngày hoặc giờ cần định dạng.

__*modifier*__  
Không hoặc nhiều bộ điều chỉnh được áp dụng cho *datetime_expression*.  Để biết thêm thông tin về bộ điều chỉnh, hãy xem [**Bộ điều chỉnh ngày**](/language-spec/functions/datetime/date_modifiers)

### Định dạng {#formats}

| Định dạng | Mô tả                                  |
|-----------|----------------------------------------|
| %d        | ngày trong tháng: 01-31                |
| %f        | giây có phần thập phân: SS.SSS         |
| %H        | giờ: 00-24                             |
| %j        | ngày trong năm: 001-366                |
| %J        | số ngày Julian                         |
| %m        | tháng: 01-12                           |
| %M        | phút: 00-59                            |
| %s        | số giây kể từ 1970-01-01               |
| %S        | giây: 00-59                            |
| %w        | ngày trong tuần 0-6 với Chủ nhật==0    |
| %W        | tuần trong năm: 00-53                  |
| %Y        | năm: 0000-9999                         |
| %%        | thoát ký hiệu phần trăm: %             |

## Giá trị trả về {#return-values}

Trả về một chuỗi đã định dạng biểu diễn datetime theo định dạng mong muốn.

* * *

## Ví dụ {#examples}

### Trả về một ngày theo định dạng Mỹ {#return-a-date-in-us-format}

```sql
SELECT name, strftime('%m/%d/%Y', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Chuyển đổi datetime sang dấu thời gian epoch {#convert-a-datetime-to-an-epoch-timestamp}

```sql
SELECT name, strftime('%s', creationTimestamp) as created_at
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Tính số ngày giữa hai ngày {#calculate-the-days-between-two-dates}

```sql
SELECT name, (strftime('%s','now')-strftime('%s', creationTimestamp))/86400 as days_since_creation
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_datefunc.html](https://www.sqlite.org/lang_datefunc.html)
