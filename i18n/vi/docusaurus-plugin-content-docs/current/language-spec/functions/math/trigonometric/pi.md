---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Lấy hằng số toán học π (pi) bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Trả về hằng số toán học π (pi), là tỷ số giữa chu vi và đường kính của một hình tròn.

Xem thêm:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT PI() AS pi_value;
```

## Đối số {#arguments}

Hàm này không yêu cầu đối số nào.

## Giá trị trả về {#return-values}
Trả về hằng số π, xấp xỉ bằng 3.14159.

* * *

## Ví dụ {#examples}

### Lấy giá trị của π {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

Truy vấn này sẽ xuất ra giá trị của π, xấp xỉ `3.14159`, hữu ích trong nhiều phép tính toán học liên quan đến hình tròn và các hàm hình học khác.
