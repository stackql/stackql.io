---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 하이퍼볼릭 사인을 계산하여 결과를 반환합니다.
image: "/img/stackql-featured-image.png"
---
지정한 값의 하이퍼볼릭 사인을 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*value*__  
하이퍼볼릭 사인을 계산할 숫자 식입니다.

## 반환 값 {#return-values}
지정한 값의 하이퍼볼릭 사인을 반환합니다.

* * *

## 예제 {#examples}

### 1의 하이퍼볼릭 사인 계산 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

1의 하이퍼볼릭 사인은 약 1.1752이므로 이 쿼리는 약 `1.1752`를 반환합니다.

### 0의 하이퍼볼릭 사인 계산 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

이 쿼리는 0의 하이퍼볼릭 사인을 계산하며, 0의 하이퍼볼릭 사인은 정확히 0이므로 결과는 `0`입니다.
