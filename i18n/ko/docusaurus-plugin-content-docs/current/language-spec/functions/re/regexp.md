---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 정규식을 사용하여 문자열이 패턴과 일치하는지 확인합니다.
image: "/img/stackql-featured-image.png"
---
원본 문자열이 지정한 정규식 패턴과 일치하는지 확인합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*source_string*__  
패턴을 검색할 문자열입니다.

__*pattern_expression*__  
원본 문자열과 비교할 정규식 패턴입니다.

## 반환 값 {#return-values}
부울 값을 반환합니다. 원본 문자열이 정규식 패턴과 일치하면 `TRUE`, 그렇지 않으면 `FALSE`입니다.

* * *

## 예제 {#examples}

### 문자열에 숫자가 포함되어 있는지 확인 {#check-if-a-string-contains-numbers}

이 예제는 문자열 'the year is 2021'에 숫자 문자가 하나 이상 포함되어 있는지 확인합니다.

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

문자열에 `[0-9]+` 패턴과 일치하는 부분 문자열 '2021'이 포함되어 있으므로 이 쿼리는 `TRUE`를 반환합니다. 이 패턴은 하나 이상의 숫자를 나타냅니다.

### 이메일 형식 유효성 검사 {#validate-email-format}

문자열이 유효한 이메일 형식인지 확인하는 방법은 다음과 같습니다.

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

이 쿼리는 `TRUE`를 반환합니다. 즉, 'user@example.com'은 지정한 정규식 패턴과 일치하는 일반적인 이메일 주소 형식입니다.
