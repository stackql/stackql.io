---
title: 正規表現リファレンス
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の正規表現リファレンス：REGEXP_LIKE、REGEXP_REPLACE、REGEXP_SUBSTR で使用する構文、文字クラス、アンカー、量指定子、グループ。"
image: "/img/stackql-featured-image.png"
---
StackQL で使用する正規表現のリファレンスです。  

関連項目：  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## サポートされている構文 {#supported-syntax}

StackQL でサポートされている正規表現構文は次のとおりです。    

| 式 | 説明 |
|------------|-------------|
| `X*` | `X` の 0 回以上の出現 |
| `X+` | `X` の 1 回以上の出現 | 
| `X?` | `X` の 0 回または 1 回の出現 |
| `(X)` | `X` に一致 |
| `X`&vert;`Y` | `X` または `Y` |
| `^X` | 文字列の先頭にある `X` |
| `X$` | 文字列の末尾にある `X` |
| `.` | 任意の 1 文字に一致 |
| `\c` | 文字 `c`（`c` は `\{}()[]`&vert;`*+?.` のいずれか） |
| `\c` | `c` が `afnrtv` のいずれかである C 言語のエスケープ。例：`\t` または `\n` |
| `[abc]` | 集合 `abc` に含まれる任意の 1 文字 |
| `[^abc]` | 集合 `abc` に含まれない任意の 1 文字 |
| `[a-z]` | 範囲 `a-z` に含まれる任意の 1 文字 |
| `[^a-z]` | 範囲 `a-z` に含まれない任意の 1 文字 |  

&nbsp;  
&nbsp;  
詳細については、[https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md) を参照してください。
