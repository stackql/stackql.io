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
| `X{n}` | `X` のちょうど `n` 回の出現 |
| `X{n,}` | `X` の `n` 回以上の出現 |
| `X{n,m}` | `X` の `n` 回以上 `m` 回以下の出現 |
| `(X)` | `X` に一致 |
| `X`&vert;`Y` | `X` または `Y` |
| `^X` | 文字列の先頭にある `X` |
| `X$` | 文字列の末尾にある `X` |
| `.` | 改行を含む任意の 1 文字に一致 |
| `\c` | 文字 `c`（`c` は `\{}()[]`&vert;`*+?.` のいずれか） |
| `\c` | `c` が `afnrtv` のいずれかである C 言語のエスケープ。例：`\t` または `\n` |
| `[abc]` | 集合 `abc` に含まれる任意の 1 文字 |
| `[^abc]` | 集合 `abc` に含まれない任意の 1 文字 |
| `[a-z]` | 範囲 `a-z` に含まれる任意の 1 文字 |
| `[^a-z]` | 範囲 `a-z` に含まれない任意の 1 文字 |
| `\d`, `\w`, `\s` | 数字、単語構成文字（`[0-9A-Za-z_]`）、空白文字 |
| `\D`, `\W`, `\S` | 数字以外、単語構成文字以外、空白文字以外の任意の文字 |
| `\b` | 単語境界 |  

&nbsp;  
&nbsp;  
パターンには、Go の `regexp` パッケージが実装する RE2 構文を使用します。後方参照（`\1`）と、先読みおよび後読みのアサーションはサポートされていません。これらを使用するパターンはエラーとして拒否されます。

StackQL の文字列リテラル内のバックスラッシュはエスケープシーケンスを開始します（`'\n'` は改行です）。そのため、正規表現の一部であるバックスラッシュは 2 つ重ねて記述します。`'\\d+'` は 1 桁以上の数字に一致し、`'[0-9]+\\.[0-9]+'` は数字の間にあるドット文字そのものに一致します。

完全な構文については、[RE2 構文](https://github.com/google/re2/wiki/Syntax)を参照してください。
