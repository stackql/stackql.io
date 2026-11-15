---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 UPPER 함수: 문자열을 대문자로 변환합니다."
image: "/img/stackql-featured-image.png"
---
입력 문자열을 대문자로 반환합니다.  

참고 항목:  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ 데이터 형식 ]](/language-spec/data-types)

* * * 

## 구문 {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> `upper()` 함수는 ASCII 문자에서만 작동합니다.

## 인수 {#arguments}

__*character_expression*__  
ASCII 문자열입니다.

## 반환 값 {#return-values}
문자열 값을 반환합니다.

* * *

## 예제 {#examples}

### 대문자 문자열 반환 {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

자세한 내용은 [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper) 문서를 참조하세요.
