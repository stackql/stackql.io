---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 자연 로그를 계산합니다.
image: "/img/stackql-featured-image.png"
---
주어진 숫자의 자연 로그(밑이 e인 로그)를 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
자연 로그를 계산할 양의 숫자 식입니다.

## 반환 값 {#return-values}
지정한 양의 숫자 값의 자연 로그를 반환합니다.

* * *

## 예제 {#examples}

### 2.71828(약 e)의 자연 로그 계산 {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

e(약 2.71828)의 자연 로그는 1이므로 이 쿼리는 `1`에 가까운 값을 반환합니다.

### 더 큰 숫자의 자연 로그 계산 {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

이 쿼리는 10의 자연 로그를 계산하여 밑이 e인 10의 로그 값을 제공합니다.
