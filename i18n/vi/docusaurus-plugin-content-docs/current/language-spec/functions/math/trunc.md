---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm TRUNC trong StackQL: phần nguyên của một số, làm tròn về phía 0 và loại bỏ mọi phần thập phân."
image: "/img/stackql-featured-image.png"
---

Trả về phần nguyên của một số bằng cách làm tròn về phía 0, loại bỏ mọi phần thập phân.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức thuộc nhóm kiểu dữ liệu số chính xác hoặc số xấp xỉ.

## Giá trị trả về {#return-values}
Trả về một giá trị số đã được cắt bớt đến phần nguyên.

* * *

## Ví dụ {#examples}

### Cắt bớt số ngày kể từ khi tạo các bucket Google Cloud Storage thành số ngày nguyên {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-whole-days}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
