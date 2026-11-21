---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tính cosin của một góc, cho theo radian, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tính cosin của một góc đã chỉ định, cho theo radian.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*radian_value*__  
Một biểu thức số chỉ định góc tính bằng radian cần tính cosin.

## Giá trị trả về {#return-values}
Trả về cosin của góc đã chỉ định.

* * *

## Ví dụ {#examples}

### Tính cosin của π/3 radian {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

Truy vấn này trả về `0.5000000000000001`, giá trị xấp xỉ dấu phẩy động của cosin của π/3 radian, vốn bằng 0.5.

### Tính cosin của π radian {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

Truy vấn này tính cosin của π radian, cho kết quả `-1`, vì cosin của π bằng -1.
