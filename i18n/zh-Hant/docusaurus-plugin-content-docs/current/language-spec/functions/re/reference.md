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
| `X{n}` | `X` 恰好出現 `n` 次 |
| `X{n,}` | `X` 出現 `n` 次或更多次 |
| `X{n,m}` | `X` 出現 `n` 到 `m` 次 |
| `(X)` | 比對 `X` |
| `X`&vert;`Y` | `X` 或 `Y` |
| `^X` | `X` 出現在字串開頭 |
| `X$` | `X` 出現在字串結尾 |
| `.` | 比對任何單一字元，包括換行字元 |
| `\c` | 字元 `c`，其中 `c` 為 `\{}()[]`&vert;`*+?.` 之一 |
| `\c` | `c` 為 `afnrtv` 之一時的 C 語言逸出序列，例如：`\t` 或 `\n` |
| `[abc]` | 集合 `abc` 中的任何單一字元 |
| `[^abc]` | 不在集合 `abc` 中的任何單一字元 |
| `[a-z]` | 範圍 `a-z` 內的任何單一字元 |
| `[^a-z]` | 不在範圍 `a-z` 內的任何單一字元 |
| `\d`, `\w`, `\s` | 數字、單字字元（`[0-9A-Za-z_]`）、空白字元 |
| `\D`, `\W`, `\S` | 任何非數字、非單字字元、非空白字元的字元 |
| `\b` | 單字邊界 |  

&nbsp;  
&nbsp;  
模式使用 RE2 語法，由 Go 的 `regexp` 套件實作。不支援反向參考（`\1`）與環視斷言（lookaround）：使用它們的模式會遭到拒絕並傳回錯誤。

在 StackQL 字串常值中，反斜線會開始一個逸出序列（`'\n'` 是換行字元），因此屬於正規表示式的反斜線要寫兩次：`'\\d+'` 比對一個或多個數字，`'[0-9]+\\.[0-9]+'` 比對數字之間的句點字元本身。

如需完整語法，請參閱 [RE2 語法](https://github.com/google/re2/wiki/Syntax)。
