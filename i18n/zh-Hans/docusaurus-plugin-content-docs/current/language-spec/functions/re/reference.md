---
title: 正则表达式参考
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 正则表达式参考：REGEXP_LIKE、REGEXP_REPLACE 和 REGEXP_SUBSTR 使用的语法、字符类、锚点、量词和分组。"
image: "/img/stackql-featured-image.png"
---
StackQL 中使用的正则表达式参考。  

另请参阅：  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## 支持的语法 {#supported-syntax}

StackQL 支持以下正则表达式语法：    

| 表达式 | 说明 |
|------------|-------------|
| `X*` | `X` 出现 0 次或多次 |
| `X+` | `X` 出现 1 次或多次 | 
| `X?` | `X` 出现 0 次或 1 次 |
| `(X)` | 匹配 `X` |
| `X`&vert;`Y` | `X` 或 `Y` |
| `^X` | `X` 出现在字符串开头 |
| `X$` | `X` 出现在字符串末尾 |
| `.` | 匹配任意单个字符 |
| `\c` | 字符 `c`，其中 `c` 为 `\{}()[]`&vert;`*+?.` 之一
| `\c` | C 语言转义，其中 `c` 为 `afnrtv` 之一。例如：`\t` 或 `\n` |
| `[abc]` | 集合 `abc` 中的任意单个字符 |
| `[^abc]` | 不在集合 `abc` 中的任意单个字符 |
| `[a-z]` | 范围 `a-z` 内的任意单个字符 |
| `[^a-z]` | 不在范围 `a-z` 内的任意单个字符 |  

&nbsp;  
&nbsp;  
有关更多信息，请参阅 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
