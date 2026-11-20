---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Chèn giá trị mới vào tài liệu JSON mà không ghi đè dữ liệu hiện có, bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Chèn giá trị mới vào tài liệu JSON tại các đường dẫn được chỉ định mà không ghi đè các giá trị hiện có.

Xem thêm:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Cú pháp {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*json_column*__  
Cột hoặc biểu thức JSON cần sửa đổi.

__*path*__  
Biểu thức đường dẫn JSON chỉ vị trí cần chèn giá trị mới. Nếu đường dẫn đã tồn tại, giá trị ban đầu tại đường dẫn đó không được thay thế.

__*new_value*__  
Giá trị cần chèn tại đường dẫn được chỉ định trong tài liệu JSON.

## Giá trị trả về {#return-values}
Trả về tài liệu JSON đã được chèn giá trị mới nếu đường dẫn được chỉ định chưa tồn tại; nếu không, hàm trả về tài liệu JSON không thay đổi.

:::note Hành vi khi khóa đã tồn tại

| Hàm                                                          | Ghi đè nếu đã tồn tại?       | Tạo nếu chưa tồn tại?     |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Không                        | Có                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Có                           | Không                     |
| [`json_set()`](/language-spec/functions/json/json_set)       | Có                           | Có                        |

:::

* * *

## Ví dụ {#examples}

### Chèn một cặp khóa-giá trị mới vào đối tượng JSON {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

Truy vấn này chèn một khóa mới tên là 'region' với giá trị 'Europe' vào tài liệu JSON nếu khóa 'region' chưa tồn tại. Nếu 'region' đã là một khóa trong tài liệu, truy vấn trả về tài liệu ban đầu mà không thay đổi.
