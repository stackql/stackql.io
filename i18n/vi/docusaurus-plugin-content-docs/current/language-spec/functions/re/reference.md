---
title: Tham khảo biểu thức chính quy
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Tài liệu tham khảo biểu thức chính quy cho StackQL: cú pháp, lớp ký tự, neo, bộ định lượng và nhóm cho REGEXP_LIKE, REGEXP_REPLACE và REGEXP_SUBSTR."
image: "/img/stackql-featured-image.png"
---
Tài liệu tham khảo về biểu thức chính quy được dùng trong StackQL.  

Xem thêm:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## Cú pháp được hỗ trợ {#supported-syntax}

StackQL hỗ trợ cú pháp biểu thức chính quy sau:    

| Biểu thức | Mô tả |
|------------|-------------|
| `X*` | không hoặc nhiều lần xuất hiện của `X` |
| `X+` | một hoặc nhiều lần xuất hiện của `X` | 
| `X?` | không hoặc một lần xuất hiện của `X` |
| `X{n}` | đúng `n` lần xuất hiện của `X` |
| `X{n,}` | `n` lần xuất hiện của `X` trở lên |
| `X{n,m}` | từ `n` đến `m` lần xuất hiện của `X` |
| `(X)` | khớp `X` |
| `X`&vert;`Y` | `X` hoặc `Y` |
| `^X` | `X` xuất hiện ở đầu chuỗi |
| `X$` | `X` xuất hiện ở cuối chuỗi |
| `.` | Khớp với một ký tự bất kỳ, kể cả ký tự xuống dòng |
| `\c` | Ký tự `c`, trong đó `c` là một trong các ký tự `\{}()[]`&vert;`*+?.` |
| `\c` | Chuỗi thoát của ngôn ngữ C cho `c` thuộc `afnrtv`. Ví dụ: `\t` hoặc `\n` |
| `[abc]` | Một ký tự bất kỳ trong tập `abc` |
| `[^abc]` | Một ký tự bất kỳ không thuộc tập `abc` |
| `[a-z]` | Một ký tự bất kỳ trong khoảng `a-z` |
| `[^a-z]` | Một ký tự bất kỳ không thuộc khoảng `a-z` |
| `\d`, `\w`, `\s` | Một chữ số, một ký tự từ (`[0-9A-Za-z_]`), một ký tự khoảng trắng |
| `\D`, `\W`, `\S` | Một ký tự bất kỳ không phải chữ số, không phải ký tự từ, không phải ký tự khoảng trắng |
| `\b` | Một ranh giới từ |  

&nbsp;  
&nbsp;  
Các mẫu dùng cú pháp RE2, như được hiện thực trong gói `regexp` của Go.  Tham chiếu ngược (`\1`) và các khẳng định lookaround không được hỗ trợ: một mẫu sử dụng chúng sẽ bị từ chối kèm theo lỗi.

Dấu gạch chéo ngược trong một literal chuỗi StackQL bắt đầu một chuỗi thoát (`'\n'` là ký tự xuống dòng), nên dấu gạch chéo ngược thuộc về biểu thức chính quy được viết hai lần: `'\\d+'` khớp với một hoặc nhiều chữ số và `'[0-9]+\\.[0-9]+'` khớp với ký tự dấu chấm nằm giữa các chữ số.

Để biết cú pháp đầy đủ, hãy xem [cú pháp RE2](https://github.com/google/re2/wiki/Syntax).
