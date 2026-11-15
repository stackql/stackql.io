---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 아크사인(역사인)을 계산하여 라디안 단위의 각도를 반환합니다.
image: "/img/stackql-featured-image.png"
---
숫자의 아크사인(역사인), 즉 사인이 지정한 숫자가 되는 라디안 단위의 각도를 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*value*__  
아크사인을 계산할 -1 이상 1 이하 범위의 숫자 식입니다.

## 반환 값 {#return-values}
지정한 값의 아크사인을 라디안 단위로 반환합니다.

* * *

## 예제 {#examples}

### 0.5의 아크사인 계산 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

0.5의 아크사인은 π/6 라디안의 각도에 해당하므로 이 쿼리는 π/6, 즉 약 `0.5236`을 반환합니다.

### -1의 아크사인 계산 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

이 쿼리는 -1의 아크사인을 계산하며, -π/2의 사인은 -1이므로 결과는 -π/2, 즉 약 `-1.5708`입니다.
