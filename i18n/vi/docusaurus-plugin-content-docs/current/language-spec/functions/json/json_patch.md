---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Áp dụng JSON MergePatch cho tài liệu JSON bằng SQL trong StackQL để sửa đổi hoặc hợp nhất các đối tượng JSON.
image: "/img/stackql-featured-image.png"
---
Áp dụng một JSON MergePatch (RFC 7396) cho tài liệu JSON và sửa đổi tài liệu đó theo các thao tác được chỉ định. Hàm này được dùng để thêm, sửa đổi hoặc xóa các phần tử trong đối tượng JSON nhưng coi mảng JSON là đơn vị nguyên khối.

Xem thêm:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Cú pháp {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*target_json*__  
Tài liệu JSON cần vá.

__*patch_json*__  
Một tài liệu JSON mô tả các thay đổi cần áp dụng theo thuật toán MergePatch.

## Giá trị trả về {#return-values}
Trả về tài liệu JSON đã sửa đổi sau khi áp dụng bản vá.

* * *

## Ví dụ {#examples}

### MergePatch đơn giản {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Đầu ra: `{"a":1,"b":2,"c":3,"d":4}`

### Thay thế một mảng bằng một giá trị {#replace-an-array-with-a-value}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Đầu ra: `{"a":9,"b":2}`

### Xóa phần tử bằng null {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Đầu ra: `{"a":9,"c":8}`

### MergePatch lồng nhau {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Đầu ra: `{"a":{"x":1,"y":9},"b":3,"c":8}`

Tính năng này cho phép cập nhật linh hoạt và động dữ liệu JSON được lưu trong cơ sở dữ liệu, đặc biệt hữu ích cho các ứng dụng cần sửa đổi cấu trúc dữ liệu có điều kiện.
