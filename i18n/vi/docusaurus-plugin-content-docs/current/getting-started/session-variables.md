---
title: Biến phiên
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - biến phiên
  - set
  - unset
  - identifier
description: "Biến phiên trong StackQL: đặt giá trị vô hướng có kiểu bằng SET, giải tham chiếu bằng $name trong vị từ, biểu thức và dùng làm tên quan hệ hoặc tên cột với IDENTIFIER."
image: "/img/stackql-featured-image.png"
---

Xem thêm:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Phiên provider ]](/getting-started/provider-sessions) [[ Sử dụng biến ]](/getting-started/variables)

Biến phiên là các giá trị vô hướng do phiên hiện tại lưu giữ và được tham chiếu từ các câu lệnh sau đó.  Biến phiên có từ StackQL `v1.0` trở đi, và hoạt động trong shell tương tác, trong các tập lệnh `exec`, qua giao thức wire của PostgreSQL và thông qua máy chủ MCP.

Biến phiên khác với các biến template được mô tả trong [Sử dụng biến](/getting-started/variables).  Biến template được truyền cho `stackql exec` thông qua `--iqldata` và `--var` và được thay vào văn bản tập lệnh trước khi tập lệnh được phân tích cú pháp.  Biến phiên được phân giải bên trong câu lệnh đã phân tích cú pháp, giữ nguyên kiểu của chúng và có thể được thay đổi giữa các câu lệnh.

## Đặt và xóa biến {#setting-and-removing-variables}

Câu lệnh [`SET`](/language-spec/set) gán một chuỗi, số, boolean hoặc `null`.  Câu lệnh [`UNSET`](/language-spec/unset) xóa một biến; `SET name = DEFAULT` có tác dụng tương đương.  Một giá trị `null` tường minh là một giá trị được lưu, không phải là việc đặt lại.

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

Mảng và đối tượng không phải là giá trị vô hướng.  Để lưu một tài liệu JSON, hãy lưu nó dưới dạng chuỗi:

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## Giải tham chiếu một biến {#dereferencing-a-variable}

Dùng `$name`, không đặt trong dấu nháy, ở bất kỳ vị trí biểu thức nào.  Chuỗi được thoát thành literal SQL; số, boolean và `null` giữ nguyên kiểu.  `'$name'` trong dấu nháy đơn là văn bản literal `$name`, không phải là một phép giải tham chiếu.  Một tham chiếu đến biến không tồn tại sẽ thất bại trước khi bất kỳ yêu cầu nào đến provider được gửi đi.

Các giá trị đã phân giải có thể được dùng trong các biểu thức SQL thông thường, bao gồm `CAST`, `CONVERT`, nối chuỗi bằng `||` và lời gọi hàm:

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

Phép gán chỉ chấp nhận literal vô hướng hoặc tham chiếu trực tiếp; hãy áp dụng các phép chuyển đổi và biểu thức khác tại nơi biến được dùng, không phải trong câu lệnh `SET`.

## Biến trong vị từ {#variables-in-predicates}

Một biến trong mệnh đề `WHERE` được phân giải trước khi câu lệnh được lập kế hoạch, nên biến đó được định tuyến đến API của provider giống hệt như một literal.  Do đó, các tham số bắt buộc có thể được cung cấp từ biến:

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

Các giá trị có kiểu được so sánh theo kiểu của chúng.  `WHERE active = $enabled` với `SET enabled = false` lọc theo giá trị boolean, và `WHERE id = CAST($some_id AS TEXT)` là dạng cần dùng khi cột của provider là chuỗi còn biến là số.

## Biến làm tên đối tượng {#variables-as-object-names}

Biến có thể đặt tên cho quan hệ và cột, không chỉ cho giá trị.  Tên được chuyển thành định danh trong câu lệnh đã phân tích cú pháp và không bao giờ được ghép vào văn bản SQL, nên một giá trị chứa câu lệnh, rỗng hoặc không phải là chuỗi sẽ bị từ chối trước mọi truy cập đến provider.

| Vị trí | Dạng |
|--|--|
| Mệnh đề `FROM` | `FROM $relation_name`, hoặc theo từng thành phần, `FROM $provider.$service.$resource` |
| Cột trong một biểu thức | `IDENTIFIER($column_name)`, xem [`IDENTIFIER`](/language-spec/functions/general/identifier) |
| Bí danh cột hoặc bảng | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| Tên cột trong DDL, danh sách cột của `INSERT`, đích của `UPDATE` | dạng `"$name"` trong dấu ngoặc kép |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

Phép nối, CTE và truy vấn con phân giải các biến quan hệ của chúng theo cùng cách.  Giá trị quan hệ có dấu chấm có tối đa bốn thành phần.  Biến không phải là các đoạn SQL tùy ý: không thể thay thế một từ khóa, một danh sách cột phân cách bằng dấu phẩy hoặc toàn bộ một mệnh đề `WHERE`.

## Phạm vi và cô lập {#scope-and-isolation}

Biến thuộc về phiên đã đặt chúng.  Các kết nối riêng biệt đến một máy chủ `stackql srv` có các biến riêng biệt, các lần gọi `exec` riêng biệt cũng vậy.  Mỗi yêu cầu MCP là một phiên riêng, nên một `SET` và truy vấn dùng nó phải được gửi trong cùng một lô câu lệnh.  Phiên mới bắt đầu mà không có biến nào.

Phiên là phạm vi duy nhất.  `SET SESSION` và `UNSET SESSION` được chấp nhận và có nghĩa hoàn toàn giống `SET` và `UNSET`; từ khóa này có mặt để các tập lệnh đọc giống như trên các cơ sở dữ liệu mà ở đó thiết lập phiên và thiết lập toàn cục khác nhau.  `GLOBAL` bị từ chối, vì không có trạng thái nào được dùng chung giữa các phiên.

Câu lệnh chuẩn bị sẵn (prepared statement) phân giải biến khi được thực thi, không phải khi được chuẩn bị, và một kế hoạch chứa giá trị phiên đã liên kết sẽ không được dùng lại giữa các lần thực thi.

Khi [quản lý truy cập](/command-line-usage/access-management) được bật, việc đọc một biến cần quyền `session/variables:read` và việc đặt hoặc đặt lại một biến cần quyền `session/config:write`.  Việc phân giải một biến thành tên đối tượng không trao quyền nào trên đối tượng đó; quan hệ đã phân giải được ủy quyền trước khi thực thi như thường lệ.
