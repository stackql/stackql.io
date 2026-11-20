---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm CHAR trong StackQL: các ký tự ứng với một hoặc nhiều mã ký tự, để chèn ký tự xuống dòng, dấu nháy, tab và các ký tự điều khiển khác vào chuỗi."
image: "/img/stackql-featured-image.png"
---

Hàm `CHAR()` trả về một chuỗi gồm các ký tự ứng với một hoặc nhiều mã ký tự.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * *

:::note
Khi dùng StackQL với backend PostgreSQL, hàm tương đương là `CHR()`, hàm này nhận một mã duy nhất.
:::

## Cú pháp {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> Mỗi đối số thêm một ký tự vào kết quả, vì vậy `CHAR(72, 105)` trả về `Hi`.

## Đối số {#arguments}

**_ascii_code_**  
Một hoặc nhiều số nguyên, mỗi số là mã của một ký tự: mã ASCII hoặc bất kỳ điểm mã Unicode nào.

## Giá trị trả về {#return-values}

Trả về một chuỗi có một ký tự cho mỗi đối số *ascii_code*.

* * *

## Các mã ASCII thông dụng {#common-ascii-codes}

| Ký tự         | Mô tả                 | Mã ASCII   | Ví dụ sử dụng                   |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | Xuống dòng            | 10         | `CHAR(10)`                      |
| `'`           | Dấu nháy đơn          | 39         | `CHAR(39)`                      |
| `"`           | Dấu nháy kép          | 34         | `CHAR(34)`                      |
| (dấu cách)    | Dấu cách              | 32         | `CHAR(32)`                      |
| `:`           | Dấu hai chấm          | 58         | `CHAR(58)`                      |
| `;`           | Dấu chấm phẩy         | 59         | `CHAR(59)`                      |
| `\t`          | Tab ngang             | 9          | `CHAR(9)`                       |
| `\r`          | Về đầu dòng           | 13         | `CHAR(13)`                      |
| `\b`          | Xóa lùi               | 8          | `CHAR(8)`                       |
| `\f`          | Nạp trang             | 12         | `CHAR(12)`                      |

* * *

## Ví dụ {#examples}

### Nối chuỗi với các ký tự đặc biệt {#concatenate-strings-with-special-characters}

Để nối các chuỗi với một ký tự xuống dòng ở giữa:

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**Kết quả** (cho người dùng có tên hiển thị `Jane Doe` và ID `1234567890123456`):

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
