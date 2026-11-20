---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Cập nhật hoặc chèn giá trị trong tài liệu JSON bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Cập nhật các giá trị hiện có hoặc chèn giá trị mới vào tài liệu JSON tại các đường dẫn được chỉ định.

Xem thêm:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Cú pháp {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*json_column*__  
Cột hoặc biểu thức JSON cần sửa đổi.

__*path*__  
Biểu thức đường dẫn JSON chỉ vị trí cần đặt giá trị. Nếu đường dẫn tồn tại, giá trị được cập nhật; nếu đường dẫn không tồn tại, một cặp khóa-giá trị mới được thêm vào.

__*value_to_set*__  
Giá trị cần đặt tại đường dẫn được chỉ định trong tài liệu JSON.

## Giá trị trả về {#return-values}
Trả về tài liệu JSON với giá trị đã được đặt tại đường dẫn được chỉ định. Nếu đường dẫn tồn tại, giá trị hiện có được cập nhật; nếu không, một cặp khóa-giá trị mới được thêm vào.

:::note Hành vi khi khóa đã tồn tại

| Hàm                                                          | Ghi đè nếu đã tồn tại?       | Tạo nếu chưa tồn tại?     |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Không                        | Có                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Có                           | Không                     |
| [`json_set()`](/language-spec/functions/json/json_set)       | Có                           | Có                        |

:::

* * *

## Ví dụ {#examples}

### Cập nhật một giá trị hiện có trong đối tượng JSON {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending","region":"US"}', '$.status', 'active') as updated_json;

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne","status":"active","region":"US"}   |
-- |---------------------------------------------------------|
```

Truy vấn này cập nhật khóa 'status' thành 'active' trong tài liệu JSON nếu khóa 'status' tồn tại. Nếu 'status' không tồn tại trong tài liệu, khóa này được thêm vào với giá trị 'active'.

### Thêm một cặp khóa-giá trị mới vào đối tượng JSON {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with a date value to a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending"}', '$.lastChecked', '2024-04-29') as updated_json;

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne","status":"pending","lastChecked":"2024-04-29"}   |
|-----------------------------------------------------------------------|
*/
```

Truy vấn này thêm khóa mới 'lastChecked' với giá trị '2024-04-29' vào tài liệu JSON nếu 'lastChecked' chưa tồn tại. Nếu khóa đã tồn tại, giá trị hiện có được cập nhật thành '2024-04-29'.
