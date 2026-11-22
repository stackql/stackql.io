---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tổng hợp các cặp khóa-giá trị thành một đối tượng JSON bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tổng hợp các cặp khóa-giá trị thành một đối tượng JSON, nhóm theo một cột được chỉ định. Hàm `JSON_GROUP_OBJECT()` thu thập mọi cặp khóa-giá trị của từng nhóm trong các cột được chỉ định và kết hợp chúng thành một đối tượng JSON duy nhất cho mỗi nhóm.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Đối số {#arguments}

__*key_column*__  
Cột cung cấp các khóa cho đối tượng JSON.

__*value_column*__  
Cột cung cấp các giá trị cho đối tượng JSON.

__*grouping_column*__  
Cột trong mệnh đề `GROUP BY`; cột này không phải là đối số của hàm `JSON_GROUP_OBJECT`, vốn nhận đúng hai đối số. Một đối tượng JSON được trả về cho mỗi nhóm.

## Giá trị trả về {#return-values}
Trả về một đối tượng JSON chứa tất cả các cặp khóa-giá trị đã tổng hợp từ các cột được chỉ định, được nhóm theo cột `grouping_column`.

* * *

## Ví dụ {#examples}

### Tổng hợp kích thước kho lưu trữ theo ngôn ngữ lập trình {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

Truy vấn này trả về một đối tượng JSON cho mỗi ngôn ngữ lập trình, trong đó mỗi đối tượng chứa tên kho lưu trữ và kích thước tương ứng dưới dạng cặp khóa-giá trị. Dưới đây là một ví dụ về đầu ra dự kiến:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

Đầu ra này hữu ích để trực quan hóa sự phân bố kích thước kho lưu trữ giữa các ngôn ngữ lập trình khác nhau trong một tổ chức, cung cấp dữ liệu rõ ràng, có tổ chức và được nhóm theo ngôn ngữ.
