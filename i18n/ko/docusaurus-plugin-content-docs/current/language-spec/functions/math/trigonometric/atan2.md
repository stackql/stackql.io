---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 두 숫자(y/x)의 아크탄젠트를 계산하여 라디안 단위의 각도를 반환합니다.
image: "/img/stackql-featured-image.png"
---
두 숫자의 아크탄젠트, 즉 탄젠트가 지정한 두 숫자의 비(y를 x로 나눈 값)가 되는 라디안 단위의 각도 \( \theta \)를 계산합니다. 입력 값의 부호를 올바르게 처리하여 정확한 사분면을 결정합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*y*__  
점의 y 좌표(분자)입니다.

__*x*__  
점의 x 좌표(분모)입니다.

## 반환 값 {#return-values}
각도의 정확한 사분면을 고려하여 y/x의 아크탄젠트를 라디안 단위로 반환합니다.

* * *

## 예제 {#examples}

### 점 (1,1)의 아크탄젠트 계산 {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

점 (1,1)의 아크탄젠트는 제1사분면에서 π/4 라디안의 각도에 해당하므로 이 쿼리는 π/4, 즉 약 `0.785`를 반환합니다.

### 점 (10, -10)의 아크탄젠트 계산 {#calculate-the-arc-tangent-of-the-point-10--10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

이 쿼리는 점 (10, -10)의 아크탄젠트를 계산하며, 이 각도의 탄젠트는 제2사분면에서 -1이므로 결과는 3π/4, 즉 약 `2.356`입니다.
