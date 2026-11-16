---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 하이퍼볼릭 아크탄젠트(역하이퍼볼릭 탄젠트)를 계산하여 라디안 단위의 각도를 반환합니다.
image: "/img/stackql-featured-image.png"
---
숫자의 하이퍼볼릭 아크탄젠트(역하이퍼볼릭 탄젠트), 즉 하이퍼볼릭 탄젠트가 지정한 숫자가 되는 라디안 단위의 값을 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*value*__  
하이퍼볼릭 아크탄젠트를 계산할 -1에서 1 사이(양 끝 제외) 범위의 숫자 식입니다.

## 반환 값 {#return-values}
지정한 값의 하이퍼볼릭 아크탄젠트를 라디안 단위로 반환합니다.

* * *

## 예제 {#examples}

### 0.5의 하이퍼볼릭 아크탄젠트 계산 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

0.5의 하이퍼볼릭 아크탄젠트는 약 0.5493라디안이므로 이 쿼리는 약 `0.5493`을 반환합니다.

### -0.5의 하이퍼볼릭 아크탄젠트 계산 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

이 쿼리는 -0.5의 하이퍼볼릭 아크탄젠트를 계산하며, 결과는 약 `-0.5493`입니다. 이 값은 하이퍼볼릭 탄젠트가 -0.5가 되는 라디안 단위의 값입니다.
