---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 밑이 10인 로그를 계산합니다.
image: "/img/stackql-featured-image.png"
---
주어진 숫자의 밑이 10인 로그를 계산합니다. 즉, 해당 값을 얻기 위해 10을 몇 제곱해야 하는지를 구합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*numeric_expression*__  
밑이 10인 로그를 계산할 양의 숫자 식입니다.

## 반환 값 {#return-values}
지정한 숫자 값의 밑이 10인 로그를 반환합니다.

* * *

## 예제 {#examples}

### 밑이 10인 100의 로그 계산 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

10^2 = 100이므로 이 쿼리는 `2`를 반환합니다.

### 밑이 10인 1000의 로그 계산 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

이 쿼리는 밑이 10인 1000의 로그를 계산하며, 10^3 = 1000이므로 결과는 `3`입니다.
