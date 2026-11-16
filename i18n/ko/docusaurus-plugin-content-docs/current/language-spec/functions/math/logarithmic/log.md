---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 임의의 밑에 대한 숫자의 로그를 계산합니다.
image: "/img/stackql-featured-image.png"
---
지정한 밑에 대한 숫자의 로그를 계산합니다. 이는 밑 e를 사용하는 LN보다 더 일반적인 형태의 로그 함수입니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) 

* * * 

## 구문 {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*base*__  
로그의 밑으로, 1이 아닌 양의 숫자 식입니다.

__*numeric_expression*__  
로그를 계산할 양의 숫자 식입니다.

## 반환 값 {#return-values}
지정한 밑에 대한 지정한 숫자 값의 로그를 반환합니다.

* * *

## 예제 {#examples}

### 밑이 10인 1000의 로그 계산 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

10^3 = 1000이므로 이 쿼리는 `3`을 반환합니다.

### 밑이 2인 16의 로그 계산 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
이 쿼리는 밑이 2인 16의 로그를 계산하며, 2^4 = 16이므로 결과는 `4`입니다.
