---
title: 正規表示式參考
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 正規表示式參考：REGEXP_LIKE、REGEXP_REPLACE 和 REGEXP_SUBSTR 的語法、字元類別、錨點、數量詞和群組。"
image: "/img/stackql-featured-image.png"
---
StackQL 中使用的正規表示式參考。  

另請參閱：  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## 支援的語法 {#supported-syntax}

StackQL 支援下列正規表示式語法：    

| 運算式 | 描述 |
|------------|-------------|
| `X*` | `X` 出現零次或多次 |
| `X+` | `X` 出現一次或多次 | 
| `X?` | `X` 出現零次或一次 |
| `(X)` | 比對 `X` |
| `X`&vert;`Y` | `X` 或 `Y` |
| `^X` | `X` 出現在字串開頭 |
| `X$` | `X` 出現在字串結尾 |
| `.` | 比對任何單一字元 |
| `\c` | 字元 `c`，其中 `c` 為 `\{}()[]`&vert;`*+?.` 之一 |
| `\c` | `c` 為 `afnrtv` 之一時的 C 語言逸出序列，例如：`\t` 或 `\n` |
| `[abc]` | 集合 `abc` 中的任何單一字元 |
| `[^abc]` | 不在集合 `abc` 中的任何單一字元 |
| `[a-z]` | 範圍 `a-z` 內的任何單一字元 |
| `[^a-z]` | 不在範圍 `a-z` 內的任何單一字元 |  

&nbsp;  
&nbsp;  
如需詳細資訊，請參閱 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)。
