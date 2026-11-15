---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 LOWER 함수: 문자열을 소문자로 변환합니다."
image: "/img/stackql-featured-image.png"
---
입력 문자열을 소문자로 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

## 구문 {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> `lower()` 함수는 ASCII 문자에서만 작동합니다.

## 인수 {#arguments}

__*character_expression*__  
ASCII 문자열입니다.

## 반환 값 {#return-values}
문자열 값을 반환합니다.

* * *

## 예제 {#examples}

### 소문자 문자열 반환 {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower) 문서를 참조하세요.
