---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 밑이 2인 로그를 계산합니다.
image: "/img/stackql-featured-image.png"
---
주어진 숫자의 밑이 2인 로그를 계산합니다. 즉, 해당 값을 얻기 위해 2를 몇 제곱해야 하는지를 반환합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
밑이 2인 로그를 계산할 양의 숫자 식입니다.

## 반환 값 {#return-values}
지정한 숫자 값의 밑이 2인 로그를 반환합니다.

* * *

## 예제 {#examples}

### 밑이 2인 16의 로그 계산 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

2^4 = 16이므로 이 쿼리는 `4`를 반환합니다.

### 밑이 2인 32의 로그 계산 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

이 쿼리는 밑이 2인 32의 로그를 계산하며, 2^5 = 32이므로 결과는 `5`입니다.
