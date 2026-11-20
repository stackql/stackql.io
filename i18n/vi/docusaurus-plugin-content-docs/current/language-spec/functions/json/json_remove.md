---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Xóa các phần tử khỏi tài liệu JSON theo đường dẫn chỉ định bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Xóa các phần tử được chỉ định khỏi tài liệu JSON bằng biểu thức đường dẫn. Nếu một đường dẫn được chỉ định không tồn tại trong tài liệu, hàm sẽ bỏ qua đường dẫn đó. Việc xử lý tuần tự các đường dẫn có thể ảnh hưởng đến cấu trúc cho các lần xóa tiếp theo.

Xem thêm:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Cú pháp {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*json_document*__  
Tài liệu JSON cần xóa phần tử.

__*path1, path2, ...*__  
Một hoặc nhiều biểu thức đường dẫn trỏ đến các phần tử cần xóa trong tài liệu JSON.

## Giá trị trả về {#return-values}
Trả về một bản sao của tài liệu JSON đã xóa các phần tử được chỉ định. Nếu không cung cấp đường dẫn nào, hàm trả về JSON đầu vào đã được định dạng lại và loại bỏ khoảng trắng thừa.

* * *

## Ví dụ {#examples}

### Xóa một phần tử khỏi mảng JSON {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Đầu ra: `'[0,1,3,4]'`

### Xóa nhiều phần tử khỏi mảng JSON {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Đầu ra: `'[1,3,4]'`

### Xóa phần tử cuối và phần tử đầu bằng chỉ mục âm {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Đầu ra: `'[1,2,3]'`

### Xóa đường dẫn không tồn tại {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Đầu ra: `'{"x":25,"y":42}'`

### Xóa khóa hiện có khỏi đối tượng JSON {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Đầu ra: `'{"x":25}'`

### Trường hợp biên: xóa với đường dẫn gốc {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Đầu ra: `NULL`

Các ví dụ này minh họa cách dùng hàm `json_remove()` để thao tác dữ liệu JSON một cách động, bao quát nhiều tình huống từ các thao tác xóa đơn giản đến xử lý các trường hợp biên khi sửa đổi cấu trúc dữ liệu.
