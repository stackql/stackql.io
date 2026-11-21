---
title: 정규식 참조
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL 정규식 참조: REGEXP_LIKE, REGEXP_REPLACE 및 REGEXP_SUBSTR에서 사용하는 구문, 문자 클래스, 앵커, 수량자 및 그룹."
image: "/img/stackql-featured-image.png"
---
StackQL에서 사용하는 정규식에 대한 참조입니다.  

참고 항목:  
[[` REGEXP_LIKE `]](/language-spec/functions/re/regexp_like)  [[` REGEXP_SUBSTR `]](/language-spec/functions/re/regexp_substr)  [[` REGEXP_REPLACE `]](/language-spec/functions/re/regexp_replace)

* * * 

## 지원되는 구문 {#supported-syntax}

StackQL에서 지원되는 정규식 구문은 다음과 같습니다.    

| 식 | 설명 |
|------------|-------------|
| `X*` | 0회 이상 나타나는 `X` |
| `X+` | 1회 이상 나타나는 `X` | 
| `X?` | 0회 또는 1회 나타나는 `X` |
| `X{n}` | 정확히 `n`회 나타나는 `X` |
| `X{n,}` | `n`회 이상 나타나는 `X` |
| `X{n,m}` | `n`회 이상 `m`회 이하 나타나는 `X` |
| `(X)` | `X`와 일치 |
| `X`&vert;`Y` | `X` 또는 `Y` |
| `^X` | 문자열의 시작 부분에 나타나는 `X` |
| `X$` | 문자열의 끝 부분에 나타나는 `X` |
| `.` | 줄 바꿈 문자를 포함한 임의의 단일 문자와 일치 |
| `\c` | `c`가 `\{}()[]`&vert;`*+?.` 중 하나인 문자 `c` |
| `\c` | `afnrtv`에 속하는 `c`에 대한 C 언어 이스케이프. 예: `\t` 또는 `\n` |
| `[abc]` | `abc` 집합에 속하는 임의의 단일 문자 |
| `[^abc]` | `abc` 집합에 속하지 않는 임의의 단일 문자 |
| `[a-z]` | `a-z` 범위에 속하는 임의의 단일 문자 |
| `[^a-z]` | `a-z` 범위에 속하지 않는 임의의 단일 문자 |
| `\d`, `\w`, `\s` | 숫자, 단어 문자(`[0-9A-Za-z_]`), 공백 문자 |
| `\D`, `\W`, `\S` | 숫자가 아닌 임의의 문자, 단어 문자가 아닌 임의의 문자, 공백 문자가 아닌 임의의 문자 |
| `\b` | 단어 경계 |  

&nbsp;  
&nbsp;  
패턴은 Go의 `regexp` 패키지가 구현하는 RE2 구문을 사용합니다.  역참조(`\1`)와 전후방 탐색(lookaround) 어설션은 지원되지 않으며, 이를 사용하는 패턴은 오류와 함께 거부됩니다.

StackQL 문자열 리터럴에서 백슬래시는 이스케이프 시퀀스를 시작하므로(`'\n'` 리터럴은 줄 바꿈 문자입니다), 정규식에 속하는 백슬래시는 두 번 씁니다. `'\\d+'` 패턴은 하나 이상의 숫자와 일치하고, `'[0-9]+\\.[0-9]+'` 패턴은 숫자 사이의 리터럴 점과 일치합니다.

전체 구문은 [RE2 구문](https://github.com/google/re2/wiki/Syntax)을 참조하세요.
