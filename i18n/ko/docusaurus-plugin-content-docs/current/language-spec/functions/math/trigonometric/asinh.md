---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 하이퍼볼릭 아크사인(역하이퍼볼릭 사인)을 계산합니다.
image: "/img/stackql-featured-image.png"
---
숫자의 하이퍼볼릭 아크사인(역하이퍼볼릭 사인), 즉 하이퍼볼릭 사인이 지정한 숫자가 되는 값을 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*value*__  
하이퍼볼릭 아크사인을 계산할 숫자 식입니다.

## 반환 값 {#return-values}
지정한 값의 하이퍼볼릭 아크사인을 반환합니다.

* * *

## 예제 {#examples}

### 0.5의 하이퍼볼릭 아크사인 계산 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

0.5의 하이퍼볼릭 아크사인은 약 0.4812이므로 이 쿼리는 약 `0.4812`를 반환합니다.

### -1의 하이퍼볼릭 아크사인 계산 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

이 쿼리는 -1의 하이퍼볼릭 아크사인을 계산하며, 결과는 약 `-0.8814`입니다. 이 값은 하이퍼볼릭 사인이 -1이 되는 값입니다.
