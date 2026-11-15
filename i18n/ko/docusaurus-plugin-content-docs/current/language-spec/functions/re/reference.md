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
| `(X)` | `X`와 일치 |
| `X`&vert;`Y` | `X` 또는 `Y` |
| `^X` | 문자열의 시작 부분에 나타나는 `X` |
| `X$` | 문자열의 끝 부분에 나타나는 `X` |
| `.` | 임의의 단일 문자와 일치 |
| `\c` | `c`가 `\{}()[]`&vert;`*+?.` 중 하나인 문자 `c`
| `\c` | `afnrtv`에 속하는 `c`에 대한 C 언어 이스케이프. 예: `\t` 또는 `\n` |
| `[abc]` | `abc` 집합에 속하는 임의의 단일 문자 |
| `[^abc]` | `abc` 집합에 속하지 않는 임의의 단일 문자 |
| `[a-z]` | `a-z` 범위에 속하는 임의의 단일 문자 |
| `[^a-z]` | `a-z` 범위에 속하지 않는 임의의 단일 문자 |  

&nbsp;  
&nbsp;  
자세한 내용은 [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md) 문서를 참조하세요.
