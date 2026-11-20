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
| `(X)` | khớp `X` |
| `X`&vert;`Y` | `X` hoặc `Y` |
| `^X` | `X` xuất hiện ở đầu chuỗi |
| `X$` | `X` xuất hiện ở cuối chuỗi |
| `.` | Khớp với một ký tự bất kỳ |
| `\c` | Ký tự `c`, trong đó `c` là một trong các ký tự `\{}()[]`&vert;`*+?.` |
| `\c` | Chuỗi thoát của ngôn ngữ C cho `c` thuộc `afnrtv`. Ví dụ: `\t` hoặc `\n` |
| `[abc]` | Một ký tự bất kỳ trong tập `abc` |
| `[^abc]` | Một ký tự bất kỳ không thuộc tập `abc` |
| `[a-z]` | Một ký tự bất kỳ trong khoảng `a-z` |
| `[^a-z]` | Một ký tự bất kỳ không thuộc khoảng `a-z` |  

&nbsp;  
&nbsp;  
Để biết thêm thông tin, hãy xem [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
