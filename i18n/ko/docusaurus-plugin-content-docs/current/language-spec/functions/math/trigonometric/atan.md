---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 숫자의 아크탄젠트(역탄젠트)를 계산하여 라디안 단위의 각도를 반환합니다.
image: "/img/stackql-featured-image.png"
---
숫자의 아크탄젠트(역탄젠트), 즉 탄젠트가 지정한 숫자가 되는 라디안 단위의 각도를 계산합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select)

* * * 

## 구문 {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## 인수 {#arguments}

__*value*__  
아크탄젠트를 계산할 숫자 식입니다.

## 반환 값 {#return-values}
지정한 값의 아크탄젠트를 라디안 단위로 반환합니다.

* * *

## 예제 {#examples}

### 1의 아크탄젠트 계산 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

1의 아크탄젠트는 π/4 라디안이므로 이 쿼리는 π/4, 즉 약 `0.785`를 반환합니다.

### 0의 아크탄젠트 계산 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

이 쿼리는 0의 아크탄젠트를 계산하며, 0라디안의 탄젠트는 0이므로 결과는 `0`입니다.
