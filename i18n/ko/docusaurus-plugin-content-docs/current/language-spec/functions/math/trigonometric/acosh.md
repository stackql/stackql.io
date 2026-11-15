---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 하이퍼볼릭 아크코사인(역하이퍼볼릭 코사인)을 계산하여 라디안 단위의 각도를 반환합니다.
image: "/img/stackql-featured-image.png"
---
숫자의 하이퍼볼릭 아크코사인(역하이퍼볼릭 코사인), 즉 하이퍼볼릭 코사인이 지정한 숫자가 되는 라디안 단위의 음수가 아닌 값 \( \theta \)를 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*value*__  
하이퍼볼릭 아크코사인을 계산할 1 이상의 숫자 식입니다.

## 반환 값 {#return-values}
지정한 값의 하이퍼볼릭 아크코사인을 라디안 단위로 반환합니다.

* * *

## 예제 {#examples}

### 1의 하이퍼볼릭 아크코사인 계산 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

1의 하이퍼볼릭 아크코사인은 0라디안이므로 이 쿼리는 `0`을 반환합니다.

### 10의 하이퍼볼릭 아크코사인 계산 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

이 쿼리는 10의 하이퍼볼릭 아크코사인을 계산하며, 결과는 약 `2.9932`입니다. 이 값은 하이퍼볼릭 코사인이 10이 되는 라디안 단위의 값입니다.
